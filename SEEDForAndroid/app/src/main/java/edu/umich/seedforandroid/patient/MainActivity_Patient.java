package edu.umich.seedforandroid.patient;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.appspot.umichseed.seed.Seed;
import com.appspot.umichseed.seed.SeedRequest;
import com.appspot.umichseed.seed.model.MessagesGcmCredsPut;

import java.io.IOException;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.account.GoogleAccountManager;
import edu.umich.seedforandroid.api.ApiThread;
import edu.umich.seedforandroid.api.SeedApi;
import edu.umich.seedforandroid.main.MainActivity;
import edu.umich.seedforandroid.patient.fragments.MyHealth_Frag;
import edu.umich.seedforandroid.patient.fragments.MySepsisNurse_Frag;
import edu.umich.seedforandroid.patient.fragments.Patient_Help_Frag;
import edu.umich.seedforandroid.patient.fragments.Patient_Profile_Frag;
import edu.umich.seedforandroid.patient.fragments.Patient_Settings_Frag;
import edu.umich.seedforandroid.util.SharedPrefsUtil;
import edu.umich.seedforandroid.util.Utils;

public class MainActivity_Patient extends Activity implements NavigationDrawerFragment_Patient.NavigationDrawerCallbacks {

    private static final String TAG = MainActivity_Patient.class.getSimpleName();

    private NavigationDrawerFragment_Patient mNavigationDrawerFragment;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity__patient);

        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00274c")));
        actionBar.setIcon(R.drawable.seed_system_letter_icon);

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
        else if (position == 4) { // help

            Fragment frag = new Patient_Help_Frag();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, frag)
                    .commit();
        }
        else  {

            showLogOutAlertDialog();
        }
    }

    private void showLogOutAlertDialog()  {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity_Patient.this);
        View convertView = (View) getLayoutInflater().inflate(R.layout.patient_alert_dialog_log_out, null);
        alertDialog.setCustomTitle(convertView);
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        })
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {

                logout();
            }
        });

        // Set the line color
        Dialog d = alertDialog.show();
        int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = d.findViewById(dividerId);
        divider.setBackground(new ColorDrawable(Color.parseColor("#00274c")));
    }

    private void notifyUiOfUnregisterPushNotificationError() {

        // todo: notify the user that we couldn't unregister their push notifications
        // tell them something like "an error occurred while logging you out. Unfortunately, you may
        // still receive push notifications on this device. To fix this issue, please uninstall and reinstall
        // the app. We apologize for this inconvenience". And then navigate home afterwards.
        navigateHome();
    }

    private void logout()  {

        Utils.logout(this, new Utils.ILogoutResultListener() {
            @Override
            public void onLogoutComplete(boolean pushNotificationsUnregistered) {
                if (pushNotificationsUnregistered) navigateHome();
                else notifyUiOfUnregisterPushNotificationError();
            }
        });
    }

    private void navigateHome() {

        Intent intent = new Intent(MainActivity_Patient.this, MainActivity.class);
        startActivity(intent);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)  {

        /*
        if (!mNavigationDrawerFragment.isDrawerOpen())  {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
        */
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)  {

        //return super.onOptionsItemSelected(item);

        return false;
    }
}