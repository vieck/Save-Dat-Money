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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.realm.implementation.RealmLineData;
import com.github.mikephil.charting.data.realm.implementation.RealmLineDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.purdue.vieck.budgetapp.CustomObjects.DataItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.R;
import io.realm.RealmResults;

public class ProjectionsActivity extends AppCompatActivity {

    private LineChart mProjectionChart;
    private SharedPreferences mSharedPreferences;
    private int actionBarColor;

    private RealmHandler mRealmHandler;

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projections);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        actionBarColor = mSharedPreferences.getInt("actionBarColor", getResources().getColor(R.color.md_black_1000));

        setUpToolbar();
        setUpNavigationDrawer();
        setUpNavigationView();

        mRealmHandler = new RealmHandler(this);

        createProjectionChart();
    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setBackgroundColor(actionBarColor);
        mToolbar.setTitleTextColor(Color.WHITE);
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
            mNavigationView.setItemIconTintList(ColorStateList.valueOf(Color.WHITE));
            mNavigationView.setItemTextColor(ColorStateList.valueOf(Color.WHITE));
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

    private void createProjectionChart() {
        mProjectionChart = (LineChart) findViewById(R.id.line_chart);
        mProjectionChart.getAxisRight().setEnabled(false);

        List<Entry> expenseEntries = new ArrayList<>();
        List<Entry> expenseProjectionEntries = new ArrayList<>();
        HashMap<String, Float> uniqueMonths = mRealmHandler.getAllMonthsAsOneElement(0);
        Set<String> keys = uniqueMonths.keySet();
        String[] labels = keys.toArray(new String[keys.size()]);

        float past, present, future;
        int i = 0;
        for (String label : labels) {
            if (i >= 1) {
                present = uniqueMonths.get(labels[i]);
                past = uniqueMonths.get(labels[i-1]);
                future = ((present - past) / past) * uniqueMonths.get(labels[i-1]);
                Log.i("Projection", "" + past + " " + present + " " + future + " i " + i);
                expenseProjectionEntries.add(new Entry(future, i));
            } else {
                expenseProjectionEntries.add(new Entry(uniqueMonths.get(label), i));
            }
            expenseEntries.add(new Entry(uniqueMonths.get(label), i++));
        }

        LineDataSet expenseLineSet = new LineDataSet(expenseEntries, "Expense");
        expenseLineSet.setDrawCubic(false);
        expenseLineSet.setLabel("Expenses");
        expenseLineSet.setDrawCircleHole(false);
        expenseLineSet.setColor(ColorTemplate.rgb("#FF5722"));
        expenseLineSet.setCircleColor(ColorTemplate.rgb("#FF5722"));
        expenseLineSet.setLineWidth(1.8f);

        LineDataSet expenseProjectionLineSet = new LineDataSet(expenseProjectionEntries, "Expense Projection");
        expenseProjectionLineSet.setDrawCubic(false);
        expenseProjectionLineSet.enableDashedLine(10f, 10f, 10f);
        expenseProjectionLineSet.setLabel("Expenses");
        expenseProjectionLineSet.setDrawCircleHole(false);
        expenseProjectionLineSet.setColor(ColorTemplate.rgb("#FF5722"));
        expenseProjectionLineSet.setCircleColor(ColorTemplate.rgb("#FF5722"));
        expenseProjectionLineSet.setLineWidth(1.8f);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(expenseLineSet); // add the dataset
        dataSets.add(expenseProjectionLineSet);

        // create a data object with the dataset list
        LineData data = new LineData(labels, dataSets);
        data.setValueTextSize(8f);
        data.setValueTextColor(Color.DKGRAY);

        // expenseLineSet data
        mProjectionChart.setData(data);
        mProjectionChart.animateY(1400, Easing.EasingOption.EaseInOutQuart);

    }
}
