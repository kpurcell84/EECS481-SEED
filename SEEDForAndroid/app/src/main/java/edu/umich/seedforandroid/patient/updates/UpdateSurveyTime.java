package edu.umich.seedforandroid.patient.updates;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.patient.MainActivity_Patient;
import edu.umich.seedforandroid.util.SharedPrefsUtil;
import edu.umich.seedforandroid.util.Utils;

public class UpdateSurveyTime extends Activity implements View.OnClickListener  {

    private Button bMorningTimeSlot, bEveningTimeSlot, bSave;
    private SharedPrefsUtil mSharedPrefsUtilInst;
    private int[] mMorningTimeSlot, mEveningTimeSlot;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_survey_time);

        initialSetup();
    }

    private void initialSetup()  {

        getActionBar().hide();
        mSharedPrefsUtilInst = new SharedPrefsUtil(UpdateSurveyTime.this);
        mMorningTimeSlot = new int[2];
        mEveningTimeSlot = new int[2];

        // Buttons
        bMorningTimeSlot = (Button) findViewById(R.id.bMorningTimeUpdate);
        bEveningTimeSlot = (Button) findViewById(R.id.bEveningTimeUpdate);
        bSave = (Button) findViewById(R.id.bSaveTimeSlotsUpdate);
        bMorningTimeSlot.setOnClickListener(this);
        bEveningTimeSlot.setOnClickListener(this);
        bSave.setOnClickListener(this);

        // Get the survey notification times
        mMorningTimeSlot = mSharedPrefsUtilInst.getMorningSurveyNotificationTime(9, 0);
        mEveningTimeSlot = mSharedPrefsUtilInst.getEveningSurveyNotificationTime(21, 0);

        String[] morningTime12Hour = Utils.convert24HourTo12Hour(String.valueOf(mMorningTimeSlot[0]));
        String[] eveningTime12Hour = Utils.convert24HourTo12Hour(String.valueOf(mEveningTimeSlot[0]));

        bMorningTimeSlot.setText(morningTime12Hour[0].concat(":").concat(Utils.formatMinutePretty(mMorningTimeSlot[1])).concat(" ").concat(morningTime12Hour[1]));
        bEveningTimeSlot.setText(eveningTime12Hour[0].concat(":").concat(Utils.formatMinutePretty(mEveningTimeSlot[1])).concat(" ").concat(eveningTime12Hour[1]));
    }

    private void updateMorningTime()  {

        TimePickerDialog tpd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener()  {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute)  {

                if (hourOfDay < 7)  {

                    view.setCurrentHour(7);
                    view.setCurrentMinute(0);
                    Toast.makeText(UpdateSurveyTime.this, "For morning survey notification, please pick a time between 7:00 AM and 10:00 AM", Toast.LENGTH_SHORT).show();
                }

                if (hourOfDay > 10)  {

                    view.setCurrentHour(10);
                    view.setCurrentMinute(0);
                    Toast.makeText(UpdateSurveyTime.this, "For morning survey notification, please pick a time between 7:00 AM and 10:00 AM", Toast.LENGTH_SHORT).show();
                }

                mMorningTimeSlot[0] = view.getCurrentHour();
                mMorningTimeSlot[1] = view.getCurrentMinute();

                String[] time = Utils.convert24HourTo12Hour(String.valueOf(mMorningTimeSlot[0]));
                bMorningTimeSlot.setText(time[0].concat(":").concat(Utils.formatMinutePretty(mMorningTimeSlot[1])).concat(" ").concat(time[1]));
            }
        }, mMorningTimeSlot[0], mMorningTimeSlot[1], false);

        tpd.setTitle("Morning Survey Notification");
        tpd.show();
    }

    private void updateEveningTime()  {

        TimePickerDialog tpd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener()  {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute)  {

//                if (hourOfDay < 19)  {
//
//                    view.setCurrentHour(19);
//                    view.setCurrentMinute(0);
//                    Toast.makeText(UpdateSurveyTime.this, "For evening survey notification, please pick a time between 7:00 PM and 10:00 PM", Toast.LENGTH_SHORT).show();
//                }
//
//                if (hourOfDay > 22)  {
//
//                    view.setCurrentHour(22);
//                    view.setCurrentMinute(0);
//                    Toast.makeText(UpdateSurveyTime.this, "For evening survey notification, please pick a time between 7:00 PM and 10:00 PM", Toast.LENGTH_SHORT).show();
//                }

                mEveningTimeSlot[0] = view.getCurrentHour();
                mEveningTimeSlot[1] = view.getCurrentMinute();

                String[] time = Utils.convert24HourTo12Hour(String.valueOf(mEveningTimeSlot[0]));
                bEveningTimeSlot.setText(time[0].concat(":").concat(Utils.formatMinutePretty(mEveningTimeSlot[1])).concat(" ").concat(time[1]));
            }
        }, mEveningTimeSlot[0], mEveningTimeSlot[1], false);

        tpd.setTitle("Evening Survey Notification");
        tpd.show();
    }

    private void saveTimeSlots()  {

        mSharedPrefsUtilInst.setMorningSurveyNotificationTime(mMorningTimeSlot[0], mMorningTimeSlot[1]);
        mSharedPrefsUtilInst.setEveningSurveyNotificationTime(mEveningTimeSlot[0], mEveningTimeSlot[1]);
        Toast.makeText(getApplicationContext(), "Saved" + String.valueOf(mEveningTimeSlot[0]) + " " + String.valueOf(mEveningTimeSlot[1]), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(UpdateSurveyTime.this, MainActivity_Patient.class);
        Bundle extras = new Bundle();
        extras.putInt("tabSelection", MainActivity_Patient.SETTINGS);
        intent.putExtras(extras);
        startActivity(intent);
    }

    @Override
    public void onClick(View v)  {

        if (v.getId() == R.id.bMorningTimeUpdate)  {

            updateMorningTime();
        }
        else if (v.getId() == R.id.bEveningTimeUpdate)  {

            updateEveningTime();
        }
        else if (v.getId() == R.id.bSaveTimeSlotsUpdate)  {

            saveTimeSlots();
        }
    }
}