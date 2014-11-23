package edu.umich.seedforandroid.doctor.fragments;

public class DoctorAlertsWrapper  {

    private String patientFirstName;
    private String patientLastName;
    private String patientEmail;
    private com.google.api.client.util.DateTime timeAlerted;
    private String priority;

    public DoctorAlertsWrapper(String patientFirstName, String patientLastName,
                               String patientEmail, com.google.api.client.util.DateTime timeAlerted, String priority)  {

        super();
        this.patientFirstName = patientFirstName;
        this.patientLastName = patientLastName;
        this.patientEmail = patientEmail;
        this.timeAlerted = timeAlerted;
        this.priority = priority;
    }

    public String getPatientFirstName()  {

        return patientFirstName;
    }

    public String getPatientLastName()  {

        return patientLastName;
    }

    public String getPatientEmail()  {

        return patientEmail;
    }

    public com.google.api.client.util.DateTime getTimeAlerted()  {

        return timeAlerted;
    }

    public String getPriority()  {

        return priority;
    }
}