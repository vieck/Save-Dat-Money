package edu.purdue.vieck.budgetapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Currency;
import java.util.Stack;

import edu.purdue.vieck.budgetapp.Activities.EditActivity;
import edu.purdue.vieck.budgetapp.CustomObjects.RealmDataItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by vieck on 7/16/15.
 */
public class ChartRecyclerAdapter extends RecyclerView.Adapter<ChartRecyclerAdapter.mViewHolder> {

    Context mContext;
    int month, year, position;
    String currencySymbol;
    RealmHandler mRealmHandler;
    Stack<RealmDataItem> mDataset = new Stack<>();
    SharedPreferences mSharedPreferences;

    public ChartRecyclerAdapter(Context mContext, int month, int year, int position) {
        this.mContext = mContext;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        currencySymbol = mSharedPreferences.getString("currencySymbol",Currency.getInstance(mContext.getResources().getConfiguration().locale).getSymbol());
        this.month = month;
        this.year = year;
        this.position = position;
        mRealmHandler = new RealmHandler(mContext);
        if (month != -1 || year != -1) {
            mDataset = mRealmHandler.getSpecificMonthYearAsStack(month, year, position);
        } else {
            mDataset = mRealmHandler.getAllDataAsStack(position);
        }
        Log.d("RecyclerView Amount",Integer.toString(mDataset.size()));
    }

    public void updatePosition(int position) {
        this.position = position;
        if (month != -1 || year != -1) {
            mDataset = mRealmHandler.getSpecificMonthYearAsStack(month, year, position);
        } else {
            mDataset = mRealmHandler.getAllDataAsStack(position);
        }
        notifyDataSetChanged();
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_chart, viewGroup, false);
        mViewHolder viewHolder = new mViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(mViewHolder viewHolder, final int i) {
        final RealmDataItem realmDataItem = mDataset.get(i);
        viewHolder.date.setText(realmDataItem.getMonth() + "-" + realmDataItem.getDay() + "-" + realmDataItem.getYear());
        viewHolder.category.setText("" + realmDataItem.getCategory());
        viewHolder.subcategory.setText("" + realmDataItem.getSubcategory());
        viewHolder.amount.setText(realmDataItem.getAmount() + " " + currencySymbol);
        if (realmDataItem.getType()) {
            viewHolder.amount.setTextColor(mContext.getResources().getColor(R.color.Lime));
        } else {
            viewHolder.amount.setTextColor(mContext.getResources().getColor(R.color.md_red_A400));
        }
        //viewHolder.income.setText("" + realmDataItem.getCategory());
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Deleted", "Deleted Item");
                Bundle bundle = new Bundle();
                bundle.putInt("Id", realmDataItem.getId());
                bundle.putBoolean("Type", realmDataItem.getType());
                bundle.putString("TypeString", realmDataItem.getTypeString());
                bundle.putString("Category", realmDataItem.getCategory());
                bundle.putString("Subcategory", realmDataItem.getSubcategory());
                bundle.putDouble("Amount", realmDataItem.getAmount());
                bundle.putString("Note", realmDataItem.getNote());
                bundle.putInt("Month", realmDataItem.getMonth());
                bundle.putInt("Day", realmDataItem.getDay());
                bundle.putInt("Year", realmDataItem.getYear());
                bundle.putString("MonthString", realmDataItem.getMonthString());
                bundle.putInt("Image", realmDataItem.getImage());
                Intent intent = new Intent(mContext, EditActivity.class);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                // databaseHandler.delete(realmDataItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class mViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView date, amount, category, subcategory, income;

        public mViewHolder(View v) {
            super(v);
            cardView = (CardView) v.findViewById(R.id.cardview);
            date = (TextView) v.findViewById(R.id.cardview_date);
            amount = (TextView) v.findViewById(R.id.cardview_amount);
            category = (TextView) v.findViewById(R.id.cardview_category);
            subcategory = (TextView) v.findViewById(R.id.cardview_subcategory);
            income = (TextView) v.findViewById(R.id.cardview_budget);
        }
    }
}
