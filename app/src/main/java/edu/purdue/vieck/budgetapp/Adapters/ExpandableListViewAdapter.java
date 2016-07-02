package edu.purdue.vieck.budgetapp.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import edu.purdue.vieck.budgetapp.CustomObjects.RealmDataItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by vieck on 12/20/15.
 */
public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    Context context;
    RealmHandler mRealmHandler;
    HashMap<Integer, List<RealmDataItem>> years;
    List<RealmDataItem> mMonths;
    String currencySymbol;
    int actionBarColor;
    SharedPreferences mSharedPreferences;

    public ExpandableListViewAdapter(Context context, RealmHandler realmHandler, String filter) {
        this.context = context;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        currencySymbol = mSharedPreferences.getString("currencySymbol","$");
        actionBarColor = mSharedPreferences.getInt("actionBarColor", Color.BLACK);
        mRealmHandler = realmHandler;
        if (filter == "") {
            mMonths = mRealmHandler.getAllUniqueMonthsAsList(2);
        } else {
            mMonths = mRealmHandler.getAllUniqueMonthsAsLinkedList(2);
        }
    }

    @Override
    public int getGroupCount() {
        return mMonths.size();
    }

    @Override
    public int getChildrenCount(int group) {
        return mRealmHandler.getDataByMonthYearAndType(mMonths.get(group).getMonth(), mMonths.get(group).getYear(), 2).size();
    }

    @Override
    public Object getGroup(int group) {
        return mMonths.get(group);
    }

    @Override
    public Object getChild(int group, int child) {
        return mRealmHandler.getDataByMonthYearAndType(mMonths.get(group).getMonth(), mMonths.get(group).getYear(), 2).get(child);
    }

    @Override
    public long getGroupId(int group) {
        return group;
    }

    @Override
    public long getChildId(int group, int child) {
        return child;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int group, boolean b, View view, ViewGroup viewGroup) {
        LinearLayout linearLayout;
        TextView date;
        final RealmDataItem item = (RealmDataItem) getGroup(group);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.item_data_group, null);
        }

        linearLayout = (LinearLayout) view;
        date = (TextView) view.findViewById(R.id.label_date);

        date.setText(item.getMonthString() + " " + item.getYear());
        linearLayout.setBackgroundColor(actionBarColor);
        return view;
    }

    @Override
    public View getChildView(int group, int child, boolean b, View view, ViewGroup viewGroup) {
        TextView date, amount, category, subcategory, type;
        final RealmDataItem item = (RealmDataItem) getChild(group, child);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.item_data_child, null);
        }

        date = (TextView) view.findViewById(R.id.label_date);
        amount = (TextView) view.findViewById(R.id.data_amount);
        category = (TextView) view.findViewById(R.id.label_category);
        subcategory = (TextView) view.findViewById(R.id.label_subcategory);
        type = (TextView) view.findViewById(R.id.label_type);

        date.setText(item.getMonth() + "/" + item.getDay() + "/" + item.getYear());
        amount.setText(item.getAmount() + currencySymbol);
        category.setText(item.getCategory());
        subcategory.setText(item.getSubcategory());
        type.setText(item.getTypeString());

        if (item.getType()) {
            amount.setTextColor(context.getResources().getColor(R.color.Lime));
        } else {
            amount.setTextColor(context.getResources().getColor(R.color.md_red_A400));
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int group, int child) {
        return false;
    }


}
