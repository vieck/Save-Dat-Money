package edu.purdue.vieck.budgetapp.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

import edu.purdue.vieck.budgetapp.Activities.ChartActivity;
import edu.purdue.vieck.budgetapp.CustomObjects.BudgetItem;
import edu.purdue.vieck.budgetapp.DatabaseHandler;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by vieck on 7/13/15.
 */
public class AddFragment extends Fragment {

    DatabaseHandler databaseHandler;
    int iconResourceId;
    private Bundle mSavedState;
    private RelativeLayout relativeLayout;
    private Calendar calendar;
    private DatePicker datePicker;
    private RadioButton incomeButton, expenseButton;
    private TextView categories;
    private EditText amount, category, subcategory, month, day, year, note;

    private ImageButton submitButton;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    private void showSnackBar(String text) {
        relativeLayout = (RelativeLayout) getActivity().findViewById(R.id.snackbar_layout);
        Snackbar.make(relativeLayout, text, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("Month", datePicker.getMonth() + 1);
        outState.putInt("Day", datePicker.getDayOfMonth());
        outState.putInt("Year", datePicker.getYear());
        outState.putString("Category", categories.getText().toString());
        outState.putString("Subcategory", subcategory.getText().toString());
        if (!amount.getText().toString().equals("")) {
            outState.putDouble("Amount", Double.parseDouble(amount.getText().toString()));
        }
        outState.putString("Note", note.getText().toString());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add, container, false);
        Bundle bundle = getArguments();
        categories = (EditText) view.findViewById(R.id.edittext_category);
        subcategory = (EditText) view.findViewById(R.id.edittext_subcategory);
        incomeButton = (RadioButton) view.findViewById(R.id.income__button);
        expenseButton = (RadioButton) view.findViewById(R.id.expense_button);
        amount = (EditText) view.findViewById(R.id.edittext_amount);
        note = (EditText) view.findViewById(R.id.edittext_note);
        datePicker = (DatePicker) view.findViewById(R.id.datepicker);
        submitButton = (ImageButton) view.findViewById(R.id.imagebtn_submit);

        databaseHandler = new DatabaseHandler(getActivity());

        if (bundle != null) {
            Log.d("Bundle", bundle.toString());
            amount.setText(bundle.getDouble("Amount")+"");
            categories.setText(bundle.getString("Subcategory") + "");
            subcategory.setText(bundle.getString("Category") + "");
            if (bundle.getBoolean("Type")) {
                incomeButton.toggle();
            } else {
                expenseButton.toggle();
            }
            note.setText(bundle.getString("Note"));
            iconResourceId = bundle.getInt("Icon", R.drawable.cell_phone_bill_dark);
            datePicker.updateDate(bundle.getInt("Year"), bundle.getInt("Month"), bundle.getInt("Day"));
        }
        iconResourceId = R.drawable.gas_station_dark;
        final Activity currentActivity = getActivity();
        categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryFragment categoryFragment = new CategoryFragment();
                Bundle bundle = new Bundle();
                if (incomeButton.isChecked()) {
                    bundle.putBoolean("Type", true);
                } else {
                    bundle.putBoolean("Type", false);
                }
                bundle.putString("Category", categories.getText().toString());
                bundle.putString("Subcategory", subcategory.getText().toString());
                if (!amount.getText().toString().equals("")) {
                    bundle.putDouble("Amount", Double.parseDouble(amount.getText().toString()));
                }
                bundle.putString("Note", note.getText().toString());
                bundle.putInt("Month", datePicker.getMonth());
                bundle.putInt("Day", datePicker.getDayOfMonth());
                bundle.putInt("Year", datePicker.getYear());
                categoryFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, categoryFragment);
                fragmentTransaction.commit();
            }
        });

        calendar = Calendar.getInstance();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amount.getText().toString().equals("")) {
                    Toast.makeText(getActivity(),"Invalid Amount",Toast.LENGTH_LONG).show();
                    return;
                }
                Float amountV = Float.parseFloat(amount.getText().toString());
                Boolean incomeOrExpense;
                if (incomeButton.isChecked()) {
                    incomeOrExpense = true;
                } else {
                    incomeOrExpense = false;
                }
                int dayNum = datePicker.getDayOfMonth();
                int monthNum = datePicker.getMonth() + 1;
                int yearNum = datePicker.getYear();
                String categoryString = categories.getText().toString();
                String subcategoryString = subcategory.getText().toString();
                String noteString = note.getText().toString();
                BudgetItem budgetItem = new BudgetItem(
                        amountV, categoryString, subcategoryString, incomeOrExpense,
                        dayNum, monthNum, yearNum, noteString, iconResourceId);
                databaseHandler.addData(budgetItem);
                Toast.makeText(getActivity(), "Added Data", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), ChartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }
}
