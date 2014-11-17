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

public class NavigationDrawerFragment_Patient extends Fragment {

    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    ArrayAdapter<NavigationDrawerItem> adapter;
    private List<NavigationDrawerItem> myNavTabs = new ArrayList<NavigationDrawerItem>();

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    public NavigationDrawerFragment_Patient() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mDrawerListView = (ListView) inflater.inflate(
                R.layout.fragment_navigation_drawer_patient, container, false);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }

        });

        myNavTabs.add((new NavigationDrawerItem(R.drawable.evening_icon, getString(R.string.patient_title_section1))));
        myNavTabs.add((new NavigationDrawerItem(R.drawable.evening_icon, getString(R.string.patient_title_section2))));
        myNavTabs.add((new NavigationDrawerItem(R.drawable.evening_icon, getString(R.string.patient_title_section3))));
        myNavTabs.add((new NavigationDrawerItem(R.drawable.evening_icon, getString(R.string.patient_title_section4))));
        myNavTabs.add((new NavigationDrawerItem(R.drawable.evening_icon, getString(R.string.patient_title_section5))));

        adapter = new MyListAdapter();

        mDrawerListView.setAdapter(adapter);
        /*
        mDrawerListView.setAdapter(new ArrayAdapter<String>(
                getActionBar().getThemedContext(),
                R.layout.patient_navigation_drawer_listview,
                R.id.list_content,
                new String[]{
                        getString(R.string.patient_title_section1),
                        getString(R.string.patient_title_section2),
                        getString(R.string.patient_title_section3),
                        getString(R.string.patient_title_section4),
                        getString(R.string.patient_title_section5)
                }));
        */
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        return mDrawerListView;
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        /*
        if (item.getItemId() == R.id.action_example) {
            Toast.makeText(getActivity(), "Example action.", Toast.LENGTH_SHORT).show();
            return true;
        }
        */
        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }

    private ActionBar getActionBar() {
        return getActivity().getActionBar();
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }

    private class MyListAdapter extends ArrayAdapter<NavigationDrawerItem> {

        public MyListAdapter() {
            super(getActivity().getApplicationContext(), R.layout.patient_navigation_drawer_listview, myNavTabs);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)  {

            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;

            if (itemView == null)  {

                itemView = getActivity().getLayoutInflater().inflate(R.layout.patient_navigation_drawer_listview, parent, false);
            }

            // Find the item to work with.
            NavigationDrawerItem currentTab = myNavTabs.get(position);

            //Date - Month
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
