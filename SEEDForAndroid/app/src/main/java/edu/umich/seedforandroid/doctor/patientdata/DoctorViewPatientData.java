package edu.umich.seedforandroid.doctor.patientdata;

import android.app.ActionBar;
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
import android.view.MenuItem;

import edu.umich.seedforandroid.R;
import edu.umich.seedforandroid.doctor.MainActivity_Doctor;
import edu.umich.seedforandroid.doctor.fragments.myhealthviewdata.DoctorPatientAlertsFrag;
import edu.umich.seedforandroid.doctor.fragments.myhealthviewdata.DoctorPatientViewDataFrag;

public class DoctorViewPatientData extends FragmentActivity implements ActionBar.TabListener  {

    private String mPatientEmail;
    private String mPatientName;
    private ActionBar mActionBar;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_patient_data);

        initialSetup();
    }

    private void initialSetup()  {

        Bundle extras = getIntent().getExtras();
        mPatientEmail = extras.getString("patient_id");
        mPatientName = extras.getString("patient_name");

        // View
        mViewPager = (ViewPager) findViewById(R.id.pager_doctor_patient);
        mViewPager.setAdapter(new DoctorPatientViewPagerAdapter(getSupportFragmentManager()));
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()  {

            @Override
            public void onPageSelected(int arg0)  {

                mActionBar.setSelectedNavigationItem(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)  {}

            @Override
            public void onPageScrollStateChanged(int arg0)  {}
        });

        // ActionBar
        mActionBar = getActionBar();
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00274c")));
        mActionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#FAFAFA")));
        mActionBar.setIcon(R.drawable.seed_system_letter_icon);
        mActionBar.setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + mPatientName + "</font>")));

        ActionBar.Tab tab1 = mActionBar.newTab();
        tab1.setTabListener(this);
        tab1.setIcon(R.drawable.myhealth_viewdata_icon);
        tab1.setContentDescription("patientdata");

        ActionBar.Tab tab2 = mActionBar.newTab();
        tab2.setTabListener(this);
        tab2.setIcon(R.drawable.myhealth_alerts_icon);
        tab2.setContentDescription("patientalerts");

        mActionBar.addTab(tab1);
        mActionBar.addTab(tab2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)  {

        getMenuInflater().inflate(R.menu.menu_doctor_view_patient_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)  {

        int id = item.getItemId();

        if (id == R.id.home_back)  {

            goBackToMainActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void goBackToMainActivity()  {

        Intent i = new Intent(DoctorViewPatientData.this, MainActivity_Doctor.class);
        Bundle extras = new Bundle();
        extras.putInt("tabSelection", MainActivity_Doctor.MYPATIENTS);
        i.putExtras(extras);
        startActivity(i);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft)  {

        if (tab.getContentDescription().toString().contentEquals("patientdata") == true)  {

            tab.setIcon(R.drawable.myhealth_viewdata_icon);
        }
        else if (tab.getContentDescription().toString().contentEquals("patientalerts") == true)  {

            tab.setIcon(R.drawable.myhealth_alerts_icon);
        }
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft)  {

        if (tab.getContentDescription().toString().contentEquals("patientdata") == true)  {

            tab.setIcon(R.drawable.myhealth_viewdata_icon);
        }
        else if (tab.getContentDescription().toString().contentEquals("patientalerts") == true)  {

            tab.setIcon(R.drawable.myhealth_alerts_icon);
        }
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft)  {


    }
}


class DoctorPatientViewPagerAdapter extends FragmentStatePagerAdapter {

    public DoctorPatientViewPagerAdapter(FragmentManager fm)  {

        super(fm);
    }

    @Override
    public Fragment getItem(int arg)  {

        Fragment fragment = null;

        if (arg == 0)  {

            fragment = new DoctorPatientViewDataFrag();
        }
        if (arg == 1)  {

            fragment = new DoctorPatientAlertsFrag();
        }

        return fragment;
    }

    @Override
    public int getCount()  {

        return 2;
    }
}