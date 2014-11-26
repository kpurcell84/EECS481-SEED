package edu.umich.seedforandroid.lockscreenwidget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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
    private int mHour, mMinute, mDay;
    private int[] mMorningTimeSlot, mEveningTimeSlot;
    private String mMonth, mDayOfWeek, mAmPm, mCurrentTime, mUserAccountType,
                   mPriority, mTimeAlerted, mNotificationMessage;
    private boolean mNotiState;

    public LockScreenWidgetServicer()  {}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)  {

        initialSetup();
        drawWidget();

        stopSelf();
        return START_NOT_STICKY;
    }

    private void drawWidget()  {

        if (mNotiState == false)  {

            // Check if patient
            if (mUserAccountType.equals(SharedPrefsUtil.ACCOUNT_TYPE_PATIENT))  {

                // Check if there is a survey to fill out
                if ((mHour == mMorningTimeSlot[0] && mMinute == mMorningTimeSlot[1]) ||
                        (mHour == mEveningTimeSlot[0] && mMinute == mEveningTimeSlot[1]))  {

                    showSurveyNotification();
                }
                else  {

                    updateClock();
                }
            }
            else  {

                updateClock();
            }
        }
        else  {

            // Check if patient or doctor
            if (mUserAccountType.equals(SharedPrefsUtil.ACCOUNT_TYPE_DOCTOR))  { // doctor

                if (mPriority.equals(AlertsManager.PRIORITY_EARLY))  {

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
        mMinute = Integer.parseInt(timeParts[4]);
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
        mNotiState = mSharedPrefsUtilInst.getNotificationState(false);
        mNotificationMessage = mSharedPrefsUtilInst.getNotificationMessage("");
        mPriority = mSharedPrefsUtilInst.getNotificationPriority(AlertsManager.PRIORITY_EARLY);
        mTimeAlerted = mSharedPrefsUtilInst.getNotificationTimeAlerted("");
    }

    @Override
    public IBinder onBind(Intent intent)  {

        throw new UnsupportedOperationException("Not yet implemented");
    }
}