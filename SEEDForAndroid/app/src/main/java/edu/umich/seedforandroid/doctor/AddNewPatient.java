package edu.umich.seedforandroid.doctor;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.umich.seedforandroid.R;

public class AddNewPatient extends Activity implements View.OnClickListener  {

    private String mFirstName, mLastName, mEmail, mPhoneNumber;
    private EditText etFirstName, etLastName, etEmail, etPhoneNumber;
    private Button bSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_patient);

        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00274c")));
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#FAFAFA")));
        actionBar.setIcon(R.drawable.seed_system_letter_icon);
        actionBar.setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "Add New Patient" + "</font>")));

        etFirstName = (EditText) findViewById(R.id.etFirstNamePatient);
        etLastName = (EditText) findViewById(R.id.etLastNamePatient);
        etEmail = (EditText) findViewById(R.id.etEmailPatient);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumberPatient);
        bSubmit = (Button) findViewById(R.id.bAddNewPatient);
        bSubmit.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)  {

        getMenuInflater().inflate(R.menu.menu_add_new_patient, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)  {

        int id = item.getItemId();

        if (id == R.id.home)  {

            goToMainActivityDoctor();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void goToMainActivityDoctor()  {

        Intent i = new Intent(AddNewPatient.this, MainActivity_Doctor.class);
        Bundle extras = new Bundle();
        extras.putInt("tabSelection", MainActivity_Doctor.MYPATIENTS);
        i.putExtras(extras);
        startActivity(i);
    }

    @Override
    public void onClick(View v)  {

        if (v.getId() == R.id.bAddNewPatient)  {

            addNewPatient();
        }
    }

    private void addNewPatient()  {

        mFirstName = etFirstName.getText().toString();
        mLastName = etLastName.getText().toString();
        mEmail = etEmail.getText().toString();
        mPhoneNumber = etPhoneNumber.getText().toString();

        if (mFirstName.equals("") || mLastName.equals("") || mEmail.equals("") ||
                mPhoneNumber.equals(""))  {

            Toast.makeText(AddNewPatient.this, "Please include all fields", Toast.LENGTH_SHORT).show();
        }
        else  {

            // todo add new patient to the server


        }
    }
}
