package edu.umich.seedforandroid.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.doctor.MainActivity_Doctor;
import edu.umich.seedforandroid.patient.MainActivity_Patient;

public class MainActivity extends Activity implements View.OnClickListener  {

    Button bDoc, bPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getActionBar().hide();

        bDoc = (Button) findViewById(R.id.bDoctor);
        bPatient = (Button) findViewById(R.id.bPatient);

        bDoc.setOnClickListener(this);
        bPatient.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)  {

        if (v.getId() == R.id.bDoctor)  {

            Intent intent = new Intent(MainActivity.this, MainActivity_Doctor.class);
            startActivity(intent);
        }
        else if (v.getId() == R.id.bPatient)  {

            Intent intent = new Intent(MainActivity.this, MainActivity_Patient.class);
            startActivity(intent);
        }
    }
}
