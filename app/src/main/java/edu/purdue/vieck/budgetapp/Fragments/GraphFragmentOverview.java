package edu.purdue.vieck.budgetapp.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.db.chart.Tools;
import com.db.chart.model.BarSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.BarChartView;
import com.db.chart.view.HorizontalStackBarChartView;
import com.db.chart.view.StackBarChartView;
import com.db.chart.view.XController;
import com.db.chart.view.YController;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import edu.purdue.vieck.budgetapp.CustomObjects.BarChartItem;
import edu.purdue.vieck.budgetapp.CustomObjects.BudgetItem;
import edu.purdue.vieck.budgetapp.CustomObjects.ChartItem;
import edu.purdue.vieck.budgetapp.CustomObjects.LineChartItem;
import edu.purdue.vieck.budgetapp.CustomObjects.PieChartItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by mvieck on 10/7/2015.
 */
public class GraphFragmentOverview extends Fragment {

    BarChartView mCategoryBarView;
    BarChartView mBudgetBarView;
    HorizontalStackBarChartView mStackBarChartView;

    DecimalFormat decimalFormat;
    RealmHandler mRealmHandler;
    private List<BudgetItem> months;
    ImageButton mLeft, mRight;
    TextView mMonthTxt, mYearTxt;
    int type, count;
    String[] categories;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph_overview, container, false);

        Bundle bundle = getArguments();
        type = bundle.getInt("type", 2);
        decimalFormat = new DecimalFormat("0.00");
        categories = getResources().getStringArray(R.array.categoryarray);
        mRealmHandler = new RealmHandler(getActivity());
        mMonthTxt = (TextView) view.findViewById(R.id.label_month);
        mYearTxt = (TextView) view.findViewById(R.id.label_year);

        mCategoryBarView = (BarChartView) view.findViewById(R.id.category_barchart);
        mBudgetBarView = (BarChartView) view.findViewById(R.id.budget_barchart);
        mStackBarChartView = (HorizontalStackBarChartView) view.findViewById(R.id.stackchart);

        if (!mRealmHandler.isEmpty(type)) {
            months = mRealmHandler.getAllUniqueMonthsAsList(type);
            count = months.size() - 1;
            mMonthTxt.setText(months.get(count).getMonthString());
            mYearTxt.setText("" + months.get(count).getYear());
            mLeft = (ImageButton) view.findViewById(R.id.left_arrow);
            mLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (count + 1 < months.size()) {
                        count++;
                        BudgetItem item = months.get(count);
                        mMonthTxt.setText(item.getMonthString());
                        mYearTxt.setText("" + item.getYear());
                        changeAdapterMonth(item.getMonth(), item.getYear());
                    } else {
                        count = 0;
                        BudgetItem item = months.get(count);
                        mMonthTxt.setText(item.getMonthString());
                        mYearTxt.setText("" + item.getYear());
                        changeAdapterMonth(item.getMonth(), item.getYear());
                    }
                }
            });
            mRight = (ImageButton) view.findViewById(R.id.right_arrow);
            mRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (count > 0) {
                        count--;
                        BudgetItem item = months.get(count);
                        mMonthTxt.setText(item.getMonthString());
                        mYearTxt.setText("" + item.getYear());
                        changeAdapterMonth(item.getMonth(), item.getYear());
                    } else {
                        count = months.size() - 1;
                        BudgetItem item = months.get(count);
                        mMonthTxt.setText(item.getMonthString());
                        mYearTxt.setText("" + item.getYear());
                        changeAdapterMonth(item.getMonth(), item.getYear());
                    }
                }
            });
            BudgetItem item = months.get(count);
            mCategoryBarView = createCategoryChart(mCategoryBarView, item.getMonth(), item.getYear());
            mBudgetBarView = createBudgetChart(mBudgetBarView, item.getMonth(), item.getYear());
            mStackBarChartView = createStackChart(mStackBarChartView, item.getMonth(), item.getYear());
            changeAdapterMonth(item.getMonth(), item.getYear());
        } else {
            mMonthTxt.setText("No Data");
            mYearTxt.setText("");
        }
        return view;
    }

    private void changeAdapterMonth(int month, int year) {
        //updateCategoryChart(month, year);
        //updateStackChart(month, year);
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
        float total = mRealmHandler.getSpecificDateAmount(month,year,2);

        float[] income = {mRealmHandler.getSpecificDateAmount(month, year, 1)};
        String[] incomeLabel = {"Income"};
        BarSet incomeData = new BarSet(incomeLabel, income);
        incomeData.setColor(getResources().getColor(R.color.md_green_A400));

        float[] expense = {-mRealmHandler.getSpecificDateAmount(month, year, 0)};
        String[] expenseLabel = { "Expense" };


        BarSet expenseData = new BarSet(expenseLabel, expense);
        expenseData.setColor(getResources().getColor(R.color.md_red_A400));

        chartView.addData(incomeData);
        chartView.addData(expenseData);

        chartView.setBackgroundColor(getResources().getColor(R.color.md_black_1000));
        chartView.setLabelsColor(getResources().getColor(R.color.md_white_1000));
        chartView.setRoundCorners(Tools.fromDpToPx(8));
        chartView.setBarSpacing(Tools.fromDpToPx(30));
        chartView.setBorderSpacing(Tools.fromDpToPx(5))
                .setXLabels(AxisController.LabelPosition.NONE)
                .setYLabels(AxisController.LabelPosition.NONE)
                .setXAxis(true)
                .setYAxis(true)
                .setAxisBorderValues(-Math.round(total), Math.round(total));
        chartView.show();
        return chartView;
    }

    private void updateCategoryChart(int month, int year) {
        float expenseTotal = mRealmHandler.getSpecificDateAmount(month, year, 0);
        float incomeTotal = mRealmHandler.getSpecificDateAmount(month, year, 1);
        float[] income = new float[categories.length];
        for (int i = 0; i < categories.length; i++) {
            income[i] = mRealmHandler.getSpecificDateAmountByType(categories[i], month, year, type);
        }
        float[] expense = new float[categories.length];
        for (int i = 0; i < categories.length; i++){
            expense[i] = mRealmHandler.getSpecificDateAmountByType(categories[i], month, year, type);
        }

        if (expenseTotal > incomeTotal) {
            //mCategoryBarView.setAxisBorderValues(0, Math.round(expenseTotal));
        } else {
          // mCategoryBarView.setAxisBorderValues(0, Math.round(incomeTotal));
        }
        mCategoryBarView.updateValues(1, income);
        mCategoryBarView.updateValues(0, expense);
        //mCategoryBarView.notifyDataUpdate();
    }

    private void updateBudgetChart(int month, int year) {

    }

    private void updateStackChart(int month, int year) {
        float total = mRealmHandler.getSpecificDateAmount(month,year,2);

        float[] income = {mRealmHandler.getSpecificDateAmount(month, year, 1)};
        float[] expense = {-mRealmHandler.getSpecificDateAmount(month, year, 0)};

        mStackBarChartView.setAxisBorderValues(-Math.round(total), -Math.round(total));
        mStackBarChartView.updateValues(0, expense);
        mStackBarChartView.updateValues(1, income);
        //mStackBarChartView.notifyDataUpdate();
    }

}
