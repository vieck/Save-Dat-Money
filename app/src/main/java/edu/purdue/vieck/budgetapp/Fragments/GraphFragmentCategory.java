package edu.purdue.vieck.budgetapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.purdue.vieck.budgetapp.Adapters.GraphCategoryAdapter;
import edu.purdue.vieck.budgetapp.CustomObjects.AddTreeItem;
import edu.purdue.vieck.budgetapp.CustomObjects.BudgetItem;
import edu.purdue.vieck.budgetapp.DatabaseHandler;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by mvieck on 10/7/2015.
 */
public class GraphFragmentCategory extends Fragment {
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private GraphCategoryAdapter adapter;
    private DatabaseHandler databaseHandler;
    private LinkedList<BudgetItem> months;
    ImageButton left, right;
    TextView monthTxt, yearTxt;
    private int count, type;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph_category, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);

        databaseHandler = new DatabaseHandler(getActivity());

        monthTxt = (TextView) view.findViewById(R.id.label_month);
        yearTxt = (TextView) view.findViewById(R.id.label_year);
        left = (ImageButton) view.findViewById(R.id.left_arrow);
        right = (ImageButton) view.findViewById(R.id.right_arrow);

        Bundle bundle = getArguments();
        type = bundle.getInt("type",2);

        if (!databaseHandler.isEmpty(type)) {
            months = databaseHandler.getAllUniqueMonthsAsLinkedList(type);
            count = months.size() - 1;

            left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Category Position",""+count);
                    if (count < months.size() - 1) {
                        count++;
                        BudgetItem item = months.get(count);
                        adapter.changeMonth(item.getMonth(), item.getYear());
                        monthTxt.setText(item.getMonthName());
                        yearTxt.setText("" + item.getYear());
                    } else {
                        count = 0;
                        BudgetItem item = months.get(count);
                        adapter.changeMonth(item.getMonth(), item.getYear());
                        monthTxt.setText(item.getMonthName());
                        yearTxt.setText("" + item.getYear());
                    }
                }
            });

            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Category Count", "" + count);
                    if (count > 0) {
                        count--;
                        BudgetItem item = months.get(count);
                        adapter.changeMonth(item.getMonth(), item.getYear());
                        monthTxt.setText(item.getMonthName());
                        yearTxt.setText("" + item.getYear());
                    } else {
                        count = months.size() - 1;
                        BudgetItem item = months.get(count);
                        adapter.changeMonth(item.getMonth(), item.getYear());
                        monthTxt.setText(item.getMonthName());
                        yearTxt.setText("" + item.getYear());
                    }
                }
            });
            adapter = makeAdapter(adapter);
            recyclerView.setAdapter(adapter);
        } else {
            monthTxt.setText("No Data");
            yearTxt.setText("");
        }

        return view;
    }

    private GraphCategoryAdapter makeAdapter(GraphCategoryAdapter adapter) {
        List<AddTreeItem> list = new ArrayList<>();
        int[] categoryImages = {R.drawable.food_groceries_dark, R.drawable.utility_misc_dark, R.drawable.entertainment_dark, R.drawable.medical_misc_dark, R.drawable.insurance_dark, R.drawable.chart_dark};
        String[] categories = getResources().getStringArray(R.array.categoryarray);
        monthTxt.setText(months.get(count).getMonthName());
        yearTxt.setText("" + months.get(count).getYear());
        AddTreeItem item;
        for (int i = 0; i < categories.length; i++) {
            item = new AddTreeItem();
            item.setDrawableId(categoryImages[i]);
            item.setName(categories[i]);
            item.setAmount(databaseHandler.getSpecificDateAmountByType(categories[i], months.get(0).getMonth(), months.get(0).getYear(), type));
            list.add(item);
        }
        adapter = new GraphCategoryAdapter(getActivity(), list, months.get(count).getMonth(), months.get(count).getYear(), type);
        return adapter;
    }

    public void updateType(int type) {
        this.type = type;
        if (!databaseHandler.isEmpty(type)) {
            months = databaseHandler.getAllUniqueMonthsAsLinkedList(type);
            count = months.size() - 1;
            monthTxt.setText(months.get(count).getMonthName());
            yearTxt.setText("" + months.get(count).getYear() + "");
            adapter = makeAdapter(adapter);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        } else {
            monthTxt.setText("No Data");
            yearTxt.setText("");
        }
    }
}
