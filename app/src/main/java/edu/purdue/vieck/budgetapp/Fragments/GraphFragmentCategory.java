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
import java.util.List;

import edu.purdue.vieck.budgetapp.Adapters.GraphCategoryAdapter;
import edu.purdue.vieck.budgetapp.CustomObjects.AddTreeItem;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph_category, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = makeAdapter(adapter);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private GraphCategoryAdapter makeAdapter(GraphCategoryAdapter adapter) {
        List<AddTreeItem> list = new ArrayList<>();
        databaseHandler = new DatabaseHandler(getActivity());
        int[] categoryImages = {R.drawable.food_groceries_dark, R.drawable.utility_misc_dark, R.drawable.entertainment_dark, R.drawable.medical_misc_dark, R.drawable.insurance_dark, R.drawable.chart_dark};
        String[] categories = getResources().getStringArray(R.array.categoryarray);
        AddTreeItem item;
        for (int i = 0; i < categories.length; i++) {
            item = new AddTreeItem();
            item.setDrawableId(categoryImages[i]);
            item.setName(categories[i]);
            item.setAmount(databaseHandler.getTotalAmount(true, categories[i]));
            list.add(item);
        }
        adapter = new GraphCategoryAdapter(getActivity(), list);
        return adapter;
    }
}
