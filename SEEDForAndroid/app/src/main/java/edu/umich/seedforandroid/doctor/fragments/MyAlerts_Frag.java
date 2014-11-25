package edu.umich.seedforandroid.doctor.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appspot.umichseed.seed.model.MessagesAlertListResponse;
import com.appspot.umichseed.seed.model.MessagesAlertResponse;
import com.google.api.client.util.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.api.ApiThread;
import edu.umich.seedforandroid.doctor.patientdata.DoctorViewPatientData;
import edu.umich.seedforandroid.util.Utils;

public class MyAlerts_Frag extends Fragment  {

    private static final String TAG = MyAlerts_Frag.class.getSimpleName();

    private String emergencyDetection = "Emergency Detection";
    private String earlyDetection = "Early Detection";
    private List<DoctorAlertsWrapper> myAlertsList = new ArrayList<DoctorAlertsWrapper>();
    private ArrayAdapter<DoctorAlertsWrapper> adapter;
    private ApiThread mApiThread;

    public MyAlerts_Frag()  {}

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

        View view = inflater.inflate(R.layout.fragment_my_alerts_, container, false);

        initialSetup(view);

        return view;
    }

    private void initialSetup(View view)  {

        adapter = new AlertsListAdapter();
        ListView list = (ListView) view.findViewById(R.id.alertListView);
        list.setAdapter(adapter);

        // Test
        DateTime dtTmp = new DateTime(System.currentTimeMillis());
        DoctorAlertsWrapper tmp = new DoctorAlertsWrapper("Andy", "Lee", "seedsystem00@gmail.com", dtTmp, "high");
        myAlertsList.add(tmp);

        DoctorAlertsWrapper tmp2 = new DoctorAlertsWrapper("Andy", "Lee", "seedsystem00@gmail.com", dtTmp, "low");
        myAlertsList.add(tmp2);


//        updateAlertsListFromServer();
    }

    private void populateAlertsList(MessagesAlertListResponse alerts)  {

        myAlertsList.clear();
        for (MessagesAlertResponse alert : alerts.getAlerts())  {

            /*
            DoctorAlertsWrapper tmp = new DoctorPatientWrapper(ge.getFirstName(),
                    patient.getLastName(),
                    patient.getPhone(), patient.getEmail());
            myPatientList.add(tmp);
            */
        }

        adapter = new AlertsListAdapter();
        ListView list = (ListView) getView().findViewById(R.id.alertListView);
        list.setAdapter(adapter);
    }

    private void notifyUiUserNotLoggedIn() {

        //todo: user not logged in, notify and navigate back to main activity?
    }

    private void notifyUiApiError() {

        //todo: api error occurred. Notify
    }

    private void updateAlertsListFromServer()  {

        /*
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

                        if (result != null && result instanceof MessagesAlertListResponse)  {

                            populateAlertsList((MessagesAlertListResponse) result);
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
        */
    }

    private class AlertsListAdapter extends ArrayAdapter<DoctorAlertsWrapper>  {

        public AlertsListAdapter()  {

            super(getActivity().getApplicationContext(), R.layout.doctor_alerts_list_item, myAlertsList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)  {

            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;

            if (itemView == null)  {

                itemView = getActivity().getLayoutInflater().inflate(R.layout.doctor_alerts_list_item, parent, false);
            }

            // Find the item to work with.
            DoctorAlertsWrapper currentAlert = myAlertsList.get(position);

            // Email
            final TextView tvEmail = (TextView) itemView.findViewById(R.id.tvPatientEmail);
            tvEmail.setText(currentAlert.getPatientEmail());

            // Name
            TextView tvName = (TextView) itemView.findViewById(R.id.tvPatientName);
            final String patientNameTmp = currentAlert.getPatientFirstName().concat(" ").concat(currentAlert.getPatientLastName());
            tvName.setText(patientNameTmp);

            // Detection Image View
            ImageView imageViewDetection = (ImageView) itemView.findViewById(R.id.imageViewAlertType);

            // Time Alerted
            TextView tvTimeAlerted = (TextView) itemView.findViewById(R.id.tvTimeAlerted);
            DateTime timeAlerted = currentAlert.getTimeAlerted();

            DateFormat formatter = new SimpleDateFormat("yyyy:MM:dd:HH:mm");
            tvTimeAlerted.setText(formatTimePretty(formatter.format(timeAlerted.getValue()).toString()));

            // Priority Type
            final TextView tvPriority = (TextView) itemView.findViewById(R.id.tvAlertType);
            if (currentAlert.getPriority().equals("high"))  { // high, emergency detection

                tvPriority.setText(emergencyDetection);
                tvPriority.setBackground(new ColorDrawable(Color.parseColor("#d80000")));
                imageViewDetection.setImageResource(R.drawable.emergency_alert_square_icon);
            }
            else  { // low, early detection

                tvPriority.setText(earlyDetection);
                tvPriority.setBackground(new ColorDrawable(Color.parseColor("#FFA800")));
                imageViewDetection.setImageResource(R.drawable.early_alert_square_icon);
            }

            // RelativeLayout
            RelativeLayout relativeLayoutThis = (RelativeLayout) itemView.findViewById(R.id.relativelayoutalert);
            relativeLayoutThis.setOnClickListener(new View.OnClickListener()  {

                @Override
                public void onClick(View v)  {

                    gotoPatientDataPage(tvEmail.getText().toString(), patientNameTmp);
                }
            });
            return itemView;
        }
    }

    private String formatTimePretty(String time)  {

        String[] parts = time.split(":");
        String month = Utils.getMonthFullString(Integer.parseInt(parts[1]));
        String[] hour = Utils.convert24HourTo12Hour(String.valueOf(parts[3]));
        String output = parts[0] + " " + month + " " + parts[2] + ", " + hour[0] + ":" + parts[4] + hour[1];
        return output;
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