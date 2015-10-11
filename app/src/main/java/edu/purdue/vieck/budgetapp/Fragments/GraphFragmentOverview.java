package edu.purdue.vieck.budgetapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;

import java.util.ArrayList;
import java.util.Stack;

import edu.purdue.vieck.budgetapp.CustomObjects.BudgetItem;
import edu.purdue.vieck.budgetapp.DatabaseHandler;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by mvieck on 10/7/2015.
 */
public class GraphFragmentOverview extends Fragment {
    DatabaseHandler databaseHandler;
    BarChart barChart;
    SeekBar seekBar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph_overview, container, false);
        databaseHandler = new DatabaseHandler(getActivity());
        barChart = (BarChart) view.findViewById(R.id.bar_chart);
        seekBar = (SeekBar) view.findViewById(R.id.x_seekbar);
        seekBar.setMax((int)((databaseHandler.getTotalAmount(false,"") + 99) / 100)*100);
        setupBarChart();
        return view;
    }

    public void setupBarChart() {
        Stack<BudgetItem> xDataset = databaseHandler.getAllData();
        ArrayList<BarEntry> yDataset = new ArrayList<>();
        int i = 0;
        while (!xDataset.isEmpty()) {
            ++i;
            yDataset.add(new BarEntry(xDataset.pop().getAmount(), i));
        }
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });
    }
}
