package edu.purdue.vieck.budgetapp.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import edu.purdue.vieck.budgetapp.Adapters.AddCategoryAdapter;
import edu.purdue.vieck.budgetapp.CustomObjects.RealmCategoryItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.R;

public class EditCategoryFragment extends Fragment {

    private AddCategoryAdapter mAdapter;
    private FloatingActionButton mAddButton;
    private ListView mListView;
    private List<RealmCategoryItem> mCategoryList;
    private RealmHandler mRealmHandler;
    private SharedPreferences mSharedPreferences;
    private int actionBarColor;
    TypedValue primaryColor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_category, container, false);
        mListView = (ListView) view.findViewById(R.id.listview);
        mAddButton = (FloatingActionButton) view.findViewById(R.id.fab_next);

        mRealmHandler = new RealmHandler(getActivity());
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        actionBarColor = mSharedPreferences.getInt("actionBarColor",getResources().getColor(R.color.md_black_1000));
        primaryColor = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.colorPrimaryDark, primaryColor, true);

        mCategoryList = mRealmHandler.getCategoryParents();
        Log.d("Categories",Integer.toString(mCategoryList.size()));
        mAdapter = new AddCategoryAdapter(getActivity(), mRealmHandler, mCategoryList, actionBarColor, primaryColor.data);

        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mAdapter.updateSelection(position);
            }
        });

        setupAddButton();
        return view;
    }

    public void setupAddButton() {
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Show Dialog Popup for Creating Category.", Toast.LENGTH_LONG).show();
            }
        });
    }

}
