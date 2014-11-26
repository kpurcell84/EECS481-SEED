package edu.umich.seedforandroid.patient.fragments.mysepsisnurse;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.umichseed.seed.Seed;
import com.appspot.umichseed.seed.SeedRequest;
import com.appspot.umichseed.seed.model.MessagesWatsonQuestionPut;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.io.IOException;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.account.GoogleAccountManager;
import edu.umich.seedforandroid.api.ApiThread;
import edu.umich.seedforandroid.api.SeedApi;
import edu.umich.seedforandroid.watson.WatsonManager;

public class PatientAskWatsonFragment extends Fragment implements View.OnClickListener  {

    private static final String TAG = PatientAskWatsonFragment.class.getSimpleName();

    private EditText etAskWatsonQuestion;
    private Button bAskWatson;
    private TextView tvAnswer;
    private ProgressBar mProgressBar;

    private ApiThread mApiThread;
    
    public PatientAskWatsonFragment()  {}

    //todo consider greying out the question button if there's no internet connection
    @Override
    public void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);

        mApiThread = new ApiThread();
    }

    @Override
    public void onDestroyView()  {

        super.onDestroyView();

        mApiThread.stop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {

        View view = inflater.inflate(R.layout.fragment_my_sepsis_nurse__ask_watson, container, false);

        initialSetup(view);

        return view;
    }

    private void initialSetup(View view)  {

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setTitle("Ask Watson");

        etAskWatsonQuestion = (EditText) view.findViewById(R.id.etAskWatsonQuestion);
        bAskWatson = (Button) view.findViewById(R.id.bAskWatson);
        bAskWatson.setOnClickListener(this);
        tvAnswer = (TextView) view.findViewById(R.id.tvAnswer);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBarAskWatson);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v)  {

        if (v.getId() == R.id.bAskWatson)  {

            if (etAskWatsonQuestion.getText().toString().contentEquals("") == false)  {

                mProgressBar.setVisibility(View.VISIBLE);

                // Hide Keyboard
                InputMethodManager inputMethodManager = (InputMethodManager)  getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                askWatson();
            }
            else  {

                Toast.makeText(getActivity().getApplicationContext(), "Please enter your question", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void notifyUiUserNotLoggedIn() {

        //todo: user not logged in, notify and navigate back to main activity?
    }

    private void notifyUiWatsonQuestionError() {

        //todo: couldn't contact watson, notify the user
    }

    private void askWatson()  {

        GoogleAccountManager accountManager = new GoogleAccountManager(getActivity());
        if (!accountManager.tryLogIn()) {

            Log.e(TAG, "FATAL ERROR: User somehow got here without being logged in");
            notifyUiUserNotLoggedIn();
        }
        else {

            final GoogleAccountCredential credential = accountManager.getCredential();
            WatsonManager watsonManager = new WatsonManager(getActivity());

            final String query = etAskWatsonQuestion.getText().toString();
            watsonManager.executeQuery(query, new WatsonManager.IResponseListener() {

                @Override
                public void onResponseReceived(String response, double confidence)  {

                    mProgressBar.setVisibility(View.INVISIBLE);

                    if (response.length() > 500) {

                        response = response.substring(0, 499);
                        int lastPeriod = response.lastIndexOf(".");
                        response = response.substring(0, lastPeriod);
                    }

                    tvAnswer.setText(response);

                    try {

                        Seed api = SeedApi.getAuthenticatedApi(credential);
                        SeedRequest request = api.watsonQuestion().put(
                                new MessagesWatsonQuestionPut()
                                        .setQuestion(query)
                                        .setAnswer(truncateAnswer(response))
                        );

                        mApiThread.enqueueRequest(request, null);
                    } catch (IOException e) {

                        e.printStackTrace();
                    }

                }

                private String truncateAnswer(String response) {

                    if (response.length() > 500) {

                        while (response.length() > 500) {

                            int lastPeriod = response.lastIndexOf(".");
                            response = response.substring(0, lastPeriod);
                        }
                    }
                    return response;
                }

                @Override
                public void onErrorReceived(int httpErrorCode)  {

                    mProgressBar.setVisibility(View.INVISIBLE);
                    Log.e(TAG, "Error on Watson Request: " + httpErrorCode);
                }
            });
        }
    }
}