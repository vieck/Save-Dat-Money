package edu.purdue.vieck.budgetapp.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import edu.purdue.vieck.budgetapp.Activities.ChartActivity;
import edu.purdue.vieck.budgetapp.Adapters.ChartRecyclerAdapter;
import edu.purdue.vieck.budgetapp.CustomObjects.RealmCategoryItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.R;


public class ChartFragment extends Fragment {

    int month, year, type;
    RealmHandler mRealmHandler;
    private PieChart mPieChart;
    private EditText mBudgetView;
    private TextView mCurrencyLabel;
    private FloatingActionButton mConfirmButton, mCancelButton;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ChartRecyclerAdapter mChartRecyclerAdapter;
    private Context mContext;
    private SharedPreferences mSharedPreferences;

    @Override
    public void onAttach(final Activity activity) {
        mContext = activity.getApplicationContext();
        super.onAttach(activity);
        setRetainInstance(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chart, container, false);


        Bundle bundle = getArguments();
        month = bundle.getInt("month", -1);
        year = bundle.getInt("year", -1);
        type = bundle.getInt("type", 2);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        mRealmHandler = new RealmHandler(getActivity());

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.budget_recycler_view);
        mChartRecyclerAdapter = new ChartRecyclerAdapter(mContext, month, year, type);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mChartRecyclerAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mChartRecyclerAdapter = new ChartRecyclerAdapter(mContext, month, year, type);
                mRecyclerView.setAdapter(mChartRecyclerAdapter);
                setData(type);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });


        mPieChart = (PieChart) view.findViewById(R.id.pie_chart);
        mPieChart = setupPieChart(mPieChart);
        mBudgetView = (EditText) view.findViewById(R.id.edittext_budget);
        mCurrencyLabel = (TextView) view.findViewById(R.id.currency_textview);
        mCancelButton = (FloatingActionButton) view.findViewById(R.id.budget_button_cancel);
        mConfirmButton = (FloatingActionButton) view.findViewById(R.id.budget_button_ok);
        setupBudget();
        setData(type);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        l.setPosition(LegendPosition.PIECHART_CENTER);
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
                    mBudgetView.setText(budget+"");
                    mCancelButton.setVisibility(View.INVISIBLE);
                    mConfirmButton.setVisibility(View.INVISIBLE);
                    InputMethodManager inputManager = (InputMethodManager)
                            mContext.getSystemService(getActivity().INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
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
                        Toast.makeText(getActivity(),"Invalid number",Toast.LENGTH_SHORT);
                    } finally {
                        InputMethodManager inputManager = (InputMethodManager)
                                mContext.getSystemService(getActivity().INPUT_METHOD_SERVICE);

                        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
            });
        } else {
            float budget = mRealmHandler.getBudget();
            mBudgetView.setText(budget+"");
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

    public void updateAdapter(int position) {
        this.type = position;
        if (mChartRecyclerAdapter != null) {
            mChartRecyclerAdapter.updatePosition(position);
            mPieChart.clear();
            setData(position);
            mPieChart.notifyDataSetChanged();
        }
    }

}
