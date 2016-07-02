package edu.purdue.vieck.budgetapp.Fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import edu.purdue.vieck.budgetapp.Activities.AddActivity;
import edu.purdue.vieck.budgetapp.Adapters.AddAdapter;
import edu.purdue.vieck.budgetapp.CustomObjects.AddTree;
import edu.purdue.vieck.budgetapp.CustomObjects.RealmCategoryItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.R;
import io.realm.RealmResults;

/**
 * Created by mvieck on 9/27/2015.
 */
public class AddSubCategoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private AddAdapter addAdapter;
    private FloatingActionButton floatingActionButtonFoward, floatingActionButtonBackwards;
    private TypedValue primaryDarkColor;
    private int mActionBarColor;
    private RealmHandler mRealmHandler;
    private SharedPreferences mSharedPreferences;
    private Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_subcategory, container, false);
        bundle = getArguments();
        int position = bundle.getInt("Position");
        String category = bundle.getString("Category");
        bundle.remove("Position");
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        floatingActionButtonBackwards = (FloatingActionButton) getActivity().findViewById(R.id.fab_back);
        floatingActionButtonFoward = (FloatingActionButton) getActivity().findViewById(R.id.fab_next);
        layoutManager = new LinearLayoutManager(getActivity());
        mRealmHandler = new RealmHandler(getActivity());
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mActionBarColor = mSharedPreferences.getInt("actionBarColor", getResources().getColor(R.color.md_black_1000));
        primaryDarkColor = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.colorPrimaryDark, primaryDarkColor, true);

        final RealmResults<RealmCategoryItem> tree = createTree(category);
        addAdapter = new AddAdapter(getActivity(), tree, ContextCompat.getColor(getActivity(), R.color.md_deep_purple_200));

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(addAdapter);

        floatingActionButtonFoward.setBackgroundTintList(new ColorStateList(new int[][]{new int[]{0}}, new int[]{mActionBarColor}));
        floatingActionButtonBackwards.setBackgroundTintList(new ColorStateList(new int[][]{new int[]{0}}, new int[]{mActionBarColor}));
        floatingActionButtonBackwards.setVisibility(View.VISIBLE);

        floatingActionButtonBackwards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatingActionButtonBackwards.setVisibility(View.GONE);
                AddCategoryFragment addCategoryFragment = new AddCategoryFragment();
                addCategoryFragment.setArguments(bundle);
                getActivity().getFragmentManager().beginTransaction().replace(R.id.fragment_container, addCategoryFragment).commit();
            }
        });

        floatingActionButtonFoward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = 0;
                    RealmCategoryItem item = tree.get(position);
                    bundle.putString("Category", item.getCategory());
                    bundle.putString("Subcategory", item.getSubcategory());
                    floatingActionButtonFoward.setVisibility(View.GONE);
                    floatingActionButtonBackwards.setVisibility(View.GONE);
                    if (getActivity() instanceof AddActivity) {
                        AddDataFragment addDataFragment = new AddDataFragment();
                        addDataFragment.setArguments(bundle);
                        getActivity().getFragmentManager().beginTransaction().replace(R.id.fragment_container, addDataFragment).commit();
                    } else {
                        EditDataFragment editDataFragment = new EditDataFragment();
                        editDataFragment.setArguments(bundle);
                        getActivity().getFragmentManager().beginTransaction().replace(R.id.fragment_container, editDataFragment).commit();
                    }
            }
        });

        return view;
    }

    private RealmResults<RealmCategoryItem> createTree(String category) {

        AddTree tree = new AddTree(null);

        return mRealmHandler.getCategoryChildren(category);
    }
}
