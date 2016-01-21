package edu.purdue.vieck.budgetapp.ChartListViewItems;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.ChartData;

import edu.purdue.vieck.budgetapp.R;

/**
 * Created by vieck on 1/20/16.
 */
public class BarChartItem extends ChartItem {
    private Typeface mTf;
    public BarChartItem(ChartData<?> chartData, Context context) {
        super(chartData, context);
        mTf = Typeface.createFromAsset(context.getAssets(), "OpenSans-Regular.ttf");
    }
    @Override
    public int getItemType() {
        return TYPE_BARCHART;
    }

    @Override
    public View getView(int position, View convertView, Context c) {

        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();

            convertView = LayoutInflater.from(c).inflate(
                    R.layout.listview_item_barchart, null);
            holder.chart = (HorizontalBarChart) convertView.findViewById(R.id.bar_chart);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        XAxis xAxis = holder.chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);
        xAxis.setSpaceBetweenLabels(1);

        Legend l = holder.chart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        mChartData.setValueTypeface(mTf);

        // set data
        holder.chart.setData((BarData) mChartData);

        // do not forget to refresh the chart
//        holder.chart.invalidate();
        holder.chart.animateY(700);

        return convertView;
    }

    @Override
    public void updateData(ChartData<?> mChartData) {

    }

    private static class ViewHolder {
        BarChart chart;
    }

}
