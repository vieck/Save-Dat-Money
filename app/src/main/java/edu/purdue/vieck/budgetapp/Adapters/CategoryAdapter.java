package edu.purdue.vieck.budgetapp.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.purdue.vieck.budgetapp.CustomObjects.BudgetItem;
import edu.purdue.vieck.budgetapp.CustomObjects.CategoryItem;
import edu.purdue.vieck.budgetapp.CustomObjects.CategoryTree;
import edu.purdue.vieck.budgetapp.Fragments.AddFragment;
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
    public void onBindViewHolder(mViewHolder holder, final int position) {
        final CategoryItem categoryItem = categoryNodes.get(position).getItem();
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("OnCardViewClick", "Clicked");
                if (!categoryNodes.get(position).isLeafNode()) {
                    categoryNodes = categoryNodes.get(position).getChildNodes();
                    notifyDataSetChanged();
                } else {
                    CategoryItem item = categoryNodes.get(position).getItem();

                    FragmentActivity fragmentActivity = ((FragmentActivity) context);
                    Bundle bundle = new Bundle();
                    bundle.putString("Category",item.getType());
                    bundle.putString("Subcategory",item.getSubType());
                   // bundle.putString("Icon");
                    AddFragment addFragment = new AddFragment();
                    addFragment.setArguments(bundle);
                    fragmentActivity.getFragmentManager().beginTransaction().replace(R.id.fragment_container , addFragment).commit();
                }
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
