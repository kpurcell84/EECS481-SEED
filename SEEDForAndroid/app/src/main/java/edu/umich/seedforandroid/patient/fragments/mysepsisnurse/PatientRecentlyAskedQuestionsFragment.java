package edu.umich.seedforandroid.patient.fragments.mysepsisnurse;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.appspot.umichseed.seed.Seed;
import com.appspot.umichseed.seed.SeedRequest;
import com.appspot.umichseed.seed.model.MessagesPQuantDataRequest;
import com.appspot.umichseed.seed.model.MessagesWatsonQuestionPut;
import com.appspot.umichseed.seed.model.MessagesWatsonQuestionsListResponse;
import com.appspot.umichseed.seed.model.MessagesWatsonQuestionsRequest;
import com.google.api.client.util.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.api.ApiThread;
import edu.umich.seedforandroid.api.SeedApi;
import edu.umich.seedforandroid.patient.fragments.mysepsisnurse.raq.RaqAdapter;

public class PatientRecentlyAskedQuestionsFragment extends Fragment  {

    private static final long NUM_QUESTIONS = 10;

    private ExpandableListView mExpandableListView;
    private RaqAdapter mAdapter;
    private ApiThread mApiThread;

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

        View view = inflater.inflate(R.layout.fragment_my_sepsis_nurse__raq_, container, false);
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

        getQuestions();

        return view;
    }

    // region show/hide loader
    private void showLoader() {

    }

    private void hideLoader() {

    }
    // endregion

    private void getQuestions() {

        try {

            showLoader();
            Seed api = SeedApi.getUnauthenticatedApi();
            final SeedRequest request =
                    api.watsonRecentQuestions()
                    .get(
                            new MessagesWatsonQuestionsRequest()
                            .setNumQuestions(NUM_QUESTIONS)
                    );


            final Seed.PQuantData.Get request2 =
                    api.pQuantData().get(

                        new MessagesPQuantDataRequest()
                                .setEmail("jinseok@umich.edu")
                                .setStartTime(new DateTime(System.currentTimeMillis() - 10000))
                                .setEndTime(new DateTime(System.currentTimeMillis()))
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

                    /*

                    SeedApiMessagesPQuantDataListResponse test = (SeedApiMessagesPQuantDataListResponse)result;
                    test.getPdataList().get(0).

                     */
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

        mAdapter.replaceBackingData(questionMap);
        hideLoader();
    }
}
