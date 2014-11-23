package edu.umich.seedforandroid.doctor.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appspot.umichseed.seed.Seed;
import com.appspot.umichseed.seed.SeedRequest;
import com.appspot.umichseed.seed.model.MessagesEmailRequest;
import com.appspot.umichseed.seed.model.MessagesPatientListResponse;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApiThread = new ApiThread();
    }

    @Override
    public void onDestroy() {
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

        // Testing
        DoctorPatientWrapper tmp = new DoctorPatientWrapper("Andy", "Lee", "734-834-9095", "seedsystem00@gmail.com");
        myPatientList.add(tmp);
    }

    private void populatePatientList(MessagesPatientListResponse patients) {

        //todo load up patient list, call redraw on Adapter
    }

    private void populatePatientList(DoctorPatientWrapper patientData)  {

        myPatientList.add(patientData);
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
                mApiThread.enqueueRequest(request, new ApiThread.ApiResultAction() {
                    @Override
                    public void onApiResult(Object result) {

                        if (result != null && result instanceof MessagesPatientListResponse) {

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
            String patientNameTmp = currentPatient.getPatientFirstName().concat(" ").concat(currentPatient.getPatientLastName());
            tvName.setText(patientNameTmp);

            // Phone Number
            TextView tvPhoneNumber = (TextView) itemView.findViewById(R.id.tvPatientPhoneNumber);
            tvPhoneNumber.setText(currentPatient.getPatientPhoneNumber());

            // Email
            TextView tvEmail = (TextView) itemView.findViewById(R.id.tvPatientEmail);
            tvEmail.setText(currentPatient.getPatientEmail());

            // ImageView
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageViewPatientGender);

            // RelativeLayout
            RelativeLayout thisLayout = (RelativeLayout) itemView.findViewById(R.id.patientRelativeLayout);
            thisLayout.setOnClickListener(new View.OnClickListener()  {

                @Override
                public void onClick(View v)  {

//                    TextView tvEmail
                    TextView tvID = (TextView) v.findViewById(R.id.tvPatientID);
                    TextView tvName = (TextView) v.findViewById(R.id.tvPatientName);
                    gotoPatientDataPage(tvID.getText().toString(), tvName.getText().toString());
                }
            });
            return itemView;
        }
    }

    private void gotoPatientDataPage(String patientID, String patientName)  {

        Intent i = new Intent(getActivity(), DoctorViewPatientData.class);
        Bundle extras = new Bundle();
        extras.putString("patient_id", patientID);
        extras.putString("patient_name", patientName);
        i.putExtras(extras);
        startActivity(i);
    }
}