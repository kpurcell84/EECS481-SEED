package edu.umich.seedforandroid.patient.fragments;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.umich.seedforandroid.R;

public class Patient_Help_Frag extends Fragment  {

    public Patient_Help_Frag() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {

        View view = inflater.inflate(R.layout.fragment_patient__help_, container, false);

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setTitle("Help");


        return view;
    }
}
