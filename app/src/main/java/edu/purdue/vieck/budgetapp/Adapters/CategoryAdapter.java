package edu.purdue.vieck.budgetapp.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.TreeMap;

import edu.purdue.vieck.budgetapp.CustomObjects.CategoryItem;
import edu.purdue.vieck.budgetapp.CustomObjects.CategoryTree;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by mvieck on 9/27/2015.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.mViewHolder> {

    Context context;
    ArrayList<CategoryTree.Node> categoryNodes;

    public CategoryAdapter(Context context, CategoryTree categoryTree) {
        this.context = context;
        this.categoryNodes = categoryTree.getChildNodes();
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        mViewHolder viewHolder = new mViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(mViewHolder holder, int position) {
        final CategoryItem categoryItem = categoryNodes.get(position).getCategoryItem();
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                notifyDataSetChanged();
            }
        });
        holder.name.setText(categoryItem.getType());
        holder.icon.setImageDrawable(categoryItem.getIcon());
    }

    @Override
    public int getItemCount() {
        return categoryNodes.size();
    }

    public class mViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView icon;
        TextView name;

        public mViewHolder(View v) {
            super(v);
            cardView = (CardView) v.findViewById(R.id.cardview);
            icon = (ImageView) v.findViewById(R.id.imageview_category_item);
            name = (TextView) v.findViewById(R.id.textview_category_item);
        }
    }
}
