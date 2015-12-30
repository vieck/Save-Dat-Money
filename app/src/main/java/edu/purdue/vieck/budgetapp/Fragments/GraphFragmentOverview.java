package edu.purdue.vieck.budgetapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.db.chart.Tools;
import com.db.chart.model.BarSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.BarChartView;
import com.db.chart.view.HorizontalStackBarChartView;

import java.text.DecimalFormat;
import java.util.List;

import edu.purdue.vieck.budgetapp.CustomObjects.BudgetItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by mvieck on 10/7/2015.
 */
public class GraphFragmentOverview extends Fragment {

    BarChartView mBudgetBarView;

    DecimalFormat decimalFormat;
    RealmHandler mRealmHandler;
    private List<BudgetItem> months;
    ImageButton mLeft, mRight;
    TextView mMonthTxt, mYearTxt;
    int type, count;
    String[] categories;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph_overview, container, false);

        Bundle bundle = getArguments();
        type = bundle.getInt("type", 2);
        decimalFormat = new DecimalFormat("0.00");
        categories = getResources().getStringArray(R.array.categoryarray);
        mRealmHandler = new RealmHandler(getActivity());
        mMonthTxt = (TextView) view.findViewById(R.id.label_month);
        mYearTxt = (TextView) view.findViewById(R.id.label_year);

        mBudgetBarView = (BarChartView) view.findViewById(R.id.chart_two);

        if (!mRealmHandler.isEmpty(type)) {
            months = mRealmHandler.getAllUniqueMonthsAsList(type);
            count = months.size() - 1;
            mMonthTxt.setText(months.get(count).getMonthString());
            mYearTxt.setText("" + months.get(count).getYear());
            mLeft = (ImageButton) view.findViewById(R.id.left_arrow);
            mLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (count + 1 < months.size()) {
                        count++;
                        BudgetItem item = months.get(count);
                        mMonthTxt.setText(item.getMonthString());
                        mYearTxt.setText("" + item.getYear());
                        changeAdapterMonth(item.getMonth(), item.getYear());
                    } else {
                        count = 0;
                        BudgetItem item = months.get(count);
                        mMonthTxt.setText(item.getMonthString());
                        mYearTxt.setText("" + item.getYear());
                        changeAdapterMonth(item.getMonth(), item.getYear());
                    }
                }
            });
            mRight = (ImageButton) view.findViewById(R.id.right_arrow);
            mRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (count > 0) {
                        count--;
                        BudgetItem item = months.get(count);
                        mMonthTxt.setText(item.getMonthString());
                        mYearTxt.setText("" + item.getYear());
                        changeAdapterMonth(item.getMonth(), item.getYear());
                    } else {
                        count = months.size() - 1;
                        BudgetItem item = months.get(count);
                        mMonthTxt.setText(item.getMonthString());
                        mYearTxt.setText("" + item.getYear());
                        changeAdapterMonth(item.getMonth(), item.getYear());
                    }
                }
            });
            BudgetItem item = months.get(count);

            changeAdapterMonth(item.getMonth(), item.getYear());
        } else {
            mMonthTxt.setText("No Data");
            mYearTxt.setText("");
        }
        return view;
    }

    private void changeAdapterMonth(int month, int year) {
        //updateCategoryChart(month, year);
        //updateStackChart(month, year);
    }


    private void updateCategoryChart(int month, int year) {
        float expenseTotal = mRealmHandler.getSpecificDateAmount(month, year, 0);
        float incomeTotal = mRealmHandler.getSpecificDateAmount(month, year, 1);
        float[] income = new float[categories.length];
        for (int i = 0; i < categories.length; i++) {
            income[i] = mRealmHandler.getSpecificDateAmountByType(categories[i], month, year, type);
        }
        float[] expense = new float[categories.length];
        for (int i = 0; i < categories.length; i++){
            expense[i] = mRealmHandler.getSpecificDateAmountByType(categories[i], month, year, type);
        }

        if (expenseTotal > incomeTotal) {
            //mCategoryBarView.setAxisBorderValues(0, Math.round(expenseTotal));
        } else {
          // mCategoryBarView.setAxisBorderValues(0, Math.round(incomeTotal));
        }
        mCategoryBarView.updateValues(1, income);
        mCategoryBarView.updateValues(0, expense);
        //mCategoryBarView.notifyDataUpdate();
    }

    private void updateBudgetChart(int month, int year) {

    }

    private void updateStackChart(int month, int year) {
        float total = mRealmHandler.getSpecificDateAmount(month,year,2);

        float[] income = {mRealmHandler.getSpecificDateAmount(month, year, 1)};
        float[] expense = {-mRealmHandler.getSpecificDateAmount(month, year, 0)};

        mStackBarChartView.setAxisBorderValues(-Math.round(total), -Math.round(total));
        mStackBarChartView.updateValues(0, expense);
        mStackBarChartView.updateValues(1, income);
        //mStackBarChartView.notifyDataUpdate();
    }

}
