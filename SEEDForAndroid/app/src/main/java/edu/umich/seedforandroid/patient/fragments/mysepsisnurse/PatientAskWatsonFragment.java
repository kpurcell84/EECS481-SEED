package edu.umich.seedforandroid.patient.fragments.mysepsisnurse;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.api.ApiThread;
import edu.umich.seedforandroid.watson.WatsonManager;

public class PatientAskWatsonFragment extends Fragment implements View.OnClickListener  {

    private static final String TAG = PatientAskWatsonFragment.class.getSimpleName();

    private EditText etAskWatsonQuestion;
    private Button bAskWatson;
    private TextView tvAnswer;

    private ApiThread mApiThread;
    
    public PatientAskWatsonFragment()  {

    }

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
    }

    @Override
    public void onClick(View v)  {

        if (v.getId() == R.id.bAskWatson)  {

            if (etAskWatsonQuestion.getText().toString().contentEquals("") == false)  {

                askWatson();
            }
            else  {

                Toast.makeText(getActivity().getApplicationContext(), "Please enter your question", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void askWatson()  {

        WatsonManager watsonManager = new WatsonManager(this.getActivity());

        final String query = etAskWatsonQuestion.getText().toString();
        watsonManager.executeQuery(query, new WatsonManager.IResponseListener()  {

            @Override
            public void onResponseReceived(String response, double confidence)  {

                if (response.length() > 500)  {

                    response = response.substring(0, 499);
                    int lastPeriod = response.lastIndexOf(".");
                    response = response.substring(0, lastPeriod);
                }

                tvAnswer.setText(response);

                /*
                try {

                    Seed api = SeedApi.getUnauthenticatedApi();
                    SeedApiMessagesWatsonQuestionPut question = new SeedApiMessagesWatsonQuestionPut();
                    question.setQuestion(query);
                    question.setAnswer(response);
                    SeedRequest request = api.watsonRecentQuestions().put(question);
                    mApiThread.enqueueRequest(request, null);
                }
                catch (IOException e) {

                    e.printStackTrace();
                }
                */
            }
/*
            private String truncateAnswer(String response)  {

                if (response.length() > 500)  {

                    response = response.substring(0, 499);
                   // int lastPeriod = response.lastIndexOf(".");
                   // response = response.substring(0, lastPeriod);
                }
                return response;
            }
*/
            @Override
            public void onErrorReceived(int httpErrorCode) {
                Log.e(TAG, "Error on Watson Request: " + httpErrorCode);
            }
        });
    }
}