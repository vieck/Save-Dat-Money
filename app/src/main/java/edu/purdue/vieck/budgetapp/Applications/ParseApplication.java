package edu.purdue.vieck.budgetapp.Applications;

import com.parse.Parse;
import com.parse.ParseInstallation;

import edu.purdue.vieck.budgetapp.R;

/**
 * Created by vieck on 12/23/15.
 */
public class ParseApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this,
                getResources().getString(R.string.parse_application_id),
                getResources().getString(R.string.parse_client_key));
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
