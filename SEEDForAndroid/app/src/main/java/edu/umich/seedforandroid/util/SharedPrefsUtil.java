package edu.umich.seedforandroid.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Dominic on 11/8/2014.
 */
public class SharedPrefsUtil {

    public static final String ACCOUNT_TYPE_DOCTOR = "Doctor";
    public static final String ACCOUNT_TYPE_PATIENT = "Patient";
    public static final String ACCOUNT_TYPE_NONE = "None";

    private static final String SHARED_PREFS_FILENAME = "seed_shared_prefs";
    private static final String PROPERTY_CHOSEN_ACCOUNT = "chosen_account";
    private static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_REG_SUCCESS = "registration_successful";
    private static final String PROPERTY_APP_VERSION = "appVersion";

    // App Stuff
    private static final String PROPERTY_USER_ACCOUNT_TYPE = "user_account_type"; // "doctor" or "patient"
    private static final String PROPERTY_NOTIFICATION_STATE = "noti_state";
    private static final String PROPERTY_NOTIFICATION_MESSAGE = "noti_msg";
    private static final String PROPERTY_NOTIFICATION_EMAIL = "noti_email";

    // Survey Notification
    private static final String PROPERTY_SURVEY_QA_PAIR = "survey_qa";
    private static final String PROPERTY_SURVEY_MORNING_TIME = "survey_morning_time";
    private static final String PROPERTY_SURVEY_EVENING_TIME = "survey_evening_time";

    private static final String PROPERTY_PATIENT_GRAPH_FILTER = "patient_graph_filter";


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

    public void clearChosenAccount() {

        removeProperty(PROPERTY_CHOSEN_ACCOUNT);
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

    public void setRegistrationSuccessful(boolean registrationSuccessful) {

        setProperty(PROPERTY_REG_SUCCESS, registrationSuccessful);
    }

    public boolean getRegistrationSuccessful() {

        return getRegistrationSuccessful(false);
    }

    public boolean getRegistrationSuccessful(boolean defValue) {

        return mPrefs.getBoolean(PROPERTY_REG_SUCCESS, defValue);
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

    public void clearAppVersion() {

        removeProperty(PROPERTY_APP_VERSION);
    }

    public boolean setUserAccountType(String type)  {

        if (type.equals(ACCOUNT_TYPE_DOCTOR) ||
            type.equals(ACCOUNT_TYPE_PATIENT) ||
            type.equals(ACCOUNT_TYPE_NONE))  {

            setProperty(PROPERTY_USER_ACCOUNT_TYPE, type);
            return true;
        }
        else  {

            return false;
        }
    }

    public String getUserAccountType(String defValue)  {

        return mPrefs.getString(PROPERTY_USER_ACCOUNT_TYPE, defValue);
    }

    public void clearUserAccountType() {

        removeProperty(PROPERTY_USER_ACCOUNT_TYPE);
    }

    public void setNotificationState(boolean val)  { // ACTIVE or INACTIVE

        setProperty(PROPERTY_NOTIFICATION_STATE, val);
    }

    public boolean getNotificationState(boolean defValue)  {

        return mPrefs.getBoolean(PROPERTY_NOTIFICATION_STATE, defValue);
    }

    public void clearNotificationState() {

        removeProperty(PROPERTY_NOTIFICATION_STATE);
    }

    public void setNotificationMessage(String val)  {

        setProperty(PROPERTY_NOTIFICATION_MESSAGE, val);
    }

    public String getNotificationMessage(String defValue)  {

        return mPrefs.getString(PROPERTY_NOTIFICATION_MESSAGE, defValue);
    }

    public void clearNotificationMessage() {

        removeProperty(PROPERTY_NOTIFICATION_MESSAGE);
    }

    public void setNotificationEmail(String val)  {

        setProperty(PROPERTY_NOTIFICATION_EMAIL, val);
    }

    public String getNotificationEmail() {

        return getNotificationEmail("");
    }

    public String getNotificationEmail(String defValue)  {

        return mPrefs.getString(PROPERTY_NOTIFICATION_EMAIL, defValue);
    }

    public void clearNotificationEmail() {

        removeProperty(PROPERTY_NOTIFICATION_EMAIL);
    }

    public void setMorningSurveyNotificationTime(int hour, int minute)  { // 24 hour clock (23:00)

        String time = String.valueOf(hour).concat(":").concat(String.valueOf(minute));
        setProperty(PROPERTY_SURVEY_MORNING_TIME, time);
    }

    public int[] getMorningSurveyNotificationTime(int defHour, int defMinute)  {

        String tmp = String.valueOf(defHour).concat(":").concat(String.valueOf(defMinute));
        String time = mPrefs.getString(PROPERTY_SURVEY_MORNING_TIME, tmp);
        String[] timeParts = time.split(":");
        int[] timeInt = new int[2];
        timeInt[0] = Integer.parseInt(timeParts[0]);
        timeInt[1] = Integer.parseInt(timeParts[1]);
        return timeInt;
    }

    public void setEveningSurveyNotificationTime(int hour, int minute)  { // 24 hour clock (23:00)

        String time = String.valueOf(hour).concat(":").concat(String.valueOf(minute));
        setProperty(PROPERTY_SURVEY_EVENING_TIME, time);
    }

    public int[] getEveningSurveyNotificationTime(int defHour, int defMinute)  {

        String tmp = String.valueOf(defHour).concat(":").concat(String.valueOf(defMinute));
        String time = mPrefs.getString(PROPERTY_SURVEY_EVENING_TIME, tmp);
        String[] timeParts = time.split(":");
        int[] timeInt = new int[2];
        timeInt[0] = Integer.parseInt(timeParts[0]);
        timeInt[1] = Integer.parseInt(timeParts[1]);
        return timeInt;
    }

    public void setPatientGraphFilter(String filter)  {

        setProperty(PROPERTY_PATIENT_GRAPH_FILTER, filter);
    }

    public String getPatientGraphFilter(String defValue)  {

        return mPrefs.getString(PROPERTY_PATIENT_GRAPH_FILTER, defValue);
    }

    public void setDoctorGraphFilter(String filter)  {

        setProperty(PROPERTY_PATIENT_GRAPH_FILTER, filter);
    }

    public String getDoctorGraphFilter(String defValue)  {

        return mPrefs.getString(PROPERTY_PATIENT_GRAPH_FILTER, defValue);
    }


    private void setProperty(String property, String value) {

        mPrefs.edit().putString(property, value).apply();
    }

    private void setProperty(String property, int value) {

        mPrefs.edit().putInt(property, value).apply();
    }

    private void setProperty(String property, boolean value) {

        mPrefs.edit().putBoolean(property, value).apply();
    }

    private void removeProperty(String property) {

        mPrefs.edit().remove(property).apply();
    }

    public void removeAllProperties() {

        mPrefs.edit().clear().apply();
    }
}