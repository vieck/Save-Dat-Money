package edu.purdue.vieck.budgetapp.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import edu.purdue.vieck.budgetapp.Adapters.ChartRecyclerAdapter;
import edu.purdue.vieck.budgetapp.Adapters.DividerItemDecoration;
import edu.purdue.vieck.budgetapp.CustomObjects.RealmCategoryItem;
import edu.purdue.vieck.budgetapp.CustomObjects.RealmDataItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.Dialogs.ChartDatePicker;
import edu.purdue.vieck.budgetapp.R;
import edu.purdue.vieck.budgetapp.databinding.ActivityChartBinding;
import io.realm.RealmResults;


public class ChartActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    ActivityChartBinding binding;

    //private int day, month, year;
    RealmHandler mRealmHandler;

    private SharedPreferences mSharedPreferences;

    private int actionBarColor;

    ChartDatePicker datePicker;

    int allDataTab = -1;
    int weekTab = -1;
    int todayTab = -1;
    int monthTab = -1;
    int yearTab = -1;

    // Use to change date
    private GregorianCalendar date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chart);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        actionBarColor = mSharedPreferences.getInt("actionBarColor", getResources().getColor(R.color.md_black_1000));
        setUpToolbar();
        setUpNavigationDrawer();
        setUpNavigationView();

        mRealmHandler = new RealmHandler(this);

        setUpSpinner();

        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        RealmResults<RealmDataItem> items = mRealmHandler.getResultsByFilter(2);
        binding.budgetRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.budgetRecyclerView.setAdapter(new ChartRecyclerAdapter(this, items));
        binding.budgetRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL, getResources().getColor(R.color.flat_peterriver)));

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // mRecyclerView.getAdapter().inser
                // mRecyclerView.setAdapter(mChartRecyclerAdapter);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        setupPieChart();

        setupBudget();

        date = new GregorianCalendar();
//        day = date.get(Calendar.DAY_OF_MONTH);
//        month = date.get(Calendar.MONTH) + 1;
//        year = date.get(Calendar.YEAR);
        // setDateLabel(month, year);
        setupDateTabs();
        //setData(mSpinner.getSelectedItemPosition());
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
            // mRealmHandler.deleteAll();
            return true;
        } else if (id == R.id.action_add) {
            startActivity(new Intent(this, AddActivity.class));
            finish();
            return true;
        } else if (id == R.id.action_date) {
            datePicker = new ChartDatePicker();

            datePicker.show(getFragmentManager(), "datePicker");
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpToolbar() {
        binding.toolbar.setTitleTextColor(Color.WHITE);
        //   mToolbar.setBackgroundColor(actionBarColor);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    private void setUpNavigationDrawer() {
        if (binding.toolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            binding.toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
            binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.drawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }
    }

    private void setUpNavigationView() {
        final Activity currentActivity = this;
//        mNavigationView.setBackgroundColor(actionBarColor);
        binding.navigationLayout.setItemIconTintList(ColorStateList.valueOf(Color.WHITE));
        binding.navigationLayout.setItemTextColor(ColorStateList.valueOf(Color.WHITE));
        binding.navigationLayout.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
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

    /*
     * Spinner to filter income, expenses, or both
     */
    private void setUpSpinner() {
        CharSequence[] simpleSpinner = getResources().getStringArray(R.array.chartarray);
        CustomArrayAdapter<CharSequence> spinnerArrayAdapter = new CustomArrayAdapter<>(this, simpleSpinner);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(spinnerArrayAdapter);
        // spinner.setSelection(0);
        binding.spinner.dispatchSetSelected(true);
//        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                //Toast.makeText(mContext, mSpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
//                switch (mSpinner.getSelectedItemPosition()) {
//                    case 0:
//                        setDateLabel();
//                        break;
//                    case 1:
//                        Toast.makeText(getApplicationContext(), (date.get(Calendar.MONTH) + 1) + "/" + date.get(Calendar.DAY_OF_MONTH) + "/" + date.get(Calendar.YEAR), Toast.LENGTH_SHORT).show();
//                        setDateLabel(date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.MONTH) + 1, date.get(Calendar.YEAR));
//                        break;
//                    case 2:
//                        date.add(Calendar.WEEK_OF_YEAR, -1);
//
//                        int weekday = date.get(Calendar.DAY_OF_WEEK);
//
//                        int firstDay, lastDay, firstMonth, secondMonth;
//                        date.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
//                        firstDay = date.get(Calendar.DAY_OF_MONTH);
//                        firstMonth = date.get(Calendar.MONTH);
//
//                        date.add(Calendar.DATE, 7);
//                        lastDay = date.get(Calendar.DAY_OF_MONTH);
//                        secondMonth = date.get(Calendar.MONTH);
//
//                        date.set(Calendar.DAY_OF_WEEK, weekday);
//
//                        Toast.makeText(getApplicationContext(), (date.get(Calendar.MONTH) + 1) + "/" + date.get(Calendar.DAY_OF_MONTH) + "/" + date.get(Calendar.YEAR), Toast.LENGTH_SHORT).show();
//
//                        setDateLabel(firstDay, lastDay, firstMonth + 1, secondMonth + 1, date.get(Calendar.YEAR));
//                        break;
//                    case 3:
//                        setDateLabel(date.get(Calendar.MONTH) + 1, date.get(Calendar.YEAR));
//                        break;
//                    case 4:
//                        setDateLabel(date.get(Calendar.YEAR));
//                        break;
//                }
//                setData(mSpinner.getSelectedItemPosition());
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
    }

    private class CustomArrayAdapter<T> extends ArrayAdapter<T> {
        public CustomArrayAdapter(Context context, T[] objects) {
            super(context, R.layout.simple_spinner_item, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = super.getDropDownView(position, convertView, parent);
            return view;

        }
    }

    private void setupDateTabs() {
        Log.d("Before Date Change", date.toString());

        final String dateTag = Integer.toString(date.get(Calendar.MONTH) + 1) + "," + Integer.toString(date.get(Calendar.DATE)) + "," + Integer.toString(date.get(Calendar.YEAR));
        if (!mRealmHandler.getResultsByFilter(date.get(Calendar.DATE), date.get(Calendar.MONTH), date.get(Calendar.YEAR), 2).isEmpty()) {
            binding.tablayoutDate.addTab(binding.tablayoutDate.newTab().setText("This Week").setTag(dateTag));
            weekTab = binding.tablayoutDate.getTabCount();
        }
        if (!mRealmHandler.getResultsByFilter(date.get(Calendar.DATE), date.get(Calendar.MONTH), date.get(Calendar.YEAR), 2).isEmpty()) {
            binding.tablayoutDate.addTab(binding.tablayoutDate.newTab().setText("Today").setTag(dateTag));
            todayTab = binding.tablayoutDate.getTabCount();
        }
        if (!mRealmHandler.getResultsByFilter(2).isEmpty()) {
            binding.tablayoutDate.addTab(binding.tablayoutDate.newTab().setText("All").setTag(dateTag));
            allDataTab = binding.tablayoutDate.getTabCount();
        }
        if (!mRealmHandler.getResultsByFilter(date.get(Calendar.MONTH), date.get(Calendar.YEAR), 2).isEmpty()) {
            binding.tablayoutDate.addTab(binding.tablayoutDate.newTab().setText("This Month").setTag(dateTag));
            monthTab = binding.tablayoutDate.getTabCount();
        }
        if (!mRealmHandler.getResultsByFilter(date.get(Calendar.YEAR), 2).isEmpty()) {
            binding.tablayoutDate.addTab(binding.tablayoutDate.newTab().setText("This Year").setTag(dateTag));
            yearTab = binding.tablayoutDate.getTabCount();
        }
        final int week = weekTab - 1;
        final int today = todayTab - 1;
        final int all = allDataTab - 1;
        final int month = monthTab - 1;
        final int year = yearTab - 1;
        binding.tablayoutDate.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getTag() != null) {
                    String[] dateString = tab.getTag().toString().split(",");
                    for (int i = 0; i < dateString.length; i++) {
                        date.set(Integer.parseInt(dateString[2]), Integer.parseInt(dateString[0]), Integer.parseInt(dateString[1]));
                    }
                    if (tab.getPosition() == today) {
                        setDateLabel(date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.MONTH) + 1, date.get(Calendar.YEAR));
                        setData(1);

                    } else if (tab.getPosition() == week) {
                        int firstDay, lastDay, firstMonth, secondMonth;
                        lastDay = date.get(Calendar.DAY_OF_MONTH);
                        secondMonth = date.get(Calendar.MONTH);

                        date.add(Calendar.DATE, -7);

                        firstDay = date.get(Calendar.DAY_OF_MONTH);
                        firstMonth = date.get(Calendar.MONTH);
                        setDateLabel(firstDay, lastDay, firstMonth + 1, secondMonth + 1, date.get(Calendar.YEAR));
                        setData(2);

                    } else if (tab.getPosition() == all) {
                        setDateLabel();
                        setData(0);
                    } else if (tab.getPosition() == month) {
                        setDateLabel(date.get(Calendar.MONTH) + 1, date.get(Calendar.YEAR));
                        setData(3);

                    } else if (tab.getPosition() == year) {
                        setDateLabel(date.get(Calendar.YEAR));
                        setData(4);

                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
//        leftArrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                switch (mSpinner.getSelectedItemPosition()) {
//                    case 1:
//                        date.add(Calendar.DAY_OF_MONTH, -1);
//                        setDateLabel(date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.MONTH) + 1, date.get(Calendar.YEAR));
//                        break;
//                    case 2:
//                        int firstDay, lastDay, firstMonth, secondMonth;
//                        lastDay = date.get(Calendar.DAY_OF_MONTH);
//                        secondMonth = date.get(Calendar.MONTH);
//
//                        date.add(Calendar.DATE, -7);
//
//                        firstDay = date.get(Calendar.DAY_OF_MONTH);
//                        firstMonth = date.get(Calendar.MONTH);
//                        setDateLabel(firstDay, lastDay, firstMonth + 1, secondMonth + 1, date.get(Calendar.YEAR));
//                        break;
//                    case 3:
//                        date.add(Calendar.MONTH, -1);
//                        setDateLabel(date.get(Calendar.MONTH) + 1, date.get(Calendar.YEAR));
//                        break;
//                    case 4:
//                        date.add(Calendar.YEAR, -1);
//                        setDateLabel(date.get(Calendar.YEAR));
//                        break;
//                }
//                setData(mSpinner.getSelectedItemPosition());
//            }
//        });
//        rightArrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                switch (mSpinner.getSelectedItemPosition()) {
//                    case 1:
//                        date.add(Calendar.DAY_OF_MONTH, 1);
//                        setDateLabel(date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.MONTH) + 1, date.get(Calendar.YEAR));
//                        break;
//                    case 2:
//                        int firstDay, lastDay, oldDay, firstMonth, secondMonth;
//                        oldDay = date.get(Calendar.DAY_OF_MONTH);
//                        firstDay = date.get(Calendar.DAY_OF_MONTH);
//                        firstMonth = date.get(Calendar.MONTH);
//
//                        date.add(Calendar.DATE, 7);
//
//                        lastDay = date.get(Calendar.DAY_OF_MONTH);
//                        secondMonth = date.get(Calendar.MONTH);
//                        setDateLabel(firstDay, lastDay, firstMonth + 1, secondMonth + 1, date.get(Calendar.YEAR));
//                        break;
//                    case 3:
//                        date.add(Calendar.MONTH, 1);
//                        setDateLabel(date.get(Calendar.MONTH) + 1, date.get(Calendar.YEAR));
//                        break;
//                    case 4:
//                        date.add(Calendar.YEAR, 1);
//                        setDateLabel(date.get(Calendar.YEAR));
//                        break;
//                }
//                setData(mSpinner.getSelectedItemPosition());
//            }
//        });
        Log.d("After Date Change", date.toString());
        if (allDataTab != -1) {
            binding.tablayoutDate.getTabAt(allDataTab - 1).select();
            setDateLabel();
            setData(0);
        } else {
            binding.tablayoutDate.addTab(binding.tablayoutDate.newTab().setText("No data"));
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        date.set(day, month, year);
        String monthName = date.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        String name = monthName + " " + Integer.toString(date.get(Calendar.DATE)) + ", " + Integer.toString(date.get(Calendar.YEAR));
        final String dateTag = Integer.toString(date.get(Calendar.MONTH)) + "," + Integer.toString(date.get(Calendar.DATE)) + "," + Integer.toString(date.get(Calendar.YEAR));
        if (todayTab == -1) {
            binding.tablayoutDate.addTab(binding.tablayoutDate.newTab().setText(name).setTag(dateTag));

        } else {
            binding.tablayoutDate.getTabAt(todayTab).setText(name).setTag(dateTag);
        }
        switch (binding.spinner.getSelectedItemPosition()) {
            case 1:
            case 2:
                setDateLabel(date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.MONTH) + 1, date.get(Calendar.YEAR));
                break;
            case 3:
                setDateLabel(date.get(Calendar.MONTH) + 1, date.get(Calendar.YEAR));
                break;
            case 4:
                setDateLabel(date.get(Calendar.YEAR));
                break;
        }
    }


    private void setDateLabel(int day, int month, int year) {
//        mDateLabel.setText(month + "/" + day + "/" + year);
        RealmResults<RealmDataItem> items = mRealmHandler.getResultsByFilter(day, month, year, 2);
        ChartRecyclerAdapter adapter = (ChartRecyclerAdapter) binding.budgetRecyclerView.getAdapter();
        adapter.updateData(items);
    }

    private void setDateLabel(int startDay, int endDay, int firstMonth, int secondMonth, int year) {
//        mDateLabel.setText(firstMonth + "/" + startDay + "/" + year + " - " + secondMonth + "/" + endDay + "/" + year);
        RealmResults<RealmDataItem> items = mRealmHandler.getResultsByFilter(startDay, endDay, firstMonth, secondMonth, year, 2);
        ChartRecyclerAdapter adapter = (ChartRecyclerAdapter) binding.budgetRecyclerView.getAdapter();
        adapter.updateData(items);
    }

    private void setDateLabel(int month, int year) {
//        mDateLabel.setText(month + "/" + year);
        RealmResults<RealmDataItem> items = mRealmHandler.getResultsByFilter(month, year, 2);
        ChartRecyclerAdapter adapter = (ChartRecyclerAdapter) binding.budgetRecyclerView.getAdapter();
        adapter.updateData(items);
    }

    private void setDateLabel(int year) {
//        mDateLabel.setText("" + year);
        RealmResults<RealmDataItem> items = mRealmHandler.getResultsByFilter(year, 2);
        ChartRecyclerAdapter adapter = (ChartRecyclerAdapter) binding.budgetRecyclerView.getAdapter();
        adapter.updateData(items);
    }

    private void setDateLabel() {
//        mDateLabel.setText("All");
        RealmResults<RealmDataItem> items = mRealmHandler.getResultsByFilter(2);
        ChartRecyclerAdapter adapter = (ChartRecyclerAdapter) binding.budgetRecyclerView.getAdapter();
        adapter.updateData(items);
    }

    private void setupPieChart() {
        binding.pieChart.setDescription("");
        binding.pieChart.setDescriptionColor(getResources().getColor(R.color.White));
        binding.pieChart.setUsePercentValues(true);
        binding.pieChart.setDragDecelerationFrictionCoef(0.95f);
        //mTypeface = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        binding.pieChart.setDrawHoleEnabled(true);
        binding.pieChart.setHoleColor(Color.TRANSPARENT);
        binding.pieChart.setCenterTextColor(Color.WHITE);
        binding.pieChart.setHoleRadius(60f);
        binding.pieChart.setTransparentCircleRadius(63f);
        binding.pieChart.setTransparentCircleColor(getResources().getColor(R.color.flat_wetasphalt));
        binding.pieChart.setTransparentCircleAlpha(200);
        binding.pieChart.setDrawCenterText(true);
        binding.pieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        binding.pieChart.setRotationEnabled(true);

//        mPieChart.setCenterTextSize(12.5f);

        binding.pieChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);*/

        Legend l = binding.pieChart.getLegend();
        l.setPosition(Legend.LegendPosition.PIECHART_CENTER);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(-20f);
        l.setXOffset(5f);
        l.setTextColor(Color.WHITE);
    }

    private void setupBudget() {
//        if (month != -1 && year != -1) {
//            final float budget = mRealmHandler.getBudget(month, year);
//            mBudgetView.setText(budget + "");
//            mBudgetView.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    if (charSequence.length() != i1) {
//                        mCancelButton.setVisibility(View.VISIBLE);
//                        mConfirmButton.setVisibility(View.VISIBLE);
//                    }
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//                }
//            });
//
//            mCancelButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mBudgetView.setText(budget + "");
//                    mCancelButton.setVisibility(View.INVISIBLE);
//                    mConfirmButton.setVisibility(View.INVISIBLE);
//                    InputMethodManager inputManager = (InputMethodManager)
//                            mContext.getSystemService(INPUT_METHOD_SERVICE);
//
//                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
//                            InputMethodManager.HIDE_NOT_ALWAYS);
//                }
//            });
//
//            mConfirmButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    try {
//                        float budget = Float.parseFloat(mBudgetView.getText().toString());
//                        mRealmHandler.update(budget, month, year);
//                        mCancelButton.setVisibility(View.INVISIBLE);
//                        mConfirmButton.setVisibility(View.INVISIBLE);
//                    } catch (NumberFormatException ex) {
//                        Toast.makeText(getApplicationContext(), "Invalid number", Toast.LENGTH_SHORT).show();
//                    } finally {
//                        InputMethodManager inputManager = (InputMethodManager)
//                                mContext.getSystemService(INPUT_METHOD_SERVICE);
//
//                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
//                                InputMethodManager.HIDE_NOT_ALWAYS);
//                    }
//                }
//            });
//        } else {
//            float budget = mRealmHandler.getBudget();
//            mBudgetView.setText(budget + "");
//            mBudgetView.setFocusable(false);
//            mBudgetView.setEnabled(false);
//        }
//        mCurrencyLabel.setText(mSharedPreferences.getString("currencySymbol", Currency.getInstance(mContext.getResources().getConfiguration().locale).getSymbol()));
    }

    private void setData(int type) {

        binding.pieChart.setData(null);
        binding.pieChart.invalidate();

        ArrayList<PieEntry> yVals = new ArrayList<PieEntry>();
        List<RealmCategoryItem> categories = mRealmHandler.getCategoryParents();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.

        List<Number> amounts = mRealmHandler.getPieChartData(date, type);

        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Integer> colors = new ArrayList<Integer>();

        int total = 0;
        for (int i = 0; i < categories.size(); i++) {
            float amount = amounts.get(i).floatValue();
            if (amount != 0) {
                total += amount;
                yVals.add(new PieEntry(amount, categories.get(i).getCategory()));
                xVals.add(categories.get(i).getCategory());
                colors.add(categories.get(i).getColor());
            }
        }

        if (total > 0) {
            PieDataSet dataSet = new PieDataSet(yVals, "Category Legend");
            dataSet.setSliceSpace(2f);
            dataSet.setSelectionShift(5f);
            dataSet.setColors(colors);
            PieData data = new PieData(dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(14f);
            data.setValueTextColor(Color.WHITE);
            binding.pieChart.setData(data);
            //Hide labels since the legend is shown
            binding.pieChart.setDrawEntryLabels(false);
            // undo all highlights
            binding.pieChart.highlightValues(null);

            binding.pieChart.invalidate();
        }
    }
    /*
    * Get a list of all years back as a hashmap and sort them accorrding to year
     */
//    private void setupViewPager(ViewPager viewPager) {
//        adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        String[] list = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
//        ChartFragment chartFragment = new ChartFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt("month", -1);
//        bundle.putInt("year", -1);
//        chartFragment.setArguments(bundle);
//        adapter.addFragment(chartFragment, "Total");
//        HashMap<Integer, List<RealmDataItem>> years = mRealmHandler.getAllYearsAsHashmap(2);
//
//        //Check if the arraylist is null first
//        if (years != null) {
//            ArrayList<Integer> uniqueMonths = new ArrayList<>();
//            Integer[] keys = years.keySet().toArray(new Integer[years.keySet().size()]);
//            // Sort years in ascending order
//            Arrays.sort(keys);
//            Log.d("Sorted Array",Arrays.toString(keys));
//
//            //Sort years in hashmap keys
//            for (int i : keys) {
//                List<RealmDataItem> realmDataItems = years.get(i);
//                for (RealmDataItem element : realmDataItems) {
//                    bundle = new Bundle();
//                    bundle.putInt("year", i);
//                    bundle.putInt("month", element.getMonth());
//                    chartFragment = new ChartFragment();
//                    chartFragment.setArguments(bundle);
//                    Log.d("Tabs", "YEAR " + i + " AND  MONTH " + element.getMonth() + " SIZE " + years.size());
//                    if (!uniqueMonths.contains(element.getMonth())) {
//                        Log.d("Month", element.getMonth() + " " + element.getMonthString());
//                        adapter.addFragment(chartFragment, element.getMonthString() + " " + i);
//                        uniqueMonths.add(element.getMonth());
//                    }
//                }
//                uniqueMonths.clear();
//            }
//        }
//
//        for (int i = 0; i < list.length; i++)
//            viewPager.setAdapter(adapter);
//    }

}
