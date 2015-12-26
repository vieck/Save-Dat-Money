package edu.purdue.vieck.budgetapp.Fragments;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Currency;

import edu.purdue.vieck.budgetapp.Activities.ChartActivity;
import edu.purdue.vieck.budgetapp.CustomObjects.BudgetItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by Stealth on 12/25/2015.
 */
public class EditFragment extends android.app.Fragment {
    TextView currency;
    RadioGroup radioGroup;
    RadioButton incomeButton, expenseButton;
    EditText amount, category, subcategory, note;
    DatePicker datePicker;
    FloatingActionButton editButton;
    RealmHandler mRealmHandler;
    BudgetItem budgetItem;
    private boolean editing;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        Bundle extras = getArguments();
        mRealmHandler = new RealmHandler(getActivity());
        category = (EditText) view.findViewById(R.id.edittext_category);
        subcategory = (EditText) view.findViewById(R.id.edittext_subcategory);
        radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
        incomeButton = (RadioButton) view.findViewById(R.id.income__button);
        expenseButton = (RadioButton) view.findViewById(R.id.expense_button);
        amount = (EditText) view.findViewById(R.id.edittext_amount);
        currency = (TextView) view.findViewById(R.id.currency_label);
        note = (EditText) view.findViewById(R.id.edittext_note);
        datePicker = (DatePicker) view.findViewById(R.id.datepicker);
        editButton = (FloatingActionButton) view.findViewById(R.id.edit_button);
        parseBundle(extras);
        setUpFloatingActionButton();
        return view;
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
        budgetItem.setMonth(bundle.getInt("Month")-1);
        budgetItem.setYear(bundle.getInt("Year"));
        budgetItem.setMonthString(bundle.getString("MonthString"));
        budgetItem.setImage(bundle.getInt("Image"));

        DecimalFormat df = new DecimalFormat(".##");
        Currency currencyString = Currency.getInstance(getResources().getConfiguration().locale);
        amount.setText(df.format(budgetItem.getAmount()));
        currency.setText(currencyString.getSymbol());
        category.setText(budgetItem.getCategory());
        subcategory.setText(budgetItem.getSubcategory());
        note.setText(budgetItem.getNote());
        if (budgetItem.getType()) {
            incomeButton.toggle();
        } else {
            expenseButton.toggle();
        }
        datePicker.updateDate(budgetItem.getYear(), budgetItem.getMonth(), budgetItem.getDay());

    }

    private void setUpFloatingActionButton() {
        final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        editing = false;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            if (editing) {
                                amount.setInputType(InputType.TYPE_NULL);
                                amount.setFocusable(false);
                                category.setClickable(false);
                                category.setInputType(InputType.TYPE_NULL);
                                subcategory.setClickable(false);
                                note.setInputType(InputType.TYPE_NULL);
                                datePicker.setEnabled(false);
                                budgetItem.setAmount(Float.parseFloat(amount.getText().toString()));
                                budgetItem.setCategory(category.getText().toString());
                                budgetItem.setSubcategory(subcategory.getText().toString());
                                budgetItem.setNote(note.getText().toString());
                                budgetItem.setDay(datePicker.getDayOfMonth());
                                budgetItem.setMonth(datePicker.getMonth() + 1);
                                budgetItem.setYear(datePicker.getYear());
                                budgetItem.setMonthString(months[datePicker.getMonth()]);
                                mRealmHandler.updateData(budgetItem);
                                Intent intent = new Intent(getActivity(), ChartActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                getActivity().startActivity(intent);
                            } else {
                                amount.setInputType(InputType.TYPE_CLASS_NUMBER);
                                category.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        AddCategoryFragment addCategoryFragment = new AddCategoryFragment();
                                        Bundle bundle = getArguments();
                                        if (incomeButton.isChecked()) {
                                            bundle.putBoolean("Type", true);
                                        } else {
                                            bundle.putBoolean("Type", false);
                                        }
                                        bundle.putString("Category", category.getText().toString());
                                        bundle.putString("Subcategory", subcategory.getText().toString());
                                        if (!amount.getText().toString().equals("")) {
                                            bundle.putDouble("Amount", Double.parseDouble(amount.getText().toString()));
                                        }
                                        bundle.putString("Note", note.getText().toString());
                                        bundle.putInt("Month", datePicker.getMonth());
                                        bundle.putInt("Day", datePicker.getDayOfMonth());
                                        bundle.putInt("Year", datePicker.getYear());
                                        addCategoryFragment.setArguments(bundle);
                                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                        fragmentTransaction.replace(R.id.fragment_container, addCategoryFragment);
                                        fragmentTransaction.commit();
                                    }
                                });
                                subcategory.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        AddSubcategoryFragment addSubcategoryFragment = new AddSubcategoryFragment();
                                        Bundle bundle = getArguments();
                                        if (incomeButton.isChecked()) {
                                            bundle.putBoolean("Type", true);
                                        } else {
                                            bundle.putBoolean("Type", false);
                                        }
                                        bundle.putString("Category", category.getText().toString());
                                        bundle.putString("Subcategory", subcategory.getText().toString());
                                        if (!amount.getText().toString().equals("")) {
                                            bundle.putDouble("Amount", Double.parseDouble(amount.getText().toString()));
                                        }
                                        bundle.putString("Note", note.getText().toString());
                                        bundle.putInt("Month", datePicker.getMonth());
                                        bundle.putInt("Day", datePicker.getDayOfMonth());
                                        bundle.putInt("Year", datePicker.getYear());
                                        addSubcategoryFragment.setArguments(bundle);
                                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                        fragmentTransaction.replace(R.id.fragment_container, addSubcategoryFragment);
                                        fragmentTransaction.commit();
                                    }
                                });
                                datePicker.setEnabled(true);
                                editButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_save, getActivity().getTheme()));
                                editing = true;
                            }
                        } else {
                            if (editing) {
                                amount.setInputType(InputType.TYPE_NULL);
                                amount.setFocusable(false);
                                category.setClickable(false);
                                category.setInputType(InputType.TYPE_NULL);
                                subcategory.setClickable(false);
                                datePicker.setEnabled(false);
                                note.setInputType(InputType.TYPE_NULL);
                                budgetItem.setAmount(Float.parseFloat(amount.getText().toString()));
                                budgetItem.setCategory(category.getText().toString());
                                budgetItem.setSubcategory(subcategory.getText().toString());
                                budgetItem.setNote(note.getText().toString());
                                budgetItem.setDay(datePicker.getDayOfMonth());
                                budgetItem.setMonth(datePicker.getMonth() + 1);
                                budgetItem.setYear(datePicker.getYear());
                                budgetItem.setMonthString(months[datePicker.getMonth()]);
                                mRealmHandler.updateData(budgetItem);
                                Intent intent = new Intent(getActivity(), ChartActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                getActivity().startActivity(intent);
                            } else {
                                amount.setInputType(InputType.TYPE_CLASS_NUMBER);
                                category.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        AddCategoryFragment addCategoryFragment = new AddCategoryFragment();
                                        Bundle bundle = getArguments();
                                        if (incomeButton.isChecked()) {
                                            bundle.putBoolean("Type", true);
                                        } else {
                                            bundle.putBoolean("Type", false);
                                        }
                                        bundle.putString("Category", category.getText().toString());
                                        bundle.putString("Subcategory", subcategory.getText().toString());
                                        if (!amount.getText().toString().equals("")) {
                                            bundle.putDouble("Amount", Double.parseDouble(amount.getText().toString()));
                                        }
                                        bundle.putString("Note", note.getText().toString());
                                        bundle.putInt("Month", datePicker.getMonth());
                                        bundle.putInt("Day", datePicker.getDayOfMonth());
                                        bundle.putInt("Year", datePicker.getYear());
                                        addCategoryFragment.setArguments(bundle);
                                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                        fragmentTransaction.replace(R.id.fragment_container, addCategoryFragment);
                                        fragmentTransaction.commit();
                                    }
                                });
                                subcategory.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        AddSubcategoryFragment addSubcategoryFragment = new AddSubcategoryFragment();
                                        Bundle bundle = getArguments();
                                        if (incomeButton.isChecked()) {
                                            bundle.putBoolean("Type", true);
                                        } else {
                                            bundle.putBoolean("Type", false);
                                        }
                                        bundle.putString("Category", category.getText().toString());
                                        bundle.putString("Subcategory", subcategory.getText().toString());
                                        if (!amount.getText().toString().equals("")) {
                                            bundle.putDouble("Amount", Double.parseDouble(amount.getText().toString()));
                                        }
                                        bundle.putString("Note", note.getText().toString());
                                        bundle.putInt("Month", datePicker.getMonth());
                                        bundle.putInt("Day", datePicker.getDayOfMonth());
                                        bundle.putInt("Year", datePicker.getYear());
                                        addSubcategoryFragment.setArguments(bundle);
                                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                        fragmentTransaction.replace(R.id.fragment_container, addSubcategoryFragment);
                                        fragmentTransaction.commit();
                                    }
                                });

                                datePicker.setEnabled(true);
                                editButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_save));
                                editing = true;
                            }
                        }
                    }
                });
            }
        });
        thread.start();
    }
}
