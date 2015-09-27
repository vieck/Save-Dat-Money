package edu.purdue.vieck.budgetapp.Activities;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.ToggleButton;

import java.util.Calendar;

import edu.purdue.vieck.budgetapp.CustomObjects.BudgetElement;
import edu.purdue.vieck.budgetapp.DatabaseHandler;
import edu.purdue.vieck.budgetapp.R;

public class SubmitActivity extends AppCompatActivity {

    DatabaseHandler databaseHandler;

    private Spinner mSpinner;

    private Calendar calendar;

    private DatePicker datePicker;

    private ToggleButton toggleButton;

    private EditText amount, month, day, year, note;

    private ImageButton submitButton;

    private void showSnackBar(String text) {
        Snackbar.make(findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        mSpinner = (Spinner) findViewById(R.id.spinner_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categoryarray,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        calendar = Calendar.getInstance();

        toggleButton = (ToggleButton) findViewById(R.id.income_or_expense_button);

        amount = (EditText) findViewById(R.id.edittext_amount);

        note = (EditText) findViewById(R.id.eddittext_note);

        datePicker = (DatePicker) findViewById(R.id.datepicker);

        databaseHandler = new DatabaseHandler(this);

        submitButton = (ImageButton) findViewById(R.id.imagebtn_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amount.getText().toString().equals("")) {
                    showSnackBar("Invalid Amount");
                    return;
                }
                Float amountV = Float.parseFloat(amount.getText().toString());
                String spinnerV = mSpinner.getSelectedItem().toString();
                Boolean toggleV = toggleButton.isChecked();
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1;
                int year = datePicker.getYear();
                String entry = note.getText().toString();
                BudgetElement budgetElement = new BudgetElement(
                        amountV, spinnerV, toggleV,
                        day, month, year, entry);
                databaseHandler.addData(budgetElement);
                showSnackBar("Added Data");
                finish();
            }
        });
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
