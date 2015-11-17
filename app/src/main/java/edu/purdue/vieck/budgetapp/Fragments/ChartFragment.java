package edu.purdue.vieck.budgetapp.Fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.PercentFormatter;

import java.util.ArrayList;

import edu.purdue.vieck.budgetapp.Adapters.ChartAdapter;
import edu.purdue.vieck.budgetapp.DatabaseHandler;
import edu.purdue.vieck.budgetapp.R;


public class ChartFragment extends Fragment implements OnChartValueSelectedListener {

    int month, year;
    DatabaseHandler mDatabaseHandler;
    private int mInstance;
    private int yInstance;
    private PieChart mPieChart;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ChartAdapter mChartAdapter;
    private Button chartButton;
    private Context mContext;
    private String fragmentName;

    @Override
    public void onAttach(final Activity activity) {
        mContext = activity.getApplicationContext();
        super.onAttach(activity);
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

        mDatabaseHandler = new DatabaseHandler(mContext);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.budget_recycler_view);
        mChartAdapter = new ChartAdapter(mContext, month, year);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mChartAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mChartAdapter = new ChartAdapter(mContext, month, year);
                mRecyclerView.setAdapter(mChartAdapter);
                setData(3, 100);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });


        mPieChart = (PieChart) view.findViewById(R.id.pie_chart);
        mPieChart.setDescription("");
        mPieChart.setDescriptionColor(getResources().getColor(R.color.White));
        mPieChart.setUsePercentValues(true);
        mPieChart.setDragDecelerationFrictionCoef(0.95f);
        //mTypeface = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        mPieChart.setDrawHoleEnabled(true);
        //mPieChart.setHoleColor(Color.WHITE);
        mPieChart.setCenterTextColor(Color.BLACK);
        mPieChart.setTransparentCircleColor(Color.WHITE);
        mPieChart.setHoleRadius(45f);
        mPieChart.setTransparentCircleRadius(45f);
        mPieChart.setDrawCenterText(true);

        mPieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mPieChart.setRotationEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mPieChart.setOnChartValueSelectedListener(this);

        //mPieChart.setCenterText("MPAndroidChart\nby Philipp Jahoda");
        mPieChart.setCenterTextSize(9.5f);

        setData(3, 100);

        mPieChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = mPieChart.getLegend();
        l.setPosition(LegendPosition.PIECHART_CENTER);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(7f);
        l.setYOffset(0f);
        l.setXOffset(5f);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mInstance = savedInstanceState.getInt("Month", -1);
            yInstance = savedInstanceState.getInt("Year", -1);
        } else {
            mInstance = -1;
            yInstance = -1;
        }
    }

    private void setData(int count, float range) {

        float mult = range;

        ArrayList<Entry> yVals = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.

        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Integer> colors = new ArrayList<Integer>();

        if (!mDatabaseHandler.isEmpty()) {

            if (mDatabaseHandler.getSpecificDateAmountByType("Income", month, year) != 0) {
                yVals.add(new Entry(mDatabaseHandler.getSpecificDateAmountByType("Income", month, year), 4));
                xVals.add("Income");
                colors.add(getResources().getColor(R.color.DarkNavy));
            }

            if (mDatabaseHandler.getSpecificDateAmountByType("Utilities", month, year) != 0) {
                yVals.add(new Entry(mDatabaseHandler.getSpecificDateAmountByType("Utilities", month, year), 1));
                xVals.add("Utilities");
                colors.add(getResources().getColor(R.color.PaleBlue));
            }

            if (mDatabaseHandler.getSpecificDateAmountByType("Entertainment", month, year) != 0) {
                yVals.add(new Entry(mDatabaseHandler.getSpecificDateAmountByType("Entertainment", month, year), 2));
                xVals.add("Entertainment");
                colors.add(getResources().getColor(R.color.CottonBlue));
            }

            if (mDatabaseHandler.getSpecificDateAmountByType("Medical", month, year) != 0) {
                yVals.add(new Entry(mDatabaseHandler.getSpecificDateAmountByType("Medical", month, year), 3));
                xVals.add("Medical");
                colors.add(getResources().getColor(R.color.PaleTurquoise));
            }

            if (mDatabaseHandler.getSpecificDateAmountByType("Food", month, year) != 0) {
                yVals.add(new Entry(mDatabaseHandler.getSpecificDateAmountByType("Food", month, year), 0));
                xVals.add("Food");
                colors.add(getResources().getColor(R.color.NeonBlue));
            }

            if (mDatabaseHandler.getSpecificDateAmountByType("Insurance", month, year) != 0) {
                yVals.add(new Entry(mDatabaseHandler.getSpecificDateAmountByType("Insurance", month, year), 5));
                xVals.add("Insurance");
                colors.add(getResources().getColor(R.color.SheetBlue));
            }


            PieDataSet dataSet = new PieDataSet(yVals, "Category Legend");
            dataSet.setSliceSpace(0f);
            dataSet.setSelectionShift(5f);

            // add a lot of colors

        /*for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        */

            dataSet.setColors(colors);

            PieData data = new PieData(xVals, dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.BLACK);
            mPieChart.setData(data);

            // undo all highlights
            mPieChart.highlightValues(null);

            mPieChart.invalidate();
        }
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }
}
