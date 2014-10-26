package edu.umich.seedforandroid.patient.fragments.myhealth;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.umich.seedforandroid.R;

public class MyHealth_ViewData_Frag extends Fragment  {

    public MyHealth_ViewData_Frag() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {

        View view = inflater.inflate(R.layout.fragment_my_health__view_data_, container, false);

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setTitle("View My Health Data");

        return view;
    }
}
