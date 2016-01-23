package edu.purdue.vieck.budgetapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.ChartData;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.purdue.vieck.budgetapp.Adapters.GraphMonthlyAdapter;
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
    GraphMonthlyAdapter mAdapter;

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
            DataItem item = months.get(count);
            monthTxt.setText(item.getMonthString());
            yearTxt.setText(Integer.toString(item.getYear()));

            left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Category Position", "" + count);
                    if (count < months.size() - 1) {
                        count++;
                        DataItem item = months.get(count);
                        monthTxt.setText(item.getMonthString());
                        yearTxt.setText(Integer.toString(item.getYear()));
                        updateGraphs(item.getMonth(), item.getYear());
                    } else {
                        count = 0;
                        DataItem item = months.get(count);
                        monthTxt.setText(item.getMonthString());
                        yearTxt.setText(Integer.toString(item.getYear()));
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
                        yearTxt.setText(Integer.toString(item.getYear()));
                        updateGraphs(item.getMonth(), item.getYear());
                    } else {
                        count = months.size() - 1;
                        DataItem item = months.get(count);
                        monthTxt.setText(item.getMonthString());
                        yearTxt.setText(Integer.toString(item.getYear()));
                        updateGraphs(item.getMonth(), item.getYear());
                    }
                }
            });
            produceOne(months.get(count).getMonth(), months.get(count).getYear());
            produceTwo(months.get(count).getMonth(), months.get(count).getYear());
            mAdapter = new GraphMonthlyAdapter(getActivity(), mCharts, mRealmHandler);
            mListView.setAdapter(mAdapter);
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
            yearTxt.setText(Integer.toString(months.get(count).getYear()));
            updateGraphs(months.get(count).getMonth(), months.get(count).getYear());
        } else {
            monthTxt.setText("No Data");
            yearTxt.setText("");
        }
    }

    private void updateGraphs(int month, int year) {
        mCharts.get(0).updateData(updateOne(month, year), 0);
        mCharts.get(1).updateData(updateTwo(month, year), mRealmHandler.getSpecificDateAmount(month, year, 2));
        mAdapter.notifyDataSetChanged();
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

        mCharts.add(new BarChartItem(barData));

    }

    /* Creates a graph for expenses vs income */
    private void produceTwo(int month, int year) {

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
        BarData barData = new BarData(new String[]{""}, barDataSet);

        float total = mRealmHandler.getSpecificDateAmount(month, year, 2);

        mCharts.add(new HorizontalBarChartItem(barData, total));
    }

    private ChartData updateOne(int month, int year) {
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

        return new BarData(categories, barDataSets);
    }

    private ChartData updateTwo(int month, int year) {

        float income = mRealmHandler.getSpecificDateAmount(month, year, 1);
        String[] incomeLabel = {"Income"};

        float expense = -mRealmHandler.getSpecificDateAmount(month, year, 0);
        String[] expenseLabel = {"Expense"};

        String[] labels = {"Income", "Expense"};

        ArrayList<BarEntry> yValues = new ArrayList<>();
        yValues.add(new BarEntry(new float[]{expense, income}, 0));

        BarDataSet barDataSet = new BarDataSet(yValues, "Data Comparison");
        barDataSet.setValueTextSize(7f);
        barDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        barDataSet.setBarSpacePercent(40f);
        barDataSet.setColors(new int[]{getResources().getColor(R.color.md_red_A400), getResources().getColor(R.color.md_green_A400)});
        return new BarData(new String[]{""}, barDataSet);
    }

}
