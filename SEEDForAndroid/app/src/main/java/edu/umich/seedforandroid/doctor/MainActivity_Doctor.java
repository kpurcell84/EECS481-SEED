package edu.umich.seedforandroid.doctor;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.doctor.fragments.DoctorRecentlyAskedQuestions;
import edu.umich.seedforandroid.doctor.fragments.Doctor_Profile_Frag;
import edu.umich.seedforandroid.doctor.fragments.MyAlerts_Frag;
import edu.umich.seedforandroid.doctor.fragments.MyPatients_Frag;

public class MainActivity_Doctor extends FragmentActivity implements ActionBar.TabListener  {

    private Menu menuThis;
    public static final int MYALERTS = 0;
    public static final int MYPATIENTS = 1;
    public static final int PROFILE = 2;
    public static final int RECENTLYASKEDQUESTION = 3;

    ViewPager viewPager;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle view) {

        super.onCreate(view);
        setContentView(R.layout.activity_main_activity__doctor);

        initialSetup();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)  {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.doctor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info:

                Intent i1 = new Intent(MainActivity_Doctor.this, AbousUsDoctor.class);
                startActivity(i1);
                return true;

            case R.id.addNewPatient:

                Intent i2 = new Intent(MainActivity_Doctor.this, AddNewPatient.class);
                startActivity(i2);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initialSetup() {

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0)  {

                actionBar.setSelectedNavigationItem(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)  {}

            @Override
            public void onPageScrollStateChanged(int arg0)  {}
        });

        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00274c")));
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#FAFAFA")));
        actionBar.setIcon(R.drawable.seed_system_letter_icon);
        actionBar.setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "SEED System" + "</font>")));

        ActionBar.Tab tab1 = actionBar.newTab();
        tab1.setTabListener(this);
        tab1.setIcon(R.drawable.myalert_icon_inactive);
        tab1.setContentDescription("myalerts");

        ActionBar.Tab tab2 = actionBar.newTab();
        tab2.setTabListener(this);
        tab2.setIcon(R.drawable.mypatients_icon_inactive);
        tab2.setContentDescription("mypatients");

        ActionBar.Tab tab3 = actionBar.newTab();
        tab3.setTabListener(this);
        tab3.setIcon(R.drawable.profile_icon_inactive);
        tab3.setContentDescription("profile");

        ActionBar.Tab tab4 = actionBar.newTab();
        tab4.setTabListener(this);
        tab4.setIcon(R.drawable.askwatson_doctor_icon_inactive);
        tab4.setContentDescription("recentlyaskedquestions");

        actionBar.addTab(tab1);
        actionBar.addTab(tab2);
        actionBar.addTab(tab3);
        actionBar.addTab(tab4);

        // Figure out which one to go to
        Intent in = getIntent();
        Bundle extras = in.getExtras();
        int tabSelection = extras.getInt("tabSelection");

        if (tabSelection == MYALERTS)  {

            actionBar.selectTab(tab1);
        }
        else if (tabSelection == MYPATIENTS)  {

            actionBar.selectTab(tab2);
        }
        else if (tabSelection == PROFILE)  {

            actionBar.selectTab(tab3);
        }
        else  {

            actionBar.selectTab(tab4);
        }
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft)  {

        ActionBar actionBar = getActionBar();

        if (tab.getContentDescription().toString().contentEquals("myalerts") == true)  {

            actionBar.setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "My Patients' Alerts" + "</font>")));
            tab.setIcon(R.drawable.myalert_icon_active);
        }
        else if (tab.getContentDescription().toString().contentEquals("mypatients") == true)  {

            actionBar.setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "My Patients" + "</font>")));
            tab.setIcon(R.drawable.mypatients_icon_active);
        }
        else if (tab.getContentDescription().toString().contentEquals("profile") == true)  {

            actionBar.setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "Profile" + "</font>")));
            tab.setIcon(R.drawable.profile_icon_active);
        }
        else if (tab.getContentDescription().toString().contentEquals("recentlyaskedquestions") == true)  {

            actionBar.setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "Recently Asked Questions" + "</font>")));
            tab.setIcon(R.drawable.askwatson_doctor_icon_active);
        }

        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft)  {

        if (tab.getContentDescription().toString().contentEquals("myalerts") == true)  {

            tab.setIcon(R.drawable.myalert_icon_inactive);
        }
        else if (tab.getContentDescription().toString().contentEquals("mypatients") == true)  {

            tab.setIcon(R.drawable.mypatients_icon_inactive);
        }
        else if (tab.getContentDescription().toString().contentEquals("profile") == true)  {

            tab.setIcon(R.drawable.profile_icon_inactive);
        }
        else if (tab.getContentDescription().toString().contentEquals("recentlyaskedquestions") == true)  {

            tab.setIcon(R.drawable.askwatson_doctor_icon_inactive);
        }
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft)  {

    }

    class MyAdapter extends FragmentStatePagerAdapter  {

        public MyAdapter(FragmentManager fm)  {

            super(fm);
        }

        @Override
        public Fragment getItem(int arg)  {

            Fragment fragment = null;

            if (arg == 0)  {

                fragment = new MyAlerts_Frag();
            }
            if (arg == 1)  {

                fragment = new MyPatients_Frag();
            }
            if (arg == 2)  {

                fragment = new Doctor_Profile_Frag();
            }
            if (arg == 3)  {

                fragment = new DoctorRecentlyAskedQuestions();
            }
            return fragment;
        }

        @Override
        public int getCount()  {

            return 4;
        }
    }

    @Override
    public void onBackPressed()  {}
}