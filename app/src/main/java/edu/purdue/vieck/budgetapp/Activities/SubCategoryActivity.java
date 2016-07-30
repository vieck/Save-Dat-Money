package edu.purdue.vieck.budgetapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import edu.purdue.vieck.budgetapp.Adapters.AddAdapter;
import edu.purdue.vieck.budgetapp.CustomObjects.DividerItemDecoration;
import edu.purdue.vieck.budgetapp.CustomObjects.RealmCategoryItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.R;
import edu.purdue.vieck.budgetapp.databinding.ActivitySubCategoryBinding;
import io.realm.RealmResults;

public class SubCategoryActivity extends AppCompatActivity {

    ActivitySubCategoryBinding binding;
    AddAdapter addAdapter;
    RealmHandler realmHandler;
    SharedPreferences sharedPreferences;
    Bundle bundle;
    int actionBarColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sub_category);
        realmHandler = new RealmHandler(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        actionBarColor = sharedPreferences.getInt("actionBarColor", getResources().getColor(R.color.md_black_1000));

        setSupportActionBar(binding.toolbar);

        bundle = getIntent().getExtras();

        final RealmResults<RealmCategoryItem> parents = realmHandler.getCategoryChildren(bundle.getString("Category"));//getCategoryParents();

        addAdapter = new AddAdapter(this, parents, ContextCompat.getColor(this, R.color.flat_pomegranate));

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(addAdapter);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        binding.fabConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("Subcategory",parents.get(addAdapter.getSelectedItem()).getSubcategory());
                intent.putExtra("Icon",parents.get(addAdapter.getSelectedItem()).getIcon());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
