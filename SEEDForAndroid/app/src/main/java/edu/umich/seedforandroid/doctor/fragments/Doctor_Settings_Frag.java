package edu.umich.seedforandroid.doctor.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.umich.seedforandroid.R;

public class Doctor_Settings_Frag extends Fragment  {

    public Doctor_Settings_Frag() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {

        return inflater.inflate(R.layout.fragment_doctor__settings_, container, false);
    }
}
