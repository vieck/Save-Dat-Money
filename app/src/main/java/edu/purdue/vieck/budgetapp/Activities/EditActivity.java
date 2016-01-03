package edu.purdue.vieck.budgetapp.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import edu.purdue.vieck.budgetapp.Fragments.EditFragment;
import edu.purdue.vieck.budgetapp.R;

public class EditActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private EditFragment editFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        setUpToolbar();
        Bundle bundle = getIntent().getExtras();
        editFragment = new EditFragment();
        editFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, editFragment).commit();
    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
