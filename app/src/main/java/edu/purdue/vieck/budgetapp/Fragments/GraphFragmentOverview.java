package edu.purdue.vieck.budgetapp.Fragments;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.db.chart.Tools;
import com.db.chart.model.BarSet;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.BarChartView;
import com.db.chart.view.ChartView;
import com.db.chart.view.LineChartView;
import com.github.mikephil.charting.data.LineDataSet;

import java.sql.Array;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Stack;

import edu.purdue.vieck.budgetapp.CustomObjects.BudgetItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by mvieck on 10/7/2015.
 */
public class GraphFragmentOverview extends Fragment {

    LineChartView mChartOne;
    BarChartView mChartTwo;
    LineChartView mChartThree;

    DecimalFormat decimalFormat;
    RealmHandler mRealmHandler;
    int type, count;
    String[] categories;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph_overview, container, false);
        setRetainInstance(true);
        Bundle bundle = getArguments();
        type = bundle.getInt("type", 2);
        decimalFormat = new DecimalFormat("0.00");
        categories = getResources().getStringArray(R.array.categoryarray);
        mRealmHandler = new RealmHandler(getActivity());

        mChartOne = (LineChartView) view.findViewById(R.id.chart_one);
        mChartTwo = (BarChartView) view.findViewById(R.id.chart_two);
        mChartThree = (LineChartView) view.findViewById(R.id.chart_three);

        if (mRealmHandler != null && !mRealmHandler.isEmpty(type)) {
            produceOne(mChartOne);
            produceTwo(mChartTwo);
        } else {
        }
        return view;
    }

    private void changeAdapterMonth(int month, int year) {
        //updateOne(month, year);
        //updateStackChart(month, year);
    }


    private void updateOne(LineChartView lineChartView) {
        float[] expenseArray = mRealmHandler.getAllDataAsArray(0);
        float[] incomeArray = mRealmHandler.getAllDataAsArray(1);
        float[] income = new float[categories.length];

        if (expenseArray.length > incomeArray.length) {
            lineChartView.setAxisBorderValues(0, expenseArray.length);
        } else {
            lineChartView.setAxisBorderValues(0, incomeArray.length);
        }
        lineChartView.updateValues(0, expenseArray);
        lineChartView.updateValues(1, incomeArray);
        lineChartView.notifyDataUpdate();
    }

    private void produceOne(LineChartView lineChartView) {
        float[] expenseArray = mRealmHandler.getAllDataAsArray(0);
        float[] incomeArray = mRealmHandler.getAllDataAsArray(1);
        float[] dottedLine;
        int total;
        if (expenseArray.length > incomeArray.length) {
            lineChartView.setAxisBorderValues(0, expenseArray.length);
            total = expenseArray.length;
        } else {
            lineChartView.setAxisBorderValues(0, incomeArray.length);
            total = incomeArray.length;
        }

        dottedLine = new float[total];

        for (int i = 0; i < total; i++) {
            dottedLine[i] = total / 4;
        }

        String[] labels = new String[expenseArray.length];
        Arrays.fill(labels,"");
        LineSet lineSet = new LineSet(labels, expenseArray);
        lineSet.setColor(getResources().getColor(R.color.md_red_A400))
                .setThickness(Tools.fromDpToPx(3))
                .beginAt(0).endAt(expenseArray.length);
        lineChartView.addData(lineSet);

        labels = new String[incomeArray.length];
        Arrays.fill(labels,"");
        lineSet = new LineSet(labels, incomeArray);
        lineSet.setColor(getResources().getColor(R.color.md_green_A400))
                .setThickness(Tools.fromDpToPx(3))
                .beginAt(0).endAt(expenseArray.length);
        lineChartView.addData(lineSet);

        labels = new String[total];
        Arrays.fill(labels,"");
        lineSet = new LineSet(labels, dottedLine);
        lineSet.setColor(Color.WHITE)
                .setThickness(Tools.fromDpToPx(3))
                .setDashed(new float[]{10, 10});
        lineChartView.addData(lineSet);

        Paint gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#7F97B867"));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(.75f));



        lineChartView.setBorderSpacing(Tools.fromDpToPx(0))
                .setXLabels(AxisController.LabelPosition.OUTSIDE)
                .setLabelsColor(View.SCROLL_AXIS_NONE)
                .setYLabels(AxisController.LabelPosition.NONE)
                .setXAxis(false)
                .setYAxis(false)
                .setAxisBorderValues(0,total)
                .setGrid(ChartView.GridType.HORIZONTAL, gridPaint)
        .setBackgroundColor(Color.BLACK);

        lineChartView.show();
    }

    private void produceTwo(ChartView chartView) {

        Stack<BudgetItem> stack = mRealmHandler.getAllDataAsStack(type);


        BarChartView barChartView = (BarChartView) chartView;

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
    }

    private void updateBudgetChart() {}

}
