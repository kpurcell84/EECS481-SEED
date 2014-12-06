package edu.umich.seedforandroid.patient.fragments.mysepsisnurse;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import com.appspot.umichseed.seed.Seed;
import com.appspot.umichseed.seed.SeedRequest;
import com.appspot.umichseed.seed.model.MessagesWatsonQuestionPut;
import com.appspot.umichseed.seed.model.MessagesWatsonQuestionsListResponse;
import com.appspot.umichseed.seed.model.MessagesWatsonQuestionsRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.account.GoogleAccountManager;
import edu.umich.seedforandroid.api.ApiThread;
import edu.umich.seedforandroid.api.SeedApi;
import edu.umich.seedforandroid.main.MainActivity;
import edu.umich.seedforandroid.patient.fragments.mysepsisnurse.raq.RaqAdapter;
import edu.umich.seedforandroid.util.Utils;

public class PatientRecentlyAskedQuestionsFragment extends Fragment implements View.OnClickListener  {

    private static final long NUM_QUESTIONS = 30;

    private ExpandableListView mExpandableListView;
    private RaqAdapter mAdapter;
    private ApiThread mApiThread;
    private ProgressBar mProgressBar;
    private Utils mUtils;
    private Button bOpenWifi;

    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);

        mApiThread = new ApiThread();
    }

    @Override
    public void onDestroy()  {

        super.onDestroy();

        mApiThread.stop();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {

        mUtils = new Utils();

        View view = null;
        if (mUtils.checkInternetConnection(getActivity().getApplicationContext()))  {

            view = inflater.inflate(R.layout.fragment_my_sepsis_nurse__raq_, container, false);
            view = initialSetup(view);
        }
        else  {

            view = inflater.inflate(R.layout.fragment_patient_askwatson_no_internet, container, false);
            noInternetViewSetup(view);
        }

        return view;
    }

    private void noInternetViewSetup(View view)  {

        bOpenWifi = (Button) view.findViewById(R.id.bOpenWifi);
        bOpenWifi.setOnClickListener(this);
    }

    private View initialSetup(View view)  {

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setTitle("Recently Asked Questions");

        mExpandableListView = (ExpandableListView) view.findViewById(R.id.exp_list);
        mAdapter = new RaqAdapter(getActivity().getApplicationContext(),
                new LinkedHashMap<String, List<String>>());
        mExpandableListView.setAdapter(mAdapter);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);

        getQuestions();

        return view;
    }

    // region show/hide loader
    private void showLoader() {

        if (stillAlive()) {

            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void hideLoader() {

        if (stillAlive())  {

            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
    // endregion

    private void notifiUiUserNotLoggedIn() {

        if (stillAlive()) {

            mProgressBar.setVisibility(View.INVISIBLE);

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            View convertView = getActivity().getLayoutInflater().inflate(R.layout.loggedout_alert_title, null);
            alertDialog.setCustomTitle(convertView);

            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int id) {

                    gotoMainActivity();
                }
            });

            // Set the line color
            Dialog d = alertDialog.show();
            int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
            View divider = d.findViewById(dividerId);
            divider.setBackground(new ColorDrawable(Color.parseColor("#00274c")));
        }
    }

    private void notifyUiApiError() {

        if (stillAlive()) {

            mProgressBar.setVisibility(View.INVISIBLE);

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            View convertView = getActivity().getLayoutInflater().inflate(R.layout.api_error_alert_title, null);
            alertDialog.setCustomTitle(convertView);

            alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int id) {

                }
            });

            // Set the line color
            Dialog d = alertDialog.show();
            int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
            View divider = d.findViewById(dividerId);
            divider.setBackground(new ColorDrawable(Color.parseColor("#00274c")));
        }
    }

    private void getQuestions() {

        try {

            showLoader();
            GoogleAccountManager accountManager = new GoogleAccountManager(getActivity());
            if (!accountManager.tryLogIn()) {

                notifiUiUserNotLoggedIn();
            }
            Seed api = SeedApi.getUnauthenticatedApi();
            final SeedRequest request =
                    api.watsonRecentQuestions()
                    .get(
                            new MessagesWatsonQuestionsRequest()
                            .setNumQuestions(NUM_QUESTIONS)
                    );

            mApiThread.enqueueRequest(request, new ApiThread.ApiResultAction() {

                @Override
                public Object doInBackground(Object result) {

                    return processRequestResult(result);
                }

                @Override
                public void onApiResult(Object result) {

                    if (result != null) {

                        updateListView((Map<String, List<String>>)result);
                    }
                    else notifyUiApiError();
                }

                @Override
                public void onApiError(Throwable error) {

                    notifyUiApiError();
                }
            });
        }
        catch (IOException ioe) { /*unexpected*/ hideLoader(); ioe.printStackTrace(); }
    }

    private Map<String, List<String>> processRequestResult(Object result)  {

        if (result != null)  {

            MessagesWatsonQuestionsListResponse response
                    = (MessagesWatsonQuestionsListResponse) result;

            Map<String, List<String>> questionMap = new LinkedHashMap<String, List<String>>();

            for (MessagesWatsonQuestionPut q : response.getQuestions()) {

                List<String> answers = new ArrayList<String>();
                answers.add(q.getAnswer());
                questionMap.put(q.getQuestion(), answers);
            }

            return questionMap;
        }
        else return null;
    }

    private void updateListView(Map<String, List<String>> questionMap)  {

        if (stillAlive()) {
            mAdapter.replaceBackingData(questionMap);
            mAdapter.notifyDataSetChanged();
            hideLoader();
        }
    }

    private boolean stillAlive()  {

        return getView() != null;
    }

    private void gotoMainActivity()  {

        mProgressBar.setVisibility(View.INVISIBLE);

        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onClick(View v)  {

        if (v.getId() == R.id.bOpenWifi)  {

            getActivity().startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
        }
    }
}