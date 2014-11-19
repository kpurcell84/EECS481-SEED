package edu.umich.seedforandroid.patient.fragments.myhealth;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.androidplot.ui.XLayoutStyle;
import com.androidplot.ui.YLayoutStyle;
import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;
import com.appspot.umichseed.seed.Seed;
import com.appspot.umichseed.seed.SeedRequest;
import com.appspot.umichseed.seed.model.SeedApiMessagesPQualDataListResponse;
import com.appspot.umichseed.seed.model.SeedApiMessagesPQuantDataListResponse;
import com.appspot.umichseed.seed.model.SeedApiMessagesPQuantDataRequest;
import com.appspot.umichseed.seed.model.SeedApiMessagesPQuantDataResponse;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.DateTime;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.account.GoogleAccountManager;
import edu.umich.seedforandroid.api.ApiThread;
import edu.umich.seedforandroid.api.SeedApi;
import edu.umich.seedforandroid.util.Utils;

public class MyHealth_ViewData_Frag extends Fragment  {

    private static final String TAG = MyHealth_ViewData_Frag.class.getSimpleName();

    private Utils mUtilsInst;
    private XYPlot mHeartRatePlot, mSkinTempPlot, mPerspirationPlot,
                   mBloodPressurePlot, mBodyTempPlot, mActivityTypePlot;
    private XYSeries mHeartRateSeries, mSkinTempSeries, mPerspirationSeries,
                     mBloodPressureSeries, mBodyTempSeries, mActivityTypeSeries;
    private ApiThread mApiThread;

    public MyHealth_ViewData_Frag()  {}

    @Override
    public void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mApiThread = new ApiThread();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)  {

        int id = item.getItemId();

        if (id == R.id.action_refresh)  { // rotate the refresh icon

            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ImageView iv = (ImageView)inflater.inflate(R.layout.action_refresh_image, null);
            Animation rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_refresh_icon);
            rotation.setRepeatCount(Animation.INFINITE);
            iv.startAnimation(rotation);
            item.setActionView(iv);
            return true;
        }
        else if (id == R.id.action_graph_options)  {


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)  {

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {

        initialSetup();

        View v = null;

        if (mUtilsInst.checkInternetConnection(getActivity().getApplicationContext()))  { // There is internet connection

            v = fetchPatientHealthData(v, inflater, container);
        }
        else  {

            v = noInternetConnection(v, inflater, container);
        }

        return v;
    }

    private void initialSetup()  {

        mUtilsInst = new Utils();

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setTitle("View My Health Data");
    }

    private View noInternetConnection(View view, LayoutInflater inflater, ViewGroup container)  {

        view = inflater.inflate(R.layout.fragment_my_health__view_data_no_internet, container, false);
        return view;
    }

    private View fetchPatientHealthData(View view, LayoutInflater inflater, ViewGroup container)  {

        view = inflater.inflate(R.layout.fragment_my_health__view_data_, container, false);

        setupGraphs(view);

        return view;
    }

    private void fetchDataFromServer(DateTime begin, DateTime end) {

        GoogleAccountManager manager = new GoogleAccountManager(this.getActivity());

        if (!manager.tryLogIn()) {

            Log.e(TAG, "FATAL ERROR: Somehow, user is on this page without being logged in");
            //todo: handle the probably impossible case of the user reaching this page without being
            //logged in (perhaps possible if they resume to here after clearing app cache)
        }

        try {

            Seed api = SeedApi.getAuthenticatedApi(manager.getCredential());
            SeedRequest getDataRequest = api.pQuantData().get(

                    new SeedApiMessagesPQuantDataRequest()
                            .setEmail(manager.getAccountName())
                            .setStartTime(begin)
                            .setEndTime(end)
            );

            mApiThread.enqueueRequest(getDataRequest, new ApiThread.ApiResultAction() {

                @Override
                public Object doInBackground(Object result) {

                    if (result != null && result instanceof SeedApiMessagesPQuantDataListResponse) {

                        SeedApiMessagesPQuantDataListResponse castedResult =
                                (SeedApiMessagesPQuantDataListResponse) result;

                        ViewDataGraphWrapper heartRateData = new ViewDataGraphWrapper(ViewDataGraphWrapper.HEART_RATE);
                        ViewDataGraphWrapper skinTempData = new ViewDataGraphWrapper(ViewDataGraphWrapper.SKIN_TEMP);
                        ViewDataGraphWrapper gsrData = new ViewDataGraphWrapper(ViewDataGraphWrapper.GSR);
                        ViewDataGraphWrapper bloodPressureData = new ViewDataGraphWrapper(ViewDataGraphWrapper.BLOOD_PRESSURE);
                        ViewDataGraphWrapper bodyTempData = new ViewDataGraphWrapper(ViewDataGraphWrapper.BODY_TEMP);
                        ViewDataGraphWrapper activityData = new ViewDataGraphWrapper(ViewDataGraphWrapper.ACTIVITY);

                        for (SeedApiMessagesPQuantDataResponse r : castedResult.getPdataList()) {

                            Long epoch = r.getTimeTaken().getValue();

                            if (r.getHeartRate() != null) {

                                Double data = r.getHeartRate().doubleValue();
                                heartRateData.getHealthData().add(data);
                            }
                            if (r.getSkinTemp() != null) {

                                skinTempData.getHealthData().add(r.getSkinTemp());
                            }
                            if (r.getGsr() != null) {

                                gsrData.getHealthData().add(r.getGsr());
                            }
                            if (r.getBloodPressure() != null) {

                                //todo handle blood pressure (it's a string, we need a Double)
                            }
                            if (r.getBodyTemp() != null) {

                                bodyTempData.getHealthData().add(r.getBodyTemp());
                            }
                            if (r.getActivityType() != null) {

                                Double activityLevel
                                        = ViewDataGraphWrapper.activityTypeToValue(r.getActivityType());

                                activityData.getHealthData().add(activityLevel);
                            }

                            heartRateData.getEpoch().add(epoch);
                            skinTempData.getEpoch().add(epoch);
                            gsrData.getEpoch().add(epoch);
                            bloodPressureData.getEpoch().add(epoch);
                            bodyTempData.getEpoch().add(epoch);
                            activityData.getEpoch().add(epoch);
                        }

                        populateDataIntoGraphs(heartRateData);
                        populateDataIntoGraphs(skinTempData);
                        populateDataIntoGraphs(gsrData);
                        populateDataIntoGraphs(bloodPressureData);
                        populateDataIntoGraphs(bodyTempData);
                        populateDataIntoGraphs(activityData);

                        return true;
                    }
                    else return false;
                }

                @Override
                public void onApiResult(Object result) {

                    if (result != null && result instanceof Boolean) {

                        Boolean success = (Boolean) result;

                        if (success) {

                            reDrawGraphs();
                        }
                    }
                    //todo the data failed to load from the API, handle this in the UI here
                }

                @Override
                public void onApiError(Throwable error) {
                    //todo the data failed to load from the API, handle this in the UI here
                }
            });
        }
        catch (IOException e) { Log.e(TAG, "Error occurred while fetching patient data");
    }
}

    private void populateDataIntoGraphs(ViewDataGraphWrapper data)  {

        BarFormatter stepFormatter  = new BarFormatter(Color.parseColor("#FE9A2E"), Color.parseColor("#FAEC84"));
        stepFormatter.getLinePaint().setStrokeWidth(1);
        stepFormatter.getLinePaint().setAntiAlias(false);
        stepFormatter.getVertexPaint().setColor(Color.TRANSPARENT); //the points themselves

        SimpleDateFormat xLabelFormat = new SimpleDateFormat("MMM dd");
        String day = xLabelFormat.format(data.getEpoch().get(0) * 1000);
        double domainStep = findDomainStep(data);

        if (data.getDataType() == ViewDataGraphWrapper.HEART_RATE)  {

            synchronized (MyHealth_ViewData_Frag.this) {

                mHeartRateSeries = new SimpleXYSeries(data.getEpoch(), data.getHealthData(), "Heart Rate");
                mHeartRatePlot.setDomainLabel(day);
                mHeartRatePlot.setDomainStep(XYStepMode.SUBDIVIDE, domainStep);
                mHeartRatePlot.addSeries(mHeartRateSeries, stepFormatter);
            }
        }
        else if (data.getDataType() == ViewDataGraphWrapper.SKIN_TEMP)  {

            synchronized (MyHealth_ViewData_Frag.this) {

                mSkinTempSeries = new SimpleXYSeries(data.getEpoch(), data.getHealthData(), "Skin Temperature");
                mSkinTempPlot.setDomainLabel(day);
                mSkinTempPlot.setDomainStep(XYStepMode.SUBDIVIDE, domainStep);
                mSkinTempPlot.addSeries(mSkinTempSeries, stepFormatter);
            }
        }
        else if (data.getDataType() == ViewDataGraphWrapper.GSR)  {

            synchronized (MyHealth_ViewData_Frag.this) {
                // todo this is GSR now
                mPerspirationSeries = new SimpleXYSeries(data.getEpoch(), data.getHealthData(), "Perspiration");
                mPerspirationPlot.setDomainLabel(day);
                mPerspirationPlot.setDomainStep(XYStepMode.SUBDIVIDE, domainStep);
                mPerspirationPlot.addSeries(mPerspirationSeries, stepFormatter);
            }
        }
        else if (data.getDataType() == ViewDataGraphWrapper.BLOOD_PRESSURE)  {

            synchronized (MyHealth_ViewData_Frag.this) {

                mBloodPressureSeries = new SimpleXYSeries(data.getEpoch(), data.getHealthData(), "Blood Pressure");
                mBloodPressurePlot.setDomainLabel(day);
                mBloodPressurePlot.setDomainStep(XYStepMode.SUBDIVIDE, domainStep);
                mBloodPressurePlot.addSeries(mBloodPressureSeries, stepFormatter);
            }
        }
        else if (data.getDataType() == ViewDataGraphWrapper.BODY_TEMP)  {

            synchronized (MyHealth_ViewData_Frag.this) {

                mBodyTempSeries = new SimpleXYSeries(data.getEpoch(), data.getHealthData(), "Body Temperature");
                mBodyTempPlot.setDomainLabel(day);
                mBodyTempPlot.setDomainStep(XYStepMode.SUBDIVIDE, domainStep);
                mBodyTempPlot.addSeries(mBodyTempSeries, stepFormatter);
            }
        }
        else if (data.getDataType() == ViewDataGraphWrapper.ACTIVITY)  {

            synchronized (MyHealth_ViewData_Frag.this) {

                mActivityTypeSeries = new SimpleXYSeries(data.getEpoch(), data.getHealthData(), "Activity");
                mActivityTypePlot.setDomainLabel(day);
                mActivityTypePlot.setDomainStep(XYStepMode.SUBDIVIDE, domainStep);
                mActivityTypePlot.addSeries(mActivityTypeSeries, stepFormatter);
            }
        }
    }

    private void reDrawGraphs()  {

        synchronized (MyHealth_ViewData_Frag.this) {

            mHeartRatePlot.redraw();
            mSkinTempPlot.redraw();
            mPerspirationPlot.redraw();
            mBloodPressurePlot.redraw();
            mBodyTempPlot.redraw();
            mActivityTypePlot.redraw();
        }
    }

    private double findDomainStep(ViewDataGraphWrapper ar)  {

        long first = ar.getEpoch().get(0);
        long last = ar.getEpoch().get(ar.getEpoch().size()-1);

        double hours = Math.abs((double) ((first - last) / (60 * 60)));
        if (hours < 4)  {

            return 2;
        }
        else  {

            return 4;
        }
    }

    private void setupGraphSettings(XYPlot plot)  {

        plot.getGraphWidget().getBackgroundPaint().setColor(Color.TRANSPARENT);
        plot.getGraphWidget().getGridBackgroundPaint().setColor(Color.WHITE);
        plot.getGraphWidget().setDomainLabelOrientation(-45);
        plot.getGraphWidget().setDomainLabelVerticalOffset(10); //does not offset further

        //set domain label
        plot.getDomainLabelWidget().getLabelPaint().setColor(Color.BLACK);
        plot.getDomainLabelWidget().position(-80, XLayoutStyle.ABSOLUTE_FROM_CENTER, 45, YLayoutStyle.ABSOLUTE_FROM_BOTTOM);

        plot.setGridPadding(10, 20, 10, 0); //padding of bars from edges of plot

        plot.setBorderStyle(XYPlot.BorderStyle.NONE, null, null);
        plot.getGraphWidget().getDomainLabelPaint().setColor(Color.BLACK); //domain labels color
        plot.getGraphWidget().getRangeLabelPaint().setColor(Color.BLACK);
        plot.getLegendWidget().setVisible(false);

        plot.getTitleWidget().getLabelPaint().setColor(Color.BLACK); //title

        plot.setRangeValueFormat(new DecimalFormat("###"));
        plot.setDomainValueFormat(new Format() {

            private SimpleDateFormat dateFormat = new SimpleDateFormat("hh a");

            @Override
            public StringBuffer format(Object obj, StringBuffer buffer, FieldPosition fieldPos)  {

                long stamp = ((Number) obj).longValue() * 1000;
                Date date = new Date(stamp);//- 4 * 60 * 60 * 1000);
                return dateFormat.format(date, buffer, fieldPos);
            }

            @Override
            public Object parseObject(String arg0, ParsePosition arg1) {
                return null;
            }

        });
    }

    private void setupGraphs(View view)  {

        synchronized (MyHealth_ViewData_Frag.this) {

            mHeartRatePlot = (XYPlot) view.findViewById(R.id.gHeartRate_Patient);
            mSkinTempPlot = (XYPlot) view.findViewById(R.id.gSkinTemp_Patient);
            mPerspirationPlot = (XYPlot) view.findViewById(R.id.gPerspiration_Patient);
            mBloodPressurePlot = (XYPlot) view.findViewById(R.id.gBloodPressure_Patient);
            mBodyTempPlot = (XYPlot) view.findViewById(R.id.gBodyTemp_Patient);
            mActivityTypePlot = (XYPlot) view.findViewById(R.id.gActivity_Patient);

            setupGraphSettings(mHeartRatePlot);
            setupGraphSettings(mSkinTempPlot);
            setupGraphSettings(mPerspirationPlot);
            setupGraphSettings(mBloodPressurePlot);
            setupGraphSettings(mBodyTempPlot);
            setupGraphSettings(mActivityTypePlot);
        }

        //todo: figure out base datetimes here
        fetchDataFromServer(null, null);
    }
}