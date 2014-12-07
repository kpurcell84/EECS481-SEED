package edu.umich.seedforandroid.doctor;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appspot.umichseed.seed.Seed;
import com.appspot.umichseed.seed.SeedRequest;
import com.appspot.umichseed.seed.model.MessagesPatientPut;

import java.io.IOException;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.account.GoogleAccountManager;
import edu.umich.seedforandroid.api.ApiThread;
import edu.umich.seedforandroid.api.SeedApi;

public class AddNewPatient extends Activity implements View.OnClickListener  {

    private static final String TAG = AddNewPatient.class.getSimpleName();

    private String mFirstName, mLastName, mEmail, mPhoneNumber;
    private EditText etFirstName, etLastName, etEmail, etPhoneNumber;
    private Button bSubmit;
    private ApiThread mApiThread;
    private GoogleAccountManager mAccountManager;

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

        mApiThread = new ApiThread();
        mAccountManager = new GoogleAccountManager(this);
        if (!mAccountManager.tryLogIn()) {
            //todo alert user that they aren't logged in, navigate to login screen
        }
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

    private void notifyUiApiError() {

        //todo api error occurred
    }

    private void onPatientCreateSuccess() {

        //todo patient was created successfully. Do whatever is necessary
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

            if (mEmail.contains("@gmail.com") == false)  {

                Toast.makeText(AddNewPatient.this, "Please provide an appropriate gmail address", Toast.LENGTH_SHORT).show();
            }

            try {
                Seed api = SeedApi.getAuthenticatedApi(mAccountManager.getCredential());
                SeedRequest request = api.patient().put(
                        new MessagesPatientPut()
                                .setDoctorEmail(mAccountManager.getAccountName())
                                .setEmail(mEmail)
                                .setFirstName(mFirstName)
                                .setLastName(mLastName)
                                .setPhone(mPhoneNumber)
                );
                mApiThread.enqueueRequest(request, new ApiThread.ApiResultAction() {
                    @Override
                    public void onApiResult(Object result) {
                        //who cares what the result is
                        onPatientCreateSuccess();
                    }

                    @Override
                    public void onApiError(Throwable error) {

                        Log.e(TAG, "ERROR: API returned error with message: " + error.getMessage());
                        notifyUiApiError();
                    }
                });
            }
            catch (IOException e) {

                Log.e(TAG, "ERROR: IOException occurred while creating request");
                notifyUiApiError();
            }
        }
    }
}
