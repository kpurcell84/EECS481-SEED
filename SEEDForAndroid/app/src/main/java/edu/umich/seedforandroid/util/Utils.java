package edu.umich.seedforandroid.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Dominic on 11/10/2014.
 */
public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public static boolean checkPlayServices(Activity activity) {

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);

        if (resultCode != ConnectionResult.SUCCESS) {

            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {

                GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else {

                Log.i(TAG, "This device is not supported.");
                activity.finish();
            }
            return false;
        }
        return true;
    }

    public static int getAppVersion(Context context) {

        try {

            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        }
        catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public boolean checkInternetConnection(Context context)  {

        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected())  { // there is connection to internet

            return true;
        }
        else  { // connection to internet does not exist

            return false;
        }
    }

    public String getCurrentTime()  { // yyyy:MM:dd:HH:mm:ss

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
        return sdf.format(new Date());
    }

    public String getMonth(int month)  { // 0 to 11

        String monthStr = "";

        if (month == 0)  {

            monthStr = "Jan";
        }
        else if (month == 1)  {

            monthStr = "Feb";
        }
        else if (month == 2)  {

            monthStr = "Mar";
        }
        else if (month == 3)  {

            monthStr = "Apr";
        }
        else if (month == 4)  {

            monthStr = "May";
        }
        else if (month == 5)  {

            monthStr = "Jun";
        }
        else if (month == 6)  {

            monthStr = "Jul";
        }
        else if (month == 7)  {

            monthStr = "Aug";
        }
        else if (month == 8)  {

            monthStr = "Sep";
        }
        else if (month == 9)  {

            monthStr = "Oct";
        }
        else if (month == 10)  {

            monthStr = "Nov";
        }
        else if (month == 11)  {

            monthStr = "Dec";
        }

        return monthStr;
    }

    public String get_Day_of_Week (int day)  {

        String dayStr = "";

        if (day == 1)  {

            dayStr = "Sun";
        }
        else if (day == 2)  {

            dayStr = "Mon";
        }
        else if (day == 3)  {

            dayStr = "Tues";
        }
        else if (day == 4)  {

            dayStr = "Wed";
        }
        else if (day == 5)  {

            dayStr = "Thurs";
        }
        else if (day == 6)  {

            dayStr = "Fri";
        }
        else if (day == 7)  {

            dayStr = "Sat";
        }
        return dayStr;
    }

    public String[] convert24HourTo12Hour(String hour)  { // take hour from 0 to 23 and convert it to 12 hour clock, 0 : hour, 1 : "AM" or "PM"

        String[] retArr = new String[2];
        String amPm;

        int hourInt = Integer.parseInt(hour);

        if (hourInt > 12)  {

            hourInt -= 12;
            amPm = "PM";
        }
        else  {

            if (hourInt == 0)  {

                hourInt = 12;
            }
            if (hourInt == 12)  {

                amPm = "PM";
            }
            else  {

                amPm = "AM";
            }
        }

        retArr[0] = String.valueOf(hourInt);
        retArr[1] = amPm;
        return retArr;
    }

    public String formatMinutePretty(int minute)  { // adds padding to minute (if less than 10)

        String retVal;
        if (minute < 10)  {

            retVal = "0".concat(String.valueOf(minute));
        }
        else  {

            retVal = String.valueOf(minute);
        }
        return retVal;
    }
}
