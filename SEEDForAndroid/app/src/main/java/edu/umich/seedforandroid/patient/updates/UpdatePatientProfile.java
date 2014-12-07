package edu.umich.seedforandroid.patient.updates;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.appspot.umichseed.seed.Seed;
import com.appspot.umichseed.seed.SeedRequest;
import com.appspot.umichseed.seed.model.MessagesPatientPut;

import java.io.IOException;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.account.GoogleAccountManager;
import edu.umich.seedforandroid.api.ApiThread;
import edu.umich.seedforandroid.api.SeedApi;
import edu.umich.seedforandroid.main.MainActivity;
import edu.umich.seedforandroid.patient.MainActivity_Patient;

public class UpdatePatientProfile extends Activity implements View.OnClickListener  {

    //todo this class ensures that the patients email can't be changed, but it may still have UI that makes them think it can. Remove that

    private static final String TAG = UpdatePatientProfile.class.getSimpleName();

    public static final String EXTRA_FIRSTNAME = "eFirstname";
    public static final String EXTRA_LASTNAME = "eLastname";
    public static final String EXTRA_PHONE = "ePhone";
    public static final String EXTRA_DOCTOR_EMAIL = "eDocEmail";

    private EditText etFirstName, etLastName, etEmail, etPhoneNumber;
    private String mFirstName, mLastName, mEmail, mPhoneNumber;
    private String mDoctorEmail;
    private Button bSave;
    private ApiThread mApiThread;
    private GoogleAccountManager mAccountManager;
    private String mPatientEmail = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_patient_profile);
        mApiThread = new ApiThread();

        mAccountManager = new GoogleAccountManager(UpdatePatientProfile.this);
        if (!mAccountManager.tryLogIn())  {

            Log.e(TAG, "FATAL ERROR: Somehow, the user got here without being logged in.");
            notifyUiAuthenticationError();
        }

        Bundle extras = getIntent().getExtras();
        mEmail = mAccountManager.getAccountName();
        mFirstName = extras.getString(EXTRA_FIRSTNAME);
        mLastName = extras.getString(EXTRA_LASTNAME);
        mDoctorEmail = extras.getString(EXTRA_DOCTOR_EMAIL);
        mPhoneNumber = extras.getString(EXTRA_PHONE);

        initialSetup();
    }

    @Override
    public void onDestroy()  {

        super.onDestroy();

        mApiThread.stop();
    }

    private void initialSetup()  {

        getActionBar().hide();

        etFirstName = (EditText) findViewById(R.id.etFirstNamePatient);
        etLastName = (EditText) findViewById(R.id.etLastNamePatient);
        etEmail = (EditText) findViewById(R.id.etEmailPatient);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumberPatient);
        bSave = (Button) findViewById(R.id.bUpdateProfileSavePatient);
        bSave.setOnClickListener(this);

        displayProfileInformation();
    }

    private void displayProfileInformation()  {

        if (stillAlive()) {

            String[] parts = mPhoneNumber.split("-");
            mPhoneNumber = "";
            for (int i = 0; i < parts.length; ++i) {

                mPhoneNumber += parts[i];
            }

            etFirstName.setText(mFirstName);
            etLastName.setText(mLastName);
            etEmail.setText(mEmail);
            etPhoneNumber.setText(mPhoneNumber);
        }
    }

    private void notifyUiAuthenticationError()  {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UpdatePatientProfile.this);
        View convertView = getLayoutInflater().inflate(R.layout.loggedout_alert_title, null);
        alertDialog.setCustomTitle(convertView);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {

                gotoMainActivity();
            }
        });

        // Set the line color
        Dialog d = alertDialog.show();
        int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = d.findViewById(dividerId);
        divider.setBackground(new ColorDrawable(Color.parseColor("#00274c")));
    }

    private void notifyUiApiError() {

        if (stillAlive()) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(UpdatePatientProfile.this);
            View convertView = getLayoutInflater().inflate(R.layout.api_error_alert_title, null);
            alertDialog.setCustomTitle(convertView);

            alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int id) {

                }
            });

            // Set the line color
            Dialog d = alertDialog.show();
            int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
            View divider = d.findViewById(dividerId);
            divider.setBackground(new ColorDrawable(Color.parseColor("#00274c")));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)  {

        getMenuInflater().inflate(R.menu.menu_update_patient_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)  {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void gotoMainActivity()  {

        Intent i = new Intent(UpdatePatientProfile.this, MainActivity.class);
        Bundle extras = new Bundle();
        extras.putInt("tabSelection", MainActivity_Patient.SETTINGS);
        i.putExtras(extras);
        startActivity(i);
    }

    @Override
    public void onClick(View v)  {

        if (v.getId() == R.id.bUpdateProfileSavePatient)  {

            updatePatientInfo();
        }
    }

    private void gotoMainActivityPatient()  {

        Intent i = new Intent(UpdatePatientProfile.this, MainActivity_Patient.class);
        Bundle extras = new Bundle();
        extras.putInt("tabSelection", MainActivity_Patient.PROFILE);
        i.putExtras(extras);
        startActivity(i);
    }

    private void updatePatientInfo()  {

        // todo update the patient info and go back to MainActivity Patient
        String firstname = etFirstName.getText().toString();
        mFirstName = !firstname.isEmpty() ? firstname : mFirstName;
        String lastname = etLastName.getText().toString();
        mLastName = !lastname.isEmpty() ? lastname : mLastName;
        String phoneNumber = etPhoneNumber.getText().toString();
        mPhoneNumber = !phoneNumber.isEmpty() ? phoneNumber : mPhoneNumber;

        try {
            Seed api = SeedApi.getAuthenticatedApi(mAccountManager.getCredential());
            SeedRequest request = api.patient().put(
                    new MessagesPatientPut()
                            .setEmail(mAccountManager.getAccountName())
                            .setFirstName(mFirstName)
                            .setLastName(mLastName)
                            .setDoctorEmail(mDoctorEmail)
                            .setPhone(mPhoneNumber)
            );
            mApiThread.enqueueRequest(request, new ApiThread.ApiResultAction() {
                @Override
                public void onApiResult(Object result) {
                    //ignore result
                    gotoMainActivityPatient();
                }

                @Override
                public void onApiError(Throwable error) {
                    Log.e(TAG, "ERROR: Api Returned error with message " + error.getMessage());
                    notifyUiApiError();
                }
            });
        }
        catch (IOException e) {

            Log.e(TAG, "FATAL ERROR: Could not create API request");
            notifyUiApiError();
        }
    }

    private boolean stillAlive() {

        return !isDestroyed() && !isFinishing();
    }
}