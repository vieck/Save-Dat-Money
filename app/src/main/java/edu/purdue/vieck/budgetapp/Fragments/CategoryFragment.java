package edu.purdue.vieck.budgetapp.Fragments;

import android.app.Fragment;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.purdue.vieck.budgetapp.Adapters.CategoryAdapter;
import edu.purdue.vieck.budgetapp.CustomObjects.CategoryItem;
import edu.purdue.vieck.budgetapp.CustomObjects.CategoryTree;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by mvieck on 9/27/2015.
 */
public class CategoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private CategoryAdapter categoryAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.category_recyclerview);
        layoutManager = new LinearLayoutManager(getActivity());

        String[] names = getResources().getStringArray(R.array.categoryarray);
        TypedArray drawables = getResources().obtainTypedArray(R.array.icons);

        CategoryTree tree = new CategoryTree(new CategoryItem(getActivity().getDrawable(R.drawable.calculator),"root"));
        for (int i = 0; i < names.length; i++) {
            CategoryTree.Node node = new CategoryTree.Node();
            node.setCategoryItem(new CategoryItem(drawables.getDrawable(i), names[i]));
            tree.addNode(node);
        }

        categoryAdapter = new CategoryAdapter(getActivity(), tree);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(categoryAdapter);
        return view;
    }
}
