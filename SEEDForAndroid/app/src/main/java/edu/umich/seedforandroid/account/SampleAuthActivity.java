package edu.umich.seedforandroid.account;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.appspot.umichseed.seed.Seed;
import com.appspot.umichseed.seed.SeedRequest;

import java.io.IOException;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.api.ApiThread;
import edu.umich.seedforandroid.api.SampleApiActivity;
import edu.umich.seedforandroid.api.SeedApi;

public class SampleAuthActivity extends Activity {

    private static final String TAG = SampleApiActivity.class.getSimpleName();
    private static final int PICK_ACCOUNT_RESULT = 2;

    private GoogleAccountManager mAccountManager;
    private ApiThread mApiThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_api);

        loadAccountManager();

        mApiThread = new ApiThread();


        if (login()) {

            Log.i(TAG, "Account Name: " + mAccountManager.getAccountName());
            makeSampleApiCall();
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        mApiThread.stop();
    }

    private void makeSampleApiCall() {

        Seed api = SeedApi.getAuthenticatedApi(mAccountManager.getCredential());

        try {

            SeedRequest request = api.doctor().get().setEmail("smeagol@lotr.com");

            // this call implicitly starts the thread
            mApiThread.enqueueRequest(request, new ApiThread.ApiResultAction() {

                @Override
                public Object doInBackground(Object result) {
                    return super.doInBackground(result);
                }

                @Override
                public void onApiResult(Object result) {

                }

                @Override
                public void onApiError(Throwable error) {

                }
            });
        }
        catch (IOException e) {
            // an exception occurred while trying to get the SeedRequest
            e.printStackTrace();
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
                        Log.i(TAG, "Account Name: " + mAccountManager.getAccountName());
                        makeSampleApiCall();
                    }
                    else {

                        // user didn't choose an account, so they didn't log in.
                    }
                }

                break;
        }
    }
}
