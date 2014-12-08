package edu.umich.seedforandroid.doctor.doctor_update;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import com.appspot.umichseed.seed.model.MessagesDoctorPut;

import java.io.IOException;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.account.GoogleAccountManager;
import edu.umich.seedforandroid.api.ApiThread;
import edu.umich.seedforandroid.api.SeedApi;
import edu.umich.seedforandroid.doctor.MainActivity_Doctor;
import edu.umich.seedforandroid.main.MainActivity;

public class UpdateDoctorProfile extends Activity implements View.OnClickListener  {

    private static final String TAG = UpdateDoctorProfile.class.getSimpleName();

    public static final String EXTRA_FIRSTNAME = "eFirstname";
    public static final String EXTRA_LASTNAME = "eLastname";
    public static final String EXTRA_PHONE = "ePhone";
    public static final String EXTRA_HOSPITAL = "eHospital";

    private EditText etFirstName, etLastName, etPhoneNumber, etHospital;
    private String mFirstName, mLastName, mHospital, mPhoneNumber;
    private Button bSave;
    private ApiThread mApiThread;
    private GoogleAccountManager mAccountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);

        mApiThread = new ApiThread();
        mAccountManager = new GoogleAccountManager(UpdateDoctorProfile.this);
        if (!mAccountManager.tryLogIn())  {

            Log.e(TAG, "FATAL ERROR: Somehow, the user got here without being logged in");
            notifyUiAuthenticationError();
        }

        setContentView(R.layout.activity_update_doctor_profile);

        Bundle extras = getIntent().getExtras();
        mFirstName = extras.getString(EXTRA_FIRSTNAME);
        mLastName = extras.getString(EXTRA_LASTNAME);
        mPhoneNumber = extras.getString(EXTRA_PHONE);
        mHospital = extras.getString(EXTRA_HOSPITAL);
        initialSetup();
    }

    private void initialSetup()  {

        getActionBar().hide();

        etFirstName = (EditText) findViewById(R.id.etFirstNameDoctor);
        etLastName = (EditText) findViewById(R.id.etLastNameDoctor);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumberDoctor);
        etHospital = (EditText) findViewById(R.id.etHospitalDoctor);

        bSave = (Button) findViewById(R.id.bUpdateProfileSaveDoctor);
        bSave.setOnClickListener(this);

        displayProfileInformation();
    }

    private void notifyUiAuthenticationError()  {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UpdateDoctorProfile.this);
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

    private void notifyUiApiError()  {

        if (stillAlive())  {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(UpdateDoctorProfile.this);
            View convertView = getLayoutInflater().inflate(R.layout.api_error_alert_title, null);
            alertDialog.setCustomTitle(convertView);

            alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener()  {

                @Override
                public void onClick(DialogInterface dialog, int id)  {}
            });

            // Set the line color
            Dialog d = alertDialog.show();
            int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
            View divider = d.findViewById(dividerId);
            divider.setBackground(new ColorDrawable(Color.parseColor("#00274c")));
        }
    }

    private void displayProfileInformation()  {

        if (stillAlive()) {

            // Parse the mPhoneNumber
            String[] phoneNumParts = mPhoneNumber.split("-");
            mPhoneNumber = "";
            for (int i = 0; i < phoneNumParts.length; ++i) {

                mPhoneNumber += phoneNumParts[i];
            }

            etFirstName.setText(mFirstName);
            etLastName.setText(mLastName);
            etHospital.setText(mHospital);
            etPhoneNumber.setText(mPhoneNumber);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)  {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update_doctor_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)  {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)  {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveUpdateProfile()  {

        String firstname = etFirstName.getText().toString();
        mFirstName = !firstname.isEmpty() ? firstname : mFirstName;
        String lastname = etLastName.getText().toString();
        mLastName = !lastname.isEmpty() ? lastname : mLastName;
        String hospital = etHospital.getText().toString();
        mHospital = !hospital.isEmpty() ? hospital : mHospital;
        String phone = etPhoneNumber.getText().toString();
        mPhoneNumber = !phone.isEmpty() ? phone : mPhoneNumber;

        try {
            Seed api = SeedApi.getAuthenticatedApi(mAccountManager.getCredential());
            SeedRequest request = api.doctor().put(
                    new MessagesDoctorPut()
                            .setFirstName(mFirstName)
                            .setLastName(mLastName)
                            .setEmail(mAccountManager.getAccountName())
                            .setPhone(mPhoneNumber)
                            .setHospital(mHospital)
            );
            mApiThread.enqueueRequest(request, new ApiThread.ApiResultAction() {
                @Override
                public void onApiResult(Object result) {
                    //ignore result
                    gotoMainActivityDoctor();
                }

                @Override
                public void onApiError(Throwable error) {

                    Log.e(TAG, "FATAL ERROR: Api Returned error with message " + error.getMessage());
                }
            });
        }
        catch (IOException e) {

            Log.e(TAG, "FATAL ERROR: Couldn't create API request");
            notifyUiApiError();
        }
    }

    @Override
    public void onClick(View v)  {

        if (v.getId() == R.id.bUpdateProfileSaveDoctor)  {

            saveUpdateProfile();
        }
    }

    private boolean stillAlive() {

        return !isDestroyed() && !isFinishing();
    }

    private void gotoMainActivity()  {

        Intent i = new Intent(UpdateDoctorProfile.this, MainActivity.class);
        startActivity(i);
    }

    private void gotoMainActivityDoctor()  {

        Intent i = new Intent(UpdateDoctorProfile.this, MainActivity_Doctor.class);
        Bundle extras = new Bundle();
        extras.putInt("tabSelection", MainActivity_Doctor.PROFILE);
        i.putExtras(extras);
        startActivity(i);
    }
}