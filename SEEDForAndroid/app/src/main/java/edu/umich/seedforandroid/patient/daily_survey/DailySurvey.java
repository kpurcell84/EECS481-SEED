package edu.umich.seedforandroid.patient.daily_survey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.util.SharedPrefsUtil;

public class DailySurvey extends Activity implements View.OnClickListener  {

    private int mCurrentIndex; // the current index of the survey, from 0 to 9
    private int mQuestionNum; // the question number, from 1 to 10
    private int[] mSurveyIndexArray; // the survey array, randomized
    private TextView tvSurveyQuestion;
    private RadioGroup radioGroupAnswer;
    private RadioButton radioButtonAnswer, radioButtonYes, radioButtonNo;
    private Button bContinueToNextSurvey;
    private SharedPrefsUtil sharedPrefsUtilInst;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_survey);

        unpackExtras();
        initialSetup();
        setQuestionsForThisSurvey();
    }

    private void initialSetup()  {

        tvSurveyQuestion = (TextView) findViewById(R.id.tvSurveyQuestion);
        radioGroupAnswer = (RadioGroup) findViewById(R.id.radioGroupSurvey);
        bContinueToNextSurvey = (Button) findViewById(R.id.bContinueSurvey);
        radioButtonYes = (RadioButton) findViewById(R.id.radioYes);
        radioButtonNo = (RadioButton) findViewById(R.id.radioNo);
        bContinueToNextSurvey.setOnClickListener(this);
        sharedPrefsUtilInst = new SharedPrefsUtil(DailySurvey.this);
    }

    private void setQuestionsForThisSurvey()  {

        mQuestionNum = mSurveyIndexArray[mCurrentIndex];

        switch(mQuestionNum)  {

            case 1:
                tvSurveyQuestion.setText(R.string.survey_1_question);
                break;
            case 2:
                tvSurveyQuestion.setText(R.string.survey_2_question);
                break;
            case 3:
                tvSurveyQuestion.setText(R.string.survey_3_question);
                break;
            case 4:
                tvSurveyQuestion.setText(R.string.survey_4_question);
                break;
            case 5:
                tvSurveyQuestion.setText(R.string.survey_5_question);
                break;
            case 6:
                tvSurveyQuestion.setText(R.string.survey_6_question);
                break;
            case 7:
                tvSurveyQuestion.setText(R.string.survey_7_question);
                break;
            case 8:
                tvSurveyQuestion.setText(R.string.survey_8_question);
                break;
            case 9:
                tvSurveyQuestion.setText(R.string.survey_9_question);
                break;
            case 10:
                tvSurveyQuestion.setText(R.string.survey_10_question);
                break;
        }

        if (sharedPrefsUtilInst.getSurveyQAPair(mQuestionNum, "").contentEquals("yes"))  { // yes

            radioButtonYes.setChecked(true);
            radioButtonNo.setChecked(false);
        }
        else  { // no

            radioButtonYes.setChecked(false);
            radioButtonNo.setChecked(true);
        }
    }

    private void unpackExtras()  {

        Bundle extras = getIntent().getExtras();
        mCurrentIndex = extras.getInt("current_survey_index");
        mSurveyIndexArray = extras.getIntArray("survey_array");
    }

    @Override
    public void onClick(View v)  {

        if (v.getId() == R.id.bContinueSurvey)  {

            continueToNextSurvey();
        }
    }

    private void continueToNextSurvey()  {

        int thisRadioButton = radioGroupAnswer.getCheckedRadioButtonId();

        if (thisRadioButton < 0)  {

            Toast.makeText(DailySurvey.this, "Please select a choice", Toast.LENGTH_SHORT).show();
        }
        else  {

            radioButtonAnswer = (RadioButton) findViewById(thisRadioButton);
            sharedPrefsUtilInst.setSurveyQAPair(mQuestionNum, radioButtonAnswer.getText().toString());

            if (mCurrentIndex < 9)  {

                Intent i = new Intent(DailySurvey.this, DailySurvey.class);

                Bundle extras = new Bundle();
                mCurrentIndex++;
                extras.putInt("current_survey_index", mCurrentIndex);
                extras.putIntArray("survey_array", mSurveyIndexArray);
                i.putExtras(extras);

                startActivity(i);
            }
            else  { // This is the last survey question. Take the user to submit page.

                Intent i = new Intent(DailySurvey.this, DailySurveySubmit.class);
                startActivity(i);
            }
        }
    }

    @Override
    public void onBackPressed()  {

        if (mCurrentIndex > 0)  {

            Intent i = new Intent(DailySurvey.this, DailySurvey.class);

            Bundle extras = new Bundle();
            mCurrentIndex--;
            extras.putInt("current_survey_index", mCurrentIndex);
            extras.putIntArray("survey_array", mSurveyIndexArray);
            i.putExtras(extras);

            startActivity(i);
        }
        else  {

            Intent i = new Intent(DailySurvey.this, EnterNewHealthData.class);
            startActivity(i);
        }
    }
}