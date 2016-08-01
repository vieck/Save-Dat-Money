package edu.purdue.vieck.budgetapp.Fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.purdue.vieck.budgetapp.Adapters.GraphMonthlyAdapter;
import edu.purdue.vieck.budgetapp.ChartListViewItems.BarChartItem;
import edu.purdue.vieck.budgetapp.ChartListViewItems.ChartItem;
import edu.purdue.vieck.budgetapp.ChartListViewItems.HorizontalBarChartItem;
import edu.purdue.vieck.budgetapp.CustomObjects.RealmCategoryItem;
import edu.purdue.vieck.budgetapp.CustomObjects.RealmDataItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.R;
import edu.purdue.vieck.budgetapp.databinding.FragmentGraphCategoryBinding;
import io.realm.RealmResults;

/**
 * Created by mvieck on 10/7/2015.
 */
public class GraphFragmentCategory extends Fragment {

    FragmentGraphCategoryBinding binding;

    private RealmHandler mRealmHandler;
    private LinkedList<RealmDataItem> months;
    private int count, type;

    RealmResults<RealmCategoryItem> categories;

    GraphMonthlyAdapter mAdapter;

    List<ChartItem> mCharts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_graph_category, container, false);

        mRealmHandler = new RealmHandler(getActivity());

        categories = mRealmHandler.getCategoryParents();

        mCharts = new ArrayList<>();

        if (!mRealmHandler.isEmpty(type)) {
            months = mRealmHandler.getAllUniqueMonthsAsLinkedList(type);
            count = months.size() - 1;
            RealmDataItem item = months.get(count);
            binding.labelMonth.setText(item.getMonthString());
            binding.labelYear.setText(Integer.toString(item.getYear()));
            binding.leftArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Category Position", "" + count);
                    if (count < months.size() - 1) {
                        count++;
                        RealmDataItem item = months.get(count);
                        binding.labelMonth.setText(item.getMonthString());
                        binding.labelYear.setText(Integer.toString(item.getYear()));
                        updateGraphs(item.getMonth(), item.getYear());
                    } else {
                        count = 0;
                        RealmDataItem item = months.get(count);
                        binding.labelMonth.setText(item.getMonthString());
                        binding.labelYear.setText(Integer.toString(item.getYear()));
                        updateGraphs(item.getMonth(), item.getYear());

                    }
                }
            });

            binding.rightArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Category Count", "" + count);
                    if (count > 0) {
                        count--;
                        RealmDataItem item = months.get(count);
                        binding.labelMonth.setText(item.getMonthString());
                        binding.labelYear.setText(Integer.toString(item.getYear()));
                        updateGraphs(item.getMonth(), item.getYear());
                    } else {
                        count = months.size() - 1;
                        RealmDataItem item = months.get(count);
                        binding.labelMonth.setText(item.getMonthString());
                        binding.labelYear.setText(Integer.toString(item.getYear()));
                        updateGraphs(item.getMonth(), item.getYear());
                    }
                }
            });
            produceOne(months.get(count).getMonth(), months.get(count).getYear());
            produceTwo(months.get(count).getMonth(), months.get(count).getYear());
            mAdapter = new GraphMonthlyAdapter(getActivity(), mCharts, mRealmHandler);
            binding.listview.setAdapter(mAdapter);
        } else {
            binding.labelMonth.setText("No Data");
            binding.labelYear.setText("");
        }
        return binding.getRoot();
    }

    public void updateType(int type) {
        this.type = type;
        if (mRealmHandler != null && !mRealmHandler.isEmpty(type)) {
            months = mRealmHandler.getAllUniqueMonthsAsLinkedList(type);
            count = months.size() - 1;
            binding.labelMonth.setText(months.get(count).getMonthString());
            binding.labelMonth.setText(Integer.toString(months.get(count).getYear()));
            updateGraphs(months.get(count).getMonth(), months.get(count).getYear());
        } else {
            binding.labelMonth.setText("No Data");
            binding.labelMonth.setText("");
        }
    }

    private void updateGraphs(int month, int year) {
        mCharts.get(0).updateData(updateOne(month, year), 0);
//         mCharts.get(1).updateData(updateTwo(month, year), mRealmHandler.getSpecificDateAmount(month, year, 2));
        mAdapter.notifyDataSetChanged();
    }

    /* Creates a line chart for expense vs income */
    private void produceOne(int month, int year) {

        ArrayList<BarEntry> incomeValues = new ArrayList<>();
        ArrayList<BarEntry> expenseValues = new ArrayList<>();

        int i = 0;
        for (RealmCategoryItem category : categories) {
            float incomeValue = mRealmHandler.getSpecificDateAmountByType(category.getCategory(), month, year, 1);
            float expenseValue = mRealmHandler.getSpecificDateAmountByType(category.getCategory(), month, year, 0);
            incomeValues.add(new BarEntry(i, incomeValue));
            expenseValues.add(new BarEntry(i++, expenseValue));
        }

        List<IBarDataSet> barDataSets = new ArrayList<>();
        BarDataSet dataSetIncome = new BarDataSet(incomeValues, "Income");
        dataSetIncome.setDrawValues(false);
        dataSetIncome.setColor(getResources().getColor(R.color.flat_nephritis));
        BarDataSet dataSetExpense = new BarDataSet(expenseValues, "Expense");
        dataSetExpense.setDrawValues(false);
        dataSetExpense.setColor(getResources().getColor(R.color.flat_pomegranate));
        barDataSets.add(dataSetIncome);
        barDataSets.add(dataSetExpense);

        BarData barData = new BarData(barDataSets);
        barData.setBarWidth(15f);

        mCharts.add(new BarChartItem(barData, getResources().getColor(R.color.flat_wisteria)));

    }

    /* Creates a graph for expenses vs income */
    private void produceTwo(int month, int year) {

        float total;
        List<BarEntry> barEntries = new ArrayList<>();

        float income = mRealmHandler.getSpecificDateAmount(month, year, 1);
        String[] incomeLabel = {"Income"};

        float expense = -mRealmHandler.getSpecificDateAmount(month, year, 0);
        String[] expenseLabel = {"Expense"};

        String[] labels = {"Income", "Expense"};

        if (income > -expense) {
            total = income + 10;
        } else {
            total = -expense + 10;
        }
//
//        chart.setDescription("");
//
//        chart.getAxisLeft().setEnabled(false);
//        chart.getAxisRight().setStartAtZero(false);
//        chart.getAxisRight().setAxisMaxValue(total);
//        chart.getAxisRight().setAxisMinValue(-total);
//        chart.getAxisRight().setTextSize(9f);
//
//        XAxis xAxis = chart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
//        xAxis.setDrawGridLines(false);
//        xAxis.setDrawAxisLine(false);
//        xAxis.setTextSize(9f);
//
//        Legend l = chart.getLegend();
//        l.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
//        l.setFormSize(8f);
//        l.setFormToTextSpace(4f);
//        l.setXEntrySpace(6f);

        if (income > -expense) {
            total = income + 10;
        } else {
            total = -expense + 10;
        }

//        chart.setDescription("");
//
//        chart.getAxisLeft().setEnabled(false);
//        chart.getAxisRight().setStartAtZero(false);
//        chart.getAxisRight().setAxisMaxValue(total);
//        chart.getAxisRight().setAxisMinValue(-total);
//        chart.getAxisRight().setTextSize(9f);

        ArrayList<BarEntry> yValues = new ArrayList<>();
        yValues.add(new BarEntry(0, new float[]{-expense, income}));

        BarDataSet barDataSet = new BarDataSet(yValues, "");
        barDataSet.setValueTextSize(7f);
        barDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        barDataSet.setBarBorderWidth(40f);
        barDataSet.setColors(new int[]{getResources().getColor(R.color.md_red_A400), getResources().getColor(R.color.md_green_A400)});
        barDataSet.setStackLabels(new String[]{
                "Expenses", "Income"
        });
        BarData barData = new BarData(barDataSet);

        total = mRealmHandler.getSpecificDateAmount(month, year, 2);

        mCharts.add(new HorizontalBarChartItem(barData, total, getResources().getColor(R.color.flat_wisteria)));
    }

    private ChartData updateOne(int month, int year) {
        ArrayList<BarEntry> incomeValues = new ArrayList<>();
        ArrayList<BarEntry> expenseValues = new ArrayList<>();

        int i = 0;
        for (RealmCategoryItem category : categories) {
            float value = mRealmHandler.getSpecificDateAmountByType(category.getCategory(), month, year, 1);
            incomeValues.add(new BarEntry(i++, value));
        }

        i = 0;
        for (RealmCategoryItem category : categories) {
            float value = mRealmHandler.getSpecificDateAmountByType(category.getCategory(), month, year, 0);
            expenseValues.add(new BarEntry(i++, value));
        }

        List<IBarDataSet> barDataSets = new ArrayList<>();
        BarDataSet dataSetIncome = new BarDataSet(incomeValues, "Income");
        dataSetIncome.setDrawValues(false);
        dataSetIncome.setColor(getResources().getColor(R.color.md_green_A400));
        BarDataSet dataSetExpense = new BarDataSet(expenseValues, "Expense");
        dataSetExpense.setDrawValues(false);
        dataSetExpense.setColor(getResources().getColor(R.color.md_red_A400));
        barDataSets.add(dataSetIncome);
        barDataSets.add(dataSetExpense);

        BarData barData = new BarData(barDataSets);
        barData.setBarWidth(5f);
        return barData;
    }

//    private void updateTwo(int month, int year) {
//
//        float total = mRealmHandler.getSpecificDateAmount(month, year, 2);
////        chart.getAxisRight().setAxisMaxValue(total+10);
////        chart.getAxisRight().setAxisMinValue(-total-10);
//        List<BarEntry> barEntries = new ArrayList<>();float income = mRealmHandler.getSpecificDateAmount(month, year, 1);
//        String[] incomeLabel = {"Income"};
//
//        float expense = -mRealmHandler.getSpecificDateAmount(month, year, 0);
//        String[] expenseLabel = {"Expense"};
//
//        String[] labels = {"Income", "Expense"};
//
//        ArrayList<BarEntry> yValues = new ArrayList<>();
////        yValues.add(new BarEntry(new float[]{expense, income}, 0));
//
//        BarDataSet barDataSet = new BarDataSet(yValues, "Data Comparison");
//        barDataSet.setValueTextSize(7f);
//        barDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
////        barDataSet.setBarSpacePercent(40f);
//        barDataSet.setColors(new int[]{getResources().getColor(R.color.md_red_A400), getResources().getColor(R.color.md_green_A400)});
////        return new BarData(new String[]{""}, barDataSet);
//    }

}
