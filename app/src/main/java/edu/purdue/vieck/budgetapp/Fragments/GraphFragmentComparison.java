package edu.purdue.vieck.budgetapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import edu.purdue.vieck.budgetapp.CustomObjects.RealmDataItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by mvieck on 10/7/2015.
 */
public class GraphFragmentComparison extends Fragment {

    BarChart mChart;

    DecimalFormat decimalFormat;
    RealmHandler mRealmHandler;
    String[] categories;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph_comparison, container, false);
        setRetainInstance(true);
        decimalFormat = new DecimalFormat("0.00");
        categories = getResources().getStringArray(R.array.categoryarray);
        mRealmHandler = new RealmHandler(getActivity());

        mChart = (BarChart) view.findViewById(R.id.chart_one);

        if (mRealmHandler != null && !mRealmHandler.isEmpty(2)) {
            generateChart(mChart);
        } else {
        }
        return view;
    }

    /* Creates a chart to compare */
    private void generateChart(BarChart chartView) {

        Stack<RealmDataItem> stack = mRealmHandler.getAllDataAsStack(0);
        HashMap<String, Float> uniqueMonths = mRealmHandler.getAllMonthsAsOneElement(0);
        List<BarEntry> entryList = new ArrayList<>();
        Set<String> keys = uniqueMonths.keySet();
        String[] labels = keys.toArray(new String[keys.size()]);

        int i = 0;
        BarEntry entry;
        for (String label : labels) {

            entry = new BarEntry(uniqueMonths.get(label), i++);
            entryList.add(entry);
        }

        BarDataSet dataSet = new BarDataSet(entryList, "Values");
        dataSet.setValueTextColor(getResources().getColor(R.color.md_white_1000));
        dataSet.setValueTextSize(8f);
        dataSet.setColor(getResources().getColor(R.color.md_white_1000));
        BarData data = new BarData(labels, dataSet);
        chartView.setData(data);
        chartView.setDescription("");

        chartView.setGridBackgroundColor(getResources().getColor(R.color.md_black_1000));
        chartView.setPinchZoom(true);

    }

}
