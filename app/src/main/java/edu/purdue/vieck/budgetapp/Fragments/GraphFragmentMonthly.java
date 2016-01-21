package edu.purdue.vieck.budgetapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.ChartData;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.purdue.vieck.budgetapp.ChartListViewItems.BarChartItem;
import edu.purdue.vieck.budgetapp.ChartListViewItems.ChartItem;
import edu.purdue.vieck.budgetapp.ChartListViewItems.HorizontalBarChartItem;
import edu.purdue.vieck.budgetapp.CustomObjects.DataItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by mvieck on 10/7/2015.
 */
public class GraphFragmentMonthly extends Fragment {
    private RealmHandler mRealmHandler;
    private LinkedList<DataItem> months;
    ImageButton left, right;
    TextView monthTxt, yearTxt;
    private int count, type;

    String[] categories;

    ListView mListView;
    ChartDataAdapter mAdapter;

    List<ChartItem> mCharts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph_monthly, container, false);

        categories = getResources().getStringArray(R.array.categoryarray);
        mRealmHandler = new RealmHandler(getActivity());

        mListView = (ListView) view.findViewById(R.id.listview);

        mCharts = new ArrayList<>();

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
                        DataItem item = months.get(count);
                        monthTxt.setText(item.getMonthString());
                        yearTxt.setText("" + item.getYear());
                        updateGraphs(item.getMonth(), item.getYear());
                    } else {
                        count = 0;
                        DataItem item = months.get(count);
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
                        DataItem item = months.get(count);
                        monthTxt.setText(item.getMonthString());
                        yearTxt.setText("" + item.getYear());
                        updateGraphs(item.getMonth(), item.getYear());
                    } else {
                        count = months.size() - 1;
                        DataItem item = months.get(count);
                        monthTxt.setText(item.getMonthString());
                        yearTxt.setText("" + item.getYear());
                        updateGraphs(item.getMonth(), item.getYear());
                    }
                }
            });
            produceOne(months.get(count).getMonth(), months.get(count).getYear());
            produceTwo(months.get(count).getMonth(), months.get(count).getYear());
            mAdapter = new ChartDataAdapter(getActivity(), mCharts);
            mListView.setAdapter(mAdapter);
        } else {
            monthTxt.setText("No Data");
            yearTxt.setText("");
        }
        return view;
    }

    private class ChartDataAdapter extends ArrayAdapter<ChartItem> {
        List<ChartItem> data;
        public ChartDataAdapter(Context context, List<ChartItem> data) {
            super(context, 0, data);
            this.data = data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getItem(position).getView(position, convertView, getContext());
        }

        @Override
        public int getItemViewType(int position) {
            return getItem(position).getItemType();
        }

        @Override
        public ChartItem getItem(int position) {
            return super.getItem(position);
        }

        @Override
        public int getViewTypeCount() {
            return 4;
        }

        public void updateData(int position, ChartData data) {
            switch (position) {
                case 0:
                    BarChart barChart = ((BarChart)getView(position, null, mListView));
                    barChart.getBarData();
                    barChart.getBarData().clearValues();
                    barChart.setData((BarData)data);
                    barChart.notifyDataSetChanged();
                    barChart.invalidate();
                    break;
                case 1:
                    HorizontalBarChart chart = (HorizontalBarChart) getView(position, null, mListView);
                    chart
                    chart.setData((BarData)data);
                    chart.notifyDataSetChanged();
                    chart.invalidate();
                    break;

            }
            notifyDataSetChanged();
        }


    }

    public void updateType(int type) {
        this.type = type;
        if (mRealmHandler != null && !mRealmHandler.isEmpty(type)) {
            months = mRealmHandler.getAllUniqueMonthsAsLinkedList(type);
            count = months.size() - 1;
            monthTxt.setText(months.get(count).getMonthString());
            yearTxt.setText("" + months.get(count).getYear() + "");
            updateOne(months.get(count).getMonth(), months.get(count).getYear());
            updateTwo(months.get(count).getMonth(), months.get(count).getYear());
        } else {
            monthTxt.setText("No Data");
            yearTxt.setText("");
        }
    }

    private void updateGraphs(int month, int year) {
        updateOne(month, year);
        updateTwo(month, year);
    }

    /* Creates a line chart for expense vs income */
    private void produceOne(int month, int year) {

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

        mCharts.add(new BarChartItem(barData, getActivity()));

    }

    /* Creates a graph for expenses vs income */
    private void produceTwo(int month, int year) {

        float total = mRealmHandler.getSpecificDateAmount(month, year, 2);

        float income = mRealmHandler.getSpecificDateAmount(month, year, 1);
        String[] incomeLabel = {"Income"};

        float expense = -mRealmHandler.getSpecificDateAmount(month, year, 0);
        String[] expenseLabel = {"Expense"};

        String[] labels = {"Income", "Expense"};

        ArrayList<BarEntry> yValues = new ArrayList<>();
        yValues.add(new BarEntry(new float[]{-expense, income}, 0));

        BarDataSet barDataSet = new BarDataSet(yValues, "Data Comparison");
        barDataSet.setValueTextSize(7f);
        barDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        barDataSet.setBarSpacePercent(40f);
        barDataSet.setColors(new int[]{getResources().getColor(R.color.md_red_A400), getResources().getColor(R.color.md_green_A400)});
        barDataSet.setStackLabels(new String[]{
                "Expenses", "Income"
        });
        BarData data = new BarData(new String[]{""}, barDataSet);
        mCharts.add(new HorizontalBarChartItem(data, total, getActivity()));
    }

    private void updateOne(int month, int year) {
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

        mAdapter.updateData(0, barData);

    }

    private void updateTwo(int month, int year) {
        float total = mRealmHandler.getSpecificDateAmount(month, year, 2);
        chart.getAxisRight().setAxisMaxValue(total);
        chart.getAxisRight().setAxisMinValue(-total);
        List<BarEntry> barEntries = new ArrayList<>();

        float income = mRealmHandler.getSpecificDateAmount(month, year, 1);
        String[] incomeLabel = {"Income"};

        float expense = -mRealmHandler.getSpecificDateAmount(month, year, 0);
        String[] expenseLabel = {"Expense"};

        String[] labels = {"Income", "Expense"};

        chart.getBarData().clearValues();

        ArrayList<BarEntry> yValues = new ArrayList<>();
        yValues.add(new BarEntry(new float[]{expense, income}, 0));

        BarDataSet barDataSet = new BarDataSet(yValues, "Data Comparison");
        barDataSet.setValueTextSize(7f);
        barDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        barDataSet.setBarSpacePercent(40f);
        barDataSet.setColors(new int[]{getResources().getColor(R.color.md_red_A400), getResources().getColor(R.color.md_green_A400)});
        BarData data = new BarData(new String[]{""}, barDataSet);

        mAdapter.updateData(1, data);
    }

}
