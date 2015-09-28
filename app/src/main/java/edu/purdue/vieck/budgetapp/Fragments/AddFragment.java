package edu.purdue.vieck.budgetapp.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Calendar;

import edu.purdue.vieck.budgetapp.DatabaseHandler;
import edu.purdue.vieck.budgetapp.R;
import edu.purdue.vieck.budgetapp.CustomObjects.BudgetItem;

/**
 * Created by vieck on 7/13/15.
 */
public class AddFragment extends Fragment {
    private Context mContext;

    DatabaseHandler databaseHandler;

    private Calendar calendar;

    private DatePicker datePicker;

    private ToggleButton toggleButton;

    private TextView categories;

    private EditText amount, month, day, year, note;

    private ImageButton submitButton;

    @Override
    public void onAttach(Activity activity) {
        mContext = activity.getApplicationContext();
        super.onAttach(activity);
    }


    private void showSnackBar(String text) {
        Snackbar.make(getView(), text, Snackbar.LENGTH_LONG).show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        categories = (TextView) view.findViewById(R.id.text_category);
        final Activity currentActivity = getActivity();
        categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryFragment categoryFragment = new CategoryFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, categoryFragment);
                fragmentTransaction.addToBackStack("Add");
                fragmentTransaction.commit();
            }
        });

        calendar = Calendar.getInstance();

        toggleButton = (ToggleButton) view.findViewById(R.id.income_or_expense_button);

        amount = (EditText) view.findViewById(R.id.edittext_amount);

        note = (EditText) view.findViewById(R.id.eddittext_note);

        datePicker = (DatePicker) view.findViewById(R.id.datepicker);

        databaseHandler = new DatabaseHandler(getActivity());

        submitButton = (ImageButton) view.findViewById(R.id.imagebtn_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amount.getText().toString().equals("")) {
                    showSnackBar("Invalid Amount");
                    return;
                }
                Float amountV = Float.parseFloat(amount.getText().toString());
                //String spinnerV = mSpinner.getSelectedItem().toString();
                Boolean toggleV = toggleButton.isChecked();
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1;
                int year = datePicker.getYear();
                String entry = note.getText().toString();
                BudgetItem budgetItem = new BudgetItem(
                        amountV, "Example" , toggleV,
                        day, month, year, entry);
                databaseHandler.addData(budgetItem);
                showSnackBar("Added Data");
                //finish();
            }
        });
        return view;
    }
}
