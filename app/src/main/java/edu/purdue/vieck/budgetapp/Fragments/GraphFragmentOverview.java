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
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.R;

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

        float[] expenseArray = mRealmHandler.getAllDataAsArray(0);
        float[] incomeArray = mRealmHandler.getAllDataAsArray(1);

        for (int i = 0; i < expenseArray.length; i++) {
            expenseEntries.add(new Entry(expenseArray[i], i));
            xAxisLabels.add(i + "");
        }

        for (int i = 0; i < incomeArray.length; i++) {
            incomeEntries.add(new Entry(incomeArray[i], i));
        }

        LineDataSet incomeData = new LineDataSet(incomeEntries, "income");
        incomeData.setColor(getResources().getColor(R.color.md_green_A400));
        incomeData.setValueTextSize(10f);
        incomeData.setValueTextColor(Color.WHITE);

        LineDataSet expenseData = new LineDataSet(expenseEntries, "expense");
        expenseData.setColor(getResources().getColor(R.color.md_red_A400));
        expenseData.setValueTextSize(10f);
        expenseData.setValueTextColor(Color.WHITE);
//        new String[] { "income","expense"},dataSets

        LineData data = new LineData();
        data.addDataSet(incomeData);
        data.addDataSet(expenseData);
        lineChart.setData(data);
        lineChart.animateY(1400, Easing.EasingOption.EaseInOutQuart);
        lineChart.setGridBackgroundColor(getResources().getColor(R.color.md_black_1000));
        lineChart.setBackgroundColor(getResources().getColor(R.color.md_black_1000));
        lineChart.setDescriptionColor(Color.WHITE);
        lineChart.getLegend().setTextColor(Color.WHITE);


        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setGridColor(Color.WHITE);
        leftAxis.setTextColor(Color.WHITE);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        lineChart.getXAxis().setTextColor(Color.WHITE);

        lineChart.getAxisRight().setEnabled(false);
    }

}
