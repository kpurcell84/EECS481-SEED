package edu.umich.seedforandroid.patient.fragments.myhealth;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andylee on 11/12/14.
 */
public class ViewDataGraphWrapper  {

    public static final int HEART_RATE = 0;
    public static final int SKIN_TEMP = 1;
    public static final int PERSPIRATION = 2;
    public static final int BLOOD_PRESSURE = 3;
    public static final int BODY_TEMP = 4;
    public static final int ACTIVITY = 5;

    private List<Double> data = new ArrayList<Double>();
    private List<Long> epoch = new ArrayList<Long>();
    private int dataType;

    public ViewDataGraphWrapper(List<Double> data, List<Long> epoch, int dataType)  {

        this.data = data;
        this.epoch = epoch;
        this.dataType = dataType;
    }

    public List<Double> getHealthData()  {

        return data;
    }

    public List<Long> getEpoch()  {

        return epoch;
    }

    public int getDataType()  {

        return dataType;
    }
}
