package edu.purdue.vieck.budgetapp.Fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import edu.purdue.vieck.budgetapp.R;

/**
 * Created by vieck on 11/21/15.
 */
public class SettingsFragment extends PreferenceFragment {
    ListView generalListView, themeListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
