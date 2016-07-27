package edu.purdue.vieck.budgetapp.Activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.codetroopers.betterpickers.numberpicker.NumberPickerBuilder;
import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Currency;

import edu.purdue.vieck.budgetapp.CustomObjects.RealmBudgetItem;
import edu.purdue.vieck.budgetapp.CustomObjects.RealmDataItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.Fragments.AddCategoryFragment;
import edu.purdue.vieck.budgetapp.Fragments.AddSubCategoryFragment;
import edu.purdue.vieck.budgetapp.R;
import edu.purdue.vieck.budgetapp.databinding.ActivityAddBinding;

public class AddActivity extends AppCompatActivity implements NumberPickerDialogFragment.NumberPickerDialogHandlerV2 {


    ActivityAddBinding binding;

    AddCategoryFragment addCategoryFragment;
    private SharedPreferences mSharedPreferences;
    private int actionBarColor;
    RealmHandler mRealmHandler;
    int iconResourceId;

    private Bundle bundle;

    String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add);
        mRealmHandler = new RealmHandler(this);
        WebView.setWebContentsDebuggingEnabled(true);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        actionBarColor = mSharedPreferences.getInt("actionBarColor", Color.BLACK);
        bundle = savedInstanceState;
        checkBundle(bundle);
        setupToolbar();
        binding.fabNext.setVisibility(View.VISIBLE);
        binding.fabNext.setBackgroundTintList(new ColorStateList(new int[][]{new int[]{0}}, new int[]{actionBarColor}));
        binding.addTextviewAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NumberPickerBuilder npb = new NumberPickerBuilder()
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.AmountInputStyle)
                        .setPlusMinusVisibility(View.GONE);
                npb.show();
            }
        });
        setCategoryListener();
        setSubcategoryListener();
        setSubmitButtonListener();
//        if (savedInstanceState == null) {
//            addCategoryFragment = new AddCategoryFragment();
//            getFragmentManager().beginTransaction().add(R.id.fragment_container, addCategoryFragment, "addCategory").commit();
//        } else {
//            addCategoryFragment = (AddCategoryFragment) getFragmentManager().findFragmentByTag("addCategory");
//
//            if (addDataFragment == null) {
//                addCategoryFragment = new AddCategoryFragment();
//                getFragmentManager().beginTransaction().add(R.id.fragment_container, addCategoryFragment, "addCategory");
//            } else {
//                getFragmentManager().beginTransaction().replace(R.id.fragment_container, addCategoryFragment).commit();
//            }
//        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("Month", binding.datepicker.getMonth() + 1);
        outState.putInt("Day", binding.datepicker.getDayOfMonth());
        outState.putInt("Year", binding.datepicker.getYear());
        outState.putString("Category", binding.addTextviewCategory.getText().toString());
        outState.putString("Subcategory", binding.addTextviewSubcategory.getText().toString());
        if (!binding.addTextviewAmount.getText().toString().equals("")) {
            outState.putDouble("Amount", Double.parseDouble(binding.addTextviewAmount.getText().toString()));
        }
        outState.putString("Note", binding.edittextNote.getText().toString());
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
        if (binding.toolbar != null) {
            if (actionBarColor == getResources().getColor(R.color.md_white_1000)) {
                binding.toolbar.setTitleTextColor(Color.BLACK);
            } else {
                binding.toolbar.setTitleTextColor(Color.WHITE);
            }
            binding.toolbar.setBackgroundColor(actionBarColor);
            setSupportActionBar(binding.toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                Drawable upArrow = getResources().getDrawable(R.drawable.ic_action_back);
                upArrow.setColorFilter(getResources().getColor(R.color.White), PorterDuff.Mode.SRC_ATOP);
                getSupportActionBar().setHomeAsUpIndicator(upArrow);

        }
    }

    private void checkBundle(final Bundle bundle) {
        if (bundle != null) {
            Log.d("Bundle", bundle.toString());
            if (bundle.size() > 2) {
                binding.addTextviewAmount.setText(bundle.getDouble("Amount") + "");
                if (bundle.getString("Note") != "") {
                    binding.edittextNote.setText(bundle.getString("Note"));
                } else {
                    binding.edittextNote.setHint("Type a description");
                }
                iconResourceId = bundle.getInt("Icon", R.drawable.cell_phone_bill_dark);
                binding.datepicker.updateDate(bundle.getInt("Year"), bundle.getInt("Month"), bundle.getInt("Day"));
            }

            binding.addTextviewCategory.setText(bundle.getString("Category") + "");
            binding.addTextviewSubcategory.setText(bundle.getString("Subcategory") + "");
            if (bundle.getBoolean("Type")) {
                binding.incomeButton.toggle();
            } else {
                binding.expenseButton.toggle();
            }
        }
        String currencyString = mSharedPreferences.getString("currencySymbol", Currency.getInstance(getResources().getConfiguration().locale).getSymbol());
        binding.currencyLabel.setText(currencyString);
    }

    @Override
    public void onDialogNumberSet(int reference, BigInteger number, double decimal, boolean isNegative, BigDecimal fullNumber) {
        binding.addTextviewAmount.setText(fullNumber.toString());
    }

    private void setCategoryListener() {
        binding.addTextviewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCategoryFragment addCategoryFragment = new AddCategoryFragment();
                Bundle bundle = new Bundle();
                if (binding.incomeButton.isChecked()) {
                    bundle.putBoolean("Type", true);
                } else {
                    bundle.putBoolean("Type", false);
                }
                bundle.putString("Category", binding.addTextviewCategory.getText().toString());
                bundle.putString("Subcategory", binding.addTextviewSubcategory.getText().toString());
                if (!binding.addTextviewAmount.getText().toString().equals("")) {
                    bundle.putDouble("Amount", Double.parseDouble(binding.addTextviewAmount.getText().toString()));
                }
                bundle.putString("Note", binding.edittextNote.getText().toString());
                bundle.putInt("Month", binding.datepicker.getMonth());
                bundle.putInt("Day", binding.datepicker.getDayOfMonth());
                bundle.putInt("Year", binding.datepicker.getYear());
                addCategoryFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, addCategoryFragment);
                fragmentTransaction.commit();
            }
        });
    }

    private void setSubcategoryListener() {
        binding.addTextviewSubcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddSubCategoryFragment addSubCategoryFragment = new AddSubCategoryFragment();
                Bundle bundle = new Bundle();
                if (binding.incomeButton.isChecked()) {
                    bundle.putBoolean("Type", true);
                } else {
                    bundle.putBoolean("Type", false);
                }
                bundle.putString("Category", binding.addTextviewCategory.getText().toString());
                bundle.putString("Subcategory", binding.addTextviewSubcategory.getText().toString());
                if (!binding.addTextviewAmount.getText().toString().equals("")) {
                    bundle.putDouble("Amount", Double.parseDouble(binding.addTextviewAmount.getText().toString()));
                }
                bundle.putString("Note", binding.edittextNote.getText().toString());
                bundle.putInt("Month", binding.datepicker.getMonth());
                bundle.putInt("Day", binding.datepicker.getDayOfMonth());
                bundle.putInt("Year", binding.datepicker.getYear());
                addSubCategoryFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, addSubCategoryFragment);
                fragmentTransaction.commit();
            }
        });
    }

    private void setSubmitButtonListener() {

        binding.fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.addTextviewAmount.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Invalid Amount", Toast.LENGTH_LONG).show();
                    return;
                }
                float amountV = Float.parseFloat(binding.addTextviewAmount.getText().toString());
                Boolean incomeOrExpense;
                if (binding.incomeButton.isChecked()) {
                    incomeOrExpense = true;
                } else {
                    incomeOrExpense = false;
                }
                int dayNum = binding.datepicker.getDayOfMonth();
                int monthNum = binding.datepicker.getMonth() + 1;
                int yearNum = binding.datepicker.getYear();
                String categoryString = binding.addTextviewCategory.getText().toString();
                String subcategoryString = binding.addTextviewSubcategory.getText().toString();
                String noteString = binding.edittextNote.getText().toString();
                RealmDataItem realmDataItem = new RealmDataItem();
                realmDataItem.setAmount(amountV);
                realmDataItem.setCategory(categoryString);
                realmDataItem.setSubcategory(subcategoryString);
                realmDataItem.setType(incomeOrExpense);
                realmDataItem.setDay(dayNum);
                realmDataItem.setMonth(monthNum);
                realmDataItem.setYear(yearNum);
                realmDataItem.setNote(noteString);
                realmDataItem.setImage(iconResourceId);
                realmDataItem.setMonthString(months[monthNum - 1]);

                float defaultBudget = Float.parseFloat(mSharedPreferences.getString(getResources().getString(R.string.key_budget), "500.00"));
                RealmBudgetItem realmBudgetItem = new RealmBudgetItem(monthNum, yearNum, defaultBudget);
                Toast.makeText(getApplicationContext(), "Added Data", Toast.LENGTH_LONG).show();
                try {
                    mRealmHandler.add(realmDataItem);
                    mRealmHandler.add(realmBudgetItem);
                } finally {
                    Intent intent = new Intent(getApplicationContext(), ChartActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }
}
