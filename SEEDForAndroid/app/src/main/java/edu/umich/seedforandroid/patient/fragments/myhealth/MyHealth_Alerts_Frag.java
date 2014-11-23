package edu.umich.seedforandroid.patient.fragments.myhealth;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.api.client.util.DateTime;

import java.util.Collection;
import java.util.SortedSet;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.account.GoogleAccountManager;
import edu.umich.seedforandroid.api.ApiThread;
import edu.umich.seedforandroid.util.AlertsManager;

public class MyHealth_Alerts_Frag extends Fragment  {

    private static final String TAG = MyHealth_Alerts_Frag.class.getSimpleName();

    public static final String ARG_PATIENT_EMAIL = "forPatientEmail";

    private String mPatientEmail;
    private ApiThread mApiThread;
    private SortedSet<AlertsDataWrapper> mAlerts;

    public MyHealth_Alerts_Frag() {

    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);

        mPatientEmail = args.getString(ARG_PATIENT_EMAIL);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        mApiThread = new ApiThread();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mApiThread.stop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {

        View view = inflater.inflate(R.layout.fragment_my_health__alerts_, container, false);
        initialSetup();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        //todo refresh alerts for the default time, this is where you first fetch alerts to display
        // if necessary, load alerts from disk to display here as well, you can pass them in as
        // any collection type you want, the method will handle sorting them
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)  {

        super.onCreateOptionsMenu(menu, inflater);

//        menu.findItem(R.id.action_graph_options).setVisible(false).setEnabled(false);
      //  menu.findItem(R.id.action_graph_options).setVisible(false);

        //menu.findItem(R.id.action_refresh).setVisible(false);
    }

    private void refreshAlerts(DateTime from, DateTime to) {

        refreshAlerts(from, to, null);
    }

    private void refreshAlerts(DateTime from, DateTime to,
                               Collection<AlertsDataWrapper> existingAlerts) {

        new AlertsManager(this.getActivity(), mApiThread)
                .mergeLocalWithRemote(mPatientEmail, from, to, existingAlerts,
                                      new AlertsManager.IAlertsFetchCompleteListener() {

            @Override
            public void onAlertsFetchComplete(SortedSet<AlertsDataWrapper> alerts) {

                mAlerts = alerts;
                refreshUi();
            }

            @Override
            public void onAlertsFetchFailure(Throwable error) {

                Log.e(TAG, "Refreshing alerts failed with error: " + error.getMessage());
                notifyUiOfAlertsRefreshFailure();
            }
        });
    }

    private void refreshUi() {

        //todo load the data stored in mAlerts into the UI
    }

    private void notifyUiOfAlertsRefreshFailure() {

        //todo the alerts couldn't refresh. Notify the user in the UI
    }

    private void initialSetup()  {

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setTitle("My Sepsis Alerts");
    }
}