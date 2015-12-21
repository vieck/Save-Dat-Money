package edu.purdue.vieck.budgetapp.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;

import edu.purdue.vieck.budgetapp.Adapters.ExpandableListViewAdapter;
import edu.purdue.vieck.budgetapp.DatabaseHandler;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by mvieck on 9/22/2015.
 */
public class SummaryFragment extends Fragment {
    private ExpandableListView mExpandableListView;
    private ExpandableListViewAdapter mDataAdapter;
    DatabaseHandler mDatabaseHandler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);
        mExpandableListView = (ExpandableListView) view.findViewById(R.id.data_list);
        //mExpandableListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDataAdapter = new ExpandableListViewAdapter(getActivity(), "");
        mExpandableListView.setAdapter(mDataAdapter);
        mDatabaseHandler = new DatabaseHandler(getActivity());
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void filterQuery(String filter) {
        mDataAdapter = new ExpandableListViewAdapter(getActivity(), filter);
        mExpandableListView.setAdapter(mDataAdapter);
        Log.d("SummaryFragment", "Filter " + filter);
    }
}
