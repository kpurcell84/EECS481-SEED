package edu.umich.seedforandroid.account;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import edu.umich.seedforandroid.util.SharedPrefsUtil;

/**
 * Created by Dominic on 10/13/2014.
 */
public class GoogleAccountManager {

    // default audience information
    public static final String GOOGLE_ACCOUNT_AUDIENCE_PREFIX = "server:client_id";
    public static final String SERVER_WEB_CLIENT_ID =
            "264671521534-evjhe6al5t2ahsba3eq2tf8jj78olpei.apps.googleusercontent.com";
    public static final String GOOGLE_ACCOUNT_AUDIENCE_FORMAT = "%1$s:%2$s";

    private SharedPrefsUtil mPrefsUtil;
    private GoogleAccountCredential mCredential;

    public GoogleAccountManager(Context context) {

        String audience = String.format(GOOGLE_ACCOUNT_AUDIENCE_FORMAT,
                GOOGLE_ACCOUNT_AUDIENCE_PREFIX, SERVER_WEB_CLIENT_ID);
        create(context, audience);
    }

    public GoogleAccountManager(Context context, String audience) {

        create(context, audience);
    }

    private void create(Context context, String audience) {

        mPrefsUtil = new SharedPrefsUtil(context);
        mCredential = GoogleAccountCredential.usingAudience(context, audience);
    }

    public boolean tryLogIn() {

        setSelectedAccountName(mPrefsUtil.getChosenAccount());
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

        mPrefsUtil.setChosenAccount(accountName);
        mCredential.setSelectedAccountName(accountName);
    }
}
