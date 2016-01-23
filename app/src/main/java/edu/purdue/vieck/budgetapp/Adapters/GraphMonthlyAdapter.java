package edu.purdue.vieck.budgetapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.ChartData;

import java.util.List;

import edu.purdue.vieck.budgetapp.ChartListViewItems.ChartItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by vieck on 1/21/16.
 */
public class GraphMonthlyAdapter extends ArrayAdapter<ChartItem> {
    RealmHandler mRealmHandler;
    List<ChartItem> mItems;

    public GraphMonthlyAdapter(Context context, List<ChartItem> mItems, RealmHandler mRealmHandler) {
        super(context, 0, mItems);
        this.mItems = mItems;
        this.mRealmHandler = mRealmHandler;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getItem(position).getView(position, convertView, getContext());
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getItemType();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }
}
