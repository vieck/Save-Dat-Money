package edu.purdue.vieck.budgetapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import edu.purdue.vieck.budgetapp.Fragments.AddCategoryFragment;
import edu.purdue.vieck.budgetapp.Fragments.AddDataFragment;
import edu.purdue.vieck.budgetapp.R;

public class AddActivity extends AppCompatActivity {

    AddDataFragment addDataFragment;
    AddCategoryFragment addCategoryFragment;
    private Toolbar mToolbar;
    private SharedPreferences mSharedPreferences;
    private int actionBarColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        actionBarColor = mSharedPreferences.getInt("actionBarColor", Color.BLACK);
        setupToolbar();
        if (savedInstanceState == null) {
            addCategoryFragment = new AddCategoryFragment();
            getFragmentManager().beginTransaction().add(R.id.fragment_container, addCategoryFragment, "addCategory").commit();
        } else {
            addCategoryFragment = (AddCategoryFragment) getFragmentManager().findFragmentByTag("addCategory");

            if (addDataFragment == null) {
                addCategoryFragment = new AddCategoryFragment();
                getFragmentManager().beginTransaction().add(R.id.fragment_container, addCategoryFragment, "addCategory");
            } else {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, addCategoryFragment).commit();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            Intent intent = new Intent(this, CategoryEditingActivity.class);
            startActivity(intent);
            return true;
        } else if (id == android.R.id.home) {
            Intent upIntent = new Intent(this, ChartActivity.class);
            startActivity(upIntent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            if (actionBarColor == getResources().getColor(R.color.md_white_1000)) {
                mToolbar.setTitleTextColor(Color.BLACK);
            } else {
                mToolbar.setTitleTextColor(Color.WHITE);
            }
            mToolbar.setBackgroundColor(actionBarColor);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
