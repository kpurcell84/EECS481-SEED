package edu.umich.seedforandroid.patient.fragments.myhealth;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidplot.xy.XYPlot;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.patient.utility.UtilityPatient;

public class MyHealth_ViewData_Frag extends Fragment  {

    private UtilityPatient utilityPatientInst;
    private XYPlot mHeartRatePlot, mSkinTempPlot,
                   mPerspirationPlot, mBloodPressurePlot, mBodyTempPlot,
                   mActivityTypePlot;

    public MyHealth_ViewData_Frag()  {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {

        initialSetup();

        View v = null;

        if (utilityPatientInst.checkInternetConnection(getActivity().getApplicationContext()))  { // There is internet connection

            v = fetchPatientHealthData(v, inflater, container);
        }
        else  {

            v = noInternetConnection(v, inflater, container);
        }

        return v;
    }

    private void initialSetup()  {

        utilityPatientInst = new UtilityPatient();

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

    private View setupGraphs(View view)  {

       // mHeartRatePlot =

                /*
        plot.getGraphWidget().getBackgroundPaint().setColor(Color.TRANSPARENT);
        //plot.getGraphWidget().setTicksPerDomainLabel(30);
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
            public StringBuffer format(Object obj, StringBuffer buffer,
                                       FieldPosition fieldPos) {
                long stamp = ((Number) obj).longValue() * 1000;
                Date date = new Date(stamp);//- 4 * 60 * 60 * 1000);
                return dateFormat.format(date, buffer, fieldPos);
            }

            @Override
            public Object parseObject(String arg0, ParsePosition arg1) {
                return null;
            }

        });
*/
        return null;
    }
}
