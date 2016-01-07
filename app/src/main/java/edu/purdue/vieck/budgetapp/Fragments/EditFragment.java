package edu.purdue.vieck.budgetapp.Fragments;

import android.animation.StateListAnimator;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
    FloatingActionButton deleteButton, editButton;
    RealmHandler mRealmHandler;
    BudgetItem budgetItem;
    private boolean editing, confirm;
    SharedPreferences mSharedPreferences;

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
        deleteButton = (FloatingActionButton) view.findViewById(R.id.delete_button);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
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
        budgetItem.setMonth(bundle.getInt("Month") - 1);
        budgetItem.setYear(bundle.getInt("Year"));
        budgetItem.setMonthString(bundle.getString("MonthString"));
        budgetItem.setImage(bundle.getInt("Image"));

        DecimalFormat df = new DecimalFormat(".##");
        String currencyString = mSharedPreferences.getString("currencySymbol",Currency.getInstance(getResources().getConfiguration().locale).getSymbol());
        amount.setText(df.format(budgetItem.getAmount()));
        currency.setText(currencyString);
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
        final StateListAnimator stateListAnimator = editButton.getStateListAnimator();
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirm) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        deleteButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_delete, getActivity().getTheme()));
                        editButton.setImageResource(android.R.drawable.ic_menu_edit);
                    } else {
                        deleteButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_delete));
                        editButton.setImageResource(android.R.drawable.ic_menu_edit);
                    }
                    confirm = false;
                } else if (editing) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        editButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_edit, getActivity().getTheme()));
                        deleteButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_delete, getActivity().getTheme()));
                    } else {
                        editButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_edit));
                        deleteButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_delete));
                    }
                    incomeButton.setClickable(false);
                    expenseButton.setClickable(false);
                    editing = false;
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        deleteButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_close_clear_cancel, getActivity().getTheme()));
                        editButton.setImageResource(android.R.drawable.checkbox_on_background);
                    } else {
                        deleteButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_close_clear_cancel));
                        editButton.setImageResource(android.R.drawable.checkbox_on_background);
                    }
                    confirm = true;
                }
            }
        });
    }

    private void setupEditButton() {
        final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        editing = false;
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (confirm) {
                    mRealmHandler.delete(budgetItem);
                    Intent intent = new Intent(getActivity(), ChartActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    getActivity().startActivity(intent);
                } else if (editing) {
                    amount.setInputType(InputType.TYPE_NULL);
                    amount.setFocusable(false);
                    category.setClickable(false);
                    category.setInputType(InputType.TYPE_NULL);
                    subcategory.setClickable(false);
                    note.setInputType(InputType.TYPE_NULL);
                    radioGroup.setClickable(false);
                    datePicker.setEnabled(false);
                    budgetItem.setAmount(Float.parseFloat(amount.getText().toString()));
                    budgetItem.setCategory(category.getText().toString());
                    budgetItem.setSubcategory(subcategory.getText().toString());
                    budgetItem.setNote(note.getText().toString());
                    if (expenseButton.isChecked()) {
                        budgetItem.setType(false);
                    } else {
                        budgetItem.setType(true);
                    }
                    budgetItem.setDay(datePicker.getDayOfMonth());
                    budgetItem.setMonth(datePicker.getMonth() + 1);
                    budgetItem.setYear(datePicker.getYear());
                    budgetItem.setMonthString(months[datePicker.getMonth()]);

                    try {
                        mRealmHandler.updateData(budgetItem);
                    }  finally {
                        Intent intent = new Intent(getActivity(), ChartActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    }
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
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        editButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_save, getActivity().getTheme()));
                        deleteButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_close_clear_cancel, getActivity().getTheme()));
                    } else {
                        editButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_save));
                        deleteButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_close_clear_cancel));
                    }
                    incomeButton.setClickable(true);
                    expenseButton.setClickable(true);
                    editing = true;
                }
            }
        });
    }
}
