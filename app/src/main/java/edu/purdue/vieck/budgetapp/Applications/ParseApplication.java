package edu.purdue.vieck.budgetapp.Applications;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseClassName;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;

import edu.purdue.vieck.budgetapp.CustomObjects.BudgetItem;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by vieck on 12/23/15.
 */
public class ParseApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(BudgetItem.class);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this,
                getResources().getString(R.string.parse_application_id),
                getResources().getString(R.string.parse_client_key));
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
