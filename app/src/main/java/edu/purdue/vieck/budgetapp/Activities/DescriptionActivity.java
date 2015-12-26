package edu.purdue.vieck.budgetapp.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Currency;

import edu.purdue.vieck.budgetapp.CustomObjects.BudgetItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.R;

public class DescriptionActivity extends AppCompatActivity {
    Toolbar mToolbar;
    EditText amount, type, category, subcategory, date, note;
    FloatingActionButton editButton;
    RealmHandler mRealmHandler;
    BudgetItem budgetItem;
    private boolean editing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        setUpToolbar();
        Bundle extras = getIntent().getExtras();
        mRealmHandler = new RealmHandler(this);
        amount = (EditText) findViewById(R.id.edittext_amount);
        type = (EditText) findViewById(R.id.edittext_type);
        category = (EditText) findViewById(R.id.edittext_category);
        subcategory = (EditText) findViewById(R.id.edittext_subcategory);
        date = (EditText) findViewById(R.id.edittext_date);
        note = (EditText) findViewById(R.id.edittext_note);
        parseBundle(extras); 
        setUpFloatingActionButton();
        }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void parseBundle(Bundle bundle) {
        budgetItem = new BudgetItem();
        budgetItem.setId(bundle.getInt("Id"));
        budgetItem.setAmount((float) bundle.getDouble("Amount"));
        budgetItem.setCategory(bundle.getString("Category"));
        budgetItem.setSubcategory(bundle.getString("Subcategory"));
        budgetItem.setType(bundle.getBoolean("Type"));
        budgetItem.setTypeString(bundle.getString("TypeString"));
        budgetItem.setNote(bundle.getString("Note"));
        budgetItem.setDay(bundle.getInt("Day"));
        budgetItem.setMonth(bundle.getInt("Month"));
        budgetItem.setYear(bundle.getInt("Year"));
        budgetItem.setMonthString(bundle.getString("MonthString"));
        budgetItem.setImage(bundle.getInt("Image"));

        DecimalFormat df = new DecimalFormat(".##");
        Currency currency = Currency.getInstance(getResources().getConfiguration().locale);
        amount.setText(df.format(budgetItem.getAmount()) + " " + currency.getSymbol());
        type.setText(budgetItem.getTypeString());
        category.setText(budgetItem.getCategory());
        subcategory.setText(budgetItem.getSubcategory());
        note.setText(budgetItem.getNote());
        date.setText(budgetItem.getMonthString() + "-" + budgetItem.getDay() + "-" + budgetItem.getYear());

    }

    private void setUpFloatingActionButton() {
        editButton = (FloatingActionButton) findViewById(R.id.edit_button);
        editing = false;
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (editing) {
                        amount.setInputType(InputType.TYPE_NULL);
                        category.setClickable(false);
                        subcategory.setClickable(false);
                        type.setClickable(false);
                        date.setClickable(false);
                        note.setInputType(InputType.TYPE_NULL);
                        budgetItem.setAmount(Float.parseFloat(amount.getText().toString()));
                        budgetItem.setCategory(category.getText().toString());
                        budgetItem.setSubcategory(subcategory.getText().toString());
                        budgetItem.setNote(note.getText().toString());
                        mRealmHandler.addData(budgetItem);
                        editButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_edit, getApplicationContext().getTheme()));
                        editing = false;
                    } else {
                        amount.setInputType(InputType.TYPE_CLASS_NUMBER);
                        category.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Dialog dialog = new Dialog(getApplicationContext());
                            }
                        });
                        subcategory.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Dialog dialog = new Dialog(getApplicationContext());
                            }
                        });
                        type.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
                        date.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DatePickerDialog datePickerDialog;
                            }
                        });
                        editButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_save, getApplicationContext().getTheme()));
                        editing = true;
                    }
                } else {
                    if (editing) {
                        amount.setInputType(InputType.TYPE_NULL);
                        category.setClickable(false);
                        subcategory.setClickable(false);
                        type.setClickable(false);
                        date.setClickable(false);
                        budgetItem.setAmount(Float.parseFloat(amount.getText().toString()));
                        budgetItem.setCategory(category.getText().toString());
                        budgetItem.setSubcategory(subcategory.getText().toString());
                        budgetItem.setNote(note.getText().toString());
                        mRealmHandler.addData(budgetItem);
                        editButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_edit));
                        editing = false;
                    } else {
                        amount.setInputType(InputType.TYPE_CLASS_NUMBER);
                        category.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Dialog dialog = new Dialog(getApplicationContext());
                            }
                        });
                        subcategory.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Dialog dialog = new Dialog(getApplicationContext());
                            }
                        });
                        type.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
                        date.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DatePickerDialog datePickerDialog;
                            }
                        });
                        editButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_save));
                        editing = true;
                    }
                }
            }
        });
    }
}
