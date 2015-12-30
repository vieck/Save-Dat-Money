package edu.purdue.vieck.budgetapp.Fragments;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.db.chart.Tools;
import com.db.chart.model.BarSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.BarChartView;
import com.db.chart.view.ChartView;
import com.db.chart.view.HorizontalStackBarChartView;
import com.db.chart.view.XController;
import com.db.chart.view.YController;
import com.github.mikephil.charting.charts.Chart;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.purdue.vieck.budgetapp.Adapters.GraphCategoryAdapter;
import edu.purdue.vieck.budgetapp.CustomObjects.AddTreeItem;
import edu.purdue.vieck.budgetapp.CustomObjects.BudgetItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by mvieck on 10/7/2015.
 */
public class GraphFragmentCategory extends Fragment {
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private GraphCategoryAdapter adapter;
    private RealmHandler mRealmHandler;
    private LinkedList<BudgetItem> months;
    ImageButton left, right;
    TextView monthTxt, yearTxt;
    private int count, type;

    /* First Chart */
    BarChartView mChartOne;

    /* Second Chart */

    /* Third Chart */
    HorizontalStackBarChartView mChartThree;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph_monthly, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);

        mRealmHandler = new RealmHandler(getActivity());

        mChartOne = (BarChartView) view.findViewById(R.id.chart_one);
        mChartThree = (HorizontalStackBarChartView) view.findViewById(R.id.chart_three);

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
                        adapter.changeMonth(item.getMonth(), item.getYear());
                        monthTxt.setText(item.getMonthString());
                        yearTxt.setText("" + item.getYear());
                    } else {
                        count = 0;
                        BudgetItem item = months.get(count);
                        adapter.changeMonth(item.getMonth(), item.getYear());
                        monthTxt.setText(item.getMonthString());
                        yearTxt.setText("" + item.getYear());
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
                        adapter.changeMonth(item.getMonth(), item.getYear());
                        monthTxt.setText(item.getMonthString());
                        yearTxt.setText("" + item.getYear());
                    } else {
                        count = months.size() - 1;
                        BudgetItem item = months.get(count);
                        adapter.changeMonth(item.getMonth(), item.getYear());
                        monthTxt.setText(item.getMonthString());
                        yearTxt.setText("" + item.getYear());
                    }
                }
            });
            adapter = makeAdapter(adapter);
            recyclerView.setAdapter(adapter);
        } else {
            monthTxt.setText("No Data");
            yearTxt.setText("");
        }

        return view;
    }

    private GraphCategoryAdapter makeAdapter(GraphCategoryAdapter adapter) {
        List<AddTreeItem> list = new ArrayList<>();
        int[] categoryImages = {R.drawable.food_groceries_dark, R.drawable.utility_misc_dark, R.drawable.entertainment_dark, R.drawable.medical_misc_dark, R.drawable.insurance_dark, R.drawable.chart_dark};
        String[] categories = getResources().getStringArray(R.array.categoryarray);
        monthTxt.setText(months.get(count).getMonthString());
        yearTxt.setText("" + months.get(count).getYear());
        AddTreeItem item;
        for (int i = 0; i < categories.length; i++) {
            item = new AddTreeItem();
            item.setDrawableId(categoryImages[i]);
            item.setName(categories[i]);
            item.setAmount(mRealmHandler.getSpecificDateAmountByType(categories[i], months.get(0).getMonth(), months.get(0).getYear(), type));
            list.add(item);
        }
        adapter = new GraphCategoryAdapter(getActivity(), list, months.get(count).getMonth(), months.get(count).getYear(), type);
        return adapter;
    }

    public void updateType(int type) {
        this.type = type;
        if (!mRealmHandler.isEmpty(type)) {
            months = mRealmHandler.getAllUniqueMonthsAsLinkedList(type);
            count = months.size() - 1;
            monthTxt.setText(months.get(count).getMonthString());
            yearTxt.setText("" + months.get(count).getYear() + "");
            adapter = makeAdapter(adapter);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        } else {
            monthTxt.setText("No Data");
            yearTxt.setText("");
        }
    }

    private void produceOne(ChartView chart) {
        BarChartView barChartView = (BarChartView) chart;
    }

    private void produceTwo(ChartView chart) {

    }

    private void produceThree(ChartView chart, int month, int year) {
        HorizontalStackBarChartView stackedChart = (HorizontalStackBarChartView) chart;

        float[] income = {mRealmHandler.getSpecificDateAmount(month, year, 1)};
        String[] incomeLabel = {"Income"};
        BarSet data = new BarSet(incomeLabel, income);
        data.setColor(getResources().getColor(R.color.md_green_A400));
        stackedChart.addData(data);


        float[] expense = {-mRealmHandler.getSpecificDateAmount(month, year, 0)};
        String[] expenseLabel = { "Expense" };
        data = new BarSet(expenseLabel, expense);
        data.setColor(getResources().getColor(R.color.md_red_A400));
        stackedChart.addData(data);

        float total = mRealmHandler.getSpecificDateAmount(month,year,2);
        stackedChart.setBackgroundColor(getResources().getColor(R.color.md_black_1000));
        stackedChart.setLabelsColor(getResources().getColor(R.color.md_white_1000));
        stackedChart.setRoundCorners(Tools.fromDpToPx(8));
        stackedChart.setBarSpacing(Tools.fromDpToPx(30));
        stackedChart.setBorderSpacing(Tools.fromDpToPx(5))
                .setXLabels(AxisController.LabelPosition.NONE)
                .setYLabels(AxisController.LabelPosition.NONE)
                .setXAxis(true)
                .setYAxis(true)
                .setAxisBorderValues(-Math.round(total), Math.round(total));
        stackedChart.show();
    }


    private BarChartView createCategoryChart(final BarChartView barChart, int month, int year) {
        final float[][] mValuesOne = {{9.5f, 7.5f, 5.5f, 4.5f, 50f}, {6.5f, 3.5f, 3.5f, 2.5f, 7.5f}};

        float expenseTotal = mRealmHandler.getSpecificDateAmount(month, year, 0);
        float incomeTotal = mRealmHandler.getSpecificDateAmount(month, year, 1);

        float[] income = new float[categories.length];
        for (int i = 0; i < categories.length; i++) {
            income[i] = mRealmHandler.getSpecificDateAmountByType(categories[i], month, year, 1);
        }

        float[] expense = new float[categories.length];
        for (int i = 0; i < categories.length; i++) {
            expense[i] = mRealmHandler.getSpecificDateAmountByType(categories[i], month, year, 0);
        }

        BarSet positiveData = new BarSet(categories, income);
        positiveData.setColor(getResources().getColor(R.color.md_green_400));
        barChart.addData(positiveData);

        BarSet negativeData = new BarSet(categories, expense);
        negativeData.setColor(getResources().getColor(R.color.md_red_400));
        barChart.addData(negativeData);

        barChart.setBackgroundColor(getResources().getColor(R.color.md_white_1000));
        barChart.setSetSpacing(Tools.fromDpToPx(-12));
        barChart.setBarSpacing(Tools.fromDpToPx(12));
        barChart.setRoundCorners(Tools.fromDpToPx(2));

        Paint gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#8986705C"));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(barChart.getWidth()/categories.length));

        if (expenseTotal > incomeTotal) {
            barChart.setAxisBorderValues(0,Math.round(expenseTotal));
        } else {
            barChart.setAxisBorderValues(0, Math.round(incomeTotal));
        }

        barChart.setBorderSpacing(5)
                .setGrid(BarChartView.GridType.FULL, categories.length, categories.length, gridPaint)
                .setYAxis(false)
                .setXAxis(false)
                .setXLabels(XController.LabelPosition.OUTSIDE)
                .setYLabels(YController.LabelPosition.NONE)
                .setLabelsColor(Color.parseColor("#86705c"))
                .setAxisColor(Color.parseColor("#86705c"));

        barChart.show();
        return barChart;
    }

    private BarChartView createBudgetChart(final BarChartView barChartView, int month, int year) {
        final String[] mLabelsThree = {"", "", "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", "", ""};
        final float[] mValuesThree = {2.5f, 3.7f, 4f, 8f, 4.5f, 4f, 5f, 7f, 10f, 14f,
                12f, 6f, 7f, 8f, 9f, 8f, 9f, 8f, 7f, 6f,
                5f, 4f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 11f,
                12f, 14, 13f, 10f, 9f, 8f, 7f, 5f, 4f, 6f};

        //float[] days = mRealmHandler.getListOfDays(categories[0],month,year,type);
        //String[] mLabelsThree = new String[days.length];

        BarSet data = new BarSet(mLabelsThree, mValuesThree);
        data.setColor(getResources().getColor(R.color.md_light_blue_300));
        barChartView.addData(data);

        barChartView.setBarSpacing(Tools.fromDpToPx(3));

        barChartView.setXLabels(AxisController.LabelPosition.NONE)
                .setYLabels(AxisController.LabelPosition.NONE)
                .setXAxis(false)
                .setYAxis(false);

        barChartView.show();
        return barChartView;
    }

    private HorizontalStackBarChartView createStackChart(final HorizontalStackBarChartView chartView, int month, int year) {

    }
}
