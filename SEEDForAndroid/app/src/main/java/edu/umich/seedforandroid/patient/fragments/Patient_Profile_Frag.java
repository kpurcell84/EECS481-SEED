package edu.umich.seedforandroid.patient.fragments;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.appspot.umichseed.seed.Seed;
import com.appspot.umichseed.seed.SeedRequest;
import com.appspot.umichseed.seed.model.MessagesPatientPut;

import java.io.IOException;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.account.GoogleAccountManager;
import edu.umich.seedforandroid.api.ApiThread;
import edu.umich.seedforandroid.api.SeedApi;
import edu.umich.seedforandroid.patient.daily_survey.DailySurvey;

public class Patient_Profile_Frag extends Fragment implements View.OnClickListener  {

    private static final String TAG = Patient_Profile_Frag.class.getSimpleName();

    public static final String ARG_PATIENT_EMAIL = "forPatientEmail";

    private Button bUpdateProfile;
    private ApiThread mApiThread;
    private GoogleAccountManager mAccountManager;
    private String mPatientEmail = null;

    public Patient_Profile_Frag()  {}

    @Override
    public void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        mApiThread = new ApiThread();

        mAccountManager = new GoogleAccountManager(getActivity());
        if (!mAccountManager.tryLogIn()) {

            Log.e(TAG, "FATAL ERROR: Somehow, the user got here without being logged in.");
            notifyUiAuthenticationError();
        }
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);

        mPatientEmail = args.getString(ARG_PATIENT_EMAIL);
    }

    @Override
    public void onDestroy() {
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

        bUpdateProfile = (Button) view.findViewById(R.id.bEditProfile);
        bUpdateProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)  {

        if (v.getId() == R.id.bEditProfile)  {

            updateProfile();
        }

    }

    private void updateProfile()  {

        Intent i = new Intent(getActivity(), DailySurvey.class);
        startActivity(i);
    }

    private void displayProfileInformation(MessagesPatientPut patientProfile) {

        //todo display the profile info here
    }

    private void notifyUiAuthenticationError() {

        //todo somehow, the user isn't logged in. Alert them and redirect to MainActivity
    }

    private void notifyUiApiError() {

        //todo there was an API error. Notify the user
    }

    //todo decide where to call this...probably in onStart?
    private void loadProfileInformation() {

        try {

            String patientEmail = mPatientEmail != null ?
                    mPatientEmail : mAccountManager.getAccountName();
            Seed api = SeedApi.getAuthenticatedApi(mAccountManager.getCredential());
            SeedRequest request = api.patient().get(patientEmail);
            mApiThread.enqueueRequest(request, new ApiThread.ApiResultAction() {
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
}
