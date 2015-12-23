package edu.purdue.vieck.budgetapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;

import com.parse.Parse;
import com.parse.ParseInstallation;

import java.util.ArrayList;
import java.util.List;

import edu.purdue.vieck.budgetapp.Adapters.DashboardAdapter;
import edu.purdue.vieck.budgetapp.CustomObjects.DashboardItem;
import edu.purdue.vieck.budgetapp.DatabaseHandler;
import edu.purdue.vieck.budgetapp.R;

public class DashboardActivity extends Activity {

    private Toolbar mToolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private RecyclerView mRecyclerView;
    private DashboardAdapter dashboardAdapter;
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mRecyclerView = (RecyclerView) findViewById(R.id.dashboard_recyclerview);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        List<DashboardItem> cards = new ArrayList<>();
        // <div>Icon made by <a href="http://www.simpleicon.com" title="SimpleIcon">SimpleIcon</a> from <a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a> is licensed under <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0">CC BY 3.0</a></div>
        cards.add(new DashboardItem(getDrawable(R.drawable.chart_dark), "Charts", getResources().getColor(R.color.md_light_blue_A100), new Intent(this, ChartActivity.class)));
        cards.add(new DashboardItem(getDrawable(R.drawable.chart_panel_dark), "Graphs", getResources().getColor(R.color.md_purple_500), new Intent(this, GraphActivity.class)));
        cards.add(new DashboardItem(getDrawable(R.drawable.insurance_dark), "Data", getResources().getColor(R.color.md_green_A400), new Intent(this, SummaryActivity.class)));
        cards.add(new DashboardItem(getDrawable(R.drawable.ic_action_settings), "Settings", getResources().getColor(R.color.md_amber_500), new Intent(this, SettingsActivity.class)));
        //cards.add(new DashboardItem(getActivity().getDrawable(R.drawable.currency_symbol), "Currency", getResources().getColor(R.color.Lime), new Intent(getActivity(), ChartActivity.class)));
        //cards.add(new DashboardItem(getActivity().getDrawable(R.drawable.hand_coin), "Cash Flow", getResources().getColor(R.color.Gold), new Intent(getActivity(), ChartActivity.class)));
        //cards.add(new DashboardItem(getActivity().getDrawable(R.drawable.calculator), "Calculator", getResources().getColor(R.color.BlueViolet), new Intent(getActivity(), ChartActivity.class)));
        //cards.add(new DashboardItem(getActivity().getDrawable(R.drawable.graph), "Charts", getResources().getColor(R.color.Black), new Intent(getActivity(), ChartActivity.class)));
        //cards.add(new DashboardItem(getActivity().getDrawable(R.drawable.exit), "Existential", getResources().getColor(R.color.PaleGreen), new Intent(getActivity(), SummaryActivity.class)));

        dashboardAdapter = new DashboardAdapter(this, this, cards);
        mRecyclerView.setAdapter(dashboardAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

}
