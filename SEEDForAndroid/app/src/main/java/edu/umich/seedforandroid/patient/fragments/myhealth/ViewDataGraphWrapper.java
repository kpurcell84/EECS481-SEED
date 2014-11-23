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
    public static final int REM = 5;
    public static final int DEEP = 6;
    public static final int LIGHT = 7;
    public static final int STILL = 8;
    public static final int WALK = 9;
    public static final int RUN = 10;
    public static final int BIKE = 11;


    protected static final String REM_STRING = "Rem";
    protected static final String DEEP_STRING = "Deep";
    protected static final String LIGHT_STRING = "Light";
    protected static final String STILL_STRING = "Still";
    protected static final String WALK_STRING = "Walk";
    protected static final String RUN_STRING = "Run";
    protected static final String BIKE_STRING = "Bike";

    protected static Double REM_VAL = 1d;
    protected static Double DEEP_VAL = 2d;
    protected static Double LIGHT_VAL = 3d;
    protected static Double STILL_VAL = 4d;
    protected static Double WALK_VAL = 5d;
    protected static Double RUN_VAL = 6d;
    protected static Double BIKE_VAL = 7d;

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

        if (activityType.equals(REM_STRING)) return REM_VAL;
        if (activityType.equals(DEEP_STRING)) return DEEP_VAL;
        if (activityType.equals(LIGHT_STRING)) return LIGHT_VAL;
        if (activityType.equals(STILL_STRING)) return STILL_VAL;
        if (activityType.equals(WALK_STRING)) return WALK_VAL;
        if (activityType.equals(RUN_STRING)) return RUN_VAL;
        if (activityType.equals(BIKE_STRING)) return BIKE_VAL;

        return -1.0d;
    }
}