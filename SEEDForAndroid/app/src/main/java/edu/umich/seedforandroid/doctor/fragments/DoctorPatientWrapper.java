package edu.umich.seedforandroid.doctor.fragments;

public class DoctorPatientWrapper  {

    private String patientID;
    private String patientFirstName;
    private String patientLastName;
    private String patientPhoneNumber;
    private String patientEmail;

    public DoctorPatientWrapper(String patientID, String patientFirstName, String patientLastName, String patientPhoneNumber, String patientEmail)  {

        super();
        this.patientID = patientID;
        this.patientFirstName = patientFirstName;
        this.patientLastName = patientLastName;
        this.patientPhoneNumber = patientPhoneNumber;
        this.patientEmail = patientEmail;
    }

    public String getPatientID()  {

        return patientID;
    }
    public String getPatientFirstName()  {

        return patientFirstName;
    }
    public String getPatientLastName()  {

        return patientLastName;
    }
    public String getPatientPhoneNumber()  {

        return patientPhoneNumber;
    }
    public String getPatientEmail()  {

        return patientEmail;
    }
}