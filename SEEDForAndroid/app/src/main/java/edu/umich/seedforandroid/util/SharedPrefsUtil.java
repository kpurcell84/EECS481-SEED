package edu.umich.seedforandroid.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Dominic on 11/8/2014.
 */
public class SharedPrefsUtil {

    private static final String SHARED_PREFS_FILENAME = "seed_shared_prefs";
    private static final String PROPERTY_CHOSEN_ACCOUNT = "chosen_account";
    private static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";

    private SharedPreferences mPrefs;

    public SharedPrefsUtil(Context context) {

        mPrefs = context.getSharedPreferences(SHARED_PREFS_FILENAME, Context.MODE_PRIVATE);
    }

    public void setChosenAccount(String account) {

        setProperty(PROPERTY_CHOSEN_ACCOUNT, account);
    }

    public String getChosenAccount() {

        return getChosenAccount(null);
    }

    public String getChosenAccount(String defValue) {

        return mPrefs.getString(PROPERTY_CHOSEN_ACCOUNT, defValue);
    }

    public void setRegistrationId(String id) {

        setProperty(PROPERTY_REG_ID, id);
    }

    public String getRegistrationId() {

        return getRegistrationId(null);
    }

    public String getRegistrationId(String defValue) {

        return mPrefs.getString(PROPERTY_REG_ID, defValue);
    }

    public void setAppVersion(int version) {

        setProperty(PROPERTY_APP_VERSION, version);
    }

    public int getAppVersion() {

        return getAppVersion(Integer.MIN_VALUE);
    }

    public int getAppVersion(int defValue) {

        return mPrefs.getInt(PROPERTY_APP_VERSION, defValue);
    }

    private void setProperty(String property, String value) {

        mPrefs.edit().putString(property, value).apply();
    }

    private void setProperty(String property, int value) {

        mPrefs.edit().putInt(property, value).apply();
    }
}
