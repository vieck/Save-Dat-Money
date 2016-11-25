package edu.purdue.vieck.budgetapp.Activities;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
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

import com.codetroopers.betterpickers.datepicker.DatePickerBuilder;
import com.codetroopers.betterpickers.datepicker.DatePickerDialogFragment;
import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Currency;
import java.util.Locale;

import edu.purdue.vieck.budgetapp.CustomObjects.RealmDataItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.Fragments.AddCategoryFragment;
import edu.purdue.vieck.budgetapp.R;
import edu.purdue.vieck.budgetapp.databinding.ActivityEditBinding;

public class EditActivity extends AppCompatActivity implements NumberPickerDialogFragment.NumberPickerDialogHandlerV2, DatePickerDialogFragment.DatePickerDialogHandler {
    ActivityEditBinding binding;
    RealmHandler mRealmHandler;
    RealmDataItem realmDataItem;
    int iconResourceId;
    private boolean editing, confirm;
    private SharedPreferences mSharedPreferences;
    private int actionBarColor;
    int day, month, year;
    String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        actionBarColor = mSharedPreferences.getInt("actionBarColor", getResources().getColor(R.color.md_black_1000));
        setUpToolbar();
        mRealmHandler = new RealmHandler(this);
        Bundle bundle = getIntent().getExtras();
        parseBundle(bundle);
        setDatePickerListener();
        setCategoryListener();
        setSubcategoryListener();
        setUpFloatingActionButton();
//        editDataFragment = new EditDataFragment();
//        editDataFragment.setArguments(bundle);
//        getFragmentManager().beginTransaction().replace(R.id.fragment_container, editDataFragment).commit();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            String category = data.getStringExtra("Category");
            iconResourceId = data.getIntExtra("Icon", R.drawable.insurance_dark);
            binding.addTextviewCategory.setText(category);
            binding.addTextviewSubcategory.setText("");
            binding.addIcon.setImageDrawable(getDrawable(iconResourceId));
        }

        if (requestCode == 1 && resultCode == RESULT_OK) {
            String subCategory = data.getStringExtra("Subcategory");
            binding.addTextviewSubcategory.setText(subCategory);
            iconResourceId = data.getIntExtra("Icon", R.drawable.insurance_dark);
            binding.addIcon.setImageDrawable(getDrawable(iconResourceId));
        }
    }

    private void setUpToolbar() {
        if (binding.toolbar != null) {
            if (actionBarColor == getResources().getColor(R.color.md_white_1000)) {
                binding.toolbar.setTitleTextColor(Color.BLACK);
            } else {
                binding.toolbar.setTitleTextColor(Color.WHITE);
            }
//            mToolbar.setBackgroundColor(actionBarColor);
            setSupportActionBar(binding.toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            Drawable upArrow = getResources().getDrawable(R.drawable.ic_action_back);
            upArrow.setColorFilter(getResources().getColor(R.color.White), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }
    }

    private void parseBundle(Bundle bundle) {
        realmDataItem = new RealmDataItem();
        realmDataItem.setId(bundle.getInt("Id"));
        realmDataItem.setAmount((float) bundle.getDouble("Amount"));
        realmDataItem.setCategory(bundle.getString("Category"));
        realmDataItem.setSubcategory(bundle.getString("Subcategory"));
        realmDataItem.setType(bundle.getBoolean("Type"));
        realmDataItem.setTypeString(bundle.getString("TypeString"));
        realmDataItem.setNote(bundle.getString("Note"));
        realmDataItem.setDay(bundle.getInt("Day"));
        realmDataItem.setMonth(bundle.getInt("Month") - 1);
        realmDataItem.setYear(bundle.getInt("Year"));
        realmDataItem.setMonthString(bundle.getString("MonthString"));
        realmDataItem.setImage(bundle.getInt("Image"));
        iconResourceId = bundle.getInt("Image", R.drawable.cell_phone_bill_dark);

        DecimalFormat df = new DecimalFormat(".##");
        String currencyString = mSharedPreferences.getString("currencySymbol", Currency.getInstance(getResources().getConfiguration().locale).getSymbol());
        binding.addTextviewAmount.setText(df.format(realmDataItem.getAmount()));
        binding.currencyLabel.setText(currencyString);
        binding.addTextviewCategory.setText(realmDataItem.getCategory());
        binding.addTextviewSubcategory.setText(realmDataItem.getSubcategory());
        binding.edittextNote.setText(realmDataItem.getNote());
        if (realmDataItem.getType()) {
            binding.incomeButton.toggle();
        } else {
            binding.expenseButton.toggle();
        }
        month = realmDataItem.getMonth();
        day = realmDataItem.getDay();
        year = realmDataItem.getYear();
        binding.addTextviewDate.setText(months[month] + " " + day + ", " + year);
    }

    @Override
    public void onDialogNumberSet(int reference, BigInteger number, double decimal, boolean isNegative, BigDecimal fullNumber) {
        String dec = String.format(Locale.getDefault(),"%.2f",fullNumber);
        binding.addTextviewAmount.setText(dec);
    }

    @Override
    public void onDialogDateSet(int reference, int year, int monthOfYear, int dayOfMonth) {
        this.day = dayOfMonth;
        this.month = monthOfYear;
        this.year = year;
        binding.addTextviewDate.setVisibility(View.VISIBLE);
        binding.addTextviewDate.setText(months[monthOfYear] + " " + day + ", " + year);
        binding.addButtonDate.setText(getString(R.string.add_data_date_change_button));
    }

    private void setCategoryListener() {
        binding.addTextviewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                bundle.putInt("Icon",iconResourceId);
                bundle.putString("Note", binding.edittextNote.getText().toString());
                bundle.putInt("Month", month);
                bundle.putInt("Day", day);
                bundle.putInt("Year", year);
                Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }
        });
    }

    private void setSubcategoryListener() {
        binding.addTextviewSubcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.addTextviewCategory.getText().equals("")) {
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
                    bundle.putInt("Icon",iconResourceId);
                    bundle.putString("Note", binding.edittextNote.getText().toString());
                    bundle.putInt("Month", month);
                    bundle.putInt("Day", day);
                    bundle.putInt("Year", year);
                    Intent intent = new Intent(getApplicationContext(), SubCategoryActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1);
                }
            }
        });
    }

    private void setDatePickerListener() {
        binding.addButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerBuilder dpb = new DatePickerBuilder()
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment);
                if (binding.addTextviewDate.getText().equals("")) {
                    dpb.setDayOfMonth(day);
                    dpb.setMonthOfYear(month);
                    dpb.setYear(year);
                }
                dpb.show();
            }
        });
    }

    private void setUpFloatingActionButton() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                setupEditButton();
                setupDeleteButton();
            }
        });
        thread.start();
    }

    private void setupDeleteButton() {
        confirm = false;
        binding.deleteButton.setBackgroundTintList(new ColorStateList(new int[][]{new int[]{0}}, new int[]{actionBarColor}));
        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirm) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        binding.deleteButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_delete, getTheme()));
                        binding.editButton.setImageResource(android.R.drawable.ic_menu_edit);
                    } else {
                        binding.deleteButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_delete));
                        binding.editButton.setImageResource(android.R.drawable.ic_menu_edit);
                    }
                    binding.deleteButton.setBackgroundTintList(new ColorStateList(new int[][]{new int[]{0}}, new int[]{actionBarColor}));
                    binding.editButton.setBackgroundTintList(new ColorStateList(new int[][]{new int[]{0}}, new int[]{actionBarColor}));
                    confirm = false;
                } else if (editing) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        binding.editButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_edit, getTheme()));
                        binding.deleteButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_delete, getTheme()));
                    } else {
                        binding.editButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_edit));
                        binding.deleteButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_delete));
                    }
                    binding.incomeButton.setClickable(false);
                    binding.expenseButton.setClickable(false);
                    binding.deleteButton.setBackgroundTintList(new ColorStateList(new int[][]{new int[]{0}}, new int[]{actionBarColor}));
                    binding.editButton.setBackgroundTintList(new ColorStateList(new int[][]{new int[]{0}}, new int[]{actionBarColor}));
                    editing = false;
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        binding.deleteButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_close_clear_cancel, getTheme()));
                        binding.editButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_confirm, getTheme()));
                    } else {
                        binding.deleteButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_close_clear_cancel));
                        binding.editButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_confirm));
                    }
                    binding.deleteButton.setBackgroundTintList(new ColorStateList(new int[][]{new int[]{0}}, new int[]{getResources().getColor(R.color.md_red_400)}));
                    binding.editButton.setBackgroundTintList(new ColorStateList(new int[][]{new int[]{0}}, new int[]{getResources().getColor(R.color.md_green_400)}));
                    confirm = true;
                }
            }
        });
    }

    private void setupEditButton() {
        final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        editing = false;
        binding.editButton.setBackgroundTintList(new ColorStateList(new int[][]{new int[]{0}}, new int[]{actionBarColor}));
        binding.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (confirm) {
                    mRealmHandler.delete(realmDataItem);
                    Intent intent = new Intent(getApplicationContext(), ChartActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else if (editing) {
                    binding.addTextviewAmount.setInputType(InputType.TYPE_NULL);
                    binding.addTextviewAmount.setFocusable(false);
                    binding.addTextviewCategory.setClickable(false);
                    binding.addTextviewCategory.setInputType(InputType.TYPE_NULL);
                    binding.addTextviewSubcategory.setClickable(false);
                    binding.edittextNote.setInputType(InputType.TYPE_NULL);
                    binding.radioGroup.setClickable(false);
                    binding.addButtonDate.setEnabled(false);
                    realmDataItem.setAmount(Float.parseFloat(binding.addTextviewAmount.getText().toString()));
                    realmDataItem.setCategory(binding.addTextviewCategory.getText().toString());
                    realmDataItem.setSubcategory(binding.addTextviewSubcategory.getText().toString());
                    realmDataItem.setNote(binding.edittextNote.getText().toString());
                    if (binding.expenseButton.isChecked()) {
                        realmDataItem.setType(false);
                    } else {
                        realmDataItem.setType(true);
                    }
                    realmDataItem.setDay(day);
                    realmDataItem.setMonth(month);
                    realmDataItem.setYear(year);
                    realmDataItem.setMonthString(months[month]);

                    try {
                        mRealmHandler.update(realmDataItem);
                    } finally {
                        Intent intent = new Intent(getApplicationContext(), ChartActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                    binding.deleteButton.setBackgroundTintList(new ColorStateList(new int[][]{new int[]{0}}, new int[]{actionBarColor}));
                    binding.editButton.setBackgroundTintList(new ColorStateList(new int[][]{new int[]{0}}, new int[]{actionBarColor}));
                } else {
                    binding.addTextviewAmount.setInputType(InputType.TYPE_CLASS_NUMBER);
                    binding.addTextviewCategory.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AddCategoryFragment addCategoryFragment = new AddCategoryFragment();
                            Bundle bundle = getIntent().getExtras();
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
                            bundle.putInt("Month", month);
                            bundle.putInt("Day", day);
                            bundle.putInt("Year", year);
                            addCategoryFragment.setArguments(bundle);
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, addCategoryFragment);
                            fragmentTransaction.commit();
                        }
                    });
                    binding.addTextviewSubcategory.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            AddSubCategoryFragment addSubCategoryFragment = new AddSubCategoryFragment();
                            Bundle bundle = getIntent().getExtras();
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
                            bundle.putInt("Month", month);
                            bundle.putInt("Day", day);
                            bundle.putInt("Year", year);
//                            addSubCategoryFragment.setArguments(bundle);
//                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                            fragmentTransaction.replace(R.id.fragment_container, addSubCategoryFragment);
//                            fragmentTransaction.commit();
                        }
                    });
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        binding.editButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_save, getTheme()));
                        binding.deleteButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_close_clear_cancel, getTheme()));
                    } else {
                        binding.editButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_save));
                        binding.deleteButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_close_clear_cancel));
                    }
                    binding.deleteButton.setBackgroundTintList(new ColorStateList(new int[][]{new int[]{0}}, new int[]{getResources().getColor(R.color.md_red_400)}));
                    binding.editButton.setBackgroundTintList(new ColorStateList(new int[][]{new int[]{0}}, new int[]{getResources().getColor(R.color.md_green_400)}));
                    binding.incomeButton.setClickable(true);
                    binding.expenseButton.setClickable(true);
                    editing = true;
                }
            }
        });
    }
}
