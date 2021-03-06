package edu.umich.seedforandroid.patient.fragments.myhealth;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.api.client.util.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.api.ApiThread;
import edu.umich.seedforandroid.util.AlertsManager;
import edu.umich.seedforandroid.util.Utils;

public class MyHealth_Alerts_Frag extends Fragment  {

    private static final String TAG = MyHealth_Alerts_Frag.class.getSimpleName();

    public static final String ARG_PATIENT_EMAIL = "forPatientEmail";

    private String mPatientEmail;
    private ApiThread mApiThread;
    private List<AlertsDataWrapper> myAlertsList = new ArrayList<AlertsDataWrapper>();
    private ArrayAdapter<AlertsDataWrapper> adapter;
    private TextView tvNoAlert;
    private ProgressBar mProgressBar;

    public MyHealth_Alerts_Frag()  {}

    @Override
    public void setArguments(Bundle args)  {

        super.setArguments(args);

        mPatientEmail = args.getString(ARG_PATIENT_EMAIL);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        mApiThread = new ApiThread();
        mPatientEmail = "seedsystem00@gmail.com";
    }

    @Override
    public void onDestroy()  {

        super.onDestroy();

        mApiThread.stop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {

        View view = inflater.inflate(R.layout.fragment_my_health__alerts_, container, false);
        initialSetup(view);
        return view;
    }

    @Override
    public void onStart()  {

        super.onStart();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)  {

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void refreshAlerts(DateTime from, DateTime to)  {

        refreshAlerts(from, to, null);
    }

    private void refreshAlerts(DateTime from, DateTime to,
                               Collection<AlertsDataWrapper> existingAlerts)  {

        mProgressBar.setVisibility(View.VISIBLE);

        new AlertsManager(this.getActivity(), mApiThread)
                .mergeLocalWithRemote(mPatientEmail, from, to, existingAlerts,
                                      new AlertsManager.IAlertsFetchCompleteListener()  {

            @Override
            public void onAlertsFetchComplete(List<AlertsDataWrapper> alerts)  {

                mProgressBar.setVisibility(View.INVISIBLE);

                if (alerts != null)  {

                    refreshUi(alerts);
                }
                else  {

                    Log.i("ALERTS NULL", "@@@@@@@@@@@@");
                    tvNoAlert.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAlertsFetchFailure(Throwable error)  {

                Log.e(TAG, "Refreshing alerts failed with error: " + error.getMessage());
                mProgressBar.setVisibility(View.INVISIBLE);
                notifyUiOfAlertsRefreshFailure();
            }
        });
    }

    private void refreshUi(List<AlertsDataWrapper> alerts)  {

        if (stillAlive()) {

            if (alerts.isEmpty()) {

                tvNoAlert.setVisibility(View.VISIBLE);
                return;
            }
            else  {

                tvNoAlert.setVisibility(View.GONE);
            }

            myAlertsList = alerts;

            adapter = new AlertsListAdapter();
            ListView list = (ListView) getView().findViewById(R.id.alertListViewPatient);
            list.setAdapter(adapter);
        }
    }

    private void notifyUiOfAlertsRefreshFailure()  {

        if (stillAlive()) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            View convertView = getActivity().getLayoutInflater().inflate(R.layout.api_error_alert_title, null);
            alertDialog.setCustomTitle(convertView);
            alertDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int id) {}
            });

            // Set the line color
            Dialog d = alertDialog.show();
            int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
            View divider = d.findViewById(dividerId);
            divider.setBackground(new ColorDrawable(Color.parseColor("#00274c")));
        }
    }

    private void initialSetup(View view)  {

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setTitle("My Sepsis Alerts");

        myAlertsList.clear();
        adapter = new AlertsListAdapter();
        tvNoAlert = (TextView) view.findViewById(R.id.tvNoAlerts);
        tvNoAlert.setVisibility(View.GONE);
        ListView list = (ListView) view.findViewById(R.id.alertListViewPatient);
        list.setAdapter(adapter);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);

        Calendar calStart = Calendar.getInstance();
        calStart.set(1992, Calendar.APRIL, 18);

        DateTime startTime = new DateTime(calStart.getTimeInMillis());
        DateTime endTime = new DateTime(System.currentTimeMillis());
        refreshAlerts(startTime, endTime);
    }

    private class AlertsListAdapter extends ArrayAdapter<AlertsDataWrapper>  {

        public AlertsListAdapter()  {

            super(getActivity().getApplicationContext(), R.layout.patient_alerts_item_view, myAlertsList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)  {

            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;

            if (itemView == null)  {

                itemView = getActivity().getLayoutInflater().inflate(R.layout.patient_alerts_item_view, parent, false);
            }

            // Find the item to work with.
            AlertsDataWrapper currentAlert = myAlertsList.get(position);

            // Message
            TextView tvMessage = (TextView) itemView.findViewById(R.id.tvAlertMessagePatient);
            tvMessage.setText(currentAlert.getMessage());

            // Time Alerted
            TextView tvTimeAlerted = (TextView) itemView.findViewById(R.id.tvTimeAlerted);
            DateTime timeAlerted = currentAlert.getTimeStamp();

            DateFormat formatter = new SimpleDateFormat("yyyy:MM:dd:HH:mm");
            tvTimeAlerted.setText(formatTimePretty(formatter.format(timeAlerted.getValue()).toString()));

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

    private boolean stillAlive() {

        return getView() != null;
    }
}