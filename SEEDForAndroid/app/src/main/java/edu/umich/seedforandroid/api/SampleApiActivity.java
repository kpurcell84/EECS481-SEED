package edu.umich.seedforandroid.api;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.account.GoogleAccountManager;

public class SampleApiActivity extends Activity {

    private static final int PICK_ACCOUNT_RESULT = 2;

    private GoogleAccountManager mAccountManager;
    private SeedApiManager mApiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_api);

        loadAccountManager();

        if (login()) {

            loadApiManager();
        }
    }

    private void loadAccountManager() {

        String prefix = getString(R.string.google_account_audience_prefix);
        String postfix = getString(R.string.server_web_client_id);
        String audience = getString(R.string.google_account_audience_format, prefix, postfix);
        mAccountManager = new GoogleAccountManager(this, audience);
    }

    private boolean login() {

        boolean result = true;

        if (!mAccountManager.tryLogIn()) {

            Intent intent = mAccountManager.getPickAccountIntent();
            startActivityForResult(intent, PICK_ACCOUNT_RESULT);
            result = false;
        }

        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {

            case PICK_ACCOUNT_RESULT:

                if (data != null && data.getExtras() != null) {

                    String accountName = data.getExtras().getString(AccountManager.KEY_ACCOUNT_NAME);

                    if (accountName != null) {

                        // User is authorized.
                        mAccountManager.setSelectedAccountName(accountName);
                        loadApiManager();
                    }
                    else {

                        // user didn't choose an account, so they didn't log in.
                    }
                }

                break;
        }
    }

    private void loadApiManager() {

        mApiManager = new SeedApiManager(mAccountManager.getCredential());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sample_api, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
