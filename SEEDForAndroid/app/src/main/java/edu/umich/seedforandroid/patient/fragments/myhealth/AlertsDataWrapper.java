package edu.umich.seedforandroid.patient.fragments.myhealth;

import com.google.api.client.util.DateTime;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

public class AlertsDataWrapper implements Comparable<AlertsDataWrapper>{

    @Expose
    @SerializedName("message")
    private String message = null;

    public AlertsDataWrapper setMessage(String message)  {
        
        this.message = message;
        return this;
    }

    public String getMessage()  {

        return message;
    }

    //change
    @Expose
    @SerializedName("timeStamp")
    private DateTime timeStamp = null;
    public DateTime getTimeStamp() {
        return timeStamp;
    }
    public AlertsDataWrapper setTimeStamp(DateTime timeStamp) {

        this.timeStamp = timeStamp;
        return this;
    }

    @Override
    public boolean equals(Object o) {

        if (o != null && o instanceof AlertsDataWrapper) {
            return this.timeStamp == ((AlertsDataWrapper) o).timeStamp;
        }

        return false;
    }

    @Override
    public int hashCode() {

        if (timeStamp == null) return -1;
        return timeStamp.hashCode();
    }

    @Override
    public int compareTo(AlertsDataWrapper wrapper) {

        return getComparator().compare(this, wrapper);
    }

    public static Comparator<AlertsDataWrapper> getComparator() {

        return new Comparator<AlertsDataWrapper>() {

            @Override
            public int compare(AlertsDataWrapper w1, AlertsDataWrapper w2) {

                int diff = (int)(w1.getTimeStamp().getValue() - w2.getTimeStamp().getValue());
                if (diff == 0) {

                    diff = w1.getMessage().compareTo(w2.getMessage());
                }

                return diff;
            }
        };
    }
}