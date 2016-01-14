package edu.purdue.vieck.budgetapp.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by mvieck on 10/7/2015.
 */
public class GraphFragmentOverview extends Fragment {

    LineChart mChart;

    DecimalFormat decimalFormat;
    RealmHandler mRealmHandler;
    String[] categories;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph_overview, container, false);
        setRetainInstance(true);
        decimalFormat = new DecimalFormat("0.00");
        categories = getResources().getStringArray(R.array.categoryarray);
        mRealmHandler = new RealmHandler(getActivity());

        mChart = (LineChart) view.findViewById(R.id.chart_one);
        if (mRealmHandler != null && !mRealmHandler.isEmpty(2)) {
            generateChart(mChart);
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
    private void generateChart(LineChart lineChartView) {

        List<Entry> incomeEntries = new ArrayList<>();
        List<Entry> expenseEntries = new ArrayList<>();
        List<String> xAxisLabels = new ArrayList<>();


        float[] expenseArray = mRealmHandler.getAllDataAsArray(0);
        float[] incomeArray = mRealmHandler.getAllDataAsArray(1);

        for (int i = 0; i < expenseArray.length; i++) {
            expenseEntries.add(new Entry(expenseArray[i], i));
            xAxisLabels.add(i + "");
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
        leftAxis.setGridColor(Color.WHITE);
        leftAxis.setTextColor(Color.WHITE);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        lineChartView.getXAxis().setTextColor(Color.WHITE);

        lineChartView.getAxisRight().setEnabled(false);


        LineDataSet lineSet = new LineDataSet(incomeEntries, "income");
        lineSet.setColor(getResources().getColor(R.color.md_green_A400));
        lineSet.setValueTextColor(Color.WHITE);

        lineSet = new LineDataSet(expenseEntries, "expense");
        lineSet.setColor(getResources().getColor(R.color.md_red_A400));
        lineSet.setValueTextColor(Color.WHITE);

        LineData data = new LineData(xAxisLabels, lineSet);


        lineChartView.setGridBackgroundColor(getResources().getColor(R.color.md_black_1000));
        lineChartView.setBackgroundColor(getResources().getColor(R.color.md_black_1000));
        lineChartView.setDescriptionColor(Color.WHITE);
        lineChartView.getLegend().setTextColor(Color.WHITE);
        lineChartView.setData(data);

    }

}
