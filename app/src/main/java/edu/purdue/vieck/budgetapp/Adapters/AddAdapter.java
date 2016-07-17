package edu.purdue.vieck.budgetapp.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import edu.purdue.vieck.budgetapp.CustomObjects.RealmCategoryItem;
import edu.purdue.vieck.budgetapp.R;
import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by mvieck on 9/27/2015.
 */
public class AddAdapter extends RealmRecyclerViewAdapter<RealmCategoryItem, AddAdapter.MyViewHolder>{

    int selectedItem = 4;
    int default_color;
    public AddAdapter(Context context, OrderedRealmCollection<RealmCategoryItem> data, int default_color) {
        super(context, data, true);
        this.default_color = default_color;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_add, parent, false);
        return new MyViewHolder(itemView);
    }

    public int getSelectedItem() {
        return selectedItem;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.itemView.setSelected(selectedItem == position);
        if (holder.itemView.isSelected()) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.Gold));
        } else {
            holder.itemView.setBackgroundColor(default_color);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyItemChanged(selectedItem);
                selectedItem = holder.getAdapterPosition();
                view.setBackgroundColor(default_color);
                notifyItemChanged(selectedItem);
            }
        });
        RealmCategoryItem category = getData().get(position);
        if (category.isChild()) {
            holder.textView.setText(category.getSubcategory());
        } else {
            holder.textView.setText(category.getCategory());
        }
        holder.imageView.setImageDrawable(ContextCompat.getDrawable(context, category.getIcon()));
    }



    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageview);
            textView = (TextView) view.findViewById(R.id.textview);
        }
    }
}
