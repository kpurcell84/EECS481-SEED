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

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.patient.fragments.mysepsisnurse.RecentlyAskedQuestionsFragment;
import edu.umich.seedforandroid.patient.fragments.mysepsisnurse.MySepsisNurse_AskWatson;
import edu.umich.seedforandroid.patient.fragments.mysepsisnurse.MySepsisNurse_FAQ_Frag;

public class MySepsisNurse_Frag extends Fragment  {

    private FragmentTabHost mTabHost;

    public MySepsisNurse_Frag()  {

    }

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
                        .getDrawable(R.drawable.mysepsisnurse_askwatson_icon)), MySepsisNurse_AskWatson.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("fragmentc")
                .setIndicator("", getResources()
                        .getDrawable(R.drawable.mysepsisnurse_faq_icon)), MySepsisNurse_FAQ_Frag.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("fragmentd")
                .setIndicator("", getResources()
                        .getDrawable(R.drawable.mysepsisnurse_raq_icon)), RecentlyAskedQuestionsFragment.class, null);

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00274c")));
        actionBar.setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "My Sepsis Nurse" + "</font>")));

        return view;
    }
}
