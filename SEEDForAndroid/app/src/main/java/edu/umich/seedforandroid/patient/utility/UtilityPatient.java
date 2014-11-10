package edu.umich.seedforandroid.patient.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class UtilityPatient  {

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
}
