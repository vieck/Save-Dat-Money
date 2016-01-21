package edu.purdue.vieck.budgetapp.ChartListViewItems;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.ChartData;

import edu.purdue.vieck.budgetapp.R;

/**
 * Created by vieck on 1/20/16.
 */
public class HorizontalBarChartItem extends ChartItem {
    private Typeface mTf;
    private float total;

    public HorizontalBarChartItem(ChartData<?> chartData, float total, Context context) {
        super(chartData, context);
        this.total = total;
        mTf = Typeface.createFromAsset(context.getAssets(), "OpenSans-Regular.ttf");
    }

    @Override
    public int getItemType() {
        return TYPE_HORIZONTAL_BARCHART;
    }

    @Override
    public View getView(int position, View convertView, Context c) {

        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();

            convertView = LayoutInflater.from(c).inflate(
                    R.layout.listview_item_horizontalbarchart, null);
            holder.chart = (HorizontalBarChart) convertView.findViewById(R.id.horizontal_bar_chart);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // apply styling
        holder.chart.setDescription("");
        holder.chart.setDrawGridBackground(false);
        holder.chart.setDrawBarShadow(false);

        holder.chart.getAxisLeft().setEnabled(false);
        holder.chart.getAxisRight().setStartAtZero(false);
        holder.chart.getAxisRight().setAxisMaxValue(total);
        holder.chart.getAxisRight().setAxisMinValue(-total);
        holder.chart.getAxisRight().setTextSize(9f);

        XAxis xAxis = holder.chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextSize(9f);

        Legend l = holder.chart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);

        mChartData.setValueTypeface(mTf);

        // set data
        holder.chart.setData((BarData) mChartData);

        // do not forget to refresh the chart
//        holder.chart.invalidate();
        holder.chart.animateY(700);

        return convertView;
    }

    private static class ViewHolder {
        HorizontalBarChart chart;
    }

}
