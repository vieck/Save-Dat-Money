package edu.purdue.vieck.budgetapp.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import edu.purdue.vieck.budgetapp.Adapters.AddAdapter;
import edu.purdue.vieck.budgetapp.CustomObjects.AddTree;
import edu.purdue.vieck.budgetapp.CustomObjects.DividerItemDecoration;
import edu.purdue.vieck.budgetapp.CustomObjects.RealmCategoryItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.R;
import io.realm.RealmResults;

/**
 * Created by mvieck on 9/27/2015.
 */
public class AddCategoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private AddAdapter addAdapter;
    private FloatingActionButton floatingActionButton;
    private TypedValue primaryColorDark;
    private RealmHandler mRealmHandler;
    private SharedPreferences mSharedPreferences;
    private int mActionBarColor;
    private Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_category, container, false);

        view.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;

        if (getArguments() == null) {
            bundle = new Bundle();
        } else {
            bundle = getArguments();
        }
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab_next);
        mRealmHandler = new RealmHandler(getActivity());
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mActionBarColor = mSharedPreferences.getInt("actionBarColor", getResources().getColor(R.color.md_black_1000));
        primaryColorDark = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.colorPrimaryDark, primaryColorDark, true);

        final RealmResults<RealmCategoryItem> parents = createTree();

        addAdapter = new AddAdapter(getActivity(), parents, ContextCompat.getColor(getActivity(), R.color.md_deep_purple_200));

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(addAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        floatingActionButton.setBackgroundTintList(new ColorStateList(new int[][]{new int[]{0}}, new int[]{mActionBarColor}));

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddAdapter addAdapter = (AddAdapter) recyclerView.getAdapter();
                int position = addAdapter.getSelectedItem();
                RealmCategoryItem item = parents.get(position);
                bundle.putInt("Position", position);
                bundle.putString("Category", item.getCategory());
                AddSubCategoryFragment subCategoryFragment = new AddSubCategoryFragment();
                subCategoryFragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, subCategoryFragment).setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).commit();

            }
        });

        return view;
    }

    private RealmResults<RealmCategoryItem> createTree() {

        if (mRealmHandler.isCategoriesEmpty()) {
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
            RealmCategoryItem categoryItem, subCategoryItem;
            for (int i = 0; i < drawables.length; i++) {
                switch (i) {
                    case 0:
                        categoryItem = new RealmCategoryItem(categoryNames[i], categoryNames[0], drawables[i], getResources().getColor(R.color.md_green_300), false);
                        // Grocery Sub List
                        for (int j = 0; j < subCategoryFood.length; j++) {
                            subCategoryItem = new RealmCategoryItem(categoryNames[0], subCategoryFood[j], groceryDrawables[j], getResources().getColor(R.color.md_green_300), true);
                            mRealmHandler.add(subCategoryItem);
                        }
                        mRealmHandler.add(categoryItem);
                        break;
                    case 1:
                        categoryItem = new RealmCategoryItem(categoryNames[1], categoryNames[i], drawables[i], getResources().getColor(R.color.md_blue_300), false);

                        // Utility Sub List
                        for (int j = 0; j < subCategoryUtility.length; j++) {
                            subCategoryItem = new RealmCategoryItem(categoryNames[i], subCategoryUtility[j], utilitiesDrawables[j], getResources().getColor(R.color.md_blue_300), true);
                            mRealmHandler.add(subCategoryItem);
                        }
                        mRealmHandler.add(categoryItem);
                        break;
                    case 2:
                        categoryItem = new RealmCategoryItem(categoryNames[i], categoryNames[i], drawables[i], getResources().getColor(R.color.md_yellow_300), false);

                        // Entertainment Sub List
                        for (int j = 0; j < subCategoryEntertainment.length; j++) {
                            subCategoryItem = new RealmCategoryItem(categoryNames[i], subCategoryEntertainment[j], entertainmentDrawables[j], getResources().getColor(R.color.md_yellow_300), true);
                            mRealmHandler.add(subCategoryItem);
                        }
                        mRealmHandler.add(categoryItem);
                        break;
                    case 3:
                        categoryItem = new RealmCategoryItem(categoryNames[i], categoryNames[i], drawables[i], getResources().getColor(R.color.md_red_300), false);

                        // Medical Sub List
                        for (int j = 0; j < subCategoryMedical.length; j++) {
                            subCategoryItem = new RealmCategoryItem(categoryNames[i], subCategoryMedical[j], medicalDrawables[j], getResources().getColor(R.color.md_red_300), true);
                            mRealmHandler.add(subCategoryItem);
                        }
                        mRealmHandler.add(categoryItem);
                        break;
                    case 4:
                        categoryItem = new RealmCategoryItem(categoryNames[i], categoryNames[i], drawables[i], getResources().getColor(R.color.md_purple_300), false);
                        // Insurance Sub List
                        for (int j = 0; j < subCategoryInsurance.length; j++) {
                            subCategoryItem = new RealmCategoryItem(categoryNames[i], subCategoryInsurance[j], insuranceDrawables[j], getResources().getColor(R.color.md_purple_300), true);
                            mRealmHandler.add(subCategoryItem);
                        }
                        mRealmHandler.add(categoryItem);

                }
            }
        }

        Log.d("Count", "" + mRealmHandler.getCategoryCount());

        return mRealmHandler.getCategoryParents();
    }
}
