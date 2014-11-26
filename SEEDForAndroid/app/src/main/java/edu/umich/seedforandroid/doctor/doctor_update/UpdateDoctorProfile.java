package edu.umich.seedforandroid.doctor.doctor_update;

import android.app.Activity;
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

public class UpdateDoctorProfile extends Activity implements View.OnClickListener  {

    private static final String TAG = UpdateDoctorProfile.class.getSimpleName();

    private EditText etFirstName, etLastName, etEmail, etPhoneNumber, etHospital;
    private String mFirstName, mLastName, mHospital, mPhoneNumber, mEmail;
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

        initialSetup();
    }

    private void initialSetup()  {

        getActionBar().hide();

        etFirstName = (EditText) findViewById(R.id.etFirstNameDoctor);
        etLastName = (EditText) findViewById(R.id.etLastNameDoctor);
        etEmail = (EditText) findViewById(R.id.etEmailDoctor);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumberDoctor);
        etHospital = (EditText) findViewById(R.id.etHospitalDoctor);

        bSave = (Button) findViewById(R.id.bUpdateProfileSaveDoctor);
        bSave.setOnClickListener(this);

        loadProfileInformation();
    }

    private void notifyUiAuthenticationError()  {

        //todo somehow, the user isn't logged in. Alert them and redirect to MainActivity
    }

    private void notifyUiApiError()  {

        //todo there was an API error. Notify the user
    }

    private void displayProfileInformation(MessagesDoctorPut doctorProfile)  {

        mFirstName = doctorProfile.getFirstName();
        mLastName = doctorProfile.getLastName();
        mHospital = doctorProfile.getHospital();
        mPhoneNumber = doctorProfile.getPhone();

        // Parse the mPhoneNumber
        String[] phoneNumParts = mPhoneNumber.split("-");
        mPhoneNumber = "";
        for (int i = 0; i < phoneNumParts.length; ++i)  {

            mPhoneNumber += phoneNumParts[i];
        }
        mEmail = doctorProfile.getEmail();

        etFirstName.setText(mFirstName);
        etLastName.setText(mLastName);
        etHospital.setText(mHospital);
        etPhoneNumber.setText(mPhoneNumber);
        etEmail.setText(mEmail);
    }

    //todo decide where to call this...probably in onStart?
    private void loadProfileInformation()  {

        try {

            Seed api = SeedApi.getAuthenticatedApi(mAccountManager.getCredential());
            SeedRequest request = api.doctor().get(mAccountManager.getAccountName());
            mApiThread.enqueueRequest(request, new ApiThread.ApiResultAction() {
                @Override
                public void onApiResult(Object result) {

                    if (result != null && result instanceof MessagesDoctorPut) {

                        displayProfileInformation((MessagesDoctorPut)result);
                    }
                    else {

                        Log.e(TAG, "API Error: API returned successfully, but with an invalid datatype (or null)");
                        notifyUiApiError();
                    }
                }

                @Override
                public void onApiError(Throwable error) {

                    Log.e(TAG, "API Error: API returned failure with messsge " + error.getMessage());
                    notifyUiApiError();
                }
            });
        }
        catch (IOException e) {

            Log.e(TAG, "An API Error Occurred: The API couldn't instantiate the request");
            notifyUiApiError();
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

        mFirstName = etFirstName.getText().toString();
        mLastName = etLastName.getText().toString();
        mHospital = etHospital.getText().toString();
        mEmail = etEmail.getText().toString();
        mPhoneNumber = etPhoneNumber.getText().toString();

        // todo save the new information
    }

    @Override
    public void onClick(View v)  {

        if (v.getId() == R.id.bUpdateProfileSaveDoctor)  {

            saveUpdateProfile();
        }
    }
}