package edu.umich.seedforandroid.patient.fragments;

import android.app.ActionBar;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v13.app.FragmentTabHost;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabWidget;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.patient.fragments.mysepsisnurse.PatientAskWatsonFragment;
import edu.umich.seedforandroid.patient.fragments.mysepsisnurse.PatientFrequentlyAskedQuestionsFragment;
import edu.umich.seedforandroid.patient.fragments.mysepsisnurse.PatientRecentlyAskedQuestionsFragment;

public class MySepsisNurse_Frag extends Fragment  {

    private FragmentTabHost mTabHost;

    public MySepsisNurse_Frag()  {

    }

//    @Override
//    public void onCreate(Bundle savedInstanceState)  {
//
//        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
//    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)  {
//
//        super.onCreateOptionsMenu(menu, inflater);
//
//        menu.findItem(R.id.action_graph_options).setVisible(false).setEnabled(false);
//        menu.findItem(R.id.action_refresh).setVisible(false).setEnabled(false);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_sepsis_nurse_, container, false);

        view = initialSetup(view);

        return view;
    }

    private View initialSetup(View view)  {

        mTabHost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("fragmentb")
                        .setIndicator("", getResources()
                        .getDrawable(R.drawable.mysepsisnurse_askwatson_icon)), PatientAskWatsonFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("fragmentc")
                        .setIndicator("", getResources()
                        .getDrawable(R.drawable.mysepsisnurse_faq_icon)), PatientFrequentlyAskedQuestionsFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("fragmentd")
                        .setIndicator("", getResources()
                        .getDrawable(R.drawable.mysepsisnurse_raq_icon)), PatientRecentlyAskedQuestionsFragment.class, null);

        // Set the indicator color
        TabWidget widget = mTabHost.getTabWidget();
        for (int i = 0; i < widget.getChildCount(); i++)  {

            View v = widget.getChildAt(i);
            v.setBackgroundResource(R.drawable.tab_indicator_custom);
        }

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00274c")));
        actionBar.setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "My Sepsis Nurse" + "</font>")));

        return view;
    }
}