package edu.umich.seedforandroid.patient.fragments.myhealth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

public class AlertsDataWrapper {

    @Expose
    @SerializedName("message")
    private String message = null;

    public void setMessage(String message)  {
        
        this.message = message;
    }

    public String getMessage()  {

        return message;
    }

    //change
    @Expose
    @SerializedName("timeStamp")
    private Calendar timeStamp = null;
    public Calendar getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(Calendar timeStamp) {
        this.timeStamp = timeStamp;
    }
}