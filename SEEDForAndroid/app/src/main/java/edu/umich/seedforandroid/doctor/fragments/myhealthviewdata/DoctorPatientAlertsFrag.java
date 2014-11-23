package edu.umich.seedforandroid.doctor.fragments.myhealthviewdata;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.umich.seedforandroid.R;

public class DoctorPatientAlertsFrag extends Fragment  {

    public DoctorPatientAlertsFrag()  {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {

        View view = inflater.inflate(R.layout.fragment_doctor_patient_alerts, container, false);
        return view;
    }
}