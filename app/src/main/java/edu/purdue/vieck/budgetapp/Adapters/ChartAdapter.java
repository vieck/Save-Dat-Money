package edu.purdue.vieck.budgetapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Currency;
import java.util.Stack;

import edu.purdue.vieck.budgetapp.Activities.DescriptionActivity;
import edu.purdue.vieck.budgetapp.CustomObjects.BudgetItem;
import edu.purdue.vieck.budgetapp.DatabaseHandler;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by vieck on 7/16/15.
 */
public class ChartAdapter extends RecyclerView.Adapter<ChartAdapter.mViewHolder> {

    Context context;
    int month, year;
    DatabaseHandler databaseHandler;
    Stack<BudgetItem> mDataset = new Stack<>();

    public ChartAdapter(Context context, int month, int year) {
        this.context = context;
        this.month = month;
        this.year = year;
        databaseHandler = new DatabaseHandler(context);
        if (month != -1 || year != -1) {
            mDataset = databaseHandler.getSpecificMonthYearAsStack(month, year);
        } else {
            mDataset = databaseHandler.getAllDataAsStack();
        }
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_chart, viewGroup, false);
        mViewHolder viewHolder = new mViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(mViewHolder viewHolder, final int i) {
        final BudgetItem budgetItem = mDataset.get(i);
        viewHolder.date.setText(budgetItem.getMonth() + "-" + budgetItem.getDay() + "-" + budgetItem.getYear());
        viewHolder.category.setText("" + budgetItem.getCategory());
        viewHolder.subcategory.setText("" + budgetItem.getSubcategory());
        Currency currency = Currency.getInstance(context.getResources().getConfiguration().locale);
        viewHolder.amount.setText(currency.getSymbol() + " " + budgetItem.getAmount());
        if (budgetItem.isType()) {
            viewHolder.amount.setTextColor(context.getResources().getColor(R.color.Lime));
        } else {
            viewHolder.amount.setTextColor(context.getResources().getColor(R.color.Red));
        }
        //viewHolder.income.setText("" + budgetItem.getCategory());
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Deleted", "Deleted Item");
                Bundle bundle = new Bundle();
                if (budgetItem.isType()) {
                    bundle.putString("Type", "Income");
                } else {
                    bundle.putString("Type", "Expense");
                }
                bundle.putString("Category", budgetItem.getCategory());
                bundle.putString("Subcategory", budgetItem.getSubcategory());
                bundle.putDouble("Amount",budgetItem.getAmount());
                bundle.putString("Note", budgetItem.getNote());
                bundle.putInt("Month", budgetItem.getMonth());
                bundle.putInt("Day", budgetItem.getDay());
                bundle.putInt("Year", budgetItem.getYear());
                Intent intent = new Intent(context, DescriptionActivity.class);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
               // databaseHandler.delete(budgetItem);
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
