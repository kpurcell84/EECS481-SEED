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
import edu.umich.seedforandroid.patient.fragments.myhealth.MyHealth_Alerts_Frag;
import edu.umich.seedforandroid.patient.fragments.myhealth.MyHealth_ViewData_Frag;

public class MyHealth_Frag extends Fragment  {

    private FragmentTabHost mTabHost;

    public MyHealth_Frag()  {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_health_, container, false);

        view = initialSetup(view);

        return view;
    }

    private View initialSetup(View view)  {

        mTabHost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("ViewMyHealthData")
                .setIndicator("", getResources()
                        .getDrawable(R.drawable.myhealth_viewdata_icon)), MyHealth_ViewData_Frag.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("MySepsisAlerts")
                .setIndicator("", getResources()
                        .getDrawable(R.drawable.myhealth_alerts_icon)), MyHealth_Alerts_Frag.class, null);

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00274c")));
        actionBar.setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "My Health" + "</font>")));

        return view;
    }
}
