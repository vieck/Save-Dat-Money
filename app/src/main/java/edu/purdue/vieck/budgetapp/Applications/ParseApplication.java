package edu.purdue.vieck.budgetapp.Applications;


/**
 * Created by vieck on 12/23/15.
 */
public class ParseApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // ParseObject.registerSubclass(BudgetItem.class);
       /* Parse.enableLocalDatastore(this);
        Parse.initialize(this,
                getResources().getString(R.string.parse_application_id),
                getResources().getString(R.string.parse_client_key));
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);*/
    }
}
