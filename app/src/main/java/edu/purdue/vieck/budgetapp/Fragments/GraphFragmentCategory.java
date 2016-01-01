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
    private RealmHandler mRealmHandler;
    private LinkedList<BudgetItem> months;
    ImageButton left, right;
    TextView monthTxt, yearTxt;
    private int count, type;

    String[] categories;

    /* First Chart */
    BarChartView mChartOne;

    /* Second Chart */

    /* Third Chart */
    HorizontalStackBarChartView mChartThree;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph_monthly, container, false);

        categories = getResources().getStringArray(R.array.categoryarray);
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
                        monthTxt.setText(item.getMonthString());
                        yearTxt.setText("" + item.getYear());
                    } else {
                        count = 0;
                        BudgetItem item = months.get(count);
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
                        monthTxt.setText(item.getMonthString());
                        yearTxt.setText("" + item.getYear());
                    } else {
                        count = months.size() - 1;
                        BudgetItem item = months.get(count);
                        monthTxt.setText(item.getMonthString());
                        yearTxt.setText("" + item.getYear());
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

    private void produceOne(ChartView chart, int month, int year) {
        BarChartView barChartView = (BarChartView) chart;
        float highestCategory = 0;
        float expenseTotal = mRealmHandler.getSpecificDateAmount(month, year, 0);
        float incomeTotal = mRealmHandler.getSpecificDateAmount(month, year, 1);

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

        BarSet data = new BarSet(categories, income);
        data.setColor(getResources().getColor(R.color.md_green_400));
        barChartView.addData(data);

        data = new BarSet(categories, expense);
        data.setColor(getResources().getColor(R.color.md_red_400));
        barChartView.addData(data);

        barChartView.setBackgroundColor(getResources().getColor(R.color.md_white_1000));
        barChartView.setSetSpacing(Tools.fromDpToPx(-12));
        barChartView.setBarSpacing(Tools.fromDpToPx(12));
        barChartView.setRoundCorners(Tools.fromDpToPx(2));

        Paint gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#8986705C"));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(barChartView.getWidth()/categories.length));

        barChartView.setAxisBorderValues(0, Math.round(highestCategory));

        barChartView.setBorderSpacing(5)
                .setGrid(BarChartView.GridType.FULL, categories.length, categories.length, gridPaint)
                .setYAxis(false)
                .setXAxis(false)
                .setXLabels(XController.LabelPosition.OUTSIDE)
                .setYLabels(YController.LabelPosition.NONE)
                .setLabelsColor(Color.parseColor("#86705c"))
                .setAxisColor(Color.parseColor("#86705c"));

        barChartView.show();
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
        stackedChart.setLabelsColor(getResources().getColor(R.color.md_black_1000));
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

    private void updateOne(ChartView chartView, int month, int year) {
        float[] income = new float[categories.length];
        for (int i = 0; i < categories.length; i++) {
            income[i] = mRealmHandler.getSpecificDateAmountByType(categories[i], month, year, 1);
        }

        float[] expense = new float[categories.length];
        for (int i = 0; i < categories.length; i++) {
            expense[i] = mRealmHandler.getSpecificDateAmountByType(categories[i], month, year, 0);
        }

        chartView.updateValues(0, income);
        chartView.updateValues(1, expense);
        chartView.notifyDataUpdate();
    }

    private void updateTwo(ChartView chartView, int month, int year) {

    }

    private void updateThree(ChartView chartView, int month, int year) {
        float[] income = {mRealmHandler.getSpecificDateAmount(month, year, 1)};
        float[] expense = {-mRealmHandler.getSpecificDateAmount(month, year, 0)};
        chartView.updateValues(0, income);
        chartView.updateValues(1,expense);
        chartView.notifyDataUpdate();
    }

}
