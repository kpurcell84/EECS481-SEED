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
    private static final String PROPERTY_REG_SUCCESS = "registration_successful";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String PROPERTY_USER_ACCOUNT_TYPE = "user_account_type"; // "doctor" or "patient"
    private static final String PROPERTY_NOTIFICATION_STATE = "noti_state";
    private static final String PROPERTY_SURVEY_QA_PAIR = "survey_qa";
    private static final String PROPERTY_SURVEY_NOTIFICATION_TIME = "survey_time";

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

    public void setUserAccountType(String type)  {

        setProperty(PROPERTY_USER_ACCOUNT_TYPE, type);
    }

    public String getUserAccountType(String defValue)  {

        return mPrefs.getString(PROPERTY_USER_ACCOUNT_TYPE, defValue);
    }

    public void setNotificationState(String val)  { // ACTIVE or INACTIVE

        setProperty(PROPERTY_NOTIFICATION_STATE, val);
    }

    public String getNotificationState(String defValue)  {

        return mPrefs.getString(PROPERTY_NOTIFICATION_STATE, defValue);
    }

    public void setSurveyQAPair(int question, String answer)  {

        String thisQuestionAnswerPair = PROPERTY_SURVEY_QA_PAIR.concat(String.valueOf(question));
        setProperty(thisQuestionAnswerPair, answer);
    }

    public String getSurveyQAPair(int question, String defValue)  {

        String thisQuestionAnswerPair = PROPERTY_SURVEY_QA_PAIR.concat(String.valueOf(question));
        return mPrefs.getString(thisQuestionAnswerPair, defValue);
    }

    public void setSurveyNotificationTime(int hour, int minute)  { // 24 hour clock (23:00)

        String time = String.valueOf(hour).concat(":").concat(String.valueOf(minute));
        setProperty(PROPERTY_SURVEY_NOTIFICATION_TIME, time);
    }

    public int[] getSurveyNotificationTime(int defHour, int defMinute)  {

        String tmp = String.valueOf(defHour).concat(":").concat(String.valueOf(defMinute));
        String time = mPrefs.getString(PROPERTY_SURVEY_NOTIFICATION_TIME, tmp);
        String[] timeParts = time.split(":");
        int[] timeInt = new int[2];
        timeInt[0] = Integer.parseInt(timeParts[0]);
        timeInt[1] = Integer.parseInt(timeParts[1]);
        return timeInt;
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
}