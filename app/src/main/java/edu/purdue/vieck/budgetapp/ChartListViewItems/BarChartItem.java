package edu.purdue.vieck.budgetapp.ChartListViewItems;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.ChartData;

import edu.purdue.vieck.budgetapp.R;

/**
 * Created by vieck on 1/20/16.
 */
public class BarChartItem extends ChartItem {
    public BarChartItem(ChartData<?> chartData) {
        super(chartData);
    }
    @Override
    public int getItemType() {
        return TYPE_BARCHART;
    }

    @Override
    public View getView(int position, View convertView, Context c) {

        ViewHolder holder;

        if (convertView == null) {

            holder = new ViewHolder();

            convertView = LayoutInflater.from(c).inflate(
                    R.layout.listview_item_barchart, null);
            holder.chart = (BarChart) convertView.findViewById(R.id.bar_chart);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BarData barData = (BarData) mChartData;
        XAxis xAxis = holder.chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);

        Legend l = holder.chart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        //mChartData.setValueTypeface(mTf);

        // set data
        holder.chart.setData(barData);
        holder.chart.setGridBackgroundColor(Color.BLACK);


        holder.chart.animateY(700);
        holder.chart.notifyDataSetChanged();
        holder.chart.invalidate();

        return convertView;
    }

    private static class ViewHolder {
        BarChart chart;
    }

    @Override
    public void updateData(ChartData mChartData, float total) {
        this.mChartData = mChartData;
    }
}
