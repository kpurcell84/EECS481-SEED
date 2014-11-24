package edu.umich.seedforandroid.patient.fragments;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.patient.daily_survey.DailySurvey;

public class Patient_Profile_Frag extends Fragment implements View.OnClickListener  {

    private Button bUpdateProfile;

    public Patient_Profile_Frag()  {}

    @Override
    public void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {

        View view = inflater.inflate(R.layout.fragment_patient__profile_, container, false);

        initialSetup(view);

        return view;
    }

    private void initialSetup(View view)  {

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setTitle("Profile");
        setHasOptionsMenu(false);

        bUpdateProfile = (Button) view.findViewById(R.id.bEditProfile);
        bUpdateProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)  {

        if (v.getId() == R.id.bEditProfile)  {

            updateProfile();
        }

    }

    private void updateProfile()  {

        Intent i = new Intent(getActivity(), DailySurvey.class);
        startActivity(i);
    }
}
