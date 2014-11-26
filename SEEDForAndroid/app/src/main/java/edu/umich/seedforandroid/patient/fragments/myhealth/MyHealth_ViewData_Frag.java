package edu.umich.seedforandroid.patient.fragments.myhealth;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidplot.ui.XLayoutStyle;
import com.androidplot.ui.YLayoutStyle;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;
import com.appspot.umichseed.seed.Seed;
import com.appspot.umichseed.seed.SeedRequest;
import com.appspot.umichseed.seed.model.MessagesPQuantDataListResponse;
import com.appspot.umichseed.seed.model.MessagesPQuantDataRequest;
import com.appspot.umichseed.seed.model.MessagesPQuantDataResponse;
import com.google.api.client.util.DateTime;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.account.GoogleAccountManager;
import edu.umich.seedforandroid.api.ApiThread;
import edu.umich.seedforandroid.api.SeedApi;
import edu.umich.seedforandroid.util.SharedPrefsUtil;
import edu.umich.seedforandroid.util.Utils;

public class MyHealth_ViewData_Frag extends Fragment implements View.OnClickListener  {

    private static final String TAG = MyHealth_ViewData_Frag.class.getSimpleName();

    public static final String ARG_PATIENT_EMAIL = "forPatientEmail";

    private String mPatientEmail = null;
    private SharedPrefsUtil sharedPrefsUtilInst;
    private RelativeLayout mHeartRateLayout, mSkinTempLayout, mPerspirationLayout,
            mBloodPressureLayout, mBodyTempLayout, mActivityTypeLayout;
    private Menu mMenu;
    private XYPlot mHeartRatePlot, mSkinTempPlot, mPerspirationPlot,
            mBloodPressurePlot, mBodyTempPlot, mActivityTypePlot;
    private XYSeries mHeartRateSeries, mSkinTempSeries, mPerspirationSeries,
            mBloodPressureSeries, mBodyTempSeries, mREMSeries, mDeepSeries,
            mLightSeries, mStillSeries, mWalkSeries, mRunSeries, mBikeSeries;
    private ApiThread mApiThread;
    private CharSequence[] mGraphDialogItems = {" Heart Rate ", " Activities Engaged ", " Perspiration ",
            " Skin Temperature ", " Body Temperature ", " Blood Pressure"};
    private ArrayList<Integer> mSeletedGraphDialogItems;
    private String defValueGraphFilters = "0@1@2@3@4@5";
    private Button bNextDate, bPrevDate;
    private TextView tvDate;
    private Calendar mCurrentCalendar, mTodayCalendar;

    public MyHealth_ViewData_Frag()  {}

    @Override
    public void setArguments(Bundle args)  {

        super.setArguments(args);

        mPatientEmail = args.getString(ARG_PATIENT_EMAIL);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mApiThread = new ApiThread();
        mCurrentCalendar = Calendar.getInstance();
        mTodayCalendar = Calendar.getInstance();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)  {

        int id = item.getItemId();

        if (id == R.id.action_refresh) { // rotate the refresh icon

            startProgressBar();
            return true;
        }
        else if (id == R.id.action_graph_options)  {

            showGraphFilterDiaglog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void startProgressBar()  {

        if (mMenu != null) {

            final MenuItem refreshItem = mMenu.findItem(R.id.action_refresh);

            if (refreshItem != null)  {

                refreshItem.setActionView(R.layout.action_refresh_image);
            }
        }
    }

    public void stopProgressBar()  {

        if (mMenu != null) {

            final MenuItem refreshItem = mMenu.findItem(R.id.action_refresh);

            if (refreshItem != null) {

                refreshItem.setActionView(null);
            }
        }
    }

    private void showGraphFilterDiaglog()  {

        // arraylist to keep the selected items
        mSeletedGraphDialogItems = new ArrayList();

        boolean[] checkedSelections = new boolean[6];
        Arrays.fill(checkedSelections, Boolean.FALSE);

        String userSelections = sharedPrefsUtilInst.getPatientGraphFilter(defValueGraphFilters);

        if (userSelections.equals("") == false) {

            String[] parts = userSelections.split("@");

            for (int i = 0; i < parts.length; ++i)  {

                int ind = Integer.parseInt(parts[i]);
                checkedSelections[ind] = true;
                mSeletedGraphDialogItems.add(ind);
            }
        }

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        View convertView = getActivity().getLayoutInflater().inflate(R.layout.patient_alert_dialog_title, null);
        alertDialog.setCustomTitle(convertView);
        alertDialog.setMultiChoiceItems(mGraphDialogItems, checkedSelections, new DialogInterface.OnMultiChoiceClickListener()  {

            @Override
            public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked)  {

                if (isChecked == true)  {

                    mSeletedGraphDialogItems.add(indexSelected);
                }
                else if (mSeletedGraphDialogItems.contains(indexSelected))  {

                    mSeletedGraphDialogItems.remove(Integer.valueOf(indexSelected));
                }
            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener()  {

            @Override
            public void onClick(DialogInterface dialog, int id)  {}
        })
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {

                saveGraphFilters();
            }
        });

        // Set the line color
        Dialog d = alertDialog.show();
        int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = d.findViewById(dividerId);
        divider.setBackground(new ColorDrawable(Color.parseColor("#00274c")));
    }

    private void saveGraphFilters()  {

        // Make all graphs invisible
        hideAllGraphs();

        boolean[] graphArr = new boolean[6];
        Arrays.fill(graphArr, Boolean.FALSE);

        // Show graphs based on selections
        for (int i = 0; i < mSeletedGraphDialogItems.size(); ++i)  {

            int index = mSeletedGraphDialogItems.get(i);

            graphArr[index] = true;

            if (index == 0)  {

                mHeartRateLayout.setVisibility(View.VISIBLE);
            }
            else if (index == 1)  {

                mActivityTypeLayout.setVisibility(View.VISIBLE);
            }
            else if (index == 2)  {

                mPerspirationLayout.setVisibility(View.VISIBLE);
            }
            else if (index == 3)  {

                mSkinTempLayout.setVisibility(View.VISIBLE);
            }
            else if (index == 4)  {

                mBodyTempLayout.setVisibility(View.VISIBLE);
            }
            else if (index == 5)  {

                mBloodPressureLayout.setVisibility(View.VISIBLE);
            }
        }

        String saveVal = "";
        for (int i = 0; i < 6; ++i)  {

            if (graphArr[i]) {

                saveVal += String.valueOf(i);
                if (i < 5)  {

                    saveVal += "@";
                }
            }
        }

        // Save the selections into SharedPrefs
        sharedPrefsUtilInst.setPatientGraphFilter(saveVal);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)  {

        this.mMenu = menu;
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {

        initialSetup();

        View v = null;

        if (Utils.checkInternetConnection(getActivity().getApplicationContext())) { // There is internet connection

            v = fetchPatientHealthData(v, inflater, container);
        }
        else  {

            v = noInternetConnection(v, inflater, container);
        }

        return v;
    }

    private void initialSetup()  {

        sharedPrefsUtilInst = new SharedPrefsUtil(getActivity().getApplicationContext());

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setTitle("View My Health Data");
    }

    private View noInternetConnection(View view, LayoutInflater inflater, ViewGroup container)  {

        view = inflater.inflate(R.layout.fragment_my_health__view_data_no_internet, container, false);
        return view;
    }

    private View fetchPatientHealthData(View view, LayoutInflater inflater, ViewGroup container)  {

        view = inflater.inflate(R.layout.fragment_my_health__view_data_, container, false);

        setupRelativeLayouts(view);

        tvDate = (TextView) view.findViewById(R.id.tvDataTimeStamp);
        bNextDate = (Button) view.findViewById(R.id.bNextRight);
        bPrevDate = (Button) view.findViewById(R.id.bNextLeft);
        bNextDate.setOnClickListener(this);
        bPrevDate.setOnClickListener(this);

        getTodayDate();

        setupGraphs(view);

        return view;
    }

    private void hideAllGraphs()  {

        mHeartRateLayout.setVisibility(View.GONE);
        mSkinTempLayout.setVisibility(View.GONE);
        mBodyTempLayout.setVisibility(View.GONE);
        mBloodPressureLayout.setVisibility(View.GONE);
        mActivityTypeLayout.setVisibility(View.GONE);
        mPerspirationLayout.setVisibility(View.GONE);
    }

    private void setupRelativeLayouts(View view)  {

        mHeartRateLayout = (RelativeLayout) view.findViewById(R.id.relativelayoutHeartRate);
        mActivityTypeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayoutActivitiesEngaged);
        mPerspirationLayout = (RelativeLayout) view.findViewById(R.id.relativeLayoutPerspiration);
        mSkinTempLayout = (RelativeLayout) view.findViewById(R.id.relativeLayoutSkinTemp);
        mBodyTempLayout = (RelativeLayout) view.findViewById(R.id.relativeLayoutBodyTemp);
        mBloodPressureLayout = (RelativeLayout) view.findViewById(R.id.relativeLayoutBloodPressure);

        hideAllGraphs();

        // Based on SharedPrefs, decide which graphs to show
        String graphSelections = sharedPrefsUtilInst.getPatientGraphFilter(defValueGraphFilters);

        if (graphSelections.equals("") == false)  {

            String[] parts = graphSelections.split("@");
            for (int i = 0; i < parts.length; ++i)  {

                Log.i("ON CREATE VIEW Parts ".concat(String.valueOf(parts[i])), "@@@@@@@@@");
                if (parts[i].equals("0"))  {

                    mHeartRateLayout.setVisibility(View.VISIBLE);
                }
                else if (parts[i].equals("1"))  {

                    mActivityTypeLayout.setVisibility(View.VISIBLE);
                }
                else if (parts[i].equals("2"))  {

                    mPerspirationLayout.setVisibility(View.VISIBLE);
                }
                else if (parts[i].equals("3"))  {

                    mSkinTempLayout.setVisibility(View.VISIBLE);
                }
                else if (parts[i].equals("4"))  {

                    mBodyTempLayout.setVisibility(View.VISIBLE);
                }
                else if (parts[i].equals("5"))  {

                    mBloodPressureLayout.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void fetchDataFromServer(DateTime begin, DateTime end)  {

        GoogleAccountManager manager = new GoogleAccountManager(getActivity());

        if (!manager.tryLogIn())  {

            Log.e(TAG, "FATAL ERROR: Somehow, user is on this page without being logged in");
            //todo: handle the probably impossible case of the user reaching this page without being
            //logged in (perhaps possible if they resume to here after clearing app cache)
        }

        try  {

            String patientEmail = mPatientEmail == null ? manager.getAccountName() : mPatientEmail;
            Seed api = SeedApi.getAuthenticatedApi(manager.getCredential());
            SeedRequest getDataRequest = api.pQuantData().get(

                    new MessagesPQuantDataRequest()
                            .setEmail(patientEmail)
                            .setStartTime(begin)
                            .setEndTime(end)
            );

            mApiThread.enqueueRequest(getDataRequest, new ApiThread.ApiResultAction()  {

                @Override
                public Object doInBackground(Object result)  {

                    if (result != null && result instanceof MessagesPQuantDataListResponse)  {

                        MessagesPQuantDataListResponse castedResult =
                                (MessagesPQuantDataListResponse) result;

                        ViewDataGraphWrapper heartRateData =
                                new ViewDataGraphWrapper(ViewDataGraphWrapper.HEART_RATE);
                        ViewDataGraphWrapper skinTempData =
                                new ViewDataGraphWrapper(ViewDataGraphWrapper.SKIN_TEMP);
                        ViewDataGraphWrapper gsrData =
                                new ViewDataGraphWrapper(ViewDataGraphWrapper.GSR);
                        ViewBloodPressureGraphWrapper bloodPressureData =
                                new ViewBloodPressureGraphWrapper(ViewDataGraphWrapper.BLOOD_PRESSURE);
                        ViewDataGraphWrapper bodyTempData =
                                new ViewDataGraphWrapper(ViewDataGraphWrapper.BODY_TEMP);
                        ViewDataGraphWrapper remData =
                                new ViewDataGraphWrapper(ViewDataGraphWrapper.REM);
                        ViewDataGraphWrapper deepData =
                                new ViewDataGraphWrapper(ViewDataGraphWrapper.DEEP);
                        ViewDataGraphWrapper lightData =
                                new ViewDataGraphWrapper(ViewDataGraphWrapper.LIGHT);
                        ViewDataGraphWrapper stillData =
                                new ViewDataGraphWrapper(ViewDataGraphWrapper.STILL);
                        ViewDataGraphWrapper walkData =
                                new ViewDataGraphWrapper(ViewDataGraphWrapper.WALK);
                        ViewDataGraphWrapper runData =
                                new ViewDataGraphWrapper(ViewDataGraphWrapper.RUN);
                        ViewDataGraphWrapper bikeData =
                                new ViewDataGraphWrapper(ViewDataGraphWrapper.BIKE);

                        if (castedResult.getPdataList() != null)  {

                            for (MessagesPQuantDataResponse r : castedResult.getPdataList())  {

                                if (r != null && r.getTimeTaken() != null)  {

                                    Long epoch = r.getTimeTaken().getValue();

                                    if (r.getHeartRate() != null)  {

                                        Double data = r.getHeartRate().doubleValue();
                                        heartRateData.getHealthData().add(data);
                                        heartRateData.getEpoch().add(epoch);

                                        Log.i("EPOCH FROM SERVER", String.valueOf(epoch));
                                        Log.i("HEART RATE FROM SERVER", String.valueOf(data));
                                    }
                                    if (r.getSkinTemp() != null)  {

                                        skinTempData.getHealthData().add(r.getSkinTemp());
                                        skinTempData.getEpoch().add(epoch);
                                    }
                                    if (r.getGsr() != null)  {

                                        gsrData.getHealthData().add(r.getGsr() * 1000);
                                        gsrData.getEpoch().add(epoch);
                                    }
                                    if (r.getBloodPressure() != null)  {

                                        String[] bpParts = r.getBloodPressure().split("/");

                                        if (bpParts.length == 2)  {

                                            bloodPressureData.getUpperData().add(Double.parseDouble(bpParts[0]));
                                            bloodPressureData.getLowerData().add(Double.parseDouble(bpParts[1]));
                                            bloodPressureData.getEpoch().add(epoch);
                                        }
                                        else  {

                                            Log.e(TAG, "Malformed blood pressure data received: "
                                                    + r.getBloodPressure());
                                        }
                                    }
                                    if (r.getBodyTemp() != null)  {

                                        bodyTempData.getHealthData().add(r.getBodyTemp());
                                        bodyTempData.getEpoch().add(epoch);
                                    }
                                    if (r.getActivityType() != null)  {

                                        double activityLevel
                                                = (double) ViewDataGraphWrapper.activityTypeToValue(r.getActivityType());

                                        if (activityLevel == 1)  {

                                            remData.getHealthData().add(activityLevel);
                                            remData.getEpoch().add(epoch);
                                        }
                                        else if (activityLevel == 2)  {

                                            deepData.getHealthData().add(activityLevel);
                                            deepData.getEpoch().add(epoch);
                                        }
                                        else if (activityLevel == 3)  {

                                            lightData.getHealthData().add(activityLevel);
                                            lightData.getEpoch().add(epoch);
                                        }
                                        else if (activityLevel == 4)  {

                                            stillData.getHealthData().add(activityLevel);
                                            stillData.getEpoch().add(epoch);
                                        }
                                        else if (activityLevel == 5)  {

                                            walkData.getHealthData().add(activityLevel);
                                            walkData.getEpoch().add(epoch);
                                        }
                                        else if (activityLevel == 6)  {

                                            runData.getHealthData().add(activityLevel);
                                            runData.getEpoch().add(epoch);
                                        }
                                        else if (activityLevel == 7)  {

                                            bikeData.getHealthData().add(activityLevel);
                                            bikeData.getEpoch().add(epoch);
                                        }
                                    }
                                }
                                else  {

                                    Log.e(TAG, "Error occurred while fetching patient data @@@@@@@@@@@@@");
                                }
                            }
                            populateDataIntoGraphs(heartRateData);
                            populateDataIntoGraphs(skinTempData);
                            populateDataIntoGraphs(gsrData);
                            populateDataIntoGraphs(bloodPressureData);
                            populateDataIntoGraphs(bodyTempData);
                            populateDataIntoGraphs(remData);
                            populateDataIntoGraphs(deepData);
                            populateDataIntoGraphs(lightData);
                            populateDataIntoGraphs(stillData);
                            populateDataIntoGraphs(walkData);
                            populateDataIntoGraphs(runData);
                            populateDataIntoGraphs(bikeData);
                            return true;
                        }
                    }
                    return false;
                }

                @Override
                public void onApiResult(Object result)  {

                    if (result != null && result instanceof Boolean)  {

                        Boolean success = (Boolean) result;

                        if (success)  {

                            reDrawGraphs();
                        }
                    }
                    //todo the data failed to load from the API, handle this in the UI here
                }

                @Override
                public void onApiError(Throwable error)  {
                    //todo the data failed to load from the API, handle this in the UI here
                }
            });
        }
        catch (IOException e)  {
            Log.e(TAG, "Error occurred while fetching patient data");
        }
    }

    private void populateDataIntoGraphs(ViewDataGraphWrapper data)  {

        if (data.getHealthData().isEmpty() || data.getEpoch().isEmpty())  {

            Log.e(TAG, "DATA IS EMPTY #####################");
            return;
        }

        Log.i("POPULATE DATA INTO GRAPH CALLED", "@@@@@@@@@@@@@@@@@@@@@");

        LineAndPointFormatter stepFormatter = new LineAndPointFormatter();
        stepFormatter.getFillPaint().setColor(Color.TRANSPARENT);
        stepFormatter.getLinePaint().setColor(Color.parseColor("#FE9A2E"));
        stepFormatter.getLinePaint().setStrokeWidth(10);
        stepFormatter.getVertexPaint().setColor(Color.BLACK);
        stepFormatter.getVertexPaint().setStrokeWidth(20);
        stepFormatter.getLinePaint().setAntiAlias(false);

        double domainStep = findDomainStep(data);

        if (data.getDataType() == ViewDataGraphWrapper.HEART_RATE)  {

            synchronized (MyHealth_ViewData_Frag.this)  {

                mHeartRateSeries = new SimpleXYSeries(data.getEpoch(), data.getHealthData(), "Heart Rate");
                mHeartRatePlot.addSeries(mHeartRateSeries, stepFormatter);
                mHeartRatePlot.setDomainStep(XYStepMode.SUBDIVIDE, domainStep);
            }
        }
        else if (data.getDataType() == ViewDataGraphWrapper.SKIN_TEMP)  {

            synchronized (MyHealth_ViewData_Frag.this)  {

                mSkinTempSeries = new SimpleXYSeries(data.getEpoch(), data.getHealthData(), "Skin Temperature");
                mSkinTempPlot.addSeries(mSkinTempSeries, stepFormatter);
                mSkinTempPlot.setDomainStep(XYStepMode.SUBDIVIDE, domainStep);
            }
        }
        else if (data.getDataType() == ViewDataGraphWrapper.GSR)  {

            synchronized (MyHealth_ViewData_Frag.this)  {
                // todo this is GSR now
                mPerspirationSeries = new SimpleXYSeries(data.getEpoch(), data.getHealthData(), "Galvanic Skin Response");
                mPerspirationPlot.addSeries(mPerspirationSeries, stepFormatter);
                mPerspirationPlot.setDomainStep(XYStepMode.SUBDIVIDE, domainStep);
                mPerspirationPlot.setRangeValueFormat(new DecimalFormat("#.##"));
            }
        }
        else if (data.getDataType() == ViewDataGraphWrapper.BLOOD_PRESSURE)  {

            synchronized (MyHealth_ViewData_Frag.this)  {

                mBloodPressureSeries = new SimpleXYSeries(data.getEpoch(), data.getHealthData(), "Blood Pressure");
                mBloodPressurePlot.addSeries(mBloodPressureSeries, stepFormatter);
                mBloodPressurePlot.setDomainStep(XYStepMode.SUBDIVIDE, domainStep);
            }
        }
        else if (data.getDataType() == ViewDataGraphWrapper.BODY_TEMP)  {

            synchronized (MyHealth_ViewData_Frag.this)  {

                mBodyTempSeries = new SimpleXYSeries(data.getEpoch(), data.getHealthData(), "Body Temperature");
                mBodyTempPlot.addSeries(mBodyTempSeries, stepFormatter);
                mBodyTempPlot.setDomainStep(XYStepMode.SUBDIVIDE, domainStep);
            }
        }
        else if (data.getDataType() == ViewDataGraphWrapper.REM)  {

            synchronized (MyHealth_ViewData_Frag.this)  {

                mREMSeries = new SimpleXYSeries(data.getEpoch(), data.getHealthData(), "REM");
                mActivityTypePlot.setRangeBoundaries(0, 8, BoundaryMode.FIXED);
                stepFormatter.getLinePaint().setColor(Color.TRANSPARENT);
                stepFormatter.getVertexPaint().setColor(Color.parseColor("#9A2EFE"));
                mActivityTypePlot.addSeries(mREMSeries, stepFormatter);
                mActivityTypePlot.setDomainStep(XYStepMode.SUBDIVIDE, domainStep);
                mActivityTypePlot.getRangeLabelWidget().setVisible(false);
            }
        }
        else if (data.getDataType() == ViewDataGraphWrapper.DEEP)  {

            synchronized (MyHealth_ViewData_Frag.this)  {

                mDeepSeries = new SimpleXYSeries(data.getEpoch(), data.getHealthData(), "Deep");
                mActivityTypePlot.setRangeBoundaries(0, 8, BoundaryMode.FIXED);
                stepFormatter.getLinePaint().setColor(Color.TRANSPARENT);
                stepFormatter.getVertexPaint().setColor(Color.parseColor("#FF00BF"));
                mActivityTypePlot.addSeries(mDeepSeries, stepFormatter);
                mActivityTypePlot.setDomainStep(XYStepMode.SUBDIVIDE, domainStep);
                mActivityTypePlot.getRangeLabelWidget().setVisible(false);
            }
        }
        else if (data.getDataType() == ViewDataGraphWrapper.LIGHT)  {

            synchronized (MyHealth_ViewData_Frag.this)  {

                mLightSeries = new SimpleXYSeries(data.getEpoch(), data.getHealthData(), "Light");
                mActivityTypePlot.setRangeBoundaries(0, 8, BoundaryMode.FIXED);
                stepFormatter.getLinePaint().setColor(Color.TRANSPARENT);
                stepFormatter.getVertexPaint().setColor(Color.parseColor("#0B615E"));
                mActivityTypePlot.addSeries(mLightSeries, stepFormatter);
                mActivityTypePlot.setDomainStep(XYStepMode.SUBDIVIDE, domainStep);
            }
        }
        else if (data.getDataType() == ViewDataGraphWrapper.STILL)  {

            synchronized (MyHealth_ViewData_Frag.this)  {

                mStillSeries = new SimpleXYSeries(data.getEpoch(), data.getHealthData(), "Still");
                mActivityTypePlot.setRangeBoundaries(0, 8, BoundaryMode.FIXED);
                stepFormatter.getLinePaint().setColor(Color.TRANSPARENT);
                stepFormatter.getVertexPaint().setColor(Color.parseColor("#FF0000"));
                mActivityTypePlot.addSeries(mStillSeries, stepFormatter);
                mActivityTypePlot.setDomainStep(XYStepMode.SUBDIVIDE, domainStep);
                mActivityTypePlot.getGraphWidget().getRangeLabelPaint().setColor(Color.TRANSPARENT);

            }
        }
        else if (data.getDataType() == ViewDataGraphWrapper.WALK)  {

            synchronized (MyHealth_ViewData_Frag.this)  {

                mWalkSeries = new SimpleXYSeries(data.getEpoch(), data.getHealthData(), "Walk");
                mActivityTypePlot.setRangeBoundaries(0, 8, BoundaryMode.FIXED);
                stepFormatter.getLinePaint().setColor(Color.TRANSPARENT);
                stepFormatter.getVertexPaint().setColor(Color.parseColor("#21610B"));
                mActivityTypePlot.addSeries(mWalkSeries, stepFormatter);
                mActivityTypePlot.setDomainStep(XYStepMode.SUBDIVIDE, domainStep);
                mActivityTypePlot.setRangeStep(XYStepMode.SUBDIVIDE, 10);
            }
        }
        else if (data.getDataType() == ViewDataGraphWrapper.RUN)  {

            synchronized (MyHealth_ViewData_Frag.this)  {

                mRunSeries = new SimpleXYSeries(data.getEpoch(), data.getHealthData(), "Run");
                mActivityTypePlot.setRangeBoundaries(0, 8, BoundaryMode.FIXED);
                stepFormatter.getLinePaint().setColor(Color.TRANSPARENT);
                stepFormatter.getVertexPaint().setColor(Color.parseColor("#FF8000"));
                mActivityTypePlot.addSeries(mRunSeries, stepFormatter);
                mActivityTypePlot.setDomainStep(XYStepMode.SUBDIVIDE, domainStep);
                mActivityTypePlot.setRangeStep(XYStepMode.SUBDIVIDE, 0);
            }
        }
        else if (data.getDataType() == ViewDataGraphWrapper.BIKE)  {

            synchronized (MyHealth_ViewData_Frag.this)  {

                mBikeSeries = new SimpleXYSeries(data.getEpoch(), data.getHealthData(), "Bike");
                mActivityTypePlot.setRangeBoundaries(0, 8, BoundaryMode.FIXED);
                stepFormatter.getLinePaint().setColor(Color.TRANSPARENT);
                stepFormatter.getVertexPaint().setColor(Color.parseColor("#2E2EFE"));
                mActivityTypePlot.addSeries(mBikeSeries, stepFormatter);
                mActivityTypePlot.setDomainStep(XYStepMode.SUBDIVIDE, domainStep);
            }
        }
    }

    private void reDrawGraphs()  {

        synchronized (MyHealth_ViewData_Frag.this)  {

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
        long last = ar.getEpoch().get(ar.getEpoch().size() - 1);

        double hours = Math.abs((double) ((first - last) / (60 * 60)));
        if (hours < 4)  {

            return 2;
        }
        else  {

            return 5;
        }
    }

    private void setupGraphSettings(XYPlot plot)  {

        plot.getGraphWidget().getBackgroundPaint().setColor(Color.TRANSPARENT);
        plot.getGraphWidget().getGridBackgroundPaint().setColor(Color.WHITE);

        plot.getGraphWidget().setDomainLabelOrientation(-45);
        plot.getGraphWidget().setDomainLabelVerticalOffset(10); //does not offset further

        plot.getDomainLabelWidget().getLabelPaint().setColor(Color.BLACK);
        plot.getDomainLabelWidget().position(-80, XLayoutStyle.ABSOLUTE_FROM_CENTER, 45, YLayoutStyle.ABSOLUTE_FROM_BOTTOM);

        plot.setGridPadding(10, 20, 10, 0); //padding of bars from edges of plot
        plot.setBorderStyle(XYPlot.BorderStyle.NONE, null, null);
        plot.getGraphWidget().getDomainLabelPaint().setColor(Color.BLACK); //domain labels color
        plot.getGraphWidget().getRangeLabelPaint().setColor(Color.BLACK);
        plot.getLegendWidget().setVisible(false);

        plot.getTitleWidget().getLabelPaint().setColor(Color.BLACK); //title

        plot.setRangeValueFormat(new DecimalFormat("###"));
        plot.setDomainValueFormat(new Format()  {

            private SimpleDateFormat dateFormat = new SimpleDateFormat("hh a");

            @Override
            public StringBuffer format(Object obj, StringBuffer buffer, FieldPosition fieldPos)  {

                long stamp = ((Number) obj).longValue();
                Date date = new Date(stamp);
                return dateFormat.format(date, buffer, fieldPos);
            }

            @Override
            public Object parseObject(String arg0, ParsePosition arg1)  {

                return null;
            }
        });
    }

    private void setupGraphs(View view)  {

        synchronized (MyHealth_ViewData_Frag.this)  {

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

        // For today's date, set the time to be 00:01 and the ending time to be 11:59 PM

        Calendar calStart = Calendar.getInstance();
        getDataFromServerBasedOnThis(calStart);
    }

    private void getDataFromServerBasedOnThis(Calendar cal)  {

        Calendar calStart = Calendar.getInstance();
        calStart.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 1);

        Calendar calEnd = Calendar.getInstance();
        calEnd.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 23, 59);

        DateTime startDate = new DateTime(calStart.getTimeInMillis());
        DateTime endDate = new DateTime(calEnd.getTimeInMillis());

        Log.i("FETCH DATA", "@@@@@@@@@@@@@@@@@@");
        Log.i("START DATE", String.valueOf(calStart.get(Calendar.MONTH)).concat(" ").concat(String.valueOf(calStart.get(Calendar.DAY_OF_MONTH))));
        Log.i("END DATE", String.valueOf(calEnd.get(Calendar.MONTH)).concat(" ").concat(String.valueOf(calEnd.get(Calendar.DAY_OF_MONTH))));

        // Remove existing series in the graphs
        mHeartRatePlot.clear();
        mHeartRateSeries = null;

        fetchDataFromServer(startDate, endDate);
    }

    private void getTodayDate()  {

        DateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
        String today = dateFormat.format(mCurrentCalendar.getTime());
        String[] parts = today.split(":");

        int day = mCurrentCalendar.get(Calendar.DAY_OF_WEEK);
        String dayStr = Utils.getDayOfWeekFullString(day);
        int month = Integer.parseInt(parts[1]);
        month--;
        String monthStr = Utils.getMonth(month);

        tvDate.setText(dayStr.concat(" ").concat(String.valueOf(parts[2])).concat(" ").concat(monthStr));
    }

    private void getNextDateData()  {

        long todayTime = mTodayCalendar.getTimeInMillis();
        long currentTime = mCurrentCalendar.getTimeInMillis();
        currentTime = currentTime + 1000 * 60 * 60 * 24;

        if (currentTime <= todayTime)  {

            mCurrentCalendar = Utils.getNextDate(mCurrentCalendar);
            DateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
            String tomorrow = dateFormat.format(mCurrentCalendar.getTimeInMillis());
            String[] parts = tomorrow.split(":");

            int day = mCurrentCalendar.get(Calendar.DAY_OF_WEEK);
            String dayStr = Utils.getDayOfWeekFullString(day);
            int month = Integer.parseInt(parts[1]);
            month--;
            String monthStr = Utils.getMonth(month);

            tvDate.setText(dayStr.concat(" ").concat(String.valueOf(parts[2])).concat(" ").concat(monthStr));

            // Fetch Data
            getDataFromServerBasedOnThis(mCurrentCalendar);
        }
    }

    private void getPrevDateData()  {

        mCurrentCalendar = Utils.getPrevDate(mCurrentCalendar);
        DateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
        String yesterday = dateFormat.format(mCurrentCalendar.getTimeInMillis());
        String[] parts = yesterday.split(":");

        int day = mCurrentCalendar.get(Calendar.DAY_OF_WEEK);
        String dayStr = Utils.getDayOfWeekFullString(day);
        int month = Integer.parseInt(parts[1]);
        month--;
        String monthStr = Utils.getMonth(month);

        tvDate.setText(dayStr.concat(" ").concat(String.valueOf(parts[2])).concat(" ").concat(monthStr));

        // Fetch Data
        getDataFromServerBasedOnThis(mCurrentCalendar);
    }

    @Override
    public void onClick(View v)  {

        if (v.getId() == R.id.bNextRight)  {

            getNextDateData();
        }
        else if (v.getId() == R.id.bNextLeft)  {

            getPrevDateData();
        }
    }
}