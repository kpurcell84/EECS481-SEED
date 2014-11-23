package edu.umich.seedforandroid.patient.fragments.myhealth;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dominic on 11/21/2014.
 */
public class ViewBloodPressureGraphWrapper extends ViewDataGraphWrapper {

    protected List<Double> upperData = new ArrayList<Double>();

    public ViewBloodPressureGraphWrapper(int dataType) {
        super(dataType);
    }

    public ViewBloodPressureGraphWrapper(List<Double> upperData, List<Double> lowerData,
                                         List<Long> epoch, int dataType) {
        super(lowerData, epoch, dataType);
        upperData = upperData;
    }

    public List<Double> getUpperData() { return upperData; }
    public List<Double> getLowerData() { return this.data; }
}