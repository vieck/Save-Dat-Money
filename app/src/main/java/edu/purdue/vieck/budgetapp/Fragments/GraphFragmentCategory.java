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
import com.github.mikephil.charting.components.YAxis;
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
    BarChart mChartOne;

    /* Second Chart */
    HorizontalBarChart mChartTwo;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph_monthly, container, false);

        categories = getResources().getStringArray(R.array.categoryarray);
        mRealmHandler = new RealmHandler(getActivity());

        mChartOne = (BarChart) view.findViewById(R.id.chart_one);
        mChartTwo = (HorizontalBarChart) view.findViewById(R.id.chart_three);

        monthTxt = (TextView) view.findViewById(R.id.label_month);
        yearTxt = (TextView) view.findViewById(R.id.label_year);
        left = (ImageButton) view.findViewById(R.id.left_arrow);
        right = (ImageButton) view.findViewById(R.id.right_arrow);

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
            produceTwo(mChartTwo, months.get(count).getMonth(), months.get(count).getYear());
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
            updateTwo(mChartTwo, months.get(count).getMonth(), months.get(count).getYear());
        } else {
            monthTxt.setText("No Data");
            yearTxt.setText("");
        }
    }

    private void updateGraphs(int month, int year) {
        updateOne(mChartOne, month, year);
        updateTwo(mChartTwo, month, year);
    }

    /* Creates a line chart for expense vs income */
    private void produceOne(BarChart chart, int month, int year) {
        ArrayList<BarEntry> incomeValues = new ArrayList<>();
        ArrayList<BarEntry> expenseValues = new ArrayList<>();

        for (int i = 0; i < categories.length; i++) {
            float value = mRealmHandler.getSpecificDateAmountByType(categories[i], month, year, 1);
            incomeValues.add(new BarEntry(value, i));
        }

        for (int i = 0; i < categories.length; i++) {
            float value = mRealmHandler.getSpecificDateAmountByType(categories[i], month, year, 0);
            expenseValues.add(new BarEntry(value, i));
        }

        List<BarDataSet> barDataSets = new ArrayList<>();
        BarDataSet dataSetIncome = new BarDataSet(incomeValues, "Income");
        dataSetIncome.setDrawValues(false);
        dataSetIncome.setColor(getResources().getColor(R.color.md_green_A400));
        BarDataSet dataSetExpense = new BarDataSet(expenseValues, "Expense");
        dataSetExpense.setDrawValues(false);
        dataSetExpense.setColor(getResources().getColor(R.color.md_red_A400));
        barDataSets.add(dataSetIncome);
        barDataSets.add(dataSetExpense);

        BarData barData = new BarData(categories, barDataSets);
        barData.setGroupSpace(30f);
        chart.setData(barData);
        chart.setDescription("");

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);
        xAxis.setSpaceBetweenLabels(1);

        Legend l = chart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

    }

    /* Creates a graph for expenses vs income */
    private void produceTwo(HorizontalBarChart chart, int month, int year) {

        float total = mRealmHandler.getSpecificDateAmount(month, year, 2);
        List<BarEntry> barEntries = new ArrayList<>();

        float income = mRealmHandler.getSpecificDateAmount(month, year, 1);
        String[] incomeLabel = {"Income"};

        float expense = -mRealmHandler.getSpecificDateAmount(month, year, 0);
        String[] expenseLabel = { "Expense" };

        String[] labels = {"Income","Expense"};

        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setStartAtZero(false);
        chart.getAxisRight().setAxisMaxValue(total);
        chart.getAxisRight().setAxisMinValue(-total);
        chart.getAxisRight().setTextSize(9f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextSize(9f);

        Legend l = chart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);

        ArrayList<BarEntry> yValues = new ArrayList<>();
        yValues.add(new BarEntry(new float[]{-expense, income}, 0));

        BarDataSet barDataSet = new BarDataSet(yValues, "Data Comparison");
        barDataSet.setValueTextSize(7f);
        barDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        barDataSet.setBarSpacePercent(40f);
        barDataSet.setColors(new int[] {getResources().getColor(R.color.md_red_A400), getResources().getColor(R.color.md_green_A400)});
        barDataSet.setStackLabels(new String[]{
                "Expenses", "Income"
        });
        BarData data = new BarData(new String[]{""},barDataSet);
        chart.setData(data);
    }

    private void updateOne(BarChart chartView, int month, int year) {
        ArrayList<BarEntry> incomeValues = new ArrayList<>();
        ArrayList<BarEntry> expenseValues = new ArrayList<>();

        for (int i = 0; i < categories.length; i++) {
            float value = mRealmHandler.getSpecificDateAmountByType(categories[i], month, year, 1);
            incomeValues.add(new BarEntry(value, i));
        }

        for (int i = 0; i < categories.length; i++) {
            float value = mRealmHandler.getSpecificDateAmountByType(categories[i], month, year, 0);
            expenseValues.add(new BarEntry(value, i));
        }

        List<BarDataSet> barDataSets = new ArrayList<>();
        BarDataSet dataSetIncome = new BarDataSet(incomeValues, "Income");
        dataSetIncome.setDrawValues(false);
        dataSetIncome.setColor(getResources().getColor(R.color.md_green_A400));
        BarDataSet dataSetExpense = new BarDataSet(expenseValues, "Expense");
        dataSetExpense.setDrawValues(false);
        dataSetExpense.setColor(getResources().getColor(R.color.md_red_A400));
        barDataSets.add(dataSetIncome);
        barDataSets.add(dataSetExpense);

        BarData barData = new BarData(categories, barDataSets);

        chartView.getBarData().clearValues();
        chartView.setData(barData);
        chartView.notifyDataSetChanged();
        chartView.invalidate();
    }

    private void updateTwo(HorizontalBarChart chart, int month, int year) {
        float total = mRealmHandler.getSpecificDateAmount(month, year, 2);
        chart.getAxisRight().setAxisMaxValue(total);
        chart.getAxisRight().setAxisMinValue(-total);
        List<BarEntry> barEntries = new ArrayList<>();

        float income = mRealmHandler.getSpecificDateAmount(month, year, 1);
        String[] incomeLabel = {"Income"};

        float expense = -mRealmHandler.getSpecificDateAmount(month, year, 0);
        String[] expenseLabel = { "Expense" };

        String[] labels = {"Income","Expense"};

        chart.getBarData().clearValues();

        ArrayList<BarEntry> yValues = new ArrayList<>();
        yValues.add(new BarEntry(new float[]{expense, income}, 0));

        BarDataSet barDataSet = new BarDataSet(yValues, "Data Comparison");
        barDataSet.setValueTextSize(7f);
        barDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        barDataSet.setBarSpacePercent(40f);
        barDataSet.setColors(new int[] {getResources().getColor(R.color.md_red_A400), getResources().getColor(R.color.md_green_A400)});
        barDataSet.setStackLabels(new String[]{
                "Expenses", "Income"
        });
        BarData data = new BarData(new String[]{""},barDataSet);
        chart.setData(data);
        chart.notifyDataSetChanged();
        chart.invalidate();
    }

}
