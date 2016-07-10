package edu.purdue.vieck.budgetapp.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
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

import edu.purdue.vieck.budgetapp.Adapters.AddAdapter;
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
    private FloatingActionButton fabFoward, fabBackward;
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
        fabFoward = (FloatingActionButton) getActivity().findViewById(R.id.fab_next);
        fabFoward.setVisibility(View.VISIBLE);
        fabBackward = (FloatingActionButton) getActivity().findViewById(R.id.fab_back);
        fabBackward.setVisibility(View.GONE);
        mRealmHandler = new RealmHandler(getActivity());
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mActionBarColor = mSharedPreferences.getInt("actionBarColor", getResources().getColor(R.color.md_black_1000));
        primaryColorDark = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.colorPrimaryDark, primaryColorDark, true);

        final RealmResults<RealmCategoryItem> parents = mRealmHandler.getCategoryParents();

        addAdapter = new AddAdapter(getActivity(), parents, ContextCompat.getColor(getActivity(), R.color.md_deep_purple_200));

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(addAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        fabFoward.setBackgroundTintList(new ColorStateList(new int[][]{new int[]{0}}, new int[]{mActionBarColor}));

        fabFoward.setOnClickListener(new View.OnClickListener() {
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
}
