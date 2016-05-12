package edu.purdue.vieck.budgetapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;

import edu.purdue.vieck.budgetapp.Fragments.GraphFragmentMonthly;
import edu.purdue.vieck.budgetapp.Fragments.GraphFragmentComparison;
import edu.purdue.vieck.budgetapp.Fragments.GraphFragmentOverview;
import edu.purdue.vieck.budgetapp.R;

public class GraphActivity extends AppCompatActivity {

    ViewPagerAdapter adapter;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ViewPager mViewPager;
    private PagerSlidingTabStrip mTabLayout;

    private SharedPreferences mSharedPreferences;

    private int actionBarColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        actionBarColor = mSharedPreferences.getInt("actionBarColor", getResources().getColor(R.color.md_black_1000));
        setUpToolbar();
        setUpNavigationDrawer();
        setUpNavigationView();
        setupViewPager();
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        mViewPager.setPageMargin(pageMargin);
        mTabLayout.setViewPager(mViewPager);
        mTabLayout.setOnTabReselectedListener(new PagerSlidingTabStrip.OnTabReselectedListener() {
            @Override
            public void onTabReselected(int i) {
                mViewPager.setCurrentItem(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_graph, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
            mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setBackgroundColor(actionBarColor);
        setSupportActionBar(mToolbar);
    }

    private void setUpNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mToolbar != null) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }
    }

    private void setUpNavigationView() {
        final Activity currentActivity = this;
        mNavigationView = (NavigationView) findViewById(R.id.navigation_layout);
        mNavigationView.setBackgroundColor(actionBarColor);
        mNavigationView.setItemIconTintList(ColorStateList.valueOf(Color.WHITE));
        mNavigationView.setItemTextColor(ColorStateList.valueOf(Color.WHITE));
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                Intent intent;
                switch (id) {
                    case R.id.nav_item_chart:
                        intent = new Intent(currentActivity, ChartActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        currentActivity.startActivity(intent);
                        break;
                    case R.id.nav_item_graph:
                        intent = new Intent(currentActivity, GraphActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        currentActivity.startActivity(intent);
                        break;
                    case R.id.nav_item_projections:
                        intent = new Intent(currentActivity, ProjectionsActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        currentActivity.startActivity(intent);
                        break;
                    case R.id.nav_item_list:
                        intent = new Intent(currentActivity, DataActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        currentActivity.startActivity(intent);
                        break;
                    case R.id.nav_item_backup:
                        intent = new Intent(currentActivity, BackupActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        currentActivity.startActivity(intent);
                        break;
                    case R.id.nav_item_settings:
                        intent = new Intent(currentActivity, SettingsActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        currentActivity.startActivity(intent);
                        break;
                }
                return true;
            }
        });
    }

    private Spinner setUpSpinner(final Spinner spinner) {
        ArrayAdapter<CharSequence> spinnerArrayAdapter = ArrayAdapter.createFromResource(mToolbar.getContext(), R.array.chartarray, R.layout.simple_spinner_item);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setSelection(2);
        spinner.setBackgroundColor(actionBarColor);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                adapter.changeType(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return spinner;
    }

    private void setupViewPager() {
        mTabLayout = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        String[] list = {"Overview", "Categories", "Comparison"};

        FragmentManager fragmentManager = getSupportFragmentManager();

        GraphFragmentOverview fragmentOverview = (GraphFragmentOverview) fragmentManager.findFragmentByTag("Overview");
        if (fragmentOverview == null) {
            fragmentOverview = new GraphFragmentOverview();
        }
        adapter.addFragment(fragmentOverview, "Overview");
        GraphFragmentMonthly fragmentCategory = (GraphFragmentMonthly) fragmentManager.findFragmentByTag("Category");
        if (fragmentCategory == null) {
            fragmentCategory = new GraphFragmentMonthly();
        }
        adapter.addFragment(fragmentCategory, "Category");
        GraphFragmentComparison fragmentComparison = (GraphFragmentComparison) fragmentManager.findFragmentByTag("Comparison");
        if (fragmentComparison == null) {
            fragmentComparison = new GraphFragmentComparison();
        }
        adapter.addFragment(fragmentComparison,"Comparison");
        mViewPager.setAdapter(adapter);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final ArrayList<Fragment> mFragmentList = new ArrayList<>();
        private final ArrayList<String> mTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mTitleList.add(title);
        }

        public void replaceFragment(Fragment fragment, String title) {
            for (int i = 0; i < mTitleList.size(); i++) {
                if (mTitleList.get(i).equals(title)) {
                    mFragmentList.set(i, fragment);
                    notifyDataSetChanged();
                    return;
                }
            }
        }

        public void changeType(int type) {
            GraphFragmentOverview fragmentOverview = (GraphFragmentOverview) mFragmentList.get(0);
            if (fragmentOverview.getArguments() != null) {
                fragmentOverview.getArguments().putInt("type", type);
            }
            //fragmentOverview.updateType(type);

            GraphFragmentMonthly fragmentCategory = (GraphFragmentMonthly) mFragmentList.get(1);
            if (fragmentCategory.getArguments() != null) {
                fragmentOverview.getArguments().putInt("type", type);
            }
            fragmentCategory.updateType(type);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }
    }
}
