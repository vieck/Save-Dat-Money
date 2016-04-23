package edu.purdue.vieck.budgetapp.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import edu.purdue.vieck.budgetapp.CustomObjects.RealmCategoryItem;
import edu.purdue.vieck.budgetapp.Fragments.ShowCategoriesFragment;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by vieck on 2/21/16.
 */
public class ShowCategoryAdapter  extends BaseAdapter {

    ShowCategoriesFragment fragment;
    Context context;
    List<RealmCategoryItem> categoryItems;
    Bundle bundle;
    int prevSelection, selection;

    public ShowCategoryAdapter(Context context, List<RealmCategoryItem> categoryItems, Bundle bundle, ShowCategoriesFragment fragment) {
        this.fragment = fragment;
        this.context = context;
        this.categoryItems = categoryItems;
        this.bundle = bundle;
        prevSelection = -1;
        selection = -1;
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

    public void setCategoryItems(List<RealmCategoryItem> categoryItems) {
        this.categoryItems = categoryItems;
        Log.d("CategoryItems",categoryItems.size()+"");
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
        View view = inflater.inflate(R.layout.item_choose_category, parent, false);
        final RadioButton radioButton = (RadioButton) view.findViewById(R.id.radio);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        TextView textView = (TextView) view.findViewById(R.id.textview);
        final RealmCategoryItem addItem = categoryItems.get(position);
        if (!addItem.isChild()) {
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selection = position;
                    fragment.changeCategory(categoryItems.get(selection).getCategory());
                    notifyDataSetChanged();
                }
            });
            if (position == selection) {
                radioButton.setChecked(true);
            }
        } else {
            radioButton.setVisibility(View.INVISIBLE);
        }
        if (addItem.isChild()) {
            textView.setText(addItem.getSubcategory());
        } else {
            textView.setText(addItem.getCategory());
        }
        imageView.setImageDrawable(ContextCompat.getDrawable(context, addItem.getIcon()));
        return view;
    }
}