package edu.purdue.vieck.budgetapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import edu.purdue.vieck.budgetapp.Fragments.DataFragment;
import edu.purdue.vieck.budgetapp.R;

public class DataActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    DataFragment dataFragment;

    SharedPreferences mSharedPreferences;
    private int actionBarColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        actionBarColor = mSharedPreferences.getInt("actionBarColor",0);
        setUpToolbar();
        setUpNavigationDrawer();
        setUpNavigationView();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, dataFragment = new DataFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        /*if (id == R.id.data_action_search) {
            AddDataFragment addDataFragment = new AddDataFragment();
            addDataFragment.show(getSupportFragmentManager(), "AddDataFragment");
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (actionBarColor == getResources().getColor(R.color.md_white_1000)) {
            mToolbar.setTitleTextColor(Color.BLACK);
        } else {
            mToolbar.setTitleTextColor(Color.WHITE);
        }
        mToolbar.setBackgroundColor(actionBarColor);
        setSupportActionBar(mToolbar);
    }

    private void setUpNavigationDrawer() {
        if (mToolbar != null) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            mToolbar.setNavigationIcon(R.drawable.ic_drawer);
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
        if (actionBarColor != getResources().getColor(R.color.md_white_1000)) {
            mNavigationView.setItemIconTintList(ColorStateList.valueOf(Color.WHITE));
            mNavigationView.setItemTextColor(ColorStateList.valueOf(Color.WHITE));
        }
        mNavigationView.setBackgroundColor(actionBarColor);
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
}
