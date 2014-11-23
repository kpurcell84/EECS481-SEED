package edu.umich.seedforandroid.doctor.patientdata;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import edu.umich.seedforandroid.R;

public class DoctorViewPatientData extends Activity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_patient_data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)  {

        getMenuInflater().inflate(R.menu.menu_doctor_view_patient_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)  {

        int id = item.getItemId();

        if (id == R.id.action_settings)  {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}