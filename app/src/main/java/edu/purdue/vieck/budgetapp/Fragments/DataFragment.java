package edu.purdue.vieck.budgetapp.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import edu.purdue.vieck.budgetapp.Adapters.ExpandableListViewAdapter;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by mvieck on 9/22/2015.
 */
public class DataFragment extends Fragment {
    private ExpandableListView mExpandableListView;
    private ExpandableListViewAdapter mDataAdapter;
    private RealmHandler mRealmHandler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data, container, false);
        mExpandableListView = (ExpandableListView) view.findViewById(R.id.data_list);
        //mExpandableListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRealmHandler = new RealmHandler(getActivity());
        mDataAdapter = new ExpandableListViewAdapter(getActivity(), mRealmHandler , "");
        mExpandableListView.setAdapter(mDataAdapter);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void filterQuery(String filter) {
        mDataAdapter = new ExpandableListViewAdapter(getActivity(), mRealmHandler, filter);
        mExpandableListView.setAdapter(mDataAdapter);
        Log.d("DataFragment", "Filter " + filter);
    }
}
