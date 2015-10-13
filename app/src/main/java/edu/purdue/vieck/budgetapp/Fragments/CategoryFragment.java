package edu.purdue.vieck.budgetapp.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import edu.purdue.vieck.budgetapp.Adapters.AddAdapter;
import edu.purdue.vieck.budgetapp.CustomObjects.AddItem;
import edu.purdue.vieck.budgetapp.CustomObjects.AddTree;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by mvieck on 9/27/2015.
 */
public class CategoryFragment extends Fragment {
    private ListView listView;
    private LinearLayoutManager layoutManager;
    private AddAdapter addAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        listView = (ListView) view.findViewById(R.id.listview);
        layoutManager = new LinearLayoutManager(getActivity());

        String[] categoryNames = getResources().getStringArray(R.array.categoryarray);
        String[] subCategoryFood = getResources().getStringArray(R.array.subgroceryarray);
        String[] subCategoryUtility = getResources().getStringArray(R.array.subutilityarray);
        String[] subCategoryEntertainment = getResources().getStringArray(R.array.subentertainmentarray);
        String[] subCategoryMedical = getResources().getStringArray(R.array.submedicalarray);
        String[] subCategoryInsurance = getResources().getStringArray(R.array.subinsurancearray);
        int[] drawables = {R.drawable.food_groceries_dark, R.drawable.utility_misc_dark, R.drawable.entertainment_dark, R.drawable.medical_misc_dark, R.drawable.insurance_dark, R.drawable.graph_dark};
        int[] groceryDrawables = {R.drawable.food_groceries_dark, R.drawable.food_groceries_dark, R.drawable.food_groceries_dark};
        int[] utilitiesDrawables = {R.drawable.utility_misc_dark, R.drawable.utility_gas_dark, R.drawable.landline_phone_bill_dark, R.drawable.cell_phone_bill_dark,
                R.drawable.entertainment_web_dark, R.drawable.satellite_bill_dark, R.drawable.utility_water_dark, R.drawable.utility_trash_dark, R.drawable.utility_misc_dark};
        int[] entertainmentDrawables = {R.drawable.entertainment_theater_dark, R.drawable.entertainment_vaction_dark, R.drawable.digital_media_dark, R.drawable.digital_music_dark, R.drawable.ecommerce_dark, R.drawable.entertainment_dark};
        int[] medicalDrawables = {R.drawable.medical_visit_dark, R.drawable.emergence_room_dark, R.drawable.medical_prescription_dark, R.drawable.medical_misc_dark};
        int[] insuranceDrawables = {R.drawable.home_insurance_dark, R.drawable.car_insurance_dark, R.drawable.medical_misc_dark, R.drawable.life_insurance_dark};


        AddTree tree = new AddTree(new AddItem(R.drawable.calculator, "root", categoryNames[0]));
        for (int i = 0; i < drawables.length; i++) {
            switch (i) {
                case 0:
                    AddTree.Node node = new AddTree.Node(new AddItem(drawables[i], categoryNames[i], categoryNames[i]));
                    // Grocery Sub List
                    for (int j = 0; j < subCategoryFood.length; j++) {
                        node.addChild(new AddTree.Node(new AddItem(groceryDrawables[j], subCategoryFood[j], categoryNames[0])));
                    }
                    tree.getRoot().addChild(node);
                    break;
                case 1:
                    node = new AddTree.Node(new AddItem(drawables[i], categoryNames[1], categoryNames[i]));

                    // Utility Sub List
                    for (int j = 0; j < subCategoryUtility.length; j++) {
                        node.addChild(new AddTree.Node(new AddItem(utilitiesDrawables[j], subCategoryUtility[j], categoryNames[i])));
                    }
                    tree.getRoot().addChild(node);
                    break;
                case 2:
                    node = new AddTree.Node(new AddItem(drawables[i], categoryNames[i], categoryNames[i]));

                    // Entertainment Sub List
                    for (int j = 0; j < subCategoryEntertainment.length; j++) {
                        node.addChild(new AddTree.Node(new AddItem(entertainmentDrawables[j], subCategoryEntertainment[j], categoryNames[i])));
                    }
                    tree.getRoot().addChild(node);
                    break;
                case 3:
                    node = new AddTree.Node(new AddItem(drawables[i], categoryNames[i], categoryNames[i]));

                    // Medical Sub List
                    for (int j = 0; j < subCategoryMedical.length; j++) {
                        node.getChildNodes().add(new AddTree.Node(new AddItem(medicalDrawables[j], subCategoryMedical[j], categoryNames[i])));
                    }
                    tree.getRoot().addChild(node);
                    break;
                case 4:
                    node = new AddTree.Node(new AddItem(drawables[i], categoryNames[i], categoryNames[i]));

                    // Insurance Sub List
                    for (int j = 0; j < subCategoryInsurance.length; j++) {
                        node.getChildNodes().add(new AddTree.Node(new AddItem(insuranceDrawables[j], subCategoryInsurance[j], categoryNames[i])));
                    }
                    tree.getRoot().addChild(node);

            }
        }

        addAdapter = new AddAdapter(getActivity(), tree.getChildNodes());

        listView.setAdapter(addAdapter);

        return view;
    }
}
