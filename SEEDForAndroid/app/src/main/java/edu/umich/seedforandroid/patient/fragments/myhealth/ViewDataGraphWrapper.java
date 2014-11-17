package edu.umich.seedforandroid.patient.fragments.myhealth;

import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.XYSeries;

/**
 * Created by andylee on 11/12/14.
 */
public class ViewDataGraphWrapper  {

    private XYSeries mXYseries;
    private BarFormatter mBarFormat;

    public ViewDataGraphWrapper(XYSeries xySeries, BarFormatter barFormat)  {

        this.mXYseries = xySeries;
        this.mBarFormat = barFormat;
    }

    public BarFormatter getBarFormatter()  {

        return mBarFormat;
    }

    public XYSeries getXYSeries()  {

        return mXYseries;
    }
}
