package edu.purdue.vieck.budgetapp.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import edu.purdue.vieck.budgetapp.R;

/**
 * Created by vieck on 11/21/15.
 */
public class SettingsFragment extends PreferenceFragment {
    ListView generalListView, themeListView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        generalListView = (ListView) view.findViewById(R.id.general_listview);
        return view;
    }
}
