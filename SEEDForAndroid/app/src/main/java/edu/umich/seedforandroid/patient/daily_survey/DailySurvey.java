package edu.umich.seedforandroid.patient.daily_survey;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import edu.umich.seedforandroid.R;

public class DailySurvey extends Activity implements View.OnClickListener  {

    private RadioGroup radioGroupAnswer1, radioGroupAnswer2, radioGroupAnswer3, radioGroupAnswer4,
                       radioGroupAnswer5, radioGroupAnswer6, radioGroupAnswer7, radioGroupAnswer8,
                       radioGroupAnswer9, radioGroupAnswer10;
    private Button bSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_survey);

        initialSetup();
    }

    private void initialSetup()  {

        getActionBar().hide();
    }


    @Override
    public void onClick(View v)  {

    }

    @Override
    public void onBackPressed()  {

    }
}