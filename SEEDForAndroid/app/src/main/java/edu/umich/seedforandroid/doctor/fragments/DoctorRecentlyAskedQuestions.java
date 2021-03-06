package edu.umich.seedforandroid.doctor.fragments;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class DoctorRecentlyAskedQuestions extends Fragment  {

    private static final long NUM_QUESTIONS = 30;

    private ExpandableListView mExpandableListView;
    private RaqAdapter mAdapter;
    private ApiThread mApiThread;
    private ProgressBar mProgressBar;

    public DoctorRecentlyAskedQuestions()  {}

    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);

        mApiThread = new ApiThread();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mApiThread.stop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {

        View view = inflater.inflate(R.layout.fragment_doctor_recently_asked_questions, container, false);
        view = initialSetup(view);
        return view;
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
        if (stillAlive())  {

            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void hideLoader()  {
        if (stillAlive())  {

            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
    // endregion

    private void notifyUiUserNotLoggedIn()  {

        if (stillAlive())  {

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

        if (stillAlive())  {

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
                notifyUiUserNotLoggedIn();
                return;
            }
            Seed api = SeedApi.getAuthenticatedApi(accountManager.getCredential());
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
                }

                @Override
                public void onApiError(Throwable error) {/*ignore for now*/}
            });
        }
        catch (IOException ioe) { /*unexpected*/ hideLoader(); ioe.printStackTrace(); }
    }

    private Map<String, List<String>> processRequestResult(Object result) {

        if (result != null) {

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

    private void updateListView(Map<String, List<String>> questionMap) {

        if (stillAlive()) {
            mAdapter.replaceBackingData(questionMap);
            mAdapter.notifyDataSetChanged();
            hideLoader();
        }
    }

    private boolean stillAlive() {

        return getView() != null;
    }

    private void gotoMainActivity()  {

        mProgressBar.setVisibility(View.INVISIBLE);

        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
    }
}