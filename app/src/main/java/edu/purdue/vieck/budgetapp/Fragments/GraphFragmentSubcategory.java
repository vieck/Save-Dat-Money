package edu.purdue.vieck.budgetapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.purdue.vieck.budgetapp.Adapters.GraphCategoryAdapter;
import edu.purdue.vieck.budgetapp.CustomObjects.AddTreeItem;
import edu.purdue.vieck.budgetapp.CustomObjects.BudgetItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.ParseHandler;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by mvieck on 10/7/2015.
 */
public class GraphFragmentSubcategory extends Fragment {
    private GridLayoutManager layoutManager;
    private RealmHandler mRealmHandler;
    private LinkedList<BudgetItem> months;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph_subcategory, container, false);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        mRealmHandler = new RealmHandler(getActivity());
        return view;
    }

    private GraphCategoryAdapter makeAdapter(GraphCategoryAdapter adapter, String subCategory) {
        List<AddTreeItem> list = new ArrayList<>();
        int[] categoryImages = {R.drawable.food_groceries_dark, R.drawable.utility_misc_dark, R.drawable.entertainment_dark, R.drawable.medical_misc_dark, R.drawable.insurance_dark, R.drawable.chart_dark};
        String[] subcategories = getResources().getStringArray(R.array.categoryarray);
        AddTreeItem item;
        for (int i = 0; i < subcategories.length; i++) {
            item = new AddTreeItem();
            item.setDrawableId(categoryImages[i]);
            item.setName(subcategories[i]);
            item.setAmount(mRealmHandler.getSpecificDateAmountByType(subcategories[i], months.get(0).getMonth(), months.get(0).getYear(), 0));
            list.add(item);
        }
        adapter = new GraphCategoryAdapter(getActivity(), list, months.get(0).getMonth(), months.get(0).getYear(), 0);
        return adapter;
    }
}
