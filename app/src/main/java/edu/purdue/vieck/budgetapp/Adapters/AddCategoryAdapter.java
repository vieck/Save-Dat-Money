package edu.purdue.vieck.budgetapp.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import edu.purdue.vieck.budgetapp.CustomObjects.RealmCategoryItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by vieck on 2/7/16.
 */
public class AddCategoryAdapter extends BaseAdapter {
    Context context;
    RealmHandler mRealmHandler;
    List<RealmCategoryItem> categoryItems;
    int selection;
    int actionBarColor, primaryColor;

    public AddCategoryAdapter(Context context, RealmHandler mRealmHandler, List<RealmCategoryItem> categoryItems, int actionBarColor, int primaryColor) {
        this.context = context;
        this.mRealmHandler = mRealmHandler;
        this.categoryItems = categoryItems;
        selection = -1;
        this.actionBarColor = actionBarColor;
        this.primaryColor = primaryColor;
    }

    @Override
    public int getCount() {
        return categoryItems.size();
    }

    public List<RealmCategoryItem> getCategoryItems() {
        return categoryItems;
    }

    public void updateSelection(int selection) {
        this.selection = selection;
        notifyDataSetChanged();
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
        View view = inflater.inflate(R.layout.item_add_category, parent, false);
        RelativeLayout linearLayout = (RelativeLayout) view.findViewById(R.id.linear_layout);
        if (position == selection) {
            linearLayout.setBackgroundColor(actionBarColor);
        } else {
            linearLayout.setBackgroundColor(primaryColor);
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        TextView textView = (TextView) view.findViewById(R.id.textview);
        ImageButton deleteButton = (ImageButton) view.findViewById(R.id.delete_button);
        final RealmCategoryItem categoryItem = categoryItems.get(position);
        if (categoryItem.isChild()) {
            textView.setText(categoryItem.getSubcategory());
        } else {
            textView.setText(categoryItem.getCategory());
        }
        imageView.setImageDrawable(ContextCompat.getDrawable(context, categoryItem.getIcon()));
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRealmHandler.delete(categoryItem);
                categoryItems = mRealmHandler.getCategoryParents();
                notifyDataSetChanged();
            }
        });
        return view;
    }
}
