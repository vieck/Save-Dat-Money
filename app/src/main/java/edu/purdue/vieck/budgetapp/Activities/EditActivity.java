package edu.purdue.vieck.budgetapp.Activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import edu.purdue.vieck.budgetapp.Fragments.EditDataFragment;
import edu.purdue.vieck.budgetapp.R;

public class EditActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private EditDataFragment editDataFragment;
    private SharedPreferences mSharedPreferences;
    private int actionBarColor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        actionBarColor = mSharedPreferences.getInt("actionBarColor",getResources().getColor(R.color.md_black_1000));
        setUpToolbar();
        Bundle bundle = getIntent().getExtras();
        editDataFragment = new EditDataFragment();
        editDataFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, editDataFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_budget) {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Budget");
            alertDialog.setMessage("Set the budget for the month.");
            alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            final EditText budgetText = new EditText(this);
            budgetText.setGravity(View.TEXT_ALIGNMENT_CENTER);
            budgetText.setTextSize(32f);
            budgetText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            budgetText.setLayoutParams(lp);
            alertDialog.setView(budgetText);
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
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
                Drawable upArrow = getResources().getDrawable(R.drawable.ic_action_back);
                upArrow.setColorFilter(getResources().getColor(R.color.White), PorterDuff.Mode.SRC_ATOP);
                getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }
    }
}
