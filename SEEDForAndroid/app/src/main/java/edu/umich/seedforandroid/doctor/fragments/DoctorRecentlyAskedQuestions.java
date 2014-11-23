package edu.umich.seedforandroid.doctor.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.umich.seedforandroid.R;

public class DoctorRecentlyAskedQuestions extends Fragment  {

    public DoctorRecentlyAskedQuestions()  {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {

        View view = inflater.inflate(R.layout.fragment_doctor_recently_asked_questions, container, false);

        return view;
    }
}
