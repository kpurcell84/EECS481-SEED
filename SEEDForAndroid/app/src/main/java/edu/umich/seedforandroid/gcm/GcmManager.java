package edu.umich.seedforandroid.gcm;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.Random;

import edu.umich.seedforandroid.util.SharedPrefsUtil;
import edu.umich.seedforandroid.util.Utils;

/**
 * Created by Dominic on 11/7/2014.
 */
public class GcmManager {

    private static final String TAG = GcmManager.class.getSimpleName();

    private static final String SENDER_ID = "264671521534";

    private final Context mContext;

    public GcmManager(Context context) { mContext = context; }

    public void registerInBackground(final IUploadRegistrationToServerAction uploadAction) {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {

                int attempts = 0;
                Random rand = new Random();

                while (attempts < 5) {

                    try {

                        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(mContext);
                        String regId = gcm.register(SENDER_ID);

                        uploadAction.uploadToServer(regId);
                        storeRegistrationId(regId);

                        return null;
                    }
                    catch (IOException ex) {

                        long sleepTime = 1000 * (long) Math.pow(2, attempts++) + rand.nextInt(1001);

                        try {

                            Thread.sleep(sleepTime);
                        }
                        catch (InterruptedException e) { throw new RuntimeException(e); }
                    }
                }

                return null;
            }
        }.execute();
    }

    private void storeRegistrationId(String regId) {

        SharedPrefsUtil prefsUtil = new SharedPrefsUtil(mContext);
        int appVersion = Utils.getAppVersion(mContext);
        prefsUtil.setRegistrationId(regId);
        prefsUtil.setAppVersion(appVersion);
    }

    public String getRegistrationId() {

        SharedPrefsUtil prefsUtil = new SharedPrefsUtil(mContext);

        String registrationId = prefsUtil.getRegistrationId("");

        if (registrationId.isEmpty()) {

            Log.i(TAG, "Registration not found.");
            return "";
        }

        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefsUtil.getAppVersion(Integer.MIN_VALUE);
        int currentVersion = Utils.getAppVersion(mContext);

        if (registeredVersion != currentVersion) {

            Log.i(TAG, "App version changed.");
            return "";
        }

        return registrationId;
    }

    public interface IUploadRegistrationToServerAction {

        public void uploadToServer(String newNotificationId);
    }
}
