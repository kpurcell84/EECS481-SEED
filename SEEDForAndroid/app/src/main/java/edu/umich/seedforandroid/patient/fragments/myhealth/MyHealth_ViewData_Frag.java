package edu.umich.seedforandroid.patient.fragments.myhealth;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.DatePicker;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.account.GoogleAccountManager;
import edu.umich.seedforandroid.api.ApiThread;
import edu.umich.seedforandroid.api.SeedApi;
import edu.umich.seedforandroid.main.MainActivity;
import edu.umich.seedforandroid.util.SharedPrefsUtil;
import edu.umich.seedforandroid.util.Utils;

public class MyHealth_ViewData_Frag extends Fragment implements View.OnClickListener  {

    private static final String TAG = MyHealth_ViewData_Frag.class.getSimpleName();

    public static final String ARG_PATIENT_EMAIL = "forPatientEmail";

    private String mPatientEmail = null;
    private SharedPrefsUtil sharedPrefsUtilInst;
    private RelativeLayout mHeartRateLayout, mSkinTempLayout, mPerspirationLayout, mBloodPressureLayout, mBodyTempLayout, mActivityTypeLayout;
    private Menu mMenu;
    private XYPlot mHeartRatePlot, mSkinTempPlot, mPerspirationPlot, mBloodPressurePlot, mBodyTempPlot, mActivityTypePlot;
    private XYSeries mHeartRateSeries, mSkinTempSeries, mPerspirationSeries, mBloodPressureSeriesUpper, mBloodPressureSeriesLower, mBodyTempSeries, mREMSeries, mDeepSeries,
            mLightSeries, mStillSeries, mWalkSeries, mRunSeries, mBikeSeries;
    private ApiThread mApiThread;
    private CharSequence[] mGraphDialogItems = {" Heart Rate ", " Activities Engaged ", " Perspiration ", " Skin Temperature ", " Body Temperature ", " Blood Pressure"};
    private ArrayList<Integer> mSeletedGraphDialogItems;
    private String defValueGraphFilters = "0@1@2@3@4@5";
    private Button bNextDate, bPrevDate, bDate;
    private Calendar mCurrentCalendar, mTodayCalendar; // mTodayCalendar for checking nextdate (make sure not future)
    private TextView tvNoData;

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

        if (item.getItemId() == R.id.action_refresh) { // rotate the refresh icon

            startProgressBar();
            getDataFromServerBasedOnThis(mCurrentCalendar);
            return true;
        }
        else if (item.getItemId() == R.id.action_graph_options)  {

            showGraphFilterDiaglog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void startProgressBar()  {

        if (mMenu != null)  {

            final MenuItem refreshItem = mMenu.findItem(R.id.action_refresh);
            if (refreshItem != null)  {

                refreshItem.setActionView(R.layout.action_refresh_image);
            }
        }
    }

    public void stopProgressBar()  {

        if (stillAlive())  {
            if (mMenu != null)  {

                final MenuItem refreshItem = mMenu.findItem(R.id.action_refresh);

                if (refreshItem != null)  {

                    refreshItem.setActionView(null);
                }
            }
        }
    }

    private void showGraphFilterDiaglog()  {

        // arraylist to keep the selected items
        mSeletedGraphDialogItems = new ArrayList();

        boolean[] checkedSelections = new boolean[6];
        Arrays.fill(checkedSelections, Boolean.FALSE);

        String userSelections = sharedPrefsUtilInst.getPatientGraphFilter(defValueGraphFilters);

        if (userSelections.equals("") == false)  {

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

            if (index == 0 && mHeartRateSeries != null && mHeartRateSeries.size() != 0)  {

                mHeartRateLayout.setVisibility(View.VISIBLE);
            }
            else if (index == 1)  {

                if ((mREMSeries == null || mREMSeries.size() == 0) && (mLightSeries == null || mLightSeries.size() == 0) &&
                    (mDeepSeries == null || mDeepSeries.size() == 0) && (mStillSeries == null || mStillSeries.size() == 0) &&
                    (mWalkSeries == null || mWalkSeries.size() == 0) && (mRunSeries == null || mRunSeries.size() == 0) &&
                    (mBikeSeries == null || mBikeSeries.size() == 0))  {}
                else  {

                    mActivityTypeLayout.setVisibility(View.VISIBLE);
                }
            }
            else if (index == 2 && mPerspirationSeries != null && mPerspirationSeries.size() != 0)  {

                mPerspirationLayout.setVisibility(View.VISIBLE);
            }
            else if (index == 3 && mSkinTempSeries != null && mSkinTempSeries.size() != 0)  {

                mSkinTempLayout.setVisibility(View.VISIBLE);
            }
            else if (index == 4 && mBodyTempSeries != null && mBodyTempSeries.size() != 0)  {

                mBodyTempLayout.setVisibility(View.VISIBLE);
            }
            else if (index == 5 && mBloodPressureSeriesUpper != null && mBloodPressureSeriesUpper.size() != 0 &&
                    mBloodPressureSeriesLower != null && mBloodPressureSeriesLower.size() != 0)  {

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

        bDate = (Button) view.findViewById(R.id.bDataTimeStamp);
        bNextDate = (Button) view.findViewById(R.id.bNextRight);
        bPrevDate = (Button) view.findViewById(R.id.bNextLeft);
        tvNoData = (TextView) view.findViewById(R.id.tvNoDataViewGraphsPatient);
        bNextDate.setOnClickListener(this);
        bPrevDate.setOnClickListener(this);
        bDate.setOnClickListener(this);
        tvNoData.setVisibility(View.GONE);

        getTodayDate();

        setupGraphs(view);

        // For today's date, set the time to be 00:01 and the ending time to be 11:59 PM
        getDataFromServerBasedOnThis(mCurrentCalendar);

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

                //Log.i("ON CREATE VIEW Parts ".concat(String.valueOf(parts[i])), "@@@@@@@@@");
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

    private void alertLogInError()  {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        View convertView = getActivity().getLayoutInflater().inflate(R.layout.loggedout_alert_title, null);
        alertDialog.setCustomTitle(convertView);

        alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener()  {

            @Override
            public void onClick(DialogInterface dialog, int id)  {

                goBackToLogInPage();
            }
        });

        // Set the line color
        Dialog d = alertDialog.show();
        int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = d.findViewById(dividerId);
        divider.setBackground(new ColorDrawable(Color.parseColor("#00274c")));
    }

    private void notifyUiApiError() {

        if (stillAlive()) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            View convertView = getActivity().getLayoutInflater().inflate(R.layout.api_error_alert_title, null);
            alertDialog.setCustomTitle(convertView);

            alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int id) {

                }
            });

            // Set the line color
            Dialog d = alertDialog.show();
            int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
            View divider = d.findViewById(dividerId);
            divider.setBackground(new ColorDrawable(Color.parseColor("#00274c")));
        }
    }

    private void fetchDataFromServer(DateTime begin, DateTime end)  {

        GoogleAccountManager manager = new GoogleAccountManager(getActivity());

        if (!manager.tryLogIn())  {

            Log.e(TAG, "FATAL ERROR: Somehow, user is on this page without being logged in");
            alertLogInError();
        }

        try  {

            startProgressBar();

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

                        MessagesPQuantDataListResponse castedResult = (MessagesPQuantDataListResponse) result;

                        ViewDataGraphWrapper heartRateData = new ViewDataGraphWrapper(ViewDataGraphWrapper.HEART_RATE);
                        ViewDataGraphWrapper skinTempData = new ViewDataGraphWrapper(ViewDataGraphWrapper.SKIN_TEMP);
                        ViewDataGraphWrapper gsrData = new ViewDataGraphWrapper(ViewDataGraphWrapper.GSR);
                        ViewBloodPressureGraphWrapper bloodPressureData = new ViewBloodPressureGraphWrapper(ViewDataGraphWrapper.BLOOD_PRESSURE);
                        ViewDataGraphWrapper bodyTempData = new ViewDataGraphWrapper(ViewDataGraphWrapper.BODY_TEMP);
                        ViewDataGraphWrapper remData = new ViewDataGraphWrapper(ViewDataGraphWrapper.REM);
                        ViewDataGraphWrapper deepData = new ViewDataGraphWrapper(ViewDataGraphWrapper.DEEP);
                        ViewDataGraphWrapper lightData = new ViewDataGraphWrapper(ViewDataGraphWrapper.LIGHT);
                        ViewDataGraphWrapper stillData = new ViewDataGraphWrapper(ViewDataGraphWrapper.STILL);
                        ViewDataGraphWrapper walkData = new ViewDataGraphWrapper(ViewDataGraphWrapper.WALK);
                        ViewDataGraphWrapper runData = new ViewDataGraphWrapper(ViewDataGraphWrapper.RUN);
                        ViewDataGraphWrapper bikeData = new ViewDataGraphWrapper(ViewDataGraphWrapper.BIKE);

                        List<MessagesPQuantDataResponse> dataList = castedResult.getPdataList();
                        if (dataList != null)  {

                            Collections.sort(dataList, new Comparator<MessagesPQuantDataResponse>()  {
                                @Override
                                public int compare(MessagesPQuantDataResponse messagesPQuantDataResponse, MessagesPQuantDataResponse messagesPQuantDataResponse2) {
                                    long diff = messagesPQuantDataResponse.getTimeTaken().getValue() -
                                            messagesPQuantDataResponse2.getTimeTaken().getValue();
                                    if (diff == 0) return 0;
                                    if (diff < 0) return -1;
                                    return 1;
                                }
                            });

                            for (MessagesPQuantDataResponse r : dataList)  {

                                if (r != null && r.getTimeTaken() != null)  {

                                    Long epoch = r.getTimeTaken().getValue();

                                    if (r.getHeartRate() != null)  {

                                        Double data = r.getHeartRate().doubleValue();
                                        heartRateData.getHealthData().add(data);
                                        heartRateData.getEpoch().add(epoch);

                                        //Log.i("EPOCH FROM SERVER", String.valueOf(epoch));
                                        //Log.i("HEART RATE FROM SERVER", String.valueOf(data));
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

                                        double activityLevel = (double) ViewDataGraphWrapper.activityTypeToValue(r.getActivityType());

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
                        }
                        return true;
                    }
                    return false;
                }

                @Override
                public void onApiResult(Object result)  {

                    stopProgressBar();

                    if (result != null && result instanceof Boolean)  {

                        Boolean success = (Boolean) result;

                        if (success)  {

                            reDrawGraphs();
                            return;
                        }
                        else  {

                            hideAllGraphs();
                            checkIfAllGraphRelativeLayoutsAreGone();
                        }
                    }
                    notifyUiApiError();
                }

                @Override
                public void onApiError(Throwable error)  {

                    stopProgressBar();
                    notifyUiApiError();
                }
            });
        }
        catch (IOException e)  {

            Log.e(TAG, "Error occurred while fetching patient data");
        }
    }

    private void checkIfAllGraphRelativeLayoutsAreGone()  { // Displays "No Data" if all graphs are gone

        if (mHeartRateLayout.getVisibility() == View.GONE && mSkinTempLayout.getVisibility() == View.GONE &&
                mBodyTempLayout.getVisibility() == View.GONE && mBloodPressureLayout.getVisibility() == View.GONE &&
                mActivityTypeLayout.getVisibility() == View.GONE && mPerspirationLayout.getVisibility() == View.GONE)  {

            tvNoData.setVisibility(View.VISIBLE);
        }
        else  {

            tvNoData.setVisibility(View.GONE);
        }
    }

    private void populateDataIntoGraphs(ViewBloodPressureGraphWrapper data)  { // For blood pressure

        if (data.getHealthData().size() < 2 || data.getEpoch().size() < 2)  {

            return;
        }

        LineAndPointFormatter stepFormatterUpper = new LineAndPointFormatter();
        stepFormatterUpper.getFillPaint().setColor(Color.TRANSPARENT);
        stepFormatterUpper.getLinePaint().setColor(Color.parseColor("#FE9A2E"));
        stepFormatterUpper.getLinePaint().setStrokeWidth(7);
        stepFormatterUpper.getVertexPaint().setColor(Color.BLACK);
        stepFormatterUpper.getVertexPaint().setStrokeWidth(0);
        stepFormatterUpper.getLinePaint().setAntiAlias(false);

        LineAndPointFormatter stepFormatterLower = new LineAndPointFormatter();
        stepFormatterLower.getFillPaint().setColor(Color.TRANSPARENT);
        stepFormatterLower.getLinePaint().setColor(Color.parseColor("#FF00BF"));
        stepFormatterLower.getLinePaint().setStrokeWidth(7);
        stepFormatterLower.getVertexPaint().setColor(Color.BLACK);
        stepFormatterLower.getVertexPaint().setStrokeWidth(0);
        stepFormatterLower.getLinePaint().setAntiAlias(false);

        double domainStep = findDomainStep(data);

        if (data.getDataType() == ViewBloodPressureGraphWrapper.BLOOD_PRESSURE)  {

            synchronized (MyHealth_ViewData_Frag.this)  {

                mBloodPressureSeriesUpper = new SimpleXYSeries(data.getEpoch(), data.getUpperData(), "Systolic");
                mBloodPressureSeriesLower = new SimpleXYSeries(data.getEpoch(), data.getLowerData(), "Diastolic");

                mBloodPressurePlot.addSeries(mBloodPressureSeriesUpper, stepFormatterUpper);
                mBloodPressurePlot.addSeries(mBloodPressureSeriesLower, stepFormatterLower);
                mBloodPressurePlot.setDomainStep(XYStepMode.SUBDIVIDE, domainStep);
            }
        }
    }

    private void populateDataIntoGraphs(ViewDataGraphWrapper data)  {

        if (data.getHealthData().size() < 2 || data.getEpoch().size() < 2)  {

            return;
        }

        LineAndPointFormatter stepFormatter = new LineAndPointFormatter();
        stepFormatter.getFillPaint().setColor(Color.TRANSPARENT);
        stepFormatter.getLinePaint().setColor(Color.parseColor("#FE9A2E"));
        stepFormatter.getLinePaint().setStrokeWidth(7);
        stepFormatter.getVertexPaint().setColor(Color.parseColor("#FE9A2E"));
        stepFormatter.getVertexPaint().setStrokeWidth(0);
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

                mPerspirationSeries = new SimpleXYSeries(data.getEpoch(), data.getHealthData(), "Galvanic Skin Response");
                mPerspirationPlot.addSeries(mPerspirationSeries, stepFormatter);
                mPerspirationPlot.setDomainStep(XYStepMode.SUBDIVIDE, domainStep);
                mPerspirationPlot.setRangeValueFormat(new DecimalFormat("#.##"));
            }
        }
        else if (data.getDataType() == ViewDataGraphWrapper.BODY_TEMP)  {

            synchronized (MyHealth_ViewData_Frag.this)  {

                mBodyTempSeries = new SimpleXYSeries(data.getEpoch(), data.getHealthData(), "Body Temperature");
                mBodyTempPlot.addSeries(mBodyTempSeries, stepFormatter);
                mBodyTempPlot.setDomainStep(XYStepMode.SUBDIVIDE, domainStep);
                mBodyTempPlot.setRangeValueFormat(new DecimalFormat("##"));
            }
        }
        else if (data.getDataType() == ViewDataGraphWrapper.REM)  {

            synchronized (MyHealth_ViewData_Frag.this)  {

                mREMSeries = new SimpleXYSeries(data.getEpoch(), data.getHealthData(), "REM");
                mActivityTypePlot.setRangeBoundaries(0, 8, BoundaryMode.FIXED);
                stepFormatter.getLinePaint().setColor(Color.TRANSPARENT);
                stepFormatter.getVertexPaint().setColor(Color.parseColor("#9A2EFE"));
                stepFormatter.getVertexPaint().setStrokeWidth(20);
                mActivityTypePlot.addSeries(mREMSeries, stepFormatter);
                mActivityTypePlot.setDomainStep(XYStepMode.SUBDIVIDE, domainStep);
                mActivityTypePlot.getGraphWidget().getRangeLabelPaint().setColor(Color.TRANSPARENT);
            }
        }
        else if (data.getDataType() == ViewDataGraphWrapper.DEEP)  {

            synchronized (MyHealth_ViewData_Frag.this)  {

                mDeepSeries = new SimpleXYSeries(data.getEpoch(), data.getHealthData(), "Deep");
                mActivityTypePlot.setRangeBoundaries(0, 8, BoundaryMode.FIXED);
                stepFormatter.getLinePaint().setColor(Color.TRANSPARENT);
                stepFormatter.getVertexPaint().setColor(Color.parseColor("#FF00BF"));
                stepFormatter.getVertexPaint().setStrokeWidth(20);
                mActivityTypePlot.addSeries(mDeepSeries, stepFormatter);
                mActivityTypePlot.setDomainStep(XYStepMode.SUBDIVIDE, domainStep);
                mActivityTypePlot.getGraphWidget().getRangeLabelPaint().setColor(Color.TRANSPARENT);
            }
        }
        else if (data.getDataType() == ViewDataGraphWrapper.LIGHT)  {

            synchronized (MyHealth_ViewData_Frag.this)  {

                mLightSeries = new SimpleXYSeries(data.getEpoch(), data.getHealthData(), "Light");
                mActivityTypePlot.setRangeBoundaries(0, 8, BoundaryMode.FIXED);
                stepFormatter.getLinePaint().setColor(Color.TRANSPARENT);
                stepFormatter.getVertexPaint().setColor(Color.parseColor("#0B615E"));
                stepFormatter.getVertexPaint().setStrokeWidth(20);
                mActivityTypePlot.addSeries(mLightSeries, stepFormatter);
                mActivityTypePlot.setDomainStep(XYStepMode.SUBDIVIDE, domainStep);
                mActivityTypePlot.getGraphWidget().getRangeLabelPaint().setColor(Color.TRANSPARENT);
            }
        }
        else if (data.getDataType() == ViewDataGraphWrapper.STILL)  {

            synchronized (MyHealth_ViewData_Frag.this)  {

                mStillSeries = new SimpleXYSeries(data.getEpoch(), data.getHealthData(), "Still");
                mActivityTypePlot.setRangeBoundaries(0, 8, BoundaryMode.FIXED);
                stepFormatter.getLinePaint().setColor(Color.TRANSPARENT);
                stepFormatter.getVertexPaint().setColor(Color.parseColor("#FF0000"));
                stepFormatter.getVertexPaint().setStrokeWidth(20);
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
                stepFormatter.getVertexPaint().setStrokeWidth(20);
                mActivityTypePlot.addSeries(mWalkSeries, stepFormatter);
                mActivityTypePlot.setDomainStep(XYStepMode.SUBDIVIDE, domainStep);
                mActivityTypePlot.getGraphWidget().getRangeLabelPaint().setColor(Color.TRANSPARENT);
            }
        }
        else if (data.getDataType() == ViewDataGraphWrapper.RUN)  {

            synchronized (MyHealth_ViewData_Frag.this)  {

                mRunSeries = new SimpleXYSeries(data.getEpoch(), data.getHealthData(), "Run");
                mActivityTypePlot.setRangeBoundaries(0, 8, BoundaryMode.FIXED);
                stepFormatter.getLinePaint().setColor(Color.TRANSPARENT);
                stepFormatter.getVertexPaint().setColor(Color.parseColor("#FF8000"));
                stepFormatter.getVertexPaint().setStrokeWidth(20);
                mActivityTypePlot.addSeries(mRunSeries, stepFormatter);
                mActivityTypePlot.setDomainStep(XYStepMode.SUBDIVIDE, domainStep);
                mActivityTypePlot.setDomainStep(XYStepMode.SUBDIVIDE, domainStep);
                mActivityTypePlot.getGraphWidget().getRangeLabelPaint().setColor(Color.TRANSPARENT);
            }
        }
        else if (data.getDataType() == ViewDataGraphWrapper.BIKE)  {

            synchronized (MyHealth_ViewData_Frag.this)  {

                mBikeSeries = new SimpleXYSeries(data.getEpoch(), data.getHealthData(), "Bike");
                mActivityTypePlot.setRangeBoundaries(0, 8, BoundaryMode.FIXED);
                stepFormatter.getLinePaint().setColor(Color.TRANSPARENT);
                stepFormatter.getVertexPaint().setColor(Color.parseColor("#2E2EFE"));
                stepFormatter.getVertexPaint().setStrokeWidth(20);
                mActivityTypePlot.addSeries(mBikeSeries, stepFormatter);
                mActivityTypePlot.setDomainStep(XYStepMode.SUBDIVIDE, domainStep);
                mActivityTypePlot.getGraphWidget().getRangeLabelPaint().setColor(Color.TRANSPARENT);
            }
        }
    }

    private void reDrawGraphs()  {

        if (stillAlive())  {
            synchronized (MyHealth_ViewData_Frag.this)  {

                mHeartRateLayout.setVisibility(View.VISIBLE);
                mSkinTempLayout.setVisibility(View.VISIBLE);
                mBodyTempLayout.setVisibility(View.VISIBLE);
                mBloodPressureLayout.setVisibility(View.VISIBLE);
                mActivityTypeLayout.setVisibility(View.VISIBLE);
                mPerspirationLayout.setVisibility(View.VISIBLE);

                // Get graph filters preferences
                String graphFilters = sharedPrefsUtilInst.getPatientGraphFilter("");
                if (graphFilters.equals("") == false)  {

                    boolean[] arrBool = new boolean[6];
                    Arrays.fill(arrBool, false);
                    String[] filterParts = graphFilters.split("@");
                    for (int j = 0; j < filterParts.length; ++j)  {

                        int graphInt = Integer.parseInt(filterParts[j]);
                        arrBool[graphInt] = true;
                    }

                    if (arrBool[0])  {

                        mHeartRateLayout.setVisibility(View.VISIBLE);
                    }
                    else  {

                        mHeartRateLayout.setVisibility(View.GONE);
                    }
                    if (arrBool[1]) {

                        mActivityTypeLayout.setVisibility(View.VISIBLE);
                    }
                    else {

                        mActivityTypeLayout.setVisibility(View.GONE);
                    }
                    if (arrBool[2]) {

                        mPerspirationLayout.setVisibility(View.VISIBLE);
                    }
                    else  {

                        mPerspirationLayout.setVisibility(View.GONE);
                    }
                    if (arrBool[3]) {

                        mSkinTempLayout.setVisibility(View.VISIBLE);
                    }
                    else {

                        mSkinTempLayout.setVisibility(View.GONE);
                    }
                    if (arrBool[4]) {

                        mBodyTempLayout.setVisibility(View.VISIBLE);
                    }
                    else {

                        mBodyTempLayout.setVisibility(View.GONE);
                    }
                    if (arrBool[5]) {

                        mBloodPressureLayout.setVisibility(View.VISIBLE);
                    }
                    else {

                        mBloodPressureLayout.setVisibility(View.GONE);
                    }
                }

                // check if any of the series is empty
                if (mHeartRateSeries == null || mHeartRateSeries.size() == 0)  {

                    mHeartRateLayout.setVisibility(View.GONE);
                }
                if (mSkinTempSeries == null || mSkinTempSeries.size() == 0)  {

                    mSkinTempLayout.setVisibility(View.GONE);
                }
                if (mPerspirationSeries == null || mPerspirationSeries.size() == 0)  {

                    mPerspirationLayout.setVisibility(View.GONE);
                }
                if (mBloodPressureSeriesUpper == null || mBloodPressureSeriesUpper.size() == 0) {

                    mBloodPressureLayout.setVisibility(View.GONE);
                }
                if (mBodyTempSeries == null || mBodyTempSeries.size() == 0)  {

                    mBodyTempLayout.setVisibility(View.GONE);
                }

                if ((mREMSeries == null || mREMSeries.size() == 0) && (mDeepSeries == null || mDeepSeries.size() == 0) &&
                        (mLightSeries == null || mLightSeries.size() == 0) && (mStillSeries == null || mStillSeries.size() == 0) &&
                        (mWalkSeries == null || mWalkSeries.size() == 0) && (mRunSeries == null || mRunSeries.size() == 0) &&
                        (mBikeSeries == null || mBikeSeries.size() == 0))  {

                    mActivityTypeLayout.setVisibility(View.GONE);
                }

                checkIfAllGraphRelativeLayoutsAreGone();

                mHeartRatePlot.redraw();
                mSkinTempPlot.redraw();
                mPerspirationPlot.redraw();
                mBloodPressurePlot.redraw();
                mBodyTempPlot.redraw();
                mActivityTypePlot.redraw();
            }
        }
    }

    private double findDomainStep(ViewDataGraphWrapper ar)  {

        long first = ar.getEpoch().get(0);
        long last = ar.getEpoch().get(ar.getEpoch().size() - 1);

        double hours = Math.abs((double) ((first - last) / (60 * 60)));
        if (hours < 4)  {

            return 1;
        }
        else  {

            return 5;
        }
    }

    private void setupGraphSettings(XYPlot plot)  {

        plot.getGraphWidget().getBackgroundPaint().setColor(Color.TRANSPARENT);
        plot.getGraphWidget().getGridBackgroundPaint().setColor(Color.WHITE);

        plot.getGraphWidget().setDomainLabelOrientation(-25);
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

            private SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");

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
    }

    private void getDataFromServerBasedOnThis(Calendar cal)  {

        Calendar calStart = Calendar.getInstance();
        calStart.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 1);

        Calendar calEnd = Calendar.getInstance();
        calEnd.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 23, 59);

        DateTime startDate = new DateTime(calStart.getTimeInMillis());
        DateTime endDate = new DateTime(calEnd.getTimeInMillis());

        // Remove existing series in the graphs
        mHeartRatePlot.clear();
        mSkinTempPlot.clear();
        mPerspirationPlot.clear();
        mBloodPressurePlot.clear();
        mBodyTempPlot.clear();
        mActivityTypePlot.clear();

        mHeartRateSeries = null;
        mSkinTempSeries = null;
        mPerspirationSeries = null;
        mBloodPressureSeriesUpper = null;
        mBloodPressureSeriesLower = null;
        mBodyTempSeries = null;
        mREMSeries = null;
        mDeepSeries = null;
        mLightSeries = null;
        mStillSeries = null;
        mWalkSeries = null;
        mRunSeries = null;
        mBikeSeries = null;

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

        bDate.setText(dayStr.concat(" ").concat(String.valueOf(parts[2])).concat(" ").concat(monthStr).concat(" ").concat(parts[0]));
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

            bDate.setText(dayStr.concat(" ").concat(String.valueOf(parts[2])).concat(" ").concat(monthStr).concat(" ").concat(parts[0]));

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

        bDate.setText(dayStr.concat(" ").concat(String.valueOf(parts[2])).concat(" ").concat(monthStr).concat(" ").concat(parts[0]));

        // Fetch Data
        getDataFromServerBasedOnThis(mCurrentCalendar);
    }

    private void triggerCalendarDialog()  {

        int currYear = mCurrentCalendar.get(Calendar.YEAR);
        int currMonth = mCurrentCalendar.get(Calendar.MONTH);
        int currDay = mCurrentCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener()  {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)  {

                        mCurrentCalendar.set(year, monthOfYear,dayOfMonth);
                        DateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
                        String newDate = dateFormat.format(mCurrentCalendar.getTimeInMillis());
                        String[] parts = newDate.split(":");

                        int day = mCurrentCalendar.get(Calendar.DAY_OF_WEEK);
                        String dayStr = Utils.getDayOfWeekFullString(day);
                        int month = Integer.parseInt(parts[1]);
                        month--;
                        String monthStr = Utils.getMonth(month);

                        bDate.setText(dayStr.concat(" ").concat(String.valueOf(parts[2])).concat(" ").concat(monthStr).concat(" ").concat(String.valueOf(year)));

                        // Fetch Data
                        getDataFromServerBasedOnThis(mCurrentCalendar);
                    }
                }, currYear, currMonth, currDay);
        dpd.setCancelable(true);
        dpd.setCanceledOnTouchOutside(true);
        dpd.show();
    }

    @Override
    public void onClick(View v)  {

        if (v.getId() == R.id.bNextRight)  {

            getNextDateData();
        }
        else if (v.getId() == R.id.bNextLeft)  {

            getPrevDateData();
        }
        else if (v.getId() == R.id.bDataTimeStamp)  {

            triggerCalendarDialog();
        }
    }

    public class ToSort implements Comparable{

        private double data;
        private DateTime time;

        public ToSort(Double val, DateTime time)  {

            this.data = val;
            this.time = time;
        }

        @Override
        public int compareTo(Object o) {

            ToSort f = (ToSort)o;

            if (time.getValue() > f.time.getValue()) {
                return 1;
            }
            else if (time.getValue() <  f.time.getValue()) {
                return -1;
            }
            else {
                return 0;
            }

        }
    }

    private boolean stillAlive() {

        return getView() != null;
    }

    private void goBackToLogInPage()  {

        Intent i = new Intent(getActivity(), MainActivity.class);
        getActivity().startActivity(i);
    }
}