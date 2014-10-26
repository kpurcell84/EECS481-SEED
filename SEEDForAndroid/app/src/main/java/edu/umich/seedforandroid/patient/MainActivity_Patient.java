package edu.umich.seedforandroid.patient;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.patient.fragments.MyHealth_Frag;
import edu.umich.seedforandroid.patient.fragments.MySepsisNurse_Frag;
import edu.umich.seedforandroid.patient.fragments.Patient_Help_Frag;
import edu.umich.seedforandroid.patient.fragments.Patient_Profile_Frag;
import edu.umich.seedforandroid.patient.fragments.Patient_Settings_Frag;

public class MainActivity_Patient extends Activity implements NavigationDrawerFragment_Patient.NavigationDrawerCallbacks {


    private NavigationDrawerFragment_Patient mNavigationDrawerFragment;

    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity__patient);

        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00274c")));

        mNavigationDrawerFragment = (NavigationDrawerFragment_Patient) getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position)  {

        if (position == 0)  { // profile

            Fragment frag = new Patient_Profile_Frag();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, frag)
                    .commit();
        }
        else if (position == 1)  { // my health data

            Fragment frag = new MyHealth_Frag();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, frag)
                    .commit();
        }
        else if (position == 2)  { // my sepsis nurse

            Fragment frag = new MySepsisNurse_Frag();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, frag)
                    .commit();
        }
        else if (position == 3)  { // settings

            Fragment frag = new Patient_Settings_Frag();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, frag)
                    .commit();

        }
        else  { // help

            Fragment frag = new Patient_Help_Frag();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, frag)
                    .commit();
        }
    }

    public void onSectionAttached(int number)  {

        switch (number) {
            case 1:
                mTitle = getString(R.string.patient_title_section1);
                break;
            case 2:
                mTitle = getString(R.string.patient_title_section2);
                break;
            case 3:
                mTitle = getString(R.string.patient_title_section3);
                break;
            case 4:
                mTitle = getString(R.string.patient_title_section4);
                break;
            case 5:
                mTitle = getString(R.string.patient_title_section5);
                break;
        }
    }

    public void restoreActionBar()  {

        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
    //    actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)  {

        if (!mNavigationDrawerFragment.isDrawerOpen())  {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)  {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        if (id == R.id.action_settings)  {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}