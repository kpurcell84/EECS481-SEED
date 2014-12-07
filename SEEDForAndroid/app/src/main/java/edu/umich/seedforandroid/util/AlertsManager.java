package edu.umich.seedforandroid.util;

import android.content.Context;
import android.util.Log;

import com.appspot.umichseed.seed.Seed;
import com.appspot.umichseed.seed.SeedRequest;
import com.appspot.umichseed.seed.model.MessagesAlertListResponse;
import com.appspot.umichseed.seed.model.MessagesAlertResponse;
import com.appspot.umichseed.seed.model.MessagesAlertsRequest;
import com.google.api.client.util.DateTime;

import java.io.IOException;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.account.GoogleAccountManager;
import edu.umich.seedforandroid.api.ApiThread;
import edu.umich.seedforandroid.api.SeedApi;
import edu.umich.seedforandroid.patient.fragments.myhealth.AlertsDataWrapper;

/**
 * Created by Dominic on 11/19/2014.
 */
public class AlertsManager {

    private static final String TAG = AlertsManager.class.getSimpleName();

    public static final String PRIORITY_EARLY = "Early";
    public static final String PRIORITY_EMERGENCY = "Emergency";

    private Seed mApi;
    private ApiThread mApiThread;
    private Context mContext;

    public AlertsManager(Context context) {

        create(context, new ApiThread());
    }

    public AlertsManager(Context context, ApiThread apiThread) {

        create(context, apiThread);
    }

    private void create(Context context, ApiThread apiThread) {

        GoogleAccountManager accountManager = new GoogleAccountManager(context);

        if (!accountManager.tryLogIn()) {

            String error = "FATAL: Somehow, the user got here without being logged in";
            Log.e(TAG, error);
            throw new RuntimeException(error);
        }

        mApi = SeedApi.getAuthenticatedApi(accountManager.getCredential());
        mApiThread = apiThread;
        mContext = context;
    }

    public boolean mergeLocalWithRemote
            (final String forUser, final DateTime from, final DateTime to,
             final Collection<AlertsDataWrapper> localAlerts,
             final IAlertsFetchCompleteListener listener) {

        try {

            SeedRequest request = mApi.alerts().get(
                    new MessagesAlertsRequest()
                            .setEmail(forUser)
                            .setStartTime(from)
                            .setEndTime(to)
            );
            mApiThread.enqueueRequest(request, new ApiThread.ApiResultAction() {

                @Override
                public Object doInBackground(Object result) {

                    if (result != null && result instanceof MessagesAlertListResponse) {

                        MessagesAlertListResponse castedResult = (MessagesAlertListResponse)result;
                        if (castedResult.getAlerts() != null) {

                            SortedSet<AlertsDataWrapper> sortedAlerts = buildSortedAlertsSet(castedResult);
                            // put all local alerts, removing duplicates and retaining a sorted order
                            if (localAlerts != null) {

                                for (AlertsDataWrapper w : localAlerts) {

                                    sortedAlerts.add(w);
                                }
                            }
                            return sortedAlerts;
                        }
                    }
                    return null;
                }

                @Override
                public void onApiResult(Object result) {

                    listener.onAlertsFetchComplete((SortedSet<AlertsDataWrapper>)result);
                }

                @Override
                public void onApiError(Throwable error) {

                    listener.onAlertsFetchFailure(error);
                }
            });
            return true;
        }
        catch (IOException e) {

            Log.e(TAG, "Unexpected error: IOException while creating the SEED request");
            return false;
        }
    }

    public boolean getRemoteOnly
            (final String forUser, final DateTime from, final DateTime to,
             final IAlertsFetchCompleteListener listener) {

        try {

            SeedRequest request = mApi.alerts().get(
                    new MessagesAlertsRequest()
                            .setEmail(forUser)
                            .setStartTime(from)
                            .setEndTime(to)
            );
            mApiThread.enqueueRequest(request, new ApiThread.ApiResultAction() {

                @Override
                public Object doInBackground(Object result) {

                    if (result != null && result instanceof MessagesAlertListResponse) {

                        MessagesAlertListResponse castedResult = (MessagesAlertListResponse) result;
                        if (castedResult.getAlerts() != null) {

                            return buildSortedAlertsSet(castedResult);
                        }
                    }
                    return null;
                }

                @Override
                public void onApiResult(Object result) {

                    listener.onAlertsFetchComplete((SortedSet<AlertsDataWrapper>)result);
                }

                @Override
                public void onApiError(Throwable error) {

                    listener.onAlertsFetchFailure(error);
                }
            });

            return true;
        }
        catch (IOException e) {

            Log.e(TAG, "Unexpected error: IOException while creating the SEED request");
            return false;
        }
    }

    private SortedSet<AlertsDataWrapper> buildSortedAlertsSet(MessagesAlertListResponse result) {

        // remote alerts will be built in sorted order
        SortedSet<AlertsDataWrapper> remoteAlerts =
                new TreeSet<AlertsDataWrapper>(AlertsDataWrapper.getComparator());
        SharedPrefsUtil prefsUtil = new SharedPrefsUtil(mContext);
        String accountType = prefsUtil.getUserAccountType("");
        for (MessagesAlertResponse a : result.getAlerts()) {

            String priority = a.getPriority();
            String name = String.format("%1$s %2$s", a.getFirstName(), a.getLastName());
            String message;
            if (priority.equals(PRIORITY_EARLY)) {

                //contentTitle = context.getString(R.string.notif_header_early);
                message = AlertsManager.buildEarlyAlertString(mContext, name);
            }
            else {
                //contentTitle = context.getString(R.string.notif_header_emergency);
                if (accountType.equals(SharedPrefsUtil.ACCOUNT_TYPE_DOCTOR)) {

                    message = AlertsManager.buildDoctorEmergencyAlertString(mContext, name);
                }
                else {

                    message = AlertsManager.buildPatientEmergencyAlertString(mContext);
                }
            }

            remoteAlerts.add(
                    new AlertsDataWrapper()
                            .setMessage(message)
                            .setTimeStamp(a.getTimeAlerted())
                            .setPriority(priority)
                            .setForEmail(a.getPatientEmail())
            );
        }
        return remoteAlerts;
    }

    public static String buildEarlyAlertString(Context context, String patientName) {

        return context.getString(R.string.notif_body_early, patientName);
    }

    public static String buildDoctorEmergencyAlertString(Context context, String patientName) {

        String firstArg = context.getString(R.string.notif_body_emergency_doctor_format, patientName);
        String secondArg = context.getString(R.string.notif_body_emergency_doctor_contact_message);
        return context.getString(R.string.notif_body_emergency, firstArg, secondArg);
    }

    public static String buildPatientEmergencyAlertString(Context context) {

        String firstArg = context.getString(R.string.notif_body_emergency_patient_format);
        String secondArg = context.getString(R.string.notif_body_emergency_patient_contact_message);
        return context.getString(R.string.notif_body_emergency, firstArg, secondArg);
    }

    public interface IAlertsFetchCompleteListener {
        public void onAlertsFetchComplete(SortedSet<AlertsDataWrapper> alerts);
        public void onAlertsFetchFailure(Throwable error);
    }
}
