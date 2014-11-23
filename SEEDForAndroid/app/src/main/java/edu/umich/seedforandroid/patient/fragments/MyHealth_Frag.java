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
import edu.umich.seedforandroid.patient.fragments.myhealth.MyHealth_Alerts_Frag;
import edu.umich.seedforandroid.patient.fragments.myhealth.MyHealth_ViewData_Frag;

public class MyHealth_Frag extends Fragment  {

    private static final String TAG = MyHealth_Frag.class.getSimpleName();

    private FragmentTabHost mTabHost;

    public MyHealth_Frag()  {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_health_, container, false);

        view = initialSetup(view);

        return view;
    }

    private View initialSetup(View view) {

        mTabHost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("ViewMyHealthData")
                .setIndicator("", getResources()
                        .getDrawable(R.drawable.myhealth_viewdata_icon)), MyHealth_ViewData_Frag.class, null);

        mTabHost.addTab(mTabHost.newTabSpec("MySepsisAlerts")
                .setIndicator("", getResources()
                        .getDrawable(R.drawable.myhealth_alerts_icon)), MyHealth_Alerts_Frag.class, null);

        // Set the indicator color
        TabWidget widget = mTabHost.getTabWidget();
        for (int i = 0; i < widget.getChildCount(); i++)  {

            View v = widget.getChildAt(i);
            v.setBackgroundResource(R.drawable.tab_indicator_custom);
        }

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00274c")));
        actionBar.setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "My Health" + "</font>")));

        return view;
    }
}