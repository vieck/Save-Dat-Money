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
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import edu.purdue.vieck.budgetapp.CustomObjects.BudgetItem;
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

        Stack<BudgetItem> stack = mRealmHandler.getAllDataAsStack(0);
        List<BarEntry> entryList = new ArrayList<>();

        final String[] mLabelsThree = new String[stack.size()];

        int i = 0;
        BarEntry entry;
        for (BudgetItem item : stack) {
            entry = new BarEntry(item.getAmount(), i);
            entryList.add(entry);
            mLabelsThree[i] = item.getMonth() + "/" + item.getYear();
            i++;
        }

        BarDataSet dataSet = new BarDataSet(entryList, "Values");
        dataSet.setColor(getResources().getColor(R.color.md_white_1000));
        BarData data = new BarData(mLabelsThree, dataSet);
        chartView.setData(data);

        chartView.setGridBackgroundColor(getResources().getColor(R.color.md_green_A400));
        chartView.setPinchZoom(true);

    }

}
