package edu.purdue.vieck.budgetapp.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import edu.purdue.vieck.budgetapp.CustomObjects.AddTree;
import edu.purdue.vieck.budgetapp.CustomObjects.RealmCategoryItem;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by mvieck on 9/27/2015.
 */
public class AddAdapter extends BaseAdapter {

    Context context;
    List<RealmCategoryItem> categoryItems;
    Bundle bundle;
    int selection;
    int actionBarColor, primaryColor, primaryColorDark, accentColor;

    public AddAdapter(Context context, List<RealmCategoryItem> categoryItems, Bundle bundle, int actionBarColor, int primaryColor, int primaryColorDark, int accentColor) {
        this.context = context;
        this.categoryItems = categoryItems;
        this.bundle = bundle;
        selection = -1;
        this.primaryColor = primaryColor;
        this.primaryColorDark = primaryColorDark;
        this.accentColor = accentColor;
        this.actionBarColor = actionBarColor;
    }

    public void setSelection(int selection) {
        this.selection = selection;
    }

    public int getSelection() {
        return selection;
    }

    public void updateSelection(int selection) {
        this.selection = selection;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return categoryItems.size();
    }

    public List<RealmCategoryItem> getCategoryItems() {
        return categoryItems;
    }

    @Override
    public Object getItem(int position) {
        return categoryItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_add, parent, false);
        RelativeLayout linearLayout = (RelativeLayout) view.findViewById(R.id.linear_layout);
        linearLayout.setBackgroundColor(primaryColorDark);
        if (position == selection) {
            linearLayout.setBackgroundColor(actionBarColor);
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        TextView textView = (TextView) view.findViewById(R.id.textview);
        final RealmCategoryItem addItem = categoryItems.get(position);
        if (addItem.isChild()) {
            textView.setText(addItem.getSubcategory());
        } else {
            textView.setText(addItem.getCategory());
        }
        imageView.setImageDrawable(ContextCompat.getDrawable(context, addItem.getIcon()));
        return view;
    }

}
