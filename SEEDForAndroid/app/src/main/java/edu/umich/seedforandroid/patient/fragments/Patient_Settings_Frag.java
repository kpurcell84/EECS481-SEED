package edu.umich.seedforandroid.patient.fragments;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.patient.updates.UpdateSurveyTime;
import edu.umich.seedforandroid.util.SharedPrefsUtil;
import edu.umich.seedforandroid.util.Utils;

public class Patient_Settings_Frag extends Fragment implements View.OnClickListener  {

    private Button bUpdateSurveyTimeSlots;
    private TextView tvMorningSlot, tvEveningSlot;
    private SharedPrefsUtil sharedPrefsUtilInst;

    public Patient_Settings_Frag() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {

        View view = inflater.inflate(R.layout.fragment_patient__settings_, container, false);

        initialSetup(view);

        return view;
    }

    private void initialSetup(View view)  {

        sharedPrefsUtilInst = new SharedPrefsUtil(getActivity().getApplicationContext());

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setTitle("Settings");

        int[] morningTime = sharedPrefsUtilInst.getMorningSurveyNotificationTime(9, 0);
        int[] eveningTime = sharedPrefsUtilInst.getEveningSurveyNotificationTime(21, 0);

        String[] morningTime12Hour = Utils.convert24HourTo12Hour(String.valueOf(morningTime[0]));
        String[] eveningTime12Hour = Utils.convert24HourTo12Hour(String.valueOf(eveningTime[0]));

        tvMorningSlot = (TextView) view.findViewById(R.id.tvMorningTimeSlot);
        tvEveningSlot = (TextView) view.findViewById(R.id.tvEveningTimeSlot);

        tvMorningSlot.setText(morningTime12Hour[0].concat(":").concat(Utils.formatMinutePretty(morningTime[1])).concat(" ").concat(morningTime12Hour[1]));
        tvEveningSlot.setText(eveningTime12Hour[0].concat(":").concat(Utils.formatMinutePretty(eveningTime[1])).concat(" ").concat(eveningTime12Hour[1]));

        bUpdateSurveyTimeSlots = (Button) view.findViewById(R.id.bUpdateSurveyTimeSlots);
        bUpdateSurveyTimeSlots.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)  {

        if (v.getId() == R.id.bUpdateSurveyTimeSlots)  {

            updateSurveyTimeSlots();
        }
    }

    private void updateSurveyTimeSlots()  {

        Intent intent = new Intent(getActivity().getApplicationContext(), UpdateSurveyTime.class);
        startActivity(intent);
    }
}