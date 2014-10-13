package edu.umich.seedforandroid.account;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import edu.umich.seedforandroid.R;

/**
 * Created by Dominic on 10/13/2014.
 */
public class GoogleAccountManager {

    private Context mContext;
    private SharedPreferences mPrefs;
    private GoogleAccountCredential mCredential;

    public GoogleAccountManager(@NonNull Context context, @NonNull String audience) {

        mContext = context;
        String filename = mContext.getString(R.string.shared_prefs_filename);
        mPrefs = mContext.getSharedPreferences(filename, Context.MODE_PRIVATE);
        mCredential = GoogleAccountCredential.usingAudience(mContext, audience);
    }

    public boolean tryLogIn() {

        String key = mContext.getString(R.string.shared_prefs_chosen_account);
        setSelectedAccountName(mPrefs.getString(key, null));
        return getIsLoggedIn();
    }

    /**
     * call this Intent using startActivityForResult, and then
     * pass the result:
     *
     * data.getExtras().getString(AccountManager.KEY_ACCOUNT_NAME)
     *
     * to setSelectedAccountName
     * @return an Intent to open the account picker
     */
    public Intent getPickAccountIntent() {

        return mCredential.newChooseAccountIntent();
    }

    public GoogleAccountCredential getCredential() {

        return mCredential;
    }

    public String getAccountName() {

        return mCredential.getSelectedAccountName();
    }

    public Account getAccount() {

        return mCredential.getSelectedAccount();
    }

    public boolean getIsLoggedIn() {

        return mCredential.getSelectedAccountName() != null;
    }

    public void setSelectedAccountName(String accountName) {

        String key = mContext.getString(R.string.shared_prefs_chosen_account);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(key, accountName);
        editor.commit();
        mCredential.setSelectedAccountName(accountName);
    }
}
