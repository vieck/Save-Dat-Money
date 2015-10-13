package edu.purdue.vieck.budgetapp.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.purdue.vieck.budgetapp.Activities.ChartActivity;
import edu.purdue.vieck.budgetapp.Activities.DataActivity;
import edu.purdue.vieck.budgetapp.Activities.GraphActivity;
import edu.purdue.vieck.budgetapp.Adapters.DashboardAdapter;
import edu.purdue.vieck.budgetapp.CustomObjects.DashboardItem;
import edu.purdue.vieck.budgetapp.DatabaseHandler;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by mvieck on 9/25/2015.
 */
public class DashboardFragment extends Fragment {

    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private RecyclerView recyclerView;
    private DashboardAdapter dashboardAdapter;
    private DatabaseHandler databaseHandler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.dashboard_recyclerview);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        List<DashboardItem> cards = new ArrayList<>();
        // <div>Icon made by <a href="http://www.simpleicon.com" title="SimpleIcon">SimpleIcon</a> from <a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a> is licensed under <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0">CC BY 3.0</a></div>
        cards.add(new DashboardItem(getActivity().getDrawable(R.drawable.chart_dark), "Charts", getResources().getColor(R.color.Silver), new Intent(getActivity(), ChartActivity.class)));
        cards.add(new DashboardItem(getActivity().getDrawable(R.drawable.chart_panel_dark), "Graphs", getResources().getColor(R.color.Silver), new Intent(getActivity(), GraphActivity.class)));
        cards.add(new DashboardItem(getActivity().getDrawable(R.drawable.currency_dark), "Expenses", getResources().getColor(R.color.Silver), new Intent(getActivity(), DataActivity.class)));
        //cards.add(new DashboardItem(getActivity().getDrawable(R.drawable.currency_symbol), "Currency", getResources().getColor(R.color.Lime), new Intent(getActivity(), ChartActivity.class)));
        //cards.add(new DashboardItem(getActivity().getDrawable(R.drawable.hand_coin), "Cash Flow", getResources().getColor(R.color.Gold), new Intent(getActivity(), ChartActivity.class)));
        //cards.add(new DashboardItem(getActivity().getDrawable(R.drawable.calculator), "Calculator", getResources().getColor(R.color.BlueViolet), new Intent(getActivity(), ChartActivity.class)));
        //cards.add(new DashboardItem(getActivity().getDrawable(R.drawable.graph), "Charts", getResources().getColor(R.color.Black), new Intent(getActivity(), ChartActivity.class)));
        //cards.add(new DashboardItem(getActivity().getDrawable(R.drawable.exit), "Existential", getResources().getColor(R.color.PaleGreen), new Intent(getActivity(), DataActivity.class)));


        dashboardAdapter = new DashboardAdapter(getActivity(), getActivity(), cards);
        recyclerView.setAdapter(dashboardAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return view;
    }
}
