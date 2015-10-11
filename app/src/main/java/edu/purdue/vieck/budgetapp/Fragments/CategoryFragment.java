package edu.purdue.vieck.budgetapp.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.purdue.vieck.budgetapp.Adapters.CategoryAdapter;
import edu.purdue.vieck.budgetapp.CustomObjects.AddItem;
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
        int[] drawables = {R.drawable.groceries, R.drawable.utilities, R.drawable.entertainment_main, R.drawable.medical_insurance, R.drawable.insurance, R.drawable.graph};
        int[] groceryDrawables = {R.drawable.groceries, R.drawable.groceries, R.drawable.groceries};
        int[] utilitiesDrawables = {R.drawable.utilities, R.drawable.industry4, R.drawable.home_phone, R.drawable.cell_bill,
                R.drawable.web, R.drawable.satellite_tv, R.drawable.water, R.drawable.trash, R.drawable.utilities};
        int[] entertainmentDrawables = {R.drawable.theater, R.drawable.vacation, R.drawable.digital_media, R.drawable.digitial_music, R.drawable.ecommerce, R.drawable.entertainment_main};
        int[] medicalDrawables = {R.drawable.medical_visit, R.drawable.emergency_room, R.drawable.medication, R.drawable.medical_misc};
        int[] insuranceDrawables = {R.drawable.home_insurance, R.drawable.car_insurance, R.drawable.medical_insurance, R.drawable.life_insurance};


        CategoryTree tree = new CategoryTree(new AddItem(R.drawable.calculator, "root", categoryNames[0]));
        for (int i = 0; i < drawables.length; i++) {
            switch (i) {
                case 0:
                    CategoryTree.Node node = new CategoryTree.Node(new AddItem(drawables[i], categoryNames[i], categoryNames[i]));
                    // Grocery Sub List
                    for (int j = 0; j < subCategoryFood.length; j++) {
                        node.addChild(new CategoryTree.Node(new AddItem(groceryDrawables[j], subCategoryFood[j], categoryNames[0])));
                    }
                    tree.getRoot().addChild(node);
                    break;
                case 1:
                    node = new CategoryTree.Node(new AddItem(drawables[i], categoryNames[1], categoryNames[i]));

                    // Utility Sub List
                    for (int j = 0; j < subCategoryUtility.length; j++) {
                        node.addChild(new CategoryTree.Node(new AddItem(utilitiesDrawables[j], subCategoryUtility[j], categoryNames[i])));
                    }
                    tree.getRoot().addChild(node);
                    break;
                case 2:
                    node = new CategoryTree.Node(new AddItem(drawables[i], categoryNames[i], categoryNames[i]));

                    // Entertainment Sub List
                    for (int j = 0; j < subCategoryEntertainment.length; j++) {
                        node.addChild(new CategoryTree.Node(new AddItem(entertainmentDrawables[j], subCategoryEntertainment[j], categoryNames[i])));
                    }
                    tree.getRoot().addChild(node);
                    break;
                case 3:
                    node = new CategoryTree.Node(new AddItem(drawables[i], categoryNames[i], categoryNames[i]));

                    // Medical Sub List
                    for (int j = 0; j < subCategoryMedical.length; j++) {
                        node.getChildNodes().add(new CategoryTree.Node(new AddItem(medicalDrawables[i], subCategoryMedical[i], categoryNames[i])));
                    }
                    tree.getRoot().addChild(node);
                    break;
                case 4:
                    node = new CategoryTree.Node(new AddItem(drawables[i], categoryNames[i], categoryNames[i]));

                    // Insurance Sub List
                    for (int j = 0; j < subCategoryInsurance.length; j++) {
                        node.getChildNodes().add(new CategoryTree.Node(new AddItem(insuranceDrawables[j], subCategoryInsurance[j], categoryNames[i])));
                    }
                    tree.getRoot().addChild(node);

            }
        }

        categoryAdapter = new CategoryAdapter(getActivity(), tree);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(categoryAdapter);

        return view;
    }
}
