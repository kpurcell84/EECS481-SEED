package edu.umich.seedforandroid.doctor.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appspot.umichseed.seed.Seed;
import com.appspot.umichseed.seed.SeedRequest;
import com.appspot.umichseed.seed.model.MessagesAlertListResponse;
import com.appspot.umichseed.seed.model.MessagesAlertResponse;
import com.appspot.umichseed.seed.model.MessagesAlertsRequest;
import com.google.api.client.util.DateTime;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.account.GoogleAccountManager;
import edu.umich.seedforandroid.api.ApiThread;
import edu.umich.seedforandroid.api.SeedApi;
import edu.umich.seedforandroid.doctor.patientdata.DoctorViewPatientData;
import edu.umich.seedforandroid.main.MainActivity;
import edu.umich.seedforandroid.util.AlertsManager;
import edu.umich.seedforandroid.util.Utils;

public class MyAlerts_Frag extends Fragment  {

    private static final String TAG = MyAlerts_Frag.class.getSimpleName();

    private String emergencyDetection = "Emergency Detection";
    private String earlyDetection = "Early Detection";
    private List<DoctorAlertsWrapper> myAlertsList = new ArrayList<DoctorAlertsWrapper>();
    private ArrayAdapter<DoctorAlertsWrapper> adapter;
    private TextView tvNoAlerts;
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

        tvNoAlerts = (TextView) view.findViewById(R.id.tvNoAlertsForDoctorAtAll);
        tvNoAlerts.setVisibility(View.GONE);

        Calendar calStart = Calendar.getInstance();
        calStart.set(1992, Calendar.APRIL, 18);

        DateTime startTime = new DateTime(calStart.getTimeInMillis());
        DateTime endTime = new DateTime(System.currentTimeMillis());
        updateAlertsListFromServer(startTime, endTime);
    }

    private void populateAlertsList(MessagesAlertListResponse alerts)  {

        if (stillAlive()) {
            myAlertsList.clear();
            if (alerts.isEmpty())  {

                tvNoAlerts.setVisibility(View.VISIBLE);
            }
            else  {

                tvNoAlerts.setVisibility(View.GONE);
            }

            for (MessagesAlertResponse alert : alerts.getAlerts()) {

                DoctorAlertsWrapper tmp = new DoctorAlertsWrapper(alert.getFirstName(),
                        alert.getLastName(),
                        alert.getPatientEmail(),
                        alert.getTimeAlerted(),
                        alert.getPriority());
                myAlertsList.add(tmp);
            }

            Collections.reverse(myAlertsList);

            adapter = new AlertsListAdapter();
            ListView list = (ListView) getView().findViewById(R.id.alertListView);
            list.setAdapter(adapter);
        }
    }

    private void notifyUiUserNotLoggedIn() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        View convertView = getActivity().getLayoutInflater().inflate(R.layout.loggedout_alert_title, null);
        alertDialog.setCustomTitle(convertView);
        alertDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener()  {

            @Override
            public void onClick(DialogInterface dialog, int id)  {

                goToMainActivity();
            }
        });

        // Set the line color
        Dialog d = alertDialog.show();
        int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = d.findViewById(dividerId);
        divider.setBackground(new ColorDrawable(Color.parseColor("#00274c")));
    }

    private void notifyUiApiError() {

        if (stillAlive()) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            View convertView = getActivity().getLayoutInflater().inflate(R.layout.api_error_alert_title, null);
            alertDialog.setCustomTitle(convertView);
            alertDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {

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

    private void updateAlertsListFromServer(DateTime from, DateTime to)  {

        GoogleAccountManager accountManager = new GoogleAccountManager(getActivity());
        if (!accountManager.tryLogIn()) {

            Log.e(TAG, "FATAL ERROR: The user got here without being logged in somehow");
            notifyUiUserNotLoggedIn();
        }
        else {

            try {
                Seed api = SeedApi.getAuthenticatedApi(accountManager.getCredential());
                SeedRequest request = api.alerts().get(
                        new MessagesAlertsRequest()
                                .setStartTime(from)
                                .setEndTime(to)
                                .setEmail(accountManager.getAccountName())
                );
                mApiThread.enqueueRequest(request, new ApiThread.ApiResultAction()  {
                    @Override
                    public void onApiResult(Object result)  {

                        if (result != null && result instanceof MessagesAlertListResponse)  {

                            //Log.i("ALERT RECEIVED", "@@@@@@@@@@@@@@@@@@");
                            populateAlertsList((MessagesAlertListResponse) result);
                        }
                        else  {

                            tvNoAlerts.setVisibility(View.VISIBLE);
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
            if (currentAlert.getPriority().equals(AlertsManager.PRIORITY_EMERGENCY))  { // high, emergency detection

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

    private void goToMainActivity()  {

        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
    }

    private boolean stillAlive() {

        return getView() != null;
    }
}