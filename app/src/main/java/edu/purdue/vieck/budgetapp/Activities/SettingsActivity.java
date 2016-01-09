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
import android.view.MenuItem;
import android.view.View;

import edu.purdue.vieck.budgetapp.Fragments.SettingsFragment;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by vieck on 11/17/15.
 */
public class SettingsActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private SharedPreferences mSharedPreferences;
    private int actionBarColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        actionBarColor = mSharedPreferences.getInt("actionBarColor", Color.BLACK);
        setUpToolbar();
        setUpNavigationDrawer();
        setUpNavigationView();
        getFragmentManager().beginTransaction().add(R.id.fragment_container, new SettingsFragment()).commit();
    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            if (actionBarColor == getResources().getColor(R.color.md_white_1000)) {
                mToolbar.setTitleTextColor(Color.BLACK);
            } else {
                mToolbar.setTitleTextColor(Color.WHITE);
            }
            mToolbar.setBackgroundColor(actionBarColor);
            setSupportActionBar(mToolbar);
        }
    }

    private void setUpNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
