package edu.umich.seedforandroid.patient.fragments.mysepsisnurse;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.umich.seedforandroid.R;

public class PatientFrequentlyAskedQuestionsFragment extends Fragment  {

    public PatientFrequentlyAskedQuestionsFragment()  {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {

        View view = inflater.inflate(R.layout.fragment_my_sepsis_nurse__faq_, container, false);

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setTitle("Frequently Asked Questions");

        return view;
    }
}
