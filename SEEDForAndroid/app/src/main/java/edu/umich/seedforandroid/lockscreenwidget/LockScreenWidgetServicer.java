package edu.umich.seedforandroid.lockscreenwidget;

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
import edu.umich.seedforandroid.util.SharedPrefsUtil;
import edu.umich.seedforandroid.util.Utils;

public class LockScreenWidgetServicer extends Service  {

    private Utils mUtilInst;
    private SharedPrefsUtil mSharedPrefsUtilInst;
    private PowerManager pm;
    private int mHour, mMinute, mDay;
    private String mMonth, mDayOfWeek, mAmPm, mCurrentTime, mUserAccountType, mNotiState;

    public LockScreenWidgetServicer()  {}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)  {

        initialSetup();
        drawWidget();

        stopSelf();
        return START_NOT_STICKY;
    }

    private void drawWidget()  {

        if (mNotiState.contentEquals("INACTIVE"))  {

            updateClock();
        }
    }

    private void updateClock()  {

        if (pm.isScreenOn())  {

            RemoteViews remoteView = new RemoteViews(getPackageName(), R.layout.lockscreen_widget_clock);
            remoteView.setTextViewText(R.id.tvDate_Widget_Clock, mDayOfWeek.concat(", ").concat(mMonth).concat(" ").concat(String.valueOf(mDay)));

            remoteView.setTextViewText(R.id.tvTime_Widget_Clock, String.valueOf(mHour).concat(":").concat(mUtilInst.formatMinutePretty(mMinute)));
            remoteView.setTextViewText(R.id.tv_PM_AM_Widget_Clock, mAmPm);

            ComponentName thisWidget = new ComponentName(getApplicationContext(), LockScreenWidget.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(getApplicationContext());
            manager.updateAppWidget(thisWidget, remoteView);
        }
    }

    private void initialSetup()  {

        mUtilInst = new Utils();
        mSharedPrefsUtilInst = new SharedPrefsUtil(getApplicationContext());
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        // Setup the time
        Calendar c = Calendar.getInstance();
        mCurrentTime = mUtilInst.getCurrentTime();
        String[] timeParts=  mCurrentTime.split(":"); // yyyy:MM:dd:HH:mm:ss
        int month = Integer.parseInt(timeParts[1]);
        String[] twelveHourTime = mUtilInst.convert24HourTo12Hour(timeParts[3]);
        mHour = Integer.parseInt(twelveHourTime[0]);
        mMinute = Integer.parseInt(timeParts[4]);
        mDay = Integer.parseInt(timeParts[2]);
        mAmPm = twelveHourTime[1];
        mMonth = mUtilInst.getMonth(month);
        mDayOfWeek = mUtilInst.get_Day_of_Week(c.get(Calendar.DAY_OF_WEEK));
        mUserAccountType = mSharedPrefsUtilInst.getUserAccountType("");
        mNotiState = mSharedPrefsUtilInst.getNotificationState("INACTIVE");
    }

    @Override
    public IBinder onBind(Intent intent)  {

        throw new UnsupportedOperationException("Not yet implemented");
    }
}