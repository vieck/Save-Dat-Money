package edu.purdue.vieck.budgetapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import edu.purdue.vieck.budgetapp.CustomObjects.BudgetItem;
import edu.purdue.vieck.budgetapp.ParseHandler;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by vieck on 12/20/15.
 */
public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    Context context;
    ParseHandler mParseHandler;
    HashMap<Integer, List<BudgetItem>> years;
    List<BudgetItem> mMonths;

    public ExpandableListViewAdapter(Context context, String filter) {
        this.context = context;
        mParseHandler = new ParseHandler();
        if (filter == "") {
            mMonths = mParseHandler.getAllUniqueMonthsAsList(2);
        } else {
            mMonths = mParseHandler.getAllUniqueMonthsAsLinkedList(2);
        }
    }

    @Override
    public int getGroupCount() {
        return mMonths.size();
    }

    @Override
    public int getChildrenCount(int group) {
        return mParseHandler.getSpecificMonthYearAsStack(mMonths.get(group).getMonth(), mMonths.get(group).getYear(), 2).size();
    }

    @Override
    public Object getGroup(int group) {
        return mMonths.get(group);
    }

    @Override
    public Object getChild(int group, int child) {
        return mParseHandler.getSpecificMonthYearAsStack(mMonths.get(group).getMonth(), mMonths.get(group).getYear(), 2).get(child);
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
        TextView date;
        final BudgetItem item = (BudgetItem) getGroup(group);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.item_data_group, null);
        }

        date = (TextView) view.findViewById(R.id.label_date);

        date.setText(item.getMonthName() + " " + item.getYear());
        return view;
    }

    @Override
    public View getChildView(int group, int child, boolean b, View view, ViewGroup viewGroup) {
        TextView date, amount, category, subcategory, type;
        final BudgetItem item = (BudgetItem) getChild(group, child);
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
        amount.setText(item.getAmount() + "$");
        category.setText(item.getCategory());
        subcategory.setText(item.getSubcategory());
        type.setText(item.getTypeAsString());

        if (item.isType()) {
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
