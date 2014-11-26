package edu.umich.seedforandroid.patient.daily_survey;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.appspot.umichseed.seed.Seed;
import com.appspot.umichseed.seed.SeedRequest;
import com.appspot.umichseed.seed.model.MessagesPManDataPut;

import java.io.IOException;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.account.GoogleAccountManager;
import edu.umich.seedforandroid.api.ApiThread;
import edu.umich.seedforandroid.api.SeedApi;
import edu.umich.seedforandroid.patient.MainActivity_Patient;
import edu.umich.seedforandroid.util.SharedPrefsUtil;

public class DailySurvey extends Activity implements View.OnClickListener  {

    private static final String TAG = DailySurvey.class.getSimpleName();

    private EditText etBodyTemp, etBloodPressureSystolic, etBloodPressureDiastolic;
    private RadioGroup radioGroupAnswer1, radioGroupAnswer2, radioGroupAnswer3, radioGroupAnswer4,
                       radioGroupAnswer5, radioGroupAnswer6, radioGroupAnswer7, radioGroupAnswer8,
                       radioGroupAnswer9, radioGroupAnswer10;
    private Button bSubmit, bChangeTempUnit;
    private String[] mSurveyQuestionAnswers; // 10
    private String mBodyTempType; // Cel or Fahrenheit
    private Double mBodyTemp, mSystolic, mDiastolic;
    private SharedPrefsUtil mSharedPrefs;

    private GoogleAccountManager mAccountManager;
    private ApiThread mApiThread;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_survey);

        mAccountManager = new GoogleAccountManager(this);
        if (!mAccountManager.tryLogIn()) {

            Log.e(TAG, "FATAL ERROR: Somehow, the user got here without being logged in");
            notifyUiAuthenticationError();
        }
        mApiThread = new ApiThread();
        initialSetup();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mApiThread.stop();
    }

    private void initialSetup()  {

        getActionBar().hide();

        mSharedPrefs = new SharedPrefsUtil(DailySurvey.this);
        mSharedPrefs.setNotificationState(SharedPrefsUtil.INACTIVE_NOTIFICATION);
        mSurveyQuestionAnswers = new String[10];

        // Buttons
        bSubmit = (Button) findViewById(R.id.bSubmitSurvey);
        bSubmit.setOnClickListener(this);

        bChangeTempUnit = (Button) findViewById(R.id.bCelsius);
        bChangeTempUnit.setOnClickListener(this);

        // EditText
        etBodyTemp = (EditText) findViewById(R.id.etBodyTemp);
        etBloodPressureSystolic = (EditText) findViewById(R.id.etBloodPressureSystolic);
        etBloodPressureDiastolic = (EditText) findViewById(R.id.etBloodPressureDiastolic);

        // Radio Groups
        radioGroupAnswer1 = (RadioGroup) findViewById(R.id.radioGroup_Question_1);
        radioGroupAnswer2 = (RadioGroup) findViewById(R.id.radioGroup_Question_2);
        radioGroupAnswer3 = (RadioGroup) findViewById(R.id.radioGroup_Question_3);
        radioGroupAnswer4 = (RadioGroup) findViewById(R.id.radioGroup_Question_4);
        radioGroupAnswer5 = (RadioGroup) findViewById(R.id.radioGroup_Question_5);
        radioGroupAnswer6 = (RadioGroup) findViewById(R.id.radioGroup_Question_6);
        radioGroupAnswer7 = (RadioGroup) findViewById(R.id.radioGroup_Question_7);
        radioGroupAnswer8 = (RadioGroup) findViewById(R.id.radioGroup_Question_8);
        radioGroupAnswer9 = (RadioGroup) findViewById(R.id.radioGroup_Question_9);
        radioGroupAnswer10 = (RadioGroup) findViewById(R.id.radioGroup_Question_10);
    }

    @Override
    public void onClick(View v)  {

        if (v.getId() == R.id.bSubmitSurvey)  {

            submitSurvey();
        }
        else if (v.getId() == R.id.bCelsius)  {

            changeTempUnit();
        }
    }

    private void changeTempUnit()  {

        if (bChangeTempUnit.getText().toString().equals("\u2109"))  { // F to C

            // See if the user has input anything
            if (etBodyTemp.getText().toString().equals("") == false)  {

                double temp = Double.parseDouble(etBodyTemp.getText().toString());
                temp -= 32;
                temp *= 5;
                temp /= 9;
                etBodyTemp.setText(String.valueOf(temp));
            }

            bChangeTempUnit.setText("\u2103");
        }
        else  { // C to F

            // See if the user has input anything
            if (etBodyTemp.getText().toString().equals("") == false)  {

                double temp = Double.parseDouble(etBodyTemp.getText().toString());
                temp *= 9;
                temp /= 5;
                temp += 32;
                etBodyTemp.setText(String.valueOf(temp));
            }

            bChangeTempUnit.setText("\u2109");
        }
    }

    private void notifyUiAuthenticationError()  {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DailySurvey.this);
        View convertView = getLayoutInflater().inflate(R.layout.loggedout_alert_title, null);
        alertDialog.setCustomTitle(convertView);
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener()  {

            @Override
            public void onClick(DialogInterface dialog, int id)  {

                goBackToMainActivityPatient();
            }
        });

        // Set the line color
        Dialog d = alertDialog.show();
        int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = d.findViewById(dividerId);
        divider.setBackground(new ColorDrawable(Color.parseColor("#00274c")));
    }

    private void notifyUiApiError()  {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DailySurvey.this);
        View convertView = getLayoutInflater().inflate(R.layout.api_error_alert_title, null);
        alertDialog.setCustomTitle(convertView);
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener()  {

            @Override
            public void onClick(DialogInterface dialog, int id)  {

                goBackToMainActivityPatient();
            }
        });

        // Set the line color
        Dialog d = alertDialog.show();
        int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = d.findViewById(dividerId);
        divider.setBackground(new ColorDrawable(Color.parseColor("#00274c")));
    }

    private void submitSurvey()  {

        // Make sure that all questions are answered
        int answer1 = radioGroupAnswer1.getCheckedRadioButtonId();
        int answer2 = radioGroupAnswer2.getCheckedRadioButtonId();
        int answer3 = radioGroupAnswer3.getCheckedRadioButtonId();
        int answer4 = radioGroupAnswer4.getCheckedRadioButtonId();
        int answer5 = radioGroupAnswer5.getCheckedRadioButtonId();
        int answer6 = radioGroupAnswer6.getCheckedRadioButtonId();
        int answer7 = radioGroupAnswer7.getCheckedRadioButtonId();
        int answer8 = radioGroupAnswer8.getCheckedRadioButtonId();
        int answer9 = radioGroupAnswer9.getCheckedRadioButtonId();
        int answer10 = radioGroupAnswer10.getCheckedRadioButtonId();

        if (answer1 < 0 || answer2 < 0 || answer3 < 0 || answer4 < 0 || answer5 < 0 ||
                answer6 < 0 || answer7 < 0 || answer8 < 0 || answer9 < 0 || answer10 < 0 ||
                etBodyTemp.getText().toString().isEmpty() || etBloodPressureSystolic.getText().toString().isEmpty() ||
                etBloodPressureDiastolic.getText().toString().isEmpty())  {

            popUpAlerDialog();
        }
        else  {

            RadioButton radioButton1 = (RadioButton) findViewById(answer1);
            RadioButton radioButton2 = (RadioButton) findViewById(answer2);
            RadioButton radioButton3 = (RadioButton) findViewById(answer3);
            RadioButton radioButton4 = (RadioButton) findViewById(answer4);
            RadioButton radioButton5 = (RadioButton) findViewById(answer5);
            RadioButton radioButton6 = (RadioButton) findViewById(answer6);
            RadioButton radioButton7 = (RadioButton) findViewById(answer7);
            RadioButton radioButton8 = (RadioButton) findViewById(answer8);
            RadioButton radioButton9 = (RadioButton) findViewById(answer9);
            RadioButton radioButton10 = (RadioButton) findViewById(answer10);

            // Answers
            mSurveyQuestionAnswers[0] = radioButton1.getText().toString();
            mSurveyQuestionAnswers[1] = radioButton2.getText().toString();
            mSurveyQuestionAnswers[2] = radioButton3.getText().toString();
            mSurveyQuestionAnswers[3] = radioButton4.getText().toString();
            mSurveyQuestionAnswers[4] = radioButton5.getText().toString();
            mSurveyQuestionAnswers[5] = radioButton6.getText().toString();
            mSurveyQuestionAnswers[6] = radioButton7.getText().toString();
            mSurveyQuestionAnswers[7] = radioButton8.getText().toString();
            mSurveyQuestionAnswers[8] = radioButton9.getText().toString();
            mSurveyQuestionAnswers[9] = radioButton10.getText().toString();

            mBodyTemp = Double.parseDouble(etBodyTemp.getText().toString());
            mBodyTempType = bChangeTempUnit.getText().toString();

            // Make the unit for the body temperature consistent - make it Celsius
            if (mBodyTempType.equals("℉"))  { //

                mBodyTemp -= 32;
                mBodyTemp *= 5;
                mBodyTemp /= 9;
            }

            mSystolic = Double.parseDouble(etBloodPressureSystolic.getText().toString());
            mDiastolic = Double.parseDouble(etBloodPressureDiastolic.getText().toString());

            try {

                Seed api = SeedApi.getAuthenticatedApi(mAccountManager.getCredential());
                SeedRequest request = api.pManData().put(
                        new MessagesPManDataPut()
                                .setEmail(mAccountManager.getAccountName())
                                .setA1(mSurveyQuestionAnswers[0])
                                .setA2(mSurveyQuestionAnswers[1])
                                .setA3(mSurveyQuestionAnswers[2])
                                .setA4(mSurveyQuestionAnswers[3])
                                .setA5(mSurveyQuestionAnswers[4])
                                .setA6(mSurveyQuestionAnswers[5])
                                .setA7(mSurveyQuestionAnswers[6])
                                .setA8(mSurveyQuestionAnswers[7])
                                .setA9(mSurveyQuestionAnswers[8])
                                .setA10(mSurveyQuestionAnswers[9])
                                .setBodyTemp(mBodyTemp)
                                .setBloodPressure(String.format("%1$s/%2$s", mSystolic, mDiastolic))
                );
                mApiThread.enqueueRequest(request, new ApiThread.ApiResultAction() {
                    @Override
                    public void onApiResult(Object result) {

                        goBackToMainActivityPatient();
                    }

                    @Override
                    public void onApiError(Throwable error) {

                        Log.e(TAG, "API Error: Api returned an error with message " + error.getMessage());
                        notifyUiApiError();
                    }
                });
            } catch (IOException e) {

                Log.e(TAG, "API Error: the API couldn't create the request");
                notifyUiApiError();
            }
        }
    }

    @Override
    public void onBackPressed()  {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DailySurvey.this);
        View convertView = getLayoutInflater().inflate(R.layout.patient_survey_exit_alert, null);
        alertDialog.setCustomTitle(convertView);
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        })
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {

                goBackToMainActivityPatient();
            }
        });

        // Set the line color
        Dialog d = alertDialog.show();
        int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = d.findViewById(dividerId);
        divider.setBackground(new ColorDrawable(Color.parseColor("#00274c")));
    }

    private void popUpAlerDialog()  {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DailySurvey.this);
        View convertView = getLayoutInflater().inflate(R.layout.patient_survey_warning_title, null);
        alertDialog.setCustomTitle(convertView);
        alertDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener()  {

            @Override
            public void onClick(DialogInterface dialog, int id)  {}
        });

        // Set the line color
        Dialog d = alertDialog.show();
        int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = d.findViewById(dividerId);
        divider.setBackground(new ColorDrawable(Color.parseColor("#00274c")));
    }

    private void goBackToMainActivityPatient()  {

        Intent intent = new Intent(DailySurvey.this, MainActivity_Patient.class);
        startActivity(intent);
    }
}