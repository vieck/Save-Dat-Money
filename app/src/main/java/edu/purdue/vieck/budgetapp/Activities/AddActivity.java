package edu.purdue.vieck.budgetapp.Activities;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import edu.purdue.vieck.budgetapp.Fragments.AddFragment;
import edu.purdue.vieck.budgetapp.R;

public class AddActivity extends AppCompatActivity {

    AddFragment addFragment;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                //if (getFragmentManager().getBackStackEntryCount() > 1) {
                // FragmentTransaction ft = getFragmentManager().beginTransaction();
                // ft.replace(R.id.fragment_container, new AddFragment()).addToBackStack("category").commit();
                //}
            }
        });
        if (savedInstanceState == null) {
            addFragment = new AddFragment();
            getFragmentManager().beginTransaction().add(R.id.fragment_container, addFragment, "addFragment").commit();
        } else {
            addFragment = (AddFragment) getFragmentManager().findFragmentByTag("addFragment");

            if (addFragment == null) {
                addFragment = new AddFragment();
                getFragmentManager().beginTransaction().add(R.id.fragment_container, addFragment, "addFragment");
            } else {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, addFragment).commit();
            }
        }

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
