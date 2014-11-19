package edu.umich.seedforandroid.main;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.appspot.umichseed.seed.Seed;
import com.appspot.umichseed.seed.SeedRequest;
import com.appspot.umichseed.seed.model.SeedApiMessagesGcmCredsPut;
import com.appspot.umichseed.seed.model.SeedApiMessagesUserCheckRequest;
import com.appspot.umichseed.seed.model.SeedApiMessagesUserCheckResponse;

import java.io.IOException;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.account.GoogleAccountManager;
import edu.umich.seedforandroid.api.ApiThread;
import edu.umich.seedforandroid.api.SampleApiActivity;
import edu.umich.seedforandroid.api.SeedApi;
import edu.umich.seedforandroid.doctor.MainActivity_Doctor;
import edu.umich.seedforandroid.gcm.GcmManager;
import edu.umich.seedforandroid.patient.MainActivity_Patient;
import edu.umich.seedforandroid.util.SharedPrefsUtil;

public class MainActivity extends Activity implements View.OnClickListener  {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PICK_ACCOUNT_RESULT = 2;

    private Button bLogin;
    private EditText etUsername;
    private EditText etPassword;

    private GoogleAccountManager mAccountManager;
    private ApiThread mApiThread;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getActionBar().hide();

        bLogin = (Button) findViewById(R.id.bLogin);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);

        loadAccountManager();
        // note that we explicitly opt NOT to stop this during activity lifecycle events, so
        // it can live on long enough for the GCM upload to complete
        mApiThread = new ApiThread();

        if (mAccountManager.tryLogIn()) {

            registerGcm();
            // todo: already logged in, get account type and navigate to the appropriate start page AFTER the registerGcm() line
        }

        bLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)  {

        if (v.getId() == R.id.bLogin)  {

            /*
            if (etUsername.getText().toString().contentEquals("doctor"))  {

                Intent intent = new Intent(MainActivity.this, MainActivity_Doctor.class);
                startActivity(intent);
            }
            else  {

                Intent intent = new Intent(MainActivity.this, MainActivity_Patient.class);
                startActivity(intent);
            }
            */

            login();
        }
    }

    private void loadAccountManager() {

        String prefix = GoogleAccountManager.GOOGLE_ACCOUNT_AUDIENCE_PREFIX;
        String postfix = GoogleAccountManager.SERVER_WEB_CLIENT_ID;
        String format = GoogleAccountManager.GOOGLE_ACCOUNT_AUDIENCE_FORMAT;
        String audience = String.format(format, prefix, postfix);
        mAccountManager = new GoogleAccountManager(this, audience);
    }

    private void login() {

        if (!mAccountManager.tryLogIn()) {

            Intent intent = mAccountManager.getPickAccountIntent();
            startActivityForResult(intent, PICK_ACCOUNT_RESULT);
        }
        else {

            completeLogin();
        }
    }

    private void completeLogin() {

        try {

            Seed api = SeedApi.getAuthenticatedApi(mAccountManager.getCredential());
            SeedRequest checkUser = api.userCheck().get(
                    new SeedApiMessagesUserCheckRequest()
                            .setEmail(mAccountManager.getAccountName())
            );

            mApiThread.enqueueRequest(checkUser, new ApiThread.ApiResultAction() {

                @Override
                public void onApiResult(Object result) {

                    if (result != null && result instanceof SeedApiMessagesUserCheckResponse) {

                        String type = ((SeedApiMessagesUserCheckResponse)result).getUserType();

                        if (!registerGcm()) {
                            // registerGcm won't need this thread, so we can
                            // stop it now.
                            mApiThread.stop();
                        }

                        // todo: save user type, navigate to proper experience
                    }
                    else {

                        Log.e(TAG, "FATAL ERROR: Could not validate the user");
                        //todo: logic for handling login failure here
                    }
                }

                @Override
                public void onApiError(Throwable error) {

                    Log.e(TAG, "FATAL ERROR: Could not validate the user");
                    //todo: logic for handling login failure here
                }
            });
        }
        catch (IOException e) {

            Log.e(TAG, "FATAL ERROR: Could not validate the user");
            //todo: logic for handling login failure here
        }
    }

    private boolean registerGcm() {

        GcmManager manager = new GcmManager(this);

        if (!manager.isRegistrationValid()) {

            final Seed api = SeedApi.getAuthenticatedApi(mAccountManager.getCredential());
            final String oldRegistrationId = manager.getRegistrationId();
            final String accountName = mAccountManager.getAccountName();

            manager.registerInBackground(new GcmManager.IUploadRegistrationToServerAction() {

                @Override
                public void uploadToServer(String newNotificationId) {

                    try {

                        SeedApiMessagesGcmCredsPut putData = new SeedApiMessagesGcmCredsPut()
                                .setEmail(accountName)
                                .setNewRegId(newNotificationId);

                        if (!oldRegistrationId.equals("")) {

                            putData.setOldRegId(oldRegistrationId);
                        }

                        final SeedRequest request = api.gcmCreds().put(putData);

                        MainActivity.this.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                mApiThread.enqueueRequest(request, new ApiThread.ApiResultAction() {

                                    @Override
                                    public void onApiResult(Object result) {

                                        onTokenUploadAttemptComplete(true);
                                    }

                                    @Override
                                    public void onApiError(Throwable error) {

                                        onTokenUploadAttemptComplete(false);
                                    }
                                });
                            }
                        });
                    }
                    catch (IOException e) {

                        Log.w(TAG, "Error while uploading token to server");
                        onTokenUploadAttemptComplete(false);
                    }
                }
            });

            return true;
        }
        else return false;
    }

    private void onTokenUploadAttemptComplete(boolean success) {

        new SharedPrefsUtil(MainActivity.this).setRegistrationSuccessful(success);
        mApiThread.stop();
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
                        completeLogin();
                    }
                    else {

                        // todo user didn't choose an account, so they didn't log in.
                    }
                }

                break;
        }
    }
}