package edu.purdue.vieck.budgetapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.sql.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import edu.purdue.vieck.budgetapp.CustomObjects.BudgetItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by mvieck on 10/7/2015.
 */
public class GraphFragmentOverview extends Fragment {

    LineChart mChartOne;
    BarChart mChartTwo;

    DecimalFormat decimalFormat;
    RealmHandler mRealmHandler;
    int type, count;
    String[] categories;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph_overview, container, false);
        setRetainInstance(true);
        Bundle bundle = getArguments();
        type = bundle.getInt("type", 2);
        decimalFormat = new DecimalFormat("0.00");
        categories = getResources().getStringArray(R.array.categoryarray);
        mRealmHandler = new RealmHandler(getActivity());

        mChartOne = (LineChart) view.findViewById(R.id.chart_one);
        mChartTwo = (BarChart) view.findViewById(R.id.chart_two);

        if (mRealmHandler != null && !mRealmHandler.isEmpty(type)) {
            produceOne(mChartOne);
            produceTwo(mChartTwo);
        } else {
        }
        return view;
    }

    private void changeAdapterMonth(int month, int year) {
        //updateOne(month, year);
        //updateStackChart(month, year);
    }


    /*  */
    private void updateOne(LineChart lineChartView) {
        /*float[] expenseArray = mRealmHandler.getAllDataAsArray(0);
        float[] incomeArray = mRealmHandler.getAllDataAsArray(1);
        float[] income = new float[categories.length];

        if (expenseArray.length > incomeArray.length) {
            lineChartView.setAxisBorderValues(0, expenseArray.length);
        } else {
            lineChartView.setAxisBorderValues(0, incomeArray.length);
        }
        lineChartView.updateValues(0, expenseArray);
        lineChartView.updateValues(1, incomeArray);
        lineChartView.notifyDataUpdate();*/
    }

    /* Creates a line chart for total expense vs income */
    private void produceOne(LineChart lineChartView) {

        List<Entry> incomeEntries = new ArrayList<>();
        List<Entry> expenseEntries = new ArrayList<>();

        final String[] mLabelsTwo = {"", "", "", "", "START", "", "", "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", "", "", "FINISH", "", "", "", ""};
        final float[][] mValuesTwo = {{35f, 37f, 47f, 49f, 43f, 46f, 80f, 83f, 65f, 68f, 100f, 68f, 70f, 73f, 83f, 85f, 70f, 73f, 73f, 77f,
                33f, 15f, 18f, 25f, 28f, 25f, 28f, 40f, 43f, 25f, 28f, 55f, 58f, 50f, 53f, 53f, 57f, 48f, 50f, 53f, 54f,
                25f, 27f, 35f, 37f, 35f, 80f, 82f, 55f, 59f, 85f, 82f, 60f, 55f, 63f, 65f, 58f, 60f, 63f, 60f},
                {85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f,
                        85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f,
                        85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f}};

        float[] expenseArray = mRealmHandler.getAllDataAsArray(0);
        float[] incomeArray = mRealmHandler.getAllDataAsArray(1);

        for (int i = 0; i < expenseArray.length; i++) {
            expenseEntries.add(new Entry(expenseArray[i], i));
        }

        for (int i = 0; i < incomeArray.length; i++) {
            incomeEntries.add(new Entry(incomeArray[i], i));
        }

        YAxis leftAxis = lineChartView.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        //leftAxis.setAxisMaxValue(220f);
        //leftAxis.setAxisMinValue(-50f);
        leftAxis.setStartAtZero(false);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        lineChartView.getAxisRight().setEnabled(false);


        String[] labels = new String[expenseArray.length];
        Arrays.fill(labels,"");

        LineData data = new LineData();

        LineDataSet lineSet = new LineDataSet(incomeEntries, "income");
        lineSet.setColor(getResources().getColor(R.color.md_green_A400));
        data.addDataSet(lineSet);

        lineSet = new LineDataSet(expenseEntries, "expense");
        lineSet.setColor(getResources().getColor(R.color.md_red_A400));
        data.addDataSet(lineSet);

        lineChartView.setData(data);

    }

    /* Creates a chart to compare */
    private void produceTwo(BarChart chartView) {

        Stack<BudgetItem> stack = mRealmHandler.getAllDataAsStack(type);
        List<BarEntry> entryList = new ArrayList<>();

        final String[] mLabelsThree = new String[stack.size()];
        Arrays.fill(mLabelsThree, "");
        final float[] mValuesThree = {2.5f, 3.7f, 4f, 8f, 4.5f, 4f, 5f, 7f, 10f, 14f,
                12f, 6f, 7f, 8f, 9f, 8f, 9f, 8f, 7f, 6f,
                5f, 4f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 11f,
                12f, 14, 13f, 10f, 9f, 8f, 7f, 5f, 4f, 6f};

        int i = 0;
        BarEntry entry;
        for (BudgetItem item : stack) {
            entry = new BarEntry(item.getAmount(), i++);
            entryList.add(entry);
        }

        BarDataSet dataSet = new BarDataSet(entryList, "Values");
        dataSet.setColor(getResources().getColor(R.color.md_light_blue_300));
        BarData data = new BarData(mLabelsThree, dataSet);
        chartView.setData(data);

        chartView.setPinchZoom(true);

    }

    private void updateBudgetChart() {
    }

}
