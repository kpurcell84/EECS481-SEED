package edu.umich.seedforandroid.patient;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.umich.seedforandroid.R;

public class NavigationDrawerFragment_Patient extends Fragment  {

    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    ArrayAdapter<NavigationDrawerItem> adapter;
    private List<NavigationDrawerItem> myNavTabs = new ArrayList<NavigationDrawerItem>();
    private NavigationDrawerCallbacks mCallbacks;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;
    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    public NavigationDrawerFragment_Patient() {}

    @Override
    public void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        selectItem(mCurrentSelectedPosition);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)  {

        mDrawerListView = (ListView) inflater.inflate(R.layout.fragment_navigation_drawer_patient, container, false);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener()  {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)  {

                selectItem(position);
            }
        });

        myNavTabs.add((new NavigationDrawerItem(R.drawable.evening_icon, getString(R.string.patient_title_section1))));
        myNavTabs.add((new NavigationDrawerItem(R.drawable.evening_icon, getString(R.string.patient_title_section2))));
        myNavTabs.add((new NavigationDrawerItem(R.drawable.evening_icon, getString(R.string.patient_title_section3))));
        myNavTabs.add((new NavigationDrawerItem(R.drawable.evening_icon, getString(R.string.patient_title_section4))));
        myNavTabs.add((new NavigationDrawerItem(R.drawable.evening_icon, getString(R.string.patient_title_section5))));
        myNavTabs.add((new NavigationDrawerItem(R.drawable.evening_icon, getString(R.string.patient_title_section6))));

        adapter = new MyListAdapter();

        mDrawerListView.setAdapter(adapter);
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        return mDrawerListView;
    }

    public boolean isDrawerOpen()  {

        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout)  {

        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        ) {
            @Override
            public void onDrawerClosed(View drawerView)  {

                super.onDrawerClosed(drawerView);
                if (!isAdded())  {

                    return;
                }

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView)  {

                super.onDrawerOpened(drawerView);
                if (!isAdded())  {

                    return;
                }

                if (!mUserLearnedDrawer)  {

                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().invalidateOptionsMenu();
            }
        };

        if (!mUserLearnedDrawer && !mFromSavedInstanceState)  {

            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        mDrawerLayout.post(new Runnable()  {

            @Override
            public void run()  {

                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(int position)  {

        mCurrentSelectedPosition = position;
        if (mDrawerListView != null)  {

            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null)  {

            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null)  {

            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity)  {

        super.onAttach(activity);
        try  {

            mCallbacks = (NavigationDrawerCallbacks) activity;
        }
        catch (ClassCastException e)  {

            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach()  {

        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)  {

        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)  {

        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)  {

        if (mDrawerLayout != null && isDrawerOpen())  {

            showGlobalContextActionBar();
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)  {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showGlobalContextActionBar()  {

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }

    private ActionBar getActionBar()  {

        return getActivity().getActionBar();
    }

    public static interface NavigationDrawerCallbacks  {

        void onNavigationDrawerItemSelected(int position);
    }

    private class MyListAdapter extends ArrayAdapter<NavigationDrawerItem>  {

        public MyListAdapter()  {

            super(getActivity().getApplicationContext(), R.layout.patient_navigation_drawer_listview, myNavTabs);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)  {

            View itemView = convertView;

            if (itemView == null)  {

                itemView = getActivity().getLayoutInflater().inflate(R.layout.patient_navigation_drawer_listview, parent, false);
            }

            NavigationDrawerItem currentTab = myNavTabs.get(position);

            TextView tvTitle = (TextView) itemView.findViewById(R.id.tvNavTabTitle);
            tvTitle.setText(currentTab.getTitle().toString());

            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageViewNavTab);

            if (tvTitle.getText().toString().contentEquals(getString(R.string.patient_title_section1)))  {

                imageView.setImageResource(R.drawable.profile_tab_icon);
            }
            else if (tvTitle.getText().toString().contentEquals(getString(R.string.patient_title_section2))) {

                imageView.setImageResource(R.drawable.health_tab_icon);
            }
            else if (tvTitle.getText().toString().contentEquals(getString(R.string.patient_title_section3))) {

                imageView.setImageResource(R.drawable.nurse_tab_icon);
            }
            else if (tvTitle.getText().toString().contentEquals(getString(R.string.patient_title_section4))) {

                imageView.setImageResource(R.drawable.settings_tab_icon);
            }
            else if (tvTitle.getText().toString().contentEquals(getString(R.string.patient_title_section5))) {

                imageView.setImageResource(R.drawable.help_tab_icon);
            }
            return itemView;
        }
    }
}