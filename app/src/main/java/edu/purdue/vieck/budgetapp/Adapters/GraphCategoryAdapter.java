package edu.purdue.vieck.budgetapp.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import java.util.List;

import edu.purdue.vieck.budgetapp.CustomObjects.AddTreeItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.Fragments.GraphFragmentSubcategory;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by mvieck on 10/7/2015.
 */
public class GraphCategoryAdapter extends RecyclerView.Adapter<GraphCategoryAdapter.mViewHolder> {

    private Context mContext;
    private RealmHandler mRealmHandler;
    private double max;
    private List<AddTreeItem> list;
    private int type;

    public GraphCategoryAdapter(Context mContext, List<AddTreeItem> list, int month, int year, int type) {
        this.mContext = mContext;
        this.list = list;
        this.type = type;
        mRealmHandler = new RealmHandler(mContext);
        max = mRealmHandler.getSpecificDateAmount(month, year, type);
    }

    public void changeMonth(int month, int year) {
        max = mRealmHandler.getSpecificDateAmount(month, year, type);
        String[] categories = mContext.getResources().getStringArray(R.array.categoryarray);
        for (int i = 0; i < list.size(); i++) {
            double amount = mRealmHandler.getSpecificDateAmountByType(categories[i], month, year, type);
            //list.get(i).setAmount(amount);
        }
        notifyDataSetChanged();
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_category, parent, false);
        mViewHolder viewHolder = new mViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(mViewHolder viewHolder, final int position) {
        final AddTreeItem item = list.get(position);
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentActivity fragmentActivity = ((FragmentActivity) mContext);
                Bundle bundle = new Bundle();
                bundle.putString("Category", item.getName());
                GraphFragmentSubcategory fragmentSubcategory = new GraphFragmentSubcategory();
                fragmentSubcategory.setArguments(bundle);
                fragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentSubcategory).commit();

            }
        });
        viewHolder.imageView.setImageDrawable(mContext.getDrawable(item.getDrawableId()));
        viewHolder.labelCategory.setText(item.getName());
        viewHolder.amount.setText("$ " + item.getAmount());
        //viewHolder.progressBar.setMax(max);
        viewHolder.progressBar.setProgress(item.getAmount());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class mViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView labelCategory, labelAmount, amount;
        RoundCornerProgressBar progressBar;
        ImageView imageView;

        public mViewHolder(View v) {
            super(v);
            cardView = (CardView) v.findViewById(R.id.cardview);
            imageView = (ImageView) v.findViewById(R.id.imageview);
            labelCategory = (TextView) v.findViewById(R.id.label_category);
            labelAmount = (TextView) v.findViewById(R.id.label_amount);
            amount = (TextView) v.findViewById(R.id.edittext_amount);
            progressBar = (RoundCornerProgressBar) v.findViewById(R.id.percentage_bar);
        }
    }
}
