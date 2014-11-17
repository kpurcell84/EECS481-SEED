package edu.umich.seedforandroid.patient.fragments.mysepsisnurse;

import android.app.Activity;
import android.os.Bundle;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.util.SharedPrefsUtil;

public class UpdatePatientQuestionnaireTimeSlots extends Activity  {

    private SharedPrefsUtil mSharedPrefsUtilInst;
    private int[] morningTimeSlot, eveningTimeSlot;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_patient_questionnaire_time_slots);

        initialSetup();
    }

    private void initialSetup()  {

    }
}