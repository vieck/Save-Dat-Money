package edu.purdue.vieck.budgetapp.Activities;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import edu.purdue.vieck.budgetapp.R;

public class DescriptionActivity extends AppCompatActivity {
    Toolbar mToolbar;
    TextView amount, type, category, subcategory, date, note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        setUpToolbar();
        Bundle extras = getIntent().getExtras();
        amount = (TextView) findViewById(R.id.textview_amount);
        type = (TextView) findViewById(R.id.textview_type);
        category = (TextView) findViewById(R.id.textview_category);
        subcategory = (TextView) findViewById(R.id.textview_subcategory);
        date = (TextView) findViewById(R.id.textview_date);
        note = (TextView) findViewById(R.id.textview_note);

        amount.setText(extras.getDouble("Amount")+"");
        type.setText(extras.getString("Type"));
        category.setText(extras.getString("Category"));
        subcategory.setText(extras.getString("Subcategory"));
        date.setText(extras.getInt("Month") + "-" + extras.getInt("Day") + "-" + extras.getInt("Year"));
    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
