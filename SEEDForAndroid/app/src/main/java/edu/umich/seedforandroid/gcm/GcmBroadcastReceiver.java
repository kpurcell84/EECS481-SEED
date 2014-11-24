package edu.umich.seedforandroid.gcm;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.appspot.umichseed.seed.model.MessagesAlertResponse;
import com.google.api.client.util.DateTime;

import java.util.Calendar;
import java.util.Random;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.util.AlertsManager;
import edu.umich.seedforandroid.util.SharedPrefsUtil;

public class GcmBroadcastReceiver extends BroadcastReceiver  {

    private static final String TAG = GcmBroadcastReceiver.class.getSimpleName();

    private static final String PRIORITY = "priority";
    private static final String PATIENT_EMAIL = "patient_email";
    private static final String PATIENT_NAME = "patient_name";
    private static final String PRIORITY_EARLY = "Early";
    private static final String PRIORITY_EMERGENCY = "Emergency";


    public GcmBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent)  {

        Bundle extras = intent.getExtras();
        Log.i(TAG, "Received message: " + extras.toString());
        Toast.makeText(context.getApplicationContext(), extras.get("message").toString(), Toast.LENGTH_SHORT).show();

        setResultCode(Activity.RESULT_OK);

        String priority = extras.getString(PRIORITY);
        String email = extras.getString(PATIENT_EMAIL);
        String name = extras.getString(PATIENT_NAME);

        SharedPrefsUtil prefsUtil = new SharedPrefsUtil(context);
        String userType = prefsUtil.getUserAccountType("");
        if (!userType.equals("") && !userType.equals(SharedPrefsUtil.ACCOUNT_TYPE_NONE)) {

            String contentTitle;
            String contentBody;
            if (priority.equals(PRIORITY_EARLY)) {

                contentTitle = context.getString(R.string.notif_header_early);
                contentBody = AlertsManager.buildEarlyAlertString(context, name);
            }
            else {
                contentTitle = context.getString(R.string.notif_header_emergency);
                if (userType.equals(SharedPrefsUtil.ACCOUNT_TYPE_DOCTOR)) {

                    contentBody = AlertsManager.buildDoctorEmergencyAlertString(context, name);
                }
                else {

                    contentBody = AlertsManager.buildPatientEmergencyAlertString(context);
                }
            }

            //todo make intent take directly to patient portal
            Notification notification = new Notification.Builder(context)
                    .setContentTitle(contentTitle)
                    .setContentText(contentBody)
                    //.setSmallIcon()
                    //.setLargeIcon()
                    .build();

            NotificationManager notificationManager =
                    (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            int notifId = Calendar.getInstance().get(Calendar.SECOND);
            notificationManager.notify(notifId, notification);

            prefsUtil.setNotificationMessage(contentBody);
            prefsUtil.setNotificationEmail(email);
            prefsUtil.setNotificationState(true);
        }
        else {

            Log.e(TAG, "ERROR: Push Notification received, but a user wasn't logged in");

        }

        setResultCode(Activity.RESULT_OK);
    }
}