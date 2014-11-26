package edu.umich.seedforandroid.main;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.appspot.umichseed.seed.Seed;
import com.appspot.umichseed.seed.SeedRequest;
import com.appspot.umichseed.seed.model.MessagesEmailRequest;
import com.appspot.umichseed.seed.model.MessagesGcmCredsPut;
import com.appspot.umichseed.seed.model.MessagesUserCheckResponse;

import java.io.IOException;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.account.GoogleAccountManager;
import edu.umich.seedforandroid.api.ApiThread;
import edu.umich.seedforandroid.api.SeedApi;
import edu.umich.seedforandroid.doctor.MainActivity_Doctor;
import edu.umich.seedforandroid.gcm.GcmManager;
import edu.umich.seedforandroid.patient.MainActivity_Patient;
import edu.umich.seedforandroid.util.SharedPrefsUtil;

public class MainActivity extends Activity implements View.OnClickListener  {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PICK_ACCOUNT_RESULT = 2;

    private ProgressBar mProgressBar;
    private Button bLogin;
    private GoogleAccountManager mAccountManager;
    private ApiThread mApiThread;
    private SharedPrefsUtil sharedPrefsUtilInst;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialSetup();

        loadAccountManager();
        // note that we explicitly opt NOT to stop this during activity lifecycle events, so
        // it can live on long enough for the GCM upload to complete

        mApiThread = new ApiThread();

        if (mAccountManager.tryLogIn()) {

            // User already logged in, get account type and navigate to the appropriate start page
            // Save the user type
            if (sharedPrefsUtilInst.getUserAccountType("").equals(SharedPrefsUtil.ACCOUNT_TYPE_DOCTOR))  {

                registerGcm();
                Intent intent = new Intent(MainActivity.this, MainActivity_Doctor.class);
                Bundle extras = new Bundle();
                extras.putInt("tabSelection", MainActivity_Doctor.MYALERTS);
                intent.putExtras(extras);
                startActivity(intent);
            }
            else if (sharedPrefsUtilInst.getUserAccountType("").equals(SharedPrefsUtil.ACCOUNT_TYPE_PATIENT)) {

                registerGcm();
                Intent intent = new Intent(MainActivity.this, MainActivity_Patient.class);
                startActivity(intent);
            }
            else  {

                Toast.makeText(MainActivity.this, "Please log in", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initialSetup()  {

        getActionBar().hide();

        sharedPrefsUtilInst = new SharedPrefsUtil(MainActivity.this);
        bLogin = (Button) findViewById(R.id.bLogin);
        bLogin.setOnClickListener(this);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v)  {

        if (v.getId() == R.id.bLogin)  {

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

        Intent intent = mAccountManager.getPickAccountIntent();
        startActivityForResult(intent, PICK_ACCOUNT_RESULT);
    }

    private void completeLogin() {

        try {

            Seed api = SeedApi.getAuthenticatedApi(mAccountManager.getCredential());
            SeedRequest checkUser = api.userCheck().get(
                    new MessagesEmailRequest()
                            .setEmail(mAccountManager.getAccountName())
            );

            mApiThread.enqueueRequest(checkUser, new ApiThread.ApiResultAction() {

                @Override
                public void onApiResult(Object result)  {

                    mProgressBar.setVisibility(View.INVISIBLE);

                    if (result != null && result instanceof MessagesUserCheckResponse)  {

                        String type = ((MessagesUserCheckResponse)result).getUserType();

                        if (!registerGcm()) {
                            // registerGcm won't need this thread, so we can
                            // stop it now.
                            mApiThread.stop();
                        }

                        // Save user type, navigate to proper experience - Patient, Doctor, None
                        if (type.equals(SharedPrefsUtil.ACCOUNT_TYPE_DOCTOR) ||
                            type.equals(SharedPrefsUtil.ACCOUNT_TYPE_PATIENT))  {

                            // Save the user type
                            sharedPrefsUtilInst.setUserAccountType(type);

                            if (type.equals(SharedPrefsUtil.ACCOUNT_TYPE_DOCTOR))  {

                                Intent intent = new Intent(MainActivity.this, MainActivity_Doctor.class);
                                Bundle extras = new Bundle();
                                extras.putInt("tabSelection", MainActivity_Doctor.MYALERTS);
                                intent.putExtras(extras);
                                startActivity(intent);
                            }
                            else  {

                                Intent intent = new Intent(MainActivity.this, MainActivity_Patient.class);
                                startActivity(intent);
                            }
                        }
                        else  {

                            Toast.makeText(MainActivity.this, "You are not registered in our system yet. Please contact your doctor", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {

                        Log.e(TAG, "FATAL ERROR: Could not validate the user. An Unknown API error occurred");
                        //todo: logic for handling login failure here
                    }
                }

                @Override
                public void onApiError(Throwable error) {

                    mProgressBar.setVisibility(View.INVISIBLE);

                    Log.e(TAG, "FATAL ERROR: Could not validate the user. An API Error occurred:\n" + error.getMessage());
                    //todo: logic for handling login failure here
                }
            });
        }
        catch (IOException e) {

            Log.e(TAG, "FATAL ERROR: Could not validate the user. The API could not be initialized");
            //todo: logic for handling login failure here
        }
    }

    private boolean registerGcm()  {

        GcmManager manager = new GcmManager(this);

        if (!manager.isRegistrationValid()) {

            final Seed api = SeedApi.getAuthenticatedApi(mAccountManager.getCredential());
            final String oldNotificationId = manager.getRegistrationId();
            final String accountName = mAccountManager.getAccountName();

            manager.registerInBackground(new GcmManager.IUploadRegistrationToServerAction() {

                @Override
                public void uploadToServer(String newNotificationId) {

                    try {

                        MessagesGcmCredsPut putData = new MessagesGcmCredsPut()
                                .setEmail(accountName)
                                .setNewRegId(newNotificationId);

                        if (!oldNotificationId.equals("")) {

                            putData.setOldRegId(oldNotificationId);
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
                    catch (IOException e)  {

                        Log.w(TAG, "Error while uploading token to server");
                        onTokenUploadAttemptComplete(false);
                    }
                }
            });

            return true;
        }
        else return false;
    }

    private void onTokenUploadAttemptComplete(boolean success)  {

        new SharedPrefsUtil(MainActivity.this).setRegistrationSuccessful(success);
        mApiThread.stop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode)  {

            case PICK_ACCOUNT_RESULT:

                if (data != null && data.getExtras() != null)  {

                    String accountName = data.getExtras().getString(AccountManager.KEY_ACCOUNT_NAME);

                    if (accountName != null) {

                        mProgressBar.setVisibility(View.VISIBLE);

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