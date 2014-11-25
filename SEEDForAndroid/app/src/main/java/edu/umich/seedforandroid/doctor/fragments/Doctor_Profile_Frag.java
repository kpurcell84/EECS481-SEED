package edu.umich.seedforandroid.doctor.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appspot.umichseed.seed.Seed;
import com.appspot.umichseed.seed.SeedRequest;
import com.appspot.umichseed.seed.model.MessagesDoctorPut;
import com.appspot.umichseed.seed.model.MessagesPatientPut;

import java.io.IOException;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.account.GoogleAccountManager;
import edu.umich.seedforandroid.api.ApiThread;
import edu.umich.seedforandroid.api.SeedApi;
import edu.umich.seedforandroid.util.Utils;

public class Doctor_Profile_Frag extends Fragment  {

    private static final String TAG = Doctor_Profile_Frag.class.getSimpleName();

    private ApiThread mApiThread;
    private GoogleAccountManager mAccountManager;

    public Doctor_Profile_Frag() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApiThread = new ApiThread();
        mAccountManager = new GoogleAccountManager(getActivity());
        if (!mAccountManager.tryLogIn()) {

            Log.e(TAG, "FATAL ERROR: Somehow, the user got here without being logged in");
            notifyUiAuthenticationError();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mApiThread.stop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {

        return inflater.inflate(R.layout.fragment_doctor__profile_, container, false);
    }

    private void displayProfileInformation(MessagesDoctorPut doctorProfile) {

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

    private void navigateHome() {

        //todo logout successful, navigate home
    }

    private void notifyUiOfUnregisterPushNotificationError() {

        // todo: notify the user that we couldn't unregister their push notifications
        // tell them something like "an error occurred while logging you out. Unfortunately, you may
        // still receive push notifications on this device. To fix this issue, please uninstall and reinstall
        // the app. We apologize for this inconvenience". And then navigate home afterwards.
        navigateHome();
    }

    private void logout() {

        Utils.logout(getActivity(), new Utils.ILogoutResultListener() {
            @Override
            public void onLogoutComplete(boolean pushNotificationsUnregistered) {
                if (pushNotificationsUnregistered) navigateHome();
                else notifyUiOfUnregisterPushNotificationError();
            }
        });
    }
}
