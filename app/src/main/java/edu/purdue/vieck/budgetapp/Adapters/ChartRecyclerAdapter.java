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

import edu.purdue.vieck.budgetapp.Activities.EditActivity;
import edu.purdue.vieck.budgetapp.CustomObjects.RealmDataItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.R;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

/**
 * Created by vieck on 7/16/15.
 */
public class ChartRecyclerAdapter extends RealmRecyclerViewAdapter<RealmDataItem, ChartRecyclerAdapter.mViewHolder> {

    private String currencySymbol;
    private SharedPreferences mSharedPreferences;

    public ChartRecyclerAdapter(Context context, RealmResults<RealmDataItem> data) {

        super(context, data, true);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        currencySymbol = mSharedPreferences.getString("currencySymbol", Currency.getInstance(context.getResources().getConfiguration().locale).getSymbol());
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_chart, viewGroup, false);
        return new mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(mViewHolder holder, final int i) {
        final RealmDataItem item = getItem(i);
        String date = item.getMonth() + "-" + item.getDay() + "-" + item.getYear();
        String category = item.getCategory();
        String subcategory = item.getSubcategory();
        String currency = item.getAmount() + " " + currencySymbol;

        holder.date.setText(date);
        holder.category.setText(category);
        holder.subcategory.setText(subcategory);
        holder.amount.setText(currency);
        if (item.getType()) {
            holder.amount.setTextColor(context.getResources().getColor(R.color.Lime));
        } else {
            holder.amount.setTextColor(context.getResources().getColor(R.color.md_red_A400));
        }
        //viewHolder.income.setText("" + realmDataItem.getCategory());
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    class mViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        TextView date, amount, category, subcategory, income;

        private mViewHolder(View v) {
            super(v);
            cardView = (CardView) v.findViewById(R.id.cardview);
            date = (TextView) v.findViewById(R.id.cardview_date);
            amount = (TextView) v.findViewById(R.id.cardview_amount);
            category = (TextView) v.findViewById(R.id.cardview_category);
            subcategory = (TextView) v.findViewById(R.id.cardview_subcategory);
            income = (TextView) v.findViewById(R.id.cardview_budget);
        }

        @Override
        public void onClick(View view) {
            final RealmDataItem item = getItem(getAdapterPosition());

            Log.d("Deleted", "Deleted Item");
            Bundle bundle = new Bundle();
            bundle.putInt("Id", item.getId());
            bundle.putBoolean("Type", item.getType());
            bundle.putString("TypeString", item.getTypeString());
            bundle.putString("Category", item.getCategory());
            bundle.putString("Subcategory", item.getSubcategory());
            bundle.putDouble("Amount", item.getAmount());
            bundle.putString("Note", item.getNote());
            bundle.putInt("Month", item.getMonth());
            bundle.putInt("Day", item.getDay());
            bundle.putInt("Year", item.getYear());
            bundle.putString("MonthString", item.getMonthString());
            bundle.putInt("Image", item.getImage());
            Intent intent = new Intent(context, EditActivity.class);
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            // databaseHandler.delete(realmDataItem);
        }
    }
}
