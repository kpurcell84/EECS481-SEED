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
import android.widget.ImageView;

import com.androidplot.ui.XLayoutStyle;
import com.androidplot.ui.YLayoutStyle;
import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.util.Utils;

public class MyHealth_ViewData_Frag extends Fragment  {

    private ImageView mRefreshIv;
    private Utils mUtilsInst;
    private Menu mMenu;
    private CharSequence[] mGraphDialogItems = {" Heart Rate ",
                                                " Activities Engaged ",
                                                " Perspiration ",
                                                " Skin Temperature ",
                                                " Body Temperature ",
                                                " Blood Pressure"};
    private ArrayList<Integer> mSeletedGraphDialogItems;
    private XYPlot mHeartRatePlot, mSkinTempPlot, mPerspirationPlot,
                   mBloodPressurePlot, mBodyTempPlot, mActivityTypePlot;
    private XYSeries mHeartRateSeries, mSkinTempSeries, mPerspirationSeries,
                     mBloodPressureSeries, mBodyTempSeries, mActivityTypeSeries;

    public MyHealth_ViewData_Frag()  {}

    @Override
    public void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPause()  {

        super.onPause();
        stopProgressBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)  {

        final MenuItem itemThis = item;
        int id = item.getItemId();

        if (id == R.id.action_refresh)  { // rotate the refresh icon

            startProgressBar();
            return true;
        }
        else if (id == R.id.action_graph_options)  {

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

        if (mMenu != null)  {

            final MenuItem refreshItem = mMenu.findItem(R.id.action_refresh);

            if (refreshItem != null)  {

                refreshItem.setActionView(null);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)  {

        this.mMenu = menu;
        inflater.inflate(R.menu.main, menu);
        //stopRefreshButtonAnimation();
        super.onCreateOptionsMenu(menu, inflater);
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

    private void showGraphFilterDiaglog()  {

        // arraylist to keep the selected items
        mSeletedGraphDialogItems = new ArrayList();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        View convertView = (View) getActivity().getLayoutInflater().inflate(R.layout.patient_alert_dialog_title, null);
        alertDialog.setCustomTitle(convertView);
        alertDialog.setMultiChoiceItems(mGraphDialogItems, null, new DialogInterface.OnMultiChoiceClickListener()  {
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
            public void onClick(DialogInterface dialog, int id) {}
        })
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id)  {

                for (int i = 0; i < mSeletedGraphDialogItems.size(); ++i)  {

                    int ind = (int) mSeletedGraphDialogItems.get(i);
                    Log.i("INDEX : ".concat(String.valueOf(i)), String.valueOf(ind));
                }
            }
        });

        // Set the line color
        Dialog d = alertDialog.show();
        int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = d.findViewById(dividerId);
        divider.setBackground(new ColorDrawable(Color.parseColor("#00274c")));
    }

    private void initialSetup()  {

        // Instantiate new objects
        mUtilsInst = new Utils();

        // ActionBar
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

    private void populateDataIntoGraphs(ViewDataGraphWrapper data)  {

        BarFormatter stepFormatter  = new BarFormatter(Color.parseColor("#FE9A2E"), Color.parseColor("#FAEC84"));
        stepFormatter.getLinePaint().setStrokeWidth(1);
        stepFormatter.getLinePaint().setAntiAlias(false);
        stepFormatter.getVertexPaint().setColor(Color.TRANSPARENT); //the points themselves

        SimpleDateFormat xLabelFormat = new SimpleDateFormat("MMM dd");
        String day = xLabelFormat.format(data.getEpoch().get(0) * 1000);
        double domainStep = findDomainStep(data);

        if (data.getDataType() == ViewDataGraphWrapper.HEART_RATE)  {

            mHeartRateSeries = new SimpleXYSeries(data.getEpoch(), data.getHealthData(), "Heart Rate");

            mHeartRatePlot.setDomainLabel(day);
            mHeartRatePlot.setDomainStep(XYStepMode.SUBDIVIDE, domainStep);
            mHeartRatePlot.addSeries(mHeartRateSeries, stepFormatter);
        }
        else if (data.getDataType() == ViewDataGraphWrapper.SKIN_TEMP)  {

            mSkinTempSeries = new SimpleXYSeries(data.getEpoch(), data.getHealthData(), "Skin Temperature");

            mSkinTempPlot.setDomainLabel(day);
            mSkinTempPlot.setDomainStep(XYStepMode.SUBDIVIDE, domainStep);
            mSkinTempPlot.addSeries(mSkinTempSeries, stepFormatter);
        }
        else if (data.getDataType() == ViewDataGraphWrapper.PERSPIRATION)  {

            mPerspirationSeries = new SimpleXYSeries(data.getEpoch(), data.getHealthData(), "Perspiration");

            mPerspirationPlot.setDomainLabel(day);
            mPerspirationPlot.setDomainStep(XYStepMode.SUBDIVIDE, domainStep);
            mPerspirationPlot.addSeries(mPerspirationSeries, stepFormatter);
        }
        else if (data.getDataType() == ViewDataGraphWrapper.BLOOD_PRESSURE)  {

            mBloodPressureSeries = new SimpleXYSeries(data.getEpoch(), data.getHealthData(), "Blood Pressure");

            mBloodPressurePlot.setDomainLabel(day);
            mBloodPressurePlot.setDomainStep(XYStepMode.SUBDIVIDE, domainStep);
            mBloodPressurePlot.addSeries(mBloodPressureSeries, stepFormatter);
        }
        else if (data.getDataType() == ViewDataGraphWrapper.BODY_TEMP)  {

            mBodyTempSeries = new SimpleXYSeries(data.getEpoch(), data.getHealthData(), "Body Temperature");

            mBodyTempPlot.setDomainLabel(day);
            mBodyTempPlot.setDomainStep(XYStepMode.SUBDIVIDE, domainStep);
            mBodyTempPlot.addSeries(mBodyTempSeries, stepFormatter);
        }
        else if (data.getDataType() == ViewDataGraphWrapper.ACTIVITY)  {

            mActivityTypeSeries = new SimpleXYSeries(data.getEpoch(), data.getHealthData(), "Activity");

            mActivityTypePlot.setDomainLabel(day);
            mActivityTypePlot.setDomainStep(XYStepMode.SUBDIVIDE, domainStep);
            mActivityTypePlot.addSeries(mActivityTypeSeries, stepFormatter);
        }
    }

    private void reDrawGraphs()  {

        mHeartRatePlot.redraw();
        mSkinTempPlot.redraw();
        mPerspirationPlot.redraw();
        mBloodPressurePlot.redraw();
        mBodyTempPlot.redraw();
        mActivityTypePlot.redraw();
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