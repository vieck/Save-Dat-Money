package edu.purdue.vieck.budgetapp.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import edu.purdue.vieck.budgetapp.Adapters.ChartRecyclerAdapter;
import edu.purdue.vieck.budgetapp.CustomObjects.RealmCategoryItem;
import edu.purdue.vieck.budgetapp.CustomObjects.RealmDataItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.Dialogs.ChartDatePicker;
import edu.purdue.vieck.budgetapp.R;
import io.realm.RealmResults;


public class ChartActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Spinner mSpinner;
    private Context mContext;

    private int day, month, year, type;
    RealmHandler mRealmHandler;
    private PieChart mPieChart;
    private EditText mBudgetView;
    private TextView mCurrencyLabel, mDateLabel;
    private FloatingActionButton mConfirmButton, mCancelButton;

    private ImageButton leftArrow, rightArrow;

    private RecyclerView mRecyclerView;

    private SharedPreferences mSharedPreferences;

    private int actionBarColor;

    private int spinnerPosition;

    ChartDatePicker datePicker;

    // Use to change date
    private GregorianCalendar date;

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

        day = -1;
        month = -1;
        year = -1;

        mSpinner = (Spinner) findViewById(R.id.spinner);
        mSpinner = setUpSpinner(mSpinner);

        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.budget_recycler_view);
        RealmResults<RealmDataItem> items = mRealmHandler.getResultsByFilter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(new ChartRecyclerAdapter(mContext, items));

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // mRecyclerView.getAdapter().inser
                // mRecyclerView.setAdapter(mChartRecyclerAdapter);
                setData(type);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        mPieChart = (PieChart) findViewById(R.id.pie_chart);
        mPieChart = setupPieChart(mPieChart);
        mBudgetView = (EditText) findViewById(R.id.edittext_budget);
        mCurrencyLabel = (TextView) findViewById(R.id.currency_textview);
        mCancelButton = (FloatingActionButton) findViewById(R.id.budget_button_cancel);
        mConfirmButton = (FloatingActionButton) findViewById(R.id.budget_button_ok);

        leftArrow = (ImageButton) findViewById(R.id.left_arrow);
        rightArrow = (ImageButton) findViewById(R.id.right_arrow);
        setupBudget();
        setData(type);

        mDateLabel = (TextView) findViewById(R.id.date_display_text);

        date = new GregorianCalendar();
        day = date.get(Calendar.DAY_OF_MONTH);
        month = date.get(Calendar.MONTH) + 1;
        year = date.get(Calendar.YEAR);
        setDateLabel(month, year);
        setupDateArrows();
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
    private Spinner setUpSpinner(final Spinner spinner) {
        spinnerPosition = 1;
        CharSequence[] simpleSpinner = getResources().getStringArray(R.array.chartarray);
        CustomArrayAdapter<CharSequence> spinnerArrayAdapter = new CustomArrayAdapter<>(this, simpleSpinner);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinner.setBackgroundColor(actionBarColor);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setSelection(0);
        spinner.dispatchSetSelected(true);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if ((adapterView.getChildAt(0)) != null) {
                    if (actionBarColor == getResources().getColor(R.color.md_white_1000)) {
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                    } else {
                        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                    }
                }
                spinnerPosition = position;
                //Toast.makeText(mContext, mSpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                switch (mSpinner.getSelectedItemPosition()) {
                    case 0:
                        setDateLabel();
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), (date.get(Calendar.MONTH) + 1) + "/" + date.get(Calendar.DAY_OF_MONTH) + "/" + date.get(Calendar.YEAR), Toast.LENGTH_SHORT).show();
                        setDateLabel(date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.MONTH) + 1, date.get(Calendar.YEAR));
                        break;
                    case 2:
                        date.add(Calendar.WEEK_OF_YEAR, -1);

                        int weekday = date.get(Calendar.DAY_OF_WEEK);

                        int firstDay, lastDay, firstMonth, secondMonth;
                        date.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                        firstDay = date.get(Calendar.DAY_OF_MONTH);
                        firstMonth = date.get(Calendar.MONTH);

                        date.add(Calendar.DATE, 7);
                        lastDay = date.get(Calendar.DAY_OF_MONTH);
                        secondMonth = date.get(Calendar.MONTH);

                        date.set(Calendar.DAY_OF_WEEK, weekday);

                        Toast.makeText(getApplicationContext(), (date.get(Calendar.MONTH) + 1) + "/" + date.get(Calendar.DAY_OF_MONTH) + "/" + date.get(Calendar.YEAR), Toast.LENGTH_SHORT).show();

                        setDateLabel(firstDay, lastDay, firstMonth + 1, secondMonth + 1, date.get(Calendar.YEAR));
                        break;
                    case 3:
                        setDateLabel(date.get(Calendar.MONTH) + 1, date.get(Calendar.YEAR));
                        break;
                    case 4:
                        setDateLabel(date.get(Calendar.YEAR));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return spinner;
    }

    private class CustomArrayAdapter<T> extends ArrayAdapter<T> {
        public CustomArrayAdapter(Context context, T[] objects) {
            super(context, R.layout.simple_spinner_item, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = super.getDropDownView(position, convertView, parent);
            TextView textView = (TextView) view.findViewById(R.id.simple_spinner_text_view);
            if (actionBarColor == getResources().getColor(R.color.md_white_1000)) {
                textView.setTextColor(Color.BLACK);
            } else {
                textView.setTextColor(Color.WHITE);
            }
            textView.setBackgroundColor(actionBarColor);
            return view;

        }
    }

    private void setupDateArrows() {
        Log.d("Before Date Change", date.toString());
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mSpinner.getSelectedItemPosition()) {
                    case 1:
                        date.add(Calendar.DAY_OF_MONTH, -1);
                        setDateLabel(date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.MONTH) + 1, date.get(Calendar.YEAR));
                        break;
                    case 2:
                        int firstDay, lastDay, oldDay, firstMonth, secondMonth;
                        lastDay = date.get(Calendar.DAY_OF_MONTH);
                        secondMonth = date.get(Calendar.MONTH);

                        date.add(Calendar.DATE, -7);

                        firstDay = date.get(Calendar.DAY_OF_MONTH);
                        firstMonth = date.get(Calendar.MONTH);
                        setDateLabel(firstDay, lastDay, firstMonth + 1, secondMonth + 1, date.get(Calendar.YEAR));
                        break;
                    case 3:
                        date.add(Calendar.MONTH, -1);
                        setDateLabel(date.get(Calendar.MONTH) + 1, date.get(Calendar.YEAR));
                        break;
                    case 4:
                        date.add(Calendar.YEAR, -1);
                        setDateLabel(date.get(Calendar.YEAR));
                        break;
                }
            }
        });
        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mSpinner.getSelectedItemPosition()) {
                    case 1:
                        date.add(Calendar.DAY_OF_MONTH, 1);
                        setDateLabel(date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.MONTH) + 1, date.get(Calendar.YEAR));
                        break;
                    case 2:
                        int firstDay, lastDay, oldDay, firstMonth, secondMonth;
                        oldDay = date.get(Calendar.DAY_OF_MONTH);
                        firstDay = date.get(Calendar.DAY_OF_MONTH);
                        firstMonth = date.get(Calendar.MONTH);

                        date.add(Calendar.DATE, 7);

                        lastDay = date.get(Calendar.DAY_OF_MONTH);
                        secondMonth = date.get(Calendar.MONTH);
                        setDateLabel(firstDay, lastDay, firstMonth + 1, secondMonth + 1, date.get(Calendar.YEAR));
                        break;
                    case 3:
                        date.add(Calendar.MONTH, 1);
                        setDateLabel(date.get(Calendar.MONTH) + 1, date.get(Calendar.YEAR));
                        break;
                    case 4:
                        date.add(Calendar.YEAR, 1);
                        setDateLabel(date.get(Calendar.YEAR));
                        break;
                }
            }
        });
        Log.d("After Date Change", date.toString());
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        date.set(day, month, year);
        switch (mSpinner.getSelectedItemPosition()) {
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
        mDateLabel.setText(month + "/" + day + "/" + year);
        RealmResults<RealmDataItem> items = mRealmHandler.getResultsByFilter(day, month, year, 2);
        ChartRecyclerAdapter adapter = (ChartRecyclerAdapter) mRecyclerView.getAdapter();
        adapter.updateData(items);
    }

    private void setDateLabel(int startDay, int endDay, int firstMonth, int secondMonth, int year) {
        mDateLabel.setText(firstMonth + "/" + startDay + "/" + year + " - " + secondMonth + "/" + endDay + "/" + year);
        RealmResults<RealmDataItem> items = mRealmHandler.getResultsByFilter(startDay, endDay, firstMonth, secondMonth, year, 2);
        ChartRecyclerAdapter adapter = (ChartRecyclerAdapter) mRecyclerView.getAdapter();
        adapter.updateData(items);
    }

    private void setDateLabel(int month, int year) {
        mDateLabel.setText(month + "/" + year);
        RealmResults<RealmDataItem> items = mRealmHandler.getResultsByFilter(month, year, 2);
        ChartRecyclerAdapter adapter = (ChartRecyclerAdapter) mRecyclerView.getAdapter();
        adapter.updateData(items);
    }

    private void setDateLabel(int year) {
        mDateLabel.setText("" + year);
        RealmResults<RealmDataItem> items = mRealmHandler.getResultsByFilter(year, 2);
        ChartRecyclerAdapter adapter = (ChartRecyclerAdapter) mRecyclerView.getAdapter();
        adapter.updateData(items);
    }

    private void setDateLabel() {
        mDateLabel.setText("All");
        RealmResults<RealmDataItem> items = mRealmHandler.getResultsByFilter();
        ChartRecyclerAdapter adapter = (ChartRecyclerAdapter) mRecyclerView.getAdapter();
        adapter.updateData(items);
    }

    private PieChart setupPieChart(PieChart chart) {
        chart.setDescription("");
        chart.setDescriptionColor(getResources().getColor(R.color.White));
        chart.setUsePercentValues(true);
        chart.setDragDecelerationFrictionCoef(0.95f);
        //mTypeface = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        chart.setDrawHoleEnabled(true);
        //mPieChart.setHoleColor(Color.WHITE);
        chart.setCenterTextColor(Color.BLACK);
        chart.setTransparentCircleColor(Color.WHITE);
        chart.setHoleRadius(55f);
        chart.setTransparentCircleRadius(45f);
        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);

        chart.setCenterTextSize(9.5f);

        chart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);*/

        Legend l = chart.getLegend();
        l.setPosition(Legend.LegendPosition.PIECHART_CENTER);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(-20f);
        l.setXOffset(5f);
        return chart;
    }

    private void setupBudget() {
        if (month != -1 && year != -1) {
            final float budget = mRealmHandler.getBudget(month, year);
            mBudgetView.setText(budget + "");
            mBudgetView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() != i1) {
                        mCancelButton.setVisibility(View.VISIBLE);
                        mConfirmButton.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });

            mCancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mBudgetView.setText(budget + "");
                    mCancelButton.setVisibility(View.INVISIBLE);
                    mConfirmButton.setVisibility(View.INVISIBLE);
                    InputMethodManager inputManager = (InputMethodManager)
                            mContext.getSystemService(INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            });

            mConfirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        float budget = Float.parseFloat(mBudgetView.getText().toString());
                        mRealmHandler.update(budget, month, year);
                        mCancelButton.setVisibility(View.INVISIBLE);
                        mConfirmButton.setVisibility(View.INVISIBLE);
                    } catch (NumberFormatException ex) {
                        Toast.makeText(getApplicationContext(), "Invalid number", Toast.LENGTH_SHORT).show();
                    } finally {
                        InputMethodManager inputManager = (InputMethodManager)
                                mContext.getSystemService(INPUT_METHOD_SERVICE);

                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
            });
        } else {
            float budget = mRealmHandler.getBudget();
            mBudgetView.setText(budget + "");
            mBudgetView.setFocusable(false);
            mBudgetView.setEnabled(false);
        }
        mCurrencyLabel.setText(mSharedPreferences.getString("currencySymbol", Currency.getInstance(mContext.getResources().getConfiguration().locale).getSymbol()));
    }

    private void setData(int type) {

        List<Entry> yVals = new ArrayList<Entry>();
        List<RealmCategoryItem> categories = mRealmHandler.getCategoryParents();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.

        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Integer> colors = new ArrayList<Integer>();

        if (!mRealmHandler.isEmpty(type)) {
            int index = 0;
            int total = 0;
            for (int i = 0; i < categories.size(); i++) {
                float amount = mRealmHandler.getSpecificDateAmountByType(categories.get(i).getCategory(), month, year, type);
                if (amount != 0) {
                    total += amount;
                    yVals.add(new Entry(amount, index++));
                    xVals.add(categories.get(i).getCategory());
                    colors.add(categories.get(i).getColor());
                }
            }

            if (total > 0) {
                PieDataSet dataSet = new PieDataSet(yVals, "Category Legend");
                dataSet.setSliceSpace(2f);
                dataSet.setSelectionShift(5f);
                dataSet.setColors(colors);

                PieData data = new PieData(xVals, dataSet);
                data.setValueFormatter(new PercentFormatter());
                data.setValueTextSize(11f);
                data.setValueTextColor(Color.WHITE);
                mPieChart.setData(data);

                // undo all highlights
                mPieChart.highlightValues(null);

                mPieChart.invalidate();
            }
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
