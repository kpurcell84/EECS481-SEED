package edu.umich.seedforandroid.patient.fragments.myhealth;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andylee on 11/12/14.
 */
public class ViewDataGraphWrapper  {

    public static final int HEART_RATE = 0;
    public static final int SKIN_TEMP = 1;
    public static final int GSR = 2;
    public static final int BLOOD_PRESSURE = 3;
    public static final int BODY_TEMP = 4;
    public static final int ACTIVITY = 5;


    protected static final String REM = "Rem";
    protected static final String DEEP = "Deep";
    protected static final String LIGHT = "Light";
    protected static final String STILL = "Still";
    protected static final String WALK = "Walk";
    protected static final String RUN = "Run";
    protected static final String BIKE = "Bike";

    protected static Double REM_VAL = 1.0d;
    protected static Double DEEP_VAL = 2.0d;
    protected static Double LIGHT_VAL = 3.0d;
    protected static Double STILL_VAL = 4.0d;
    protected static Double WALK_VAL = 5.0d;
    protected static Double RUN_VAL = 6.0d;
    protected static Double BIKE_VAL = 7.0d;

    protected List<Double> data = new ArrayList<Double>();
    protected List<Long> epoch = new ArrayList<Long>();
    protected int dataType;

    public ViewDataGraphWrapper(int dataType) {

        this.dataType = dataType;
    }

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

    public static Double activityTypeToValue(String activityType) {

        if (activityType.equals(REM)) return REM_VAL;
        if (activityType.equals(DEEP)) return DEEP_VAL;
        if (activityType.equals(LIGHT)) return LIGHT_VAL;
        if (activityType.equals(STILL)) return STILL_VAL;
        if (activityType.equals(WALK)) return WALK_VAL;
        if (activityType.equals(RUN)) return RUN_VAL;
        if (activityType.equals(BIKE)) return BIKE_VAL;

        return -1.0d;
    }
}
