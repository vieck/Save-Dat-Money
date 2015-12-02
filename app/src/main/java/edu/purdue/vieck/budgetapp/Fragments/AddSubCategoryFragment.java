package edu.purdue.vieck.budgetapp.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import edu.purdue.vieck.budgetapp.Adapters.AddAdapter;
import edu.purdue.vieck.budgetapp.CustomObjects.AddItem;
import edu.purdue.vieck.budgetapp.CustomObjects.AddTree;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by mvieck on 9/27/2015.
 */
public class AddSubCategoryFragment extends Fragment {
    private ListView listView;
    private LinearLayoutManager layoutManager;
    private AddAdapter addAdapter;
    private ImageView imageView;
    TypedValue primaryDarkColor;
    TypedValue primaryColor;
    private Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_category, container, false);
        bundle = getArguments();
        int position = bundle.getInt("Position");
        bundle.remove("Position");
        listView = (ListView) view.findViewById(R.id.listview);
        imageView = (ImageView) view.findViewById(R.id.imagebtn_submit);
        layoutManager = new LinearLayoutManager(getActivity());
        primaryColor = new TypedValue();
        primaryDarkColor = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.colorPrimary, primaryColor, true);
        getActivity().getTheme().resolveAttribute(R.attr.colorPrimaryDark, primaryDarkColor, true);

        final AddTree tree = createTree(position);
        addAdapter = new AddAdapter(getActivity(), tree.getChildNodes(), bundle);

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(addAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                int prevPosition = listView.getCheckedItemPosition();
                if (prevPosition != ListView.INVALID_POSITION) {
                    listView.getAdapter().getView(prevPosition, null, listView).setBackgroundColor(primaryColor.data);
                }
                listView.setItemChecked(position, true);
                view.setBackgroundColor(primaryDarkColor.data);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = listView.getCheckedItemPosition();
                if (position != ListView.INVALID_POSITION) {
                    AddItem item = tree.getChildNodes().get(position).getItem();
                    bundle.putString("Category", item.getType());
                    bundle.putString("Subcategory", item.getSubType());
                    AddFragment addFragment = new AddFragment();
                    addFragment.setArguments(bundle);
                    getActivity().getFragmentManager().beginTransaction().replace(R.id.fragment_container, addFragment).commit();
                } else {
                    Snackbar.make(getView(),"No Item Selected", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    private AddTree createTree(int position) {
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


        final AddTree tree = new AddTree(new AddItem(R.drawable.calculator, "root", categoryNames[0]));
        switch (position) {
            case 0:
                // Grocery Sub List
                for (int j = 0; j < subCategoryFood.length; j++) {
                    tree.getRoot().addChild(new AddTree.Node(new AddItem(groceryDrawables[j], subCategoryFood[j], categoryNames[0])));
                }
                break;
            case 1:
                // Utility Sub List
                for (int j = 0; j < subCategoryUtility.length; j++) {
                    tree.getRoot().addChild(new AddTree.Node(new AddItem(utilitiesDrawables[j], subCategoryUtility[j], categoryNames[1])));
                }
                break;
            case 2:
                // Entertainment Sub List
                for (int j = 0; j < subCategoryEntertainment.length; j++) {
                    tree.getRoot().addChild(new AddTree.Node(new AddItem(entertainmentDrawables[j], subCategoryEntertainment[j], categoryNames[2])));
                }
                break;
            case 3:
                // Medical Sub List
                for (int j = 0; j < subCategoryMedical.length; j++) {
                    tree.getRoot().getChildNodes().add(new AddTree.Node(new AddItem(medicalDrawables[j], subCategoryMedical[j], categoryNames[3])));
                }
                break;
            case 4:
                // Insurance Sub List
                for (int j = 0; j < subCategoryInsurance.length; j++) {
                    tree.getRoot().getChildNodes().add(new AddTree.Node(new AddItem(insuranceDrawables[j], subCategoryInsurance[j], categoryNames[4])));
                }
                break;
        }
        return tree;
    }
}
