package edu.purdue.vieck.budgetapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.purdue.vieck.budgetapp.CustomObjects.BudgetItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by mvieck on 10/7/2015.
 */
public class GraphFragmentCategory extends Fragment {
    private RealmHandler mRealmHandler;
    private LinkedList<BudgetItem> months;
    ImageButton left, right;
    TextView monthTxt, yearTxt;
    private int count, type;

    String[] categories;

    /* First Chart */
    LineChart mChartOne;

    /* Second Chart */
    BarChart mChartTwo;

    /* Third Chart */
    HorizontalBarChart mChartThree;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph_monthly, container, false);

        categories = getResources().getStringArray(R.array.categoryarray);
        mRealmHandler = new RealmHandler(getActivity());

        mChartOne = (LineChart) view.findViewById(R.id.chart_one);
        mChartTwo = (BarChart) view.findViewById(R.id.chart_two);
        mChartThree = (HorizontalBarChart) view.findViewById(R.id.chart_three);

        monthTxt = (TextView) view.findViewById(R.id.label_month);
        yearTxt = (TextView) view.findViewById(R.id.label_year);
        left = (ImageButton) view.findViewById(R.id.left_arrow);
        right = (ImageButton) view.findViewById(R.id.right_arrow);

        Bundle bundle = getArguments();
        type = bundle.getInt("type", 2);

        if (!mRealmHandler.isEmpty(type)) {
            months = mRealmHandler.getAllUniqueMonthsAsLinkedList(type);
            count = months.size() - 1;

            left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Category Position", "" + count);
                    if (count < months.size() - 1) {
                        count++;
                        BudgetItem item = months.get(count);
                        monthTxt.setText(item.getMonthString());
                        yearTxt.setText("" + item.getYear());
                        updateGraphs(item.getMonth(), item.getYear());
                    } else {
                        count = 0;
                        BudgetItem item = months.get(count);
                        monthTxt.setText(item.getMonthString());
                        yearTxt.setText("" + item.getYear());
                        updateGraphs(item.getMonth(), item.getYear());

                    }
                }
            });

            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Category Count", "" + count);
                    if (count > 0) {
                        count--;
                        BudgetItem item = months.get(count);
                        monthTxt.setText(item.getMonthString());
                        yearTxt.setText("" + item.getYear());
                        updateGraphs(item.getMonth(), item.getYear());
                    } else {
                        count = months.size() - 1;
                        BudgetItem item = months.get(count);
                        monthTxt.setText(item.getMonthString());
                        yearTxt.setText("" + item.getYear());
                        updateGraphs(item.getMonth(), item.getYear());
                    }
                }
            });
            produceOne(mChartOne, months.get(count).getMonth(), months.get(count).getYear());
            produceThree(mChartThree, months.get(count).getMonth(), months.get(count).getYear());
        } else {
            monthTxt.setText("No Data");
            yearTxt.setText("");
        }

        return view;
    }

    public void updateType(int type) {
        this.type = type;
        if (mRealmHandler != null && !mRealmHandler.isEmpty(type)) {
            months = mRealmHandler.getAllUniqueMonthsAsLinkedList(type);
            count = months.size() - 1;
            monthTxt.setText(months.get(count).getMonthString());
            yearTxt.setText("" + months.get(count).getYear() + "");
            updateOne(mChartOne, months.get(count).getMonth(), months.get(count).getYear());
            updateThree(mChartThree, months.get(count).getMonth(), months.get(count).getYear());
        } else {
            monthTxt.setText("No Data");
            yearTxt.setText("");
        }
    }

    private void updateGraphs(int month, int year) {
        updateOne(mChartOne, month, year);
        updateThree(mChartThree, month, year);
    }

    private void produceOne(LineChart chart, int month, int year) {
        float highestCategory = 0;

        float[] income = new float[categories.length];
        for (int i = 0; i < categories.length; i++) {
            income[i] = mRealmHandler.getSpecificDateAmountByType(categories[i], month, year, 1);
            if (income[i] > highestCategory) {
                highestCategory = income[i];
            }
        }

        float[] expense = new float[categories.length];
        for (int i = 0; i < categories.length; i++) {
            expense[i] = mRealmHandler.getSpecificDateAmountByType(categories[i], month, year, 0);
            if (expense[i] > highestCategory) {
                highestCategory = expense[i];
            }
        }

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);
        xAxis.setSpaceBetweenLabels(2);

        Legend l = chart.getLegend();
        l.setPosition(LegendPosition.BELOW_CHART_LEFT);
        l.setForm(LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

    }

    private void produceTwo(BarChart chart) {

    }

    private void produceThree(HorizontalBarChart chart, int month, int year) {

        List<BarEntry> barEntries = new ArrayList<>();

        float[] income = {mRealmHandler.getSpecificDateAmount(month, year, 1)};
        String[] incomeLabel = {"Income"};
        BarEntry barEntry = new BarEntry(income, 0, incomeLabel[0]);
        barEntries.add(barEntry);

        float[] expense = {-mRealmHandler.getSpecificDateAmount(month, year, 0)};
        String[] expenseLabel = { "Expense" };
        barEntry = new BarEntry(income, 0, expenseLabel[0]);
        barEntries.add(barEntry);

        BarDataSet barDataSet = new BarDataSet(barEntries, "Budget");
        String[] labels = {"Income","Expense"};
        BarData barData = new BarData(labels,barDataSet);
        barData.setValueTextSize(10f);

        chart.setData(barData);
    }

    private void updateOne(LineChart chartView, int month, int year) {

    }

    private void updateTwo(BarChart chartView, int month, int year) {

    }

    private void updateThree(HorizontalBarChart chartView, int month, int year) {
    }

}
