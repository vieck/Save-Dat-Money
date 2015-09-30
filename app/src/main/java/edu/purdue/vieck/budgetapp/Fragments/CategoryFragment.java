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

        String[] categoryNames = getResources().getStringArray(R.array.categoryarray);
        String[] subCategoryFood = getResources().getStringArray(R.array.subgroceryarray);
        String[] subCategoryUtility = getResources().getStringArray(R.array.subutilityarray);
        String[] subCategoryEntertainment = getResources().getStringArray(R.array.subentertainmentarray);
        String[] subCategoryMedical = getResources().getStringArray(R.array.submedicalarray);
        String[] subCategoryInsurance = getResources().getStringArray(R.array.subinsurancearray);
        TypedArray drawables = getResources().obtainTypedArray(R.array.icons);

        CategoryTree tree = new CategoryTree(new CategoryItem(getActivity().getDrawable(R.drawable.calculator),"root"));
        CategoryTree.Node node = new CategoryTree.Node();
        node.setCategoryItem(new CategoryItem(drawables.getDrawable(0), categoryNames[0]));
        // Grocery Sub List
        for (int i = 0; i < subCategoryFood.length; i++) {
            node.getChildNodes().add(new CategoryItem(drawables.getDrawable(0), subCategoryFood[i]));
        }
        tree.addNode(node);
        // Utility Sub List
        for (int i = 0; i < subCategoryUtility.length; i++) {

        }
        // Entertainment Sub List
        for (int i = 0; i < subCategoryEntertainment.length; i++) {

        }
        // Medical Sub List
        for (int i = 0; i < subCategoryMedical.length; i++) {

        }
        // Insurance Sub List
        for (int i = 0; i < subCategoryInsurance.length; i++) {

        }


        for (int i = 0; i < categoryNames.length; i++) {
            node = new CategoryTree.Node();

            tree.addNode(node);
        }

        categoryAdapter = new CategoryAdapter(getActivity(), tree);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(categoryAdapter);
        return view;
    }
}
