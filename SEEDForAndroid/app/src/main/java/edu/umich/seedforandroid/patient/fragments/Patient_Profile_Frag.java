package edu.umich.seedforandroid.patient.fragments;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appspot.umichseed.seed.Seed;
import com.appspot.umichseed.seed.SeedRequest;
import com.appspot.umichseed.seed.model.MessagesPatientPut;

import java.io.IOException;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.account.GoogleAccountManager;
import edu.umich.seedforandroid.api.ApiThread;
import edu.umich.seedforandroid.api.SeedApi;
import edu.umich.seedforandroid.main.MainActivity;
import edu.umich.seedforandroid.patient.updates.UpdatePatientProfile;

public class Patient_Profile_Frag extends Fragment implements View.OnClickListener  {

    private static final String TAG = Patient_Profile_Frag.class.getSimpleName();

    public static final String ARG_PATIENT_EMAIL = "forPatientEmail";

    private Button bUpdateProfile;
    private TextView tvPatientName, tvEmail, tvPhoneNumber;
    private String mFirstName, mLastName, mEmail, mPhoneNumber;
    private ApiThread mApiThread;
    private GoogleAccountManager mAccountManager;
    private String mPatientEmail = null;
    private ProgressBar mProgressBar;

    public Patient_Profile_Frag()  {}

    @Override
    public void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        mApiThread = new ApiThread();

        mAccountManager = new GoogleAccountManager(getActivity());
        if (!mAccountManager.tryLogIn())  {

            Log.e(TAG, "FATAL ERROR: Somehow, the user got here without being logged in.");
            notifyUiAuthenticationError();
        }
    }

    @Override
    public void setArguments(Bundle args)  {

        super.setArguments(args);

        mPatientEmail = args.getString(ARG_PATIENT_EMAIL);
    }

    @Override
    public void onDestroy()  {

        super.onDestroy();

        mApiThread.stop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {

        View view = inflater.inflate(R.layout.fragment_patient__profile_, container, false);

        initialSetup(view);

        return view;
    }

    private void initialSetup(View view)  {

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setTitle("Profile");
        setHasOptionsMenu(false);

        tvPatientName = (TextView) view.findViewById(R.id.tvPatientName);
        tvEmail = (TextView) view.findViewById(R.id.tvEmailPatient);
        tvPhoneNumber = (TextView) view.findViewById(R.id.tvPhoneNumberPatient);

        bUpdateProfile = (Button) view.findViewById(R.id.bEditProfile);
        bUpdateProfile.setOnClickListener(this);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);

        loadProfileInformation();
    }

    @Override
    public void onClick(View v)  {

        if (v.getId() == R.id.bEditProfile)  {

            updateProfile();
        }
    }

    private void updateProfile()  {

        mProgressBar.setVisibility(View.INVISIBLE);

        Intent i = new Intent(getActivity(), UpdatePatientProfile.class);
        startActivity(i);
    }

    private void displayProfileInformation(MessagesPatientPut patientProfile)  {

        mProgressBar.setVisibility(View.INVISIBLE);

        if (stillAlive())  {

            mFirstName = patientProfile.getFirstName();
            mLastName = patientProfile.getLastName();
            mEmail = patientProfile.getEmail();
            mPhoneNumber = patientProfile.getPhone();

            tvPatientName.setText(mFirstName.concat(" ").concat(mLastName));
            tvEmail.setText(mEmail);
            tvPhoneNumber.setText(mPhoneNumber);
        }
    }

    private void notifyUiAuthenticationError()  {

        mProgressBar.setVisibility(View.INVISIBLE);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        View convertView = getActivity().getLayoutInflater().inflate(R.layout.loggedout_alert_title, null);
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

            mProgressBar.setVisibility(View.INVISIBLE);

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            View convertView = getActivity().getLayoutInflater().inflate(R.layout.api_error_alert_title, null);
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

    private void loadProfileInformation()  {

        mProgressBar.setVisibility(View.VISIBLE);

        try {

            String patientEmail = mPatientEmail != null ?
                    mPatientEmail : mAccountManager.getAccountName();
            Seed api = SeedApi.getAuthenticatedApi(mAccountManager.getCredential());
            SeedRequest request = api.patient().get(patientEmail);
            mApiThread.enqueueRequest(request, new ApiThread.ApiResultAction()  {

                @Override
                public void onApiResult(Object result) {

                    if (result != null && result instanceof MessagesPatientPut) {

                        displayProfileInformation((MessagesPatientPut)result);
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

    private void gotoMainActivity()  {

        mProgressBar.setVisibility(View.INVISIBLE);

        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
    }

    private boolean stillAlive()  {

        return getView() != null;
    }

    @Override
    public void onDestroyView()  {

        mProgressBar.setVisibility(View.INVISIBLE);

        super.onDestroyView();
    }
}