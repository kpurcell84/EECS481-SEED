package edu.umich.seedforandroid.doctor.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.appspot.umichseed.seed.Seed;
import com.appspot.umichseed.seed.SeedRequest;
import com.appspot.umichseed.seed.model.MessagesEmailRequest;
import com.appspot.umichseed.seed.model.MessagesPatientListResponse;
import com.appspot.umichseed.seed.model.MessagesPatientPut;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.account.GoogleAccountManager;
import edu.umich.seedforandroid.api.ApiThread;
import edu.umich.seedforandroid.api.SeedApi;
import edu.umich.seedforandroid.doctor.patientdata.DoctorViewPatientData;

public class MyPatients_Frag extends Fragment  {

    private static final String TAG = MyPatients_Frag.class.getSimpleName();

    private List<DoctorPatientWrapper> myPatientList = new ArrayList<DoctorPatientWrapper>();
    private ArrayAdapter<DoctorPatientWrapper> adapter;
    private ApiThread mApiThread;

    public MyPatients_Frag()  {}

    @Override
    public void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);
        mApiThread = new ApiThread();
    }

    @Override
    public void onDestroy()  {

        super.onDestroy();
        mApiThread.stop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {

        View view = inflater.inflate(R.layout.fragment_my_patients_, container, false);

        initialSetup(view);

        return view;
    }

    private void initialSetup(View view)  {

        adapter = new PatientListAdapter();
        ListView list = (ListView) view.findViewById(R.id.patientlistView);
        list.setAdapter(adapter);

        updatePatientListFromServer();
    }

    private void populatePatientList(MessagesPatientListResponse patients) {

        //todo load up patient list, call redraw on Adapter
        myPatientList.clear();
        for (MessagesPatientPut patient : patients.getPatients())  {

            DoctorPatientWrapper tmp = new DoctorPatientWrapper(patient.getFirstName(),
                                                                patient.getLastName(),
                                                                patient.getPhone(), patient.getEmail());
            myPatientList.add(tmp);
        }

        adapter = new PatientListAdapter();
        ListView list = (ListView) getView().findViewById(R.id.patientlistView);
        list.setAdapter(adapter);
    }

    private void notifyUiUserNotLoggedIn() {

        //todo: user not logged in, notify and navigate back to main activity?
    }

    private void notifyUiApiError() {

        //todo: api error occurred. Notify
    }

    private void updatePatientListFromServer() {

        GoogleAccountManager accountManager = new GoogleAccountManager(getActivity());
        if (!accountManager.tryLogIn()) {

            Log.e(TAG, "FATAL ERROR: The user got here without being logged in somehow");
            notifyUiUserNotLoggedIn();
        }
        else {

            try {
                Seed api = SeedApi.getAuthenticatedApi(accountManager.getCredential());
                SeedRequest request = api.doctorsPatients().get(
                        new MessagesEmailRequest()
                                .setEmail(accountManager.getAccountName())
                );
                mApiThread.enqueueRequest(request, new ApiThread.ApiResultAction()  {
                    @Override
                    public void onApiResult(Object result)  {

                        if (result != null && result instanceof MessagesPatientListResponse)  {

                            populatePatientList((MessagesPatientListResponse)result);
                        }
                    }

                    @Override
                    public void onApiError(Throwable error) {

                        Log.e(TAG, "API error occurred with message: " + error.getMessage());
                        notifyUiApiError();
                    }
                });
            }
            catch (IOException e) {

                Log.e(TAG, "An unknown API error occurred - API could not build the request");
                notifyUiApiError();
            }
        }
    }

    private class PatientListAdapter extends ArrayAdapter<DoctorPatientWrapper>  {

        public PatientListAdapter()  {

            super(getActivity().getApplicationContext(), R.layout.doctor_patient_list_item, myPatientList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)  {

            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;

            if (itemView == null)  {

                itemView = getActivity().getLayoutInflater().inflate(R.layout.doctor_patient_list_item, parent, false);
            }

            // Find the item to work with.
            DoctorPatientWrapper currentPatient = myPatientList.get(position);

            // Name
            TextView tvName = (TextView) itemView.findViewById(R.id.tvPatientName);
            final String patientNameTmp = currentPatient.getPatientFirstName().concat(" ").concat(currentPatient.getPatientLastName());
            tvName.setText(patientNameTmp);

            // Phone Number
            final TextView tvPhoneNumber = (TextView) itemView.findViewById(R.id.tvPatientPhoneNumber);
            tvPhoneNumber.setText(currentPatient.getPatientPhoneNumber());
            tvPhoneNumber.setOnClickListener(new View.OnClickListener()  {

                @Override
                public void onClick(View v)  {

                    popUpPhoneCallAlertDialog(tvPhoneNumber.getText().toString(), patientNameTmp);
                }
            });

            // Email
            final TextView tvEmail = (TextView) itemView.findViewById(R.id.tvPatientEmail);
            tvEmail.setText(currentPatient.getPatientEmail());
            tvEmail.setOnClickListener(new View.OnClickListener()  {

                @Override
                public void onClick(View v)  {

                    popUpEmailAlertDialog(tvEmail.getText().toString(), patientNameTmp);
                }
            });

            // ImageView
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageViewPatientGender);

            // RelativeLayout
            ImageView imageViewNext = (ImageView) itemView.findViewById(R.id.imageViewNextIcon);
            imageViewNext.setOnClickListener(new View.OnClickListener()  {

                @Override
                public void onClick(View v)  {

                    gotoPatientDataPage(tvEmail.getText().toString(), patientNameTmp);
                }
            });
            return itemView;
        }
    }

    private void popUpEmailAlertDialog(final String emailStr, String patientName)  {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Email ".concat(patientName).concat("?"));
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()  {

            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        })
        .setPositiveButton("Yes", new DialogInterface.OnClickListener()  {

            @Override
            public void onClick(DialogInterface dialog, int id)  {

                emailPatient(emailStr);
            }
        });

        // Set the line color
        Dialog d = alertDialog.show();
        int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = d.findViewById(dividerId);
        divider.setBackground(new ColorDrawable(Color.parseColor("#00274c")));
    }

    private void popUpPhoneCallAlertDialog(final String phoneNumber, String patientName)  {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Call ".concat(patientName).concat("?"));
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()  {

            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        })
        .setPositiveButton("Yes", new DialogInterface.OnClickListener()  {

            @Override
            public void onClick(DialogInterface dialog, int id)  {

                callPatient(phoneNumber);
            }
        });

        // Set the line color
        Dialog d = alertDialog.show();
        int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = d.findViewById(dividerId);
        divider.setBackground(new ColorDrawable(Color.parseColor("#00274c")));
    }

    private void emailPatient(String emailStr)  {

        String emailaddress[] = { emailStr };

        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, emailaddress);
        emailIntent.setType("plain/text");
        startActivity(emailIntent);
    }

    private void callPatient(String phoneNumber)  {

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:".concat(phoneNumber)));
        startActivity(intent);
    }

    private void gotoPatientDataPage(String patientEmail, String patientName)  {

        Intent i = new Intent(getActivity(), DoctorViewPatientData.class);
        Bundle extras = new Bundle();
        extras.putString("patient_email", patientEmail);
        extras.putString("patient_name", patientName);
        i.putExtras(extras);
        startActivity(i);
    }
}