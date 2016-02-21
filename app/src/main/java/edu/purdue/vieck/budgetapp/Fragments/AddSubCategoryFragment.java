package edu.purdue.vieck.budgetapp.Fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
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

/**
 * Created by mvieck on 9/27/2015.
 */
public class AddSubCategoryFragment extends Fragment {
    private ListView listView;
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
        listView = (ListView) view.findViewById(R.id.listview);
        floatingActionButtonBackwards = (FloatingActionButton) view.findViewById(R.id.fab_back);
        floatingActionButtonFoward = (FloatingActionButton) view.findViewById(R.id.fab_next);
        layoutManager = new LinearLayoutManager(getActivity());
        mRealmHandler = new RealmHandler(getActivity());
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mActionBarColor = mSharedPreferences.getInt("actionBarColor", getResources().getColor(R.color.md_black_1000));
        primaryDarkColor = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.colorPrimaryDark, primaryDarkColor, true);

        final List<RealmCategoryItem> tree = createTree(category);
        addAdapter = new AddAdapter(getActivity(), tree, bundle, mActionBarColor, primaryDarkColor.data);

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(addAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                int prevPosition = listView.getCheckedItemPosition();
                addAdapter.updateSelection(position);
            }
        });

        floatingActionButtonFoward.setBackgroundTintList(new ColorStateList(new int[][]{new int[]{0}}, new int[]{mActionBarColor}));
        floatingActionButtonBackwards.setBackgroundTintList(new ColorStateList(new int[][]{new int[]{0}}, new int[]{mActionBarColor}));

        floatingActionButtonBackwards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCategoryFragment addCategoryFragment = new AddCategoryFragment();
                addCategoryFragment.setArguments(bundle);
                getActivity().getFragmentManager().beginTransaction().replace(R.id.fragment_container, addCategoryFragment).commit();
            }
        });

        floatingActionButtonFoward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = listView.getCheckedItemPosition();
                if (position != ListView.INVALID_POSITION) {
                    RealmCategoryItem item = tree.get(position);
                    bundle.putString("Category", item.getCategory());
                    bundle.putString("Subcategory", item.getSubcategory());
                    if (getActivity() instanceof AddActivity) {
                        AddDataFragment addDataFragment = new AddDataFragment();
                        addDataFragment.setArguments(bundle);
                        getActivity().getFragmentManager().beginTransaction().replace(R.id.fragment_container, addDataFragment).commit();
                    } else {
                        EditDataFragment editDataFragment = new EditDataFragment();
                        editDataFragment.setArguments(bundle);
                        getActivity().getFragmentManager().beginTransaction().replace(R.id.fragment_container, editDataFragment).commit();
                    }
                } else {
                    Snackbar.make(getView(), "No Item Selected", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    private List<RealmCategoryItem> createTree(String category) {

        AddTree tree = new AddTree(null);

        List<RealmCategoryItem> categoryChildren = mRealmHandler.getCategoryChildren(category);

        return categoryChildren;
    }
}
