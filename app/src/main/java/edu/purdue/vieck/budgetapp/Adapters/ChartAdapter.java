package edu.purdue.vieck.budgetapp.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Stack;

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
            mDataset = databaseHandler.getSpecificMonthYear(month, year);
        } else {
            mDataset = databaseHandler.getAllData();
        }
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chart, viewGroup, false);
        mViewHolder viewHolder = new mViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(mViewHolder viewHolder, int i) {
        final BudgetItem budgetItem = mDataset.get(i);
        viewHolder.date.setText(budgetItem.getMonth() + "-" + budgetItem.getDay() + "-" + budgetItem.getYear());
        viewHolder.amount.setText("" + budgetItem.getAmount());
        viewHolder.expenses.setText("" + budgetItem.isType());
        viewHolder.income.setText("" + budgetItem.getCategory());
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHandler.delete(budgetItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class mViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView date, amount, expenses, income;

        public mViewHolder(View v) {
            super(v);
            cardView = (CardView) v.findViewById(R.id.cardview);
            date = (TextView) v.findViewById(R.id.cardview_date);
            amount = (TextView) v.findViewById(R.id.cardview_amount);
            expenses = (TextView) v.findViewById(R.id.cardview_expenses);
            income = (TextView) v.findViewById(R.id.cardview_budget);
        }
    }
}
