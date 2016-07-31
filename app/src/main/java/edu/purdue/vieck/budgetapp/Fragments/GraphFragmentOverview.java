package edu.purdue.vieck.budgetapp.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.realm.implementation.RealmLineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import edu.purdue.vieck.budgetapp.CustomObjects.RealmDataItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.R;
import io.realm.RealmResults;

/**
 * Created by mvieck on 10/7/2015.
 */
public class GraphFragmentOverview extends Fragment {

    LineChart lineChart;

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

        lineChart = (LineChart) view.findViewById(R.id.chart_one);
        generateChart();
        return view;
    }

    /* Creates a line chart for total expense vs income */
    private void generateChart() {
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();

        List<Entry> incomeEntries = new ArrayList<>();
        List<Entry> expenseEntries = new ArrayList<>();
        List<String> xAxisLabels = new ArrayList<>();

        HashMap<String, Float> expenseData = mRealmHandler.getAllDataPerMonth(0);
        HashMap<String, Float> incomeData = mRealmHandler.getAllDataPerMonth(1);

        Set<String> expenseKeys = expenseData.keySet();
        int i = 0;
        for (String key : expenseKeys) {
            expenseEntries.add(new Entry(i++,expenseData.get(key)));
            xAxisLabels.add(key);
        }

        i = 0;
        Set<String> incomeKeys = incomeData.keySet();
        for (String key : incomeKeys) {
            incomeEntries.add(new Entry(i++, incomeData.get(key)));
        }

        LineDataSet income = new LineDataSet(incomeEntries, "income");
        income.setColor(getResources().getColor(R.color.md_green_A400));
        income.setValueTextSize(10f);
        income.setValueTextColor(Color.WHITE);

        LineDataSet expense = new LineDataSet(expenseEntries, "expense");
        expense.setColor(getResources().getColor(R.color.md_red_A400));
        expense.setValueTextSize(10f);
        expense.setValueTextColor(Color.WHITE);
//        new String[] { "income","expense"},dataSets

        LineData data = new LineData();
        data.addDataSet(income);
        data.addDataSet(expense);
        lineChart.setData(data);
        lineChart.animateY(1400, Easing.EasingOption.EaseInOutQuart);
        lineChart.setGridBackgroundColor(getResources().getColor(R.color.md_black_1000));
        lineChart.setBackgroundColor(getResources().getColor(R.color.md_black_1000));
        lineChart.setDescriptionColor(Color.WHITE);
        lineChart.getLegend().setTextColor(Color.WHITE);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setDrawGridLines(false);
       // leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setGridColor(Color.WHITE);
        leftAxis.setTextColor(Color.WHITE);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        lineChart.getXAxis().setTextColor(Color.WHITE);
        lineChart.getXAxis().setDrawGridLines(false);

        lineChart.getAxisRight().setEnabled(false);
    }

}
