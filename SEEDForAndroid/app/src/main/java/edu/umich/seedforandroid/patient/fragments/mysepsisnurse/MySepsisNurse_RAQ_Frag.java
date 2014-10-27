package edu.umich.seedforandroid.patient.fragments.mysepsisnurse;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appspot.umichseed.seed.Seed;
import com.appspot.umichseed.seed.SeedRequest;
import com.appspot.umichseed.seed.model.SeedApiMessagesWatsonQuestionPut;
import com.appspot.umichseed.seed.model.SeedApiMessagesWatsonQuestionsListResponse;
import com.appspot.umichseed.seed.model.SeedApiMessagesWatsonQuestionsRequest;

import java.io.IOException;
import java.util.List;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.api.ApiThread;
import edu.umich.seedforandroid.api.SeedApi;

public class MySepsisNurse_RAQ_Frag extends Fragment  {

    private static final long NUM_QUESTIONS = 10;

    private ApiThread mApiThread;

    public MySepsisNurse_RAQ_Frag() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setTitle("Recently Asked Questions");

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
                            new SeedApiMessagesWatsonQuestionsRequest()
                            .setNumQuestions(NUM_QUESTIONS)
                    );

            mApiThread.enqueRequest(request, new ApiThread.ApiResultAction() {
                @Override
                public void onApiResult(Object result) {

                    if (result != null) {

                        SeedApiMessagesWatsonQuestionsListResponse response
                                = (SeedApiMessagesWatsonQuestionsListResponse)result;

                        populateListView(response.getQuestions());
                    }
                }
            });
        }
        catch (IOException ioe) { /*unexpected*/ hideLoader(); ioe.printStackTrace(); }
    }

    private void populateListView(List<SeedApiMessagesWatsonQuestionPut> questions) {

        /*==============================
            Put the data into the listview
            here
          ==============================*/

        hideLoader();
    }
}
