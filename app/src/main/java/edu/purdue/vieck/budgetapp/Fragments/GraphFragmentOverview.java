package edu.purdue.vieck.budgetapp.Fragments;

import android.content.Context;
import android.graphics.Color;
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
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.purdue.vieck.budgetapp.CustomObjects.BarChartItem;
import edu.purdue.vieck.budgetapp.CustomObjects.BudgetItem;
import edu.purdue.vieck.budgetapp.CustomObjects.ChartItem;
import edu.purdue.vieck.budgetapp.CustomObjects.LineChartItem;
import edu.purdue.vieck.budgetapp.CustomObjects.PieChartItem;
import edu.purdue.vieck.budgetapp.DatabaseHandler;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by mvieck on 10/7/2015.
 */
public class GraphFragmentOverview extends Fragment {
    DatabaseHandler databaseHandler;
    ListView lv;
    private List<BudgetItem> months;
    ImageButton left, right;
    TextView monthTxt, yearTxt;
    int count;
    String[] categories;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph_overview, container, false);
        lv = (ListView) view.findViewById(R.id.listview);
        categories = getResources().getStringArray(R.array.categoryarray);
        databaseHandler = new DatabaseHandler(getActivity());
        monthTxt = (TextView) view.findViewById(R.id.label_month);
        yearTxt = (TextView) view.findViewById(R.id.label_year);
        if (!databaseHandler.isEmpty()) {
            months = databaseHandler.getAllUniqueMonthsAsList();
            count = months.size() - 1;
            monthTxt.setText(months.get(count).getMonthName());
            yearTxt.setText("" + months.get(count).getYear());
            left = (ImageButton) view.findViewById(R.id.left_arrow);
            left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (count + 1 < months.size()) {
                        count++;
                        BudgetItem item = months.get(count);
                        monthTxt.setText(item.getMonthName());
                        yearTxt.setText("" + item.getYear());
                        changeAdapterMonth(item.getMonth(), item.getYear());
                    } else {
                        count = 0;
                        BudgetItem item = months.get(count);
                        monthTxt.setText(item.getMonthName());
                        yearTxt.setText("" + item.getYear());
                        changeAdapterMonth(item.getMonth(), item.getYear());
                    }
                }
            });
            right = (ImageButton) view.findViewById(R.id.right_arrow);
            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (count > 0) {
                        count--;
                        BudgetItem item = months.get(count);
                        monthTxt.setText(item.getMonthName());
                        yearTxt.setText("" + item.getYear());
                        changeAdapterMonth(item.getMonth(), item.getYear());
                    } else {
                        count = months.size() - 1;
                        BudgetItem item = months.get(count);
                        monthTxt.setText(item.getMonthName());
                        yearTxt.setText("" + item.getYear());
                        changeAdapterMonth(item.getMonth(), item.getYear());
                    }
                }
            });
            BudgetItem item = months.get(count++);
            changeAdapterMonth(item.getMonth(), item.getYear());
        } else {
            monthTxt.setText("No Data");
            yearTxt.setText("");
        }
        return view;
    }

    private void changeAdapterMonth(int month, int year) {
        ArrayList<ChartItem> list = new ArrayList<ChartItem>();

        list.add(new LineChartItem(generateDataLine(0, month, year), getActivity()));
        list.add(new BarChartItem(generateDataBar(0, month, year), getActivity()));
        list.add(new PieChartItem(generateDataPie(0, month, year), getActivity()));

        ChartDataAdapter cda = new ChartDataAdapter(getActivity(), list);
        lv.setAdapter(cda);
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private LineData generateDataLine(int cnt, int month, int year) {
        ArrayList<Entry> e1 = new ArrayList<Entry>();
        int num = 0;
        for (String category : categories) {
            e1.add(new Entry(databaseHandler.getSpecificDateAmountByType(category,month,year),num++));
        }
        LineDataSet d1 = new LineDataSet(e1, "New DataSet " + cnt + ", (1)");
        d1.setLineWidth(2.5f);
        d1.setCircleSize(4.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);
        d1.setValueTextColor(getResources().getColor(R.color.CottonBlue));

        /*ArrayList<Entry> e2 = new ArrayList<Entry>();
        for (String category : categories) {
            e1.add(new Entry(databaseHandler.getSpecificDateAmountByType(category,month,year),num++));
        }*/


        /*LineDataSet d2 = new LineDataSet(e2, "New DataSet " + cnt + ", (2)");
        d2.setLineWidth(2.5f);
        d2.setCircleSize(4.5f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setValueTextColor(getResources().getColor(R.color.CottonBlue));
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setDrawValues(false);*/

        ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
        sets.add(d1);
        //sets.add(d2);

        LineData cd = new LineData(getMonths(), sets);;
        return cd;
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private BarData generateDataBar(int cnt, int month, int year) {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        for (int i = 0; i < categories.length; i++) {
            entries.add(new BarEntry(databaseHandler.getSpecificDateAmountByType(categories[i], month, year), i));
        }

        BarDataSet d = new BarDataSet(entries, "Categories " + cnt);
        d.setBarSpacePercent(10f);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        d.setHighLightAlpha(255);

        BarData cd = new BarData(getMonths(), d);
        return cd;
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private PieData generateDataPie(int cnt, int month, int year) {

        ArrayList<Entry> entries = new ArrayList<Entry>();
        for (int i = 0; i < categories.length; i++) {
            entries.add(new Entry(databaseHandler.getSpecificDateAmountByType(categories[i],month,year),i));
        }

        PieDataSet d = new PieDataSet(entries, "");

        // space between slices
        d.setSliceSpace(2f);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        PieData cd = new PieData(getQuarters(), d);
        return cd;
    }

    private String[] getQuarters() {

        return getResources().getStringArray(R.array.categoryarray);
    }

    private String[] getMonths() {

        return getResources().getStringArray(R.array.categoryarray);
    }

    /**
     * adapter that supports 3 different item types
     */
    private class ChartDataAdapter extends ArrayAdapter<ChartItem> {

        public ChartDataAdapter(Context context, List<ChartItem> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getItem(position).getView(position, convertView, getContext());
        }

        @Override
        public int getItemViewType(int position) {
            // return the views type
            return getItem(position).getItemType();
        }

        @Override
        public int getViewTypeCount() {
            return 3; // we have 3 different item-types
        }
    }
}
