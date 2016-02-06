package edu.purdue.vieck.budgetapp.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.purdue.vieck.budgetapp.CustomObjects.DataItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.Fragments.ChartFragment;
import edu.purdue.vieck.budgetapp.R;


public class ChartActivity extends AppCompatActivity {
    ViewPagerAdapter adapter;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Spinner mSpinner;
    private ViewPager mViewPager;
    private PagerSlidingTabStrip mTabLayout;
    private RealmHandler mRealmHandler;
    private Context mContext;

    private SharedPreferences mSharedPreferences;

    private int actionBarColor;

    private int spinnerPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        actionBarColor = mSharedPreferences.getInt("actionBarColor", getResources().getColor(R.color.md_black_1000));
        setUpToolbar();
        setUpNavigationDrawer();
        setUpNavigationView();

        mRealmHandler = new RealmHandler(this);

        mContext = this;

        mSpinner = (Spinner) findViewById(R.id.spinner);
        mSpinner = setUpSpinner(mSpinner);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager);

        mTabLayout = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mTabLayout.setViewPager(mViewPager);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        mViewPager.setPageMargin(pageMargin);
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
        getMenuInflater().inflate(R.menu.menu_chart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            mRealmHandler.deleteAll();
            return true;
        } else if (id == R.id.action_add) {
            startActivity(new Intent(this, AddActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setBackgroundColor(actionBarColor);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    private void setUpNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                    case R.id.nav_item_dashboard:
                        intent = new Intent(currentActivity, DashboardActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        currentActivity.startActivity(intent);
                        break;
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
        spinnerPosition = 2;
        CharSequence[] simpleSpinner = getResources().getStringArray(R.array.chartarray);
        CustomArrayAdapter<CharSequence> spinnerArrayAdapter = new CustomArrayAdapter<>(this, simpleSpinner);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinner.setBackgroundColor(actionBarColor);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setSelection(2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if ((adapterView.getChildAt(0)) != null){
                    if (actionBarColor == getResources().getColor(R.color.md_white_1000)) {
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                    } else {
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                    }
                }
                spinnerPosition = position;
                Toast.makeText(mContext, mSpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                adapter.changeType(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return spinner;
    }

    class CustomArrayAdapter<T> extends ArrayAdapter<T> {
        public CustomArrayAdapter(Context context, T[] objects) {
            super(context, R.layout.simple_spinner_item, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = super.getDropDownView(position, convertView, parent);
            TextView textView = (TextView) view.findViewById(R.id.simple_spinner_text_view);
            if (actionBarColor == getResources().getColor(R.color.md_white_1000)) {
                textView.setTextColor(Color.BLACK);
            } else  {
                textView.setTextColor(Color.WHITE);
            }
            textView.setBackgroundColor(actionBarColor);
            return view;

        }
    }

    /*
    * Get a list of all years back as a hashmap and sort them accorrding to year
     */
    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        String[] list = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        ChartFragment chartFragment = new ChartFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("month", -1);
        bundle.putInt("year", -1);
        chartFragment.setArguments(bundle);
        adapter.addFragment(chartFragment, "Total");
        HashMap<Integer, List<DataItem>> years = mRealmHandler.getAllYearsAsHashmap(2);

        //Check if the arraylist is null first
        if (years != null) {
            ArrayList<Integer> uniqueMonths = new ArrayList<>();
            Integer[] keys = years.keySet().toArray(new Integer[years.keySet().size()]);

            //Sort hashmap keys
            for (int i = 0; i < keys.length; i++) {
                for (int j = i + 1; j < keys.length; j++) {
                    if (keys[j] > keys[i]) {
                        int hold = keys[i];
                        keys[i] = keys[j];
                        keys[j] = hold;
                    }
                }
            }

            //Sort years in hashmap keys
            for (int i : keys) {
                List<DataItem> dataItems = years.get(i);
                for (DataItem element : dataItems) {
                    bundle = new Bundle();
                    bundle.putInt("year", i);
                    bundle.putInt("month", element.getMonth());
                    chartFragment = new ChartFragment();
                    chartFragment.setArguments(bundle);
                    Log.d("Tabs", "YEAR " + i + " AND  MONTH " + element.getMonth() + " SIZE " + years.size());
                    if (!uniqueMonths.contains(element.getMonth())) {
                        Log.d("Month", element.getMonth() + " " + element.getMonthString());
                        adapter.addFragment(chartFragment, element.getMonthString() + " " + i);
                        uniqueMonths.add(element.getMonth());
                    }
                }
                uniqueMonths = new ArrayList<>();
            }
        }

        for (int i = 0; i < list.length; i++)
            viewPager.setAdapter(adapter);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final ArrayList<Fragment> mFragmentList = new ArrayList<>();
        private final ArrayList<String> mTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            mFragmentList.get(position);
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

        public void removeFragment(int position) {
            mFragmentList.remove(position);
            notifyDataSetChanged();
        }

        public void changeType(int type) {
            Log.d("Fragments", "" + mFragmentList.size());
            for (int i = 0; i < mFragmentList.size(); i++) {
                ChartFragment fragment = (ChartFragment) mFragmentList.get(i);
                if (fragment.getArguments() != null) {
                    fragment.getArguments().putInt("type", type);
                }
                fragment.updateAdapter(type);
            }
            notifyDataSetChanged();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }
    }

    public int getSpinnerPosition() {
        return spinnerPosition;
    }
}
