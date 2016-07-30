package edu.purdue.vieck.budgetapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import edu.purdue.vieck.budgetapp.Adapters.AddAdapter;
import edu.purdue.vieck.budgetapp.CustomObjects.DividerItemDecoration;
import edu.purdue.vieck.budgetapp.CustomObjects.RealmCategoryItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.R;
import edu.purdue.vieck.budgetapp.databinding.ActivityCategoryBinding;
import io.realm.RealmResults;

/**
 * Created by mvieck on 7/26/16.
 */

public class CategoryActivity extends AppCompatActivity {
    ActivityCategoryBinding binding;
    AddAdapter addAdapter;
    RealmHandler realmHandler;
    SharedPreferences sharedPreferences;
    int actionBarColor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category);
        realmHandler = new RealmHandler(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        actionBarColor = sharedPreferences.getInt("actionBarColor", getResources().getColor(R.color.md_black_1000));

        setSupportActionBar(binding.toolbar);

        final RealmResults<RealmCategoryItem> parents = realmHandler.getCategoryParents();

        addAdapter = new AddAdapter(this, parents, ContextCompat.getColor(this, R.color.flat_peterriver));

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(addAdapter);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        binding.fabConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("Category",parents.get(addAdapter.getSelectedItem()).getCategory());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
