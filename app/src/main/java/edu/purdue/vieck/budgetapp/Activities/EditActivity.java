package edu.purdue.vieck.budgetapp.Activities;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import edu.purdue.vieck.budgetapp.Fragments.EditFragment;
import edu.purdue.vieck.budgetapp.R;

public class EditActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private EditFragment editFragment;
    private SharedPreferences mSharedPreferences;
    private int actionBarColor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        actionBarColor = mSharedPreferences.getInt("actionBarColor",0);
        setUpToolbar();
        Bundle bundle = getIntent().getExtras();
        editFragment = new EditFragment();
        editFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, editFragment).commit();
    }

    private void setUpToolbar() {
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
