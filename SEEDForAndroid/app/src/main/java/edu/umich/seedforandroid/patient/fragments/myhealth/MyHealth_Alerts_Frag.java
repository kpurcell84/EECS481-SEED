package edu.umich.seedforandroid.patient.fragments.myhealth;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.umich.seedforandroid.R;

public class MyHealth_Alerts_Frag extends Fragment  {

    public MyHealth_Alerts_Frag()  {

    }

    @Override
    public void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {

        View view = inflater.inflate(R.layout.fragment_my_health__alerts_, container, false);

        initialSetup();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)  {

        super.onCreateOptionsMenu(menu, inflater);

//        menu.findItem(R.id.action_graph_options).setVisible(false).setEnabled(false);
      //  menu.findItem(R.id.action_graph_options).setVisible(false);

        //menu.findItem(R.id.action_refresh).setVisible(false);
    }

    private void initialSetup()  {

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setTitle("My Sepsis Alerts");
    }
}