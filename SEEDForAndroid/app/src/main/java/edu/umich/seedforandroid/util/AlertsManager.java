package edu.umich.seedforandroid.util;

import android.content.Context;
import android.util.Log;

import com.appspot.umichseed.seed.Seed;

import java.util.HashSet;
import java.util.Set;

import edu.umich.seedforandroid.account.GoogleAccountManager;
import edu.umich.seedforandroid.api.ApiThread;
import edu.umich.seedforandroid.api.SeedApi;
import edu.umich.seedforandroid.patient.fragments.myhealth.AlertsDataWrapper;

/**
 * Created by Dominic on 11/19/2014.
 */
public class AlertsManager {

    private static final String TAG = AlertsManager.class.getSimpleName();

    private Seed mApi;
    private ApiThread mApiThread;

    public AlertsManager(Context context) {

        create(context, new ApiThread());
    }

    public AlertsManager(Context context, ApiThread apiThread) {

        create(context, apiThread);
    }

    private void create(Context context, ApiThread apiThread) {

        GoogleAccountManager accountManager = new GoogleAccountManager(context);

        if (!accountManager.getIsLoggedIn()) {

            String error = "FATAL: Somehow, the user got here without being logged in";
            Log.e(TAG, error);
            throw new RuntimeException(error);
        }

        mApi = SeedApi.getAuthenticatedApi(accountManager.getCredential());
        mApiThread = apiThread;
    }

    public Set<AlertsDataWrapper> mergeLocalWithRemote
            (final String forUser,
             final Set<AlertsDataWrapper> localAlerts,
             final IAlertsFetchCompleteListener listener) {

        return new HashSet<AlertsDataWrapper>();
    }

    public Set<AlertsDataWrapper> getRemoteOnly
            (final String forUser, final IAlertsFetchCompleteListener listener) {

        return new HashSet<AlertsDataWrapper>();
    }

    public interface IAlertsFetchCompleteListener {
        public void onAlertsFetchComplete(Set<AlertsDataWrapper> alerts);
        public void onAlertsFetchFailure(Throwable error);
    }
}
