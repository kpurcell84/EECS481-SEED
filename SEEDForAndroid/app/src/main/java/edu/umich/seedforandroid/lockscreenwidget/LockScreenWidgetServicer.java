package edu.umich.seedforandroid.lockscreenwidget;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.RemoteViews;

import java.util.Calendar;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.patient.daily_survey.DailySurvey;
import edu.umich.seedforandroid.util.AlertsManager;
import edu.umich.seedforandroid.util.SharedPrefsUtil;
import edu.umich.seedforandroid.util.Utils;

public class LockScreenWidgetServicer extends Service  {

    private SharedPrefsUtil mSharedPrefsUtilInst;
    private PowerManager pm;
    private NotificationManager mgr;
    private Notification.Builder builder;
    private static final int SURVEY_NOTIFY_ID = 3;
    private int mHour, mHour24, mMinute, mDay, mSecond;
    private int[] mMorningTimeSlot, mEveningTimeSlot;
    private String mMonth, mDayOfWeek, mAmPm, mCurrentTime, mUserAccountType,
                   mPriority, mTimeAlerted, mNotificationMessage, mNotiState;

    public LockScreenWidgetServicer()  {}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)  {

        initialSetup();
        checkIfSurveyTime();
        drawWidget();

        stopSelf();
        return START_NOT_STICKY;
    }

    private void checkIfSurveyTime()  {

        //Log.i("Time: ", String.valueOf(mHour24) + ":" + String.valueOf(mMinute));

        if ((mHour24 == mMorningTimeSlot[0] && mMinute == mMorningTimeSlot[1] && mSecond == 5) ||
                (mHour24 == mEveningTimeSlot[0] && mMinute == mEveningTimeSlot[1] && mSecond == 5))  {

            mSharedPrefsUtilInst.setNotificationState(SharedPrefsUtil.SURVEY_NOTIFICATION);
            createNotificationForSurvey();
        }
    }

    private void createNotificationForSurvey()  {

        mgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        pm = (PowerManager)getSystemService(Context.POWER_SERVICE);

        builder = new Notification.Builder(getApplicationContext());

        builder.setSmallIcon(R.drawable.seed_system_icon_small)
                .setContentTitle("SEED System Daily Survey")
                .setContentText("Please complete your daily survey.")
                .setLights(Color.parseColor("#00274c"), 5000, 1000);

        Intent i = new Intent(getApplicationContext(), DailySurvey.class);

        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pIntent);

        Notification n = builder.build();

        mgr.notify(SURVEY_NOTIFY_ID, n);

        //Wakes up the lock screen
        if (pm.isScreenOn() == false)  {

            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE,"MyLock");
            wl.acquire(5000);
            PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyCpuLock");
            wl_cpu.acquire(5000);
        }
    }

    private void drawWidget()  {

        if (mNotiState.contentEquals(SharedPrefsUtil.INACTIVE_NOTIFICATION))  {

            updateClock();
        }
        else if (mNotiState.contentEquals(SharedPrefsUtil.ACTIVE_NOTIFICATION)) {

            // Check if patient or doctor
            if (mUserAccountType.equals(SharedPrefsUtil.ACCOUNT_TYPE_DOCTOR))  { // doctor

                if (mPriority.contentEquals(AlertsManager.PRIORITY_EARLY))  {

                    showEarlyAlertDoctor();
                }
                else  {

                    showEmergencyAlertDoctor();
                }
            }
            else  { // patient

                showAlertPatient();
            }
        }
        else if (mNotiState.contentEquals(SharedPrefsUtil.SURVEY_NOTIFICATION))  {

            // Check if patient
            if (mUserAccountType.equals(SharedPrefsUtil.ACCOUNT_TYPE_PATIENT))  {

                showSurveyNotification();
            }
        }
    }

    private void showSurveyNotification()  {

        RemoteViews remoteView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.widget_survey_notification);

        Intent i = new Intent(getApplicationContext(), DailySurvey.class);

        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, i, 0);

        remoteView.setOnClickPendingIntent(R.id.bEngageSurvey, pi);

        ComponentName thiswidget = new ComponentName(getApplicationContext(), LockScreenWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(getApplicationContext());
        manager.updateAppWidget(thiswidget, remoteView);
    }

    private void showEmergencyAlertDoctor()  {

        RemoteViews remoteView = new RemoteViews(getPackageName(), R.layout.widget_doctor_emergency_alert);

        remoteView.setTextViewText(R.id.tvAlertMessage, mNotificationMessage);
        remoteView.setTextViewText(R.id.tvTimeAlerted, "(".concat(mTimeAlerted).concat(")"));

        ComponentName thisWidget = new ComponentName(getApplicationContext(), LockScreenWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(getApplicationContext());
        manager.updateAppWidget(thisWidget, remoteView);
    }


    private void showEarlyAlertDoctor()  {

        RemoteViews remoteView = new RemoteViews(getPackageName(), R.layout.widget_doctor_early_alert);

        remoteView.setTextViewText(R.id.tvAlertMessage, mNotificationMessage);
        remoteView.setTextViewText(R.id.tvTimeAlerted, "(".concat(mTimeAlerted).concat(")"));

        ComponentName thisWidget = new ComponentName(getApplicationContext(), LockScreenWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(getApplicationContext());
        manager.updateAppWidget(thisWidget, remoteView);
    }

    private void showAlertPatient()  {

        RemoteViews remoteView = new RemoteViews(getPackageName(), R.layout.widget_patient_alert);

        remoteView.setTextViewText(R.id.tvAlertMessage, mNotificationMessage);
        remoteView.setTextViewText(R.id.tvTimeAlerted, "(".concat(mTimeAlerted).concat(")"));

        ComponentName thisWidget = new ComponentName(getApplicationContext(), LockScreenWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(getApplicationContext());
        manager.updateAppWidget(thisWidget, remoteView);
    }

    private void updateClock()  {

        if (pm.isScreenOn())  {

            RemoteViews remoteView = new RemoteViews(getPackageName(), R.layout.widget_clock);
            remoteView.setTextViewText(R.id.tvDate_Widget_Clock, mDayOfWeek.concat(", ").concat(mMonth).concat(" ").concat(String.valueOf(mDay)));

            remoteView.setTextViewText(R.id.tvTime_Widget_Clock, String.valueOf(mHour).concat(":").concat(Utils.formatMinutePretty(mMinute)));
            remoteView.setTextViewText(R.id.tv_PM_AM_Widget_Clock, mAmPm);

            ComponentName thisWidget = new ComponentName(getApplicationContext(), LockScreenWidget.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(getApplicationContext());
            manager.updateAppWidget(thisWidget, remoteView);
        }
    }

    private void initialSetup()  {

        mSharedPrefsUtilInst = new SharedPrefsUtil(getApplicationContext());
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        // Setup the time
        Calendar c = Calendar.getInstance();
        mCurrentTime = Utils.getCurrentTime();
        String[] timeParts=  mCurrentTime.split(":"); // yyyy:MM:dd:HH:mm:ss
        int month = Integer.parseInt(timeParts[1]);
        String[] twelveHourTime = Utils.convert24HourTo12Hour(timeParts[3]);
        mHour = Integer.parseInt(twelveHourTime[0]);
        mHour24 = Integer.parseInt(timeParts[3]);
        mMinute = Integer.parseInt(timeParts[4]);
        mSecond = Integer.parseInt(timeParts[5]);
        mDay = Integer.parseInt(timeParts[2]);
        mAmPm = twelveHourTime[1];
        month--;
        mMonth = Utils.getMonth(month);
        mDayOfWeek = Utils.getDayOfWeek(c.get(Calendar.DAY_OF_WEEK));
        mUserAccountType = mSharedPrefsUtilInst.getUserAccountType("");

        // Get the morning and evening survey times
        mMorningTimeSlot = mSharedPrefsUtilInst.getMorningSurveyNotificationTime(9, 0);
        mEveningTimeSlot = mSharedPrefsUtilInst.getEveningSurveyNotificationTime(21, 0);

        // Get User Account Type
        mUserAccountType = mSharedPrefsUtilInst.getUserAccountType("");

        // Get Notification Stuff
        mNotiState = mSharedPrefsUtilInst.getNotificationState("INACTIVE");
        mNotificationMessage = mSharedPrefsUtilInst.getNotificationMessage("");
        mPriority = mSharedPrefsUtilInst.getNotificationPriority(AlertsManager.PRIORITY_EARLY);
        mTimeAlerted = mSharedPrefsUtilInst.getNotificationTimeAlerted("");
    }

    @Override
    public IBinder onBind(Intent intent)  {

        throw new UnsupportedOperationException("Not yet implemented");
    }
}