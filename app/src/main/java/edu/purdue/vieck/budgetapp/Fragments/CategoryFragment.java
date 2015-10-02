package edu.purdue.vieck.budgetapp.Fragments;

import android.app.Fragment;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        CategoryTree tree = new CategoryTree(new CategoryItem(getActivity().getDrawable(R.drawable.calculator), "root", categoryNames[0]));
        CategoryTree.Node node = new CategoryTree.Node(new CategoryItem(drawables.getDrawable(0), categoryNames[0], categoryNames[1]));
        // Grocery Sub List
        for (int i = 0; i < subCategoryFood.length; i++) {
            node.addChild(new CategoryTree.Node(new CategoryItem(drawables.getDrawable(0), subCategoryFood[i], categoryNames[0])));
        }
        tree.getRoot().addChild(node);
        node = new CategoryTree.Node(new CategoryItem(drawables.getDrawable(0), categoryNames[1], categoryNames[1]));
        // Utility Sub List
        for (int i = 0; i < subCategoryUtility.length; i++) {
            node.addChild(new CategoryTree.Node(new CategoryItem(drawables.getDrawable(0), subCategoryUtility[i], categoryNames[2])));
        }
        tree.getRoot().addChild(node);
        node = new CategoryTree.Node(new CategoryItem(drawables.getDrawable(0), categoryNames[2], categoryNames[2]));
        // Entertainment Sub List
        for (int i = 0; i < subCategoryEntertainment.length; i++) {
            node.addChild(new CategoryTree.Node(new CategoryItem(drawables.getDrawable(0), subCategoryEntertainment[i], categoryNames[2])));
        }
        tree.getRoot().addChild(node);
        node = new CategoryTree.Node(new CategoryItem(drawables.getDrawable(0), categoryNames[3], categoryNames[3]));
        // Medical Sub List
        for (int i = 0; i < subCategoryMedical.length; i++) {
            node.getChildNodes().add(new CategoryTree.Node(new CategoryItem(drawables.getDrawable(0), subCategoryMedical[i], categoryNames[3])));
        }
        tree.getRoot().addChild(node);
        node = new CategoryTree.Node(new CategoryItem(drawables.getDrawable(0), categoryNames[4], categoryNames[4]));
        // Insurance Sub List
        for (int i = 0; i < subCategoryInsurance.length; i++) {
            node.getChildNodes().add(new CategoryTree.Node(new CategoryItem(drawables.getDrawable(0), subCategoryInsurance[i], categoryNames[4])));
        }
        tree.getRoot().addChild(node);

        categoryAdapter = new CategoryAdapter(getActivity(), tree);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(categoryAdapter);

        return view;
    }
}
