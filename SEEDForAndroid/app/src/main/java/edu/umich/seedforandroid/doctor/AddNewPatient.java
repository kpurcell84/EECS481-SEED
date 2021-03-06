package edu.umich.seedforandroid.doctor;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import edu.umich.seedforandroid.main.MainActivity;

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


        if (!mAccountManager.tryLogIn())  {

            alertLogIn();
        }
    }

    private void alertLogIn()  {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddNewPatient.this);
        View convertView = getLayoutInflater().inflate(R.layout.loggedout_alert_title, null);
        alertDialog.setCustomTitle(convertView);

        alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id)  {

                navigateHome();
            }
        });

        // Set the line color
        Dialog d = alertDialog.show();
        int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = d.findViewById(dividerId);
        divider.setBackground(new ColorDrawable(Color.parseColor("#00274c")));
    }

    private void navigateHome()  {

        Intent i = new Intent(AddNewPatient.this, MainActivity.class);
        startActivity(i);
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

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddNewPatient.this);
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

    private void onPatientCreateSuccess()  {

        Intent i = new Intent(AddNewPatient.this, MainActivity_Doctor.class);
        Bundle extras = new Bundle();
        extras.putInt("tabSelection", MainActivity_Doctor.MYPATIENTS);
        i.putExtras(extras);
        startActivity(i);
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

            boolean checkInput = true;
            if (mEmail.contains("@gmail.com") == false)  {

                checkInput = false;
                Toast.makeText(AddNewPatient.this, "Please provide an appropriate gmail address", Toast.LENGTH_SHORT).show();
            }

            if (mPhoneNumber.length() != 10)  {

                checkInput = false;
                Toast.makeText(AddNewPatient.this, "Please provide an appropriate phone number (only numbers)", Toast.LENGTH_SHORT).show();
            }

            if (checkInput)  {

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
                    mApiThread.enqueueRequest(request, new ApiThread.ApiResultAction()  {
                        @Override
                        public void onApiResult(Object result)  {
                            //who cares what the result is
                            onPatientCreateSuccess();
                        }

                        @Override
                        public void onApiError(Throwable error)  {

                            Log.e(TAG, "ERROR: API returned error with message: " + error.getMessage());
                            notifyUiApiError();
                        }
                    });
                }
                catch (IOException e)  {

                    Log.e(TAG, "ERROR: IOException occurred while creating request");
                    notifyUiApiError();
                }
            }
        }
    }
}
