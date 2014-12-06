package edu.umich.seedforandroid.doctor.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.appspot.umichseed.seed.Seed;
import com.appspot.umichseed.seed.SeedRequest;
import com.appspot.umichseed.seed.model.MessagesDoctorPut;

import java.io.IOException;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.account.GoogleAccountManager;
import edu.umich.seedforandroid.api.ApiThread;
import edu.umich.seedforandroid.api.SeedApi;
import edu.umich.seedforandroid.doctor.doctor_update.UpdateDoctorProfile;
import edu.umich.seedforandroid.main.MainActivity;
import edu.umich.seedforandroid.util.Utils;

public class Doctor_Profile_Frag extends Fragment implements View.OnClickListener  {

    private static final String TAG = Doctor_Profile_Frag.class.getSimpleName();

    private TextView tvDoctorName, tvHospital, tvPhoneNumber, tvEmail;
    private Button bLogout, bUpdateProfile;
    private String mDoctorName, mHospital, mPhoneNumber, mEmail;
    private ApiThread mApiThread;
    private ProgressDialog mProgressDialog;
    private GoogleAccountManager mAccountManager;

    public Doctor_Profile_Frag()  {}

    @Override
    public void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);

        mApiThread = new ApiThread();
        mAccountManager = new GoogleAccountManager(getActivity());
        if (!mAccountManager.tryLogIn())  {

            Log.e(TAG, "FATAL ERROR: Somehow, the user got here without being logged in");
            notifyUiAuthenticationError();
        }
    }

    @Override
    public void onDestroy()  {

        super.onDestroy();

        mApiThread.stop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {

        View view = inflater.inflate(R.layout.fragment_doctor__profile_, container, false);

        initialSetup(view);

        return view;
    }

    private void initialSetup(View view)  {

        tvDoctorName = (TextView) view.findViewById(R.id.tvDoctorName);
        tvHospital = (TextView) view.findViewById(R.id.tvHospital);
        tvPhoneNumber = (TextView) view.findViewById(R.id.tvPhoneNumber);
        tvEmail = (TextView) view.findViewById(R.id.tvEmail);

        bUpdateProfile = (Button) view.findViewById(R.id.bEditProfile);
        bLogout = (Button) view.findViewById(R.id.bLogoutDoctor);

        bUpdateProfile.setOnClickListener(this);
        bLogout.setOnClickListener(this);

        loadProfileInformation();
    }

    private void displayProfileInformation(MessagesDoctorPut doctorProfile)  {

        if (stillAlive()) {
            mDoctorName = doctorProfile.getFirstName().concat(" ").concat(doctorProfile.getLastName());
            mHospital = doctorProfile.getHospital();
            mPhoneNumber = "Tel : ".concat(doctorProfile.getPhone());
            mEmail = doctorProfile.getEmail();

            tvDoctorName.setText(mDoctorName);
            tvHospital.setText(mHospital);
            tvPhoneNumber.setText(mPhoneNumber);
            tvEmail.setText(mEmail);
        }
    }

    private void notifyUiAuthenticationError()  {

        if (stillAlive())  {

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
    }

    private void notifyUiApiError()  {

        if (stillAlive())  {

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

    private void navigateHome() {

        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
    }

    private void notifyUiOfUnregisterPushNotificationError() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        View convertView = getActivity().getLayoutInflater().inflate(R.layout.api_unregister_error_title, null);
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

    private void logout()  {

        Utils.logout(getActivity(), new Utils.ILogoutResultListener()  {

            @Override
            public void onLogoutComplete(boolean pushNotificationsUnregistered)  {
                if (pushNotificationsUnregistered) navigateHome();
                else notifyUiOfUnregisterPushNotificationError();
            }
        });
    }

    private void popUpLogoutDialog()  {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        View convertView = getActivity().getLayoutInflater().inflate(R.layout.patient_alert_dialog_log_out, null);
        alertDialog.setCustomTitle(convertView);
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()  {

            @Override
            public void onClick(DialogInterface dialog, int id)  {
            }
        })
        .setPositiveButton("Yes", new DialogInterface.OnClickListener()  {

            @Override
            public void onClick(DialogInterface dialog, int id)  {

                mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mProgressDialog.setMessage("Logging Out...");
                mProgressDialog.show();

                logout();
            }
        });

        // Set the line color
        Dialog d = alertDialog.show();
        int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = d.findViewById(dividerId);
        divider.setBackground(new ColorDrawable(Color.parseColor("#00274c")));
    }

    private void gotoUpdateProfile()  {

        Intent i = new Intent(getActivity(), UpdateDoctorProfile.class);
        startActivity(i);
    }

    @Override
    public void onClick(View v)  {

        if (v.getId() == R.id.bEditProfile)  {

            gotoUpdateProfile();
        }
        else if (v.getId() == R.id.bLogoutDoctor)  {

            popUpLogoutDialog();
        }
    }

    private boolean stillAlive() {

        return getView() != null;
    }

    private void gotoMainActivity()  {

        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
    }
}