package edu.purdue.vieck.budgetapp.ChartListViewItems;

import android.content.Context;
import android.view.View;

import com.github.mikephil.charting.data.ChartData;

/**
 * Created by Stealth on 10/12/2015.
 */
public abstract class ChartItem {

    protected static final int TYPE_BARCHART = 0;
    protected static final int TYPE_HORIZONTAL_BARCHART = 1;
    protected static final int TYPE_LINECHART = 2;
    protected static final int TYPE_PIECHART = 3;

    protected ChartData<?> mChartData;

    public ChartItem(ChartData<?> cd) {
        this.mChartData = cd;
    }

    public abstract int getItemType();

    public abstract View getView(int position, View convertView, Context c);

    public abstract void updateData(ChartData mChartData, float total);


}
