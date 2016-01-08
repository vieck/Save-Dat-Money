package edu.purdue.vieck.budgetapp.Preferences;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Set;

import edu.purdue.vieck.budgetapp.R;

/**
 * Created by Stealth on 1/7/2016.
 */
public class ActionBarColorPreference extends ListPreference {

    ColorAdapter mPreferenceAdapter = null;
    Context mContext;
    private LayoutInflater mInflater;
    int[] mEntries;
    ArrayList<RadioButton> mButtonList;
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;

    public ActionBarColorPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mButtonList = new ArrayList<RadioButton>();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mEditor = mSharedPreferences.edit();
        mEntries = context.getResources().getIntArray(R.array.material_color_array);
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        int selectedPosition = mSharedPreferences.getInt("actionBarPosition", 0);

        if (mEntries == null) {
            throw new IllegalStateException(
                    "ListPreference requires an entries array and an entryValues array which are both the same length");
        }

        mPreferenceAdapter = new ColorAdapter(mContext, selectedPosition);

        builder.setAdapter(mPreferenceAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

    }

    class ColorAdapter extends BaseAdapter {
        private int mSelection;
        private Context mContext;
        public ColorAdapter(Context mContext, int mSelection) {
            this.mContext = mContext;
            this.mSelection = mSelection;
        }

        @Override
        public int getCount() {
            return mEntries.length;
        }

        @Override
        public Object getItem(int position) {
            return mEntries[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            ColorHolder holder = null;
            if (view == null) {
                view = mInflater.inflate(R.layout.currency_list_preference_row, parent, false);
                holder = new ColorHolder(view, position);
                view.setTag(holder);
                view.setClickable(true);
            } else {
                holder = (ColorHolder) view.getTag();
            }
            holder.rButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (RadioButton rb : mButtonList) {
                        if (rb.getId() != position)
                            rb.setChecked(false);
                    }

                    int actionBarColor = mEntries[position];
                    mEditor.putInt("actionBarColor", actionBarColor);
                    mEditor.putInt("actionBarPosition", position);
                    mEditor.commit();

                    Dialog mDialog = getDialog();
                    mDialog.dismiss();
                }
            });

            if (mSelection == position) {
                holder.rButton.setChecked(true);
            } else {
                holder.rButton.setChecked(false);
            }

            holder.background.setBackgroundColor(mEntries[position]);

            return view;
        }

        class ColorHolder {
            private View background;
            private RadioButton rButton;

            public ColorHolder(View view, int position) {
                background = view;
                rButton = (RadioButton) view.findViewById(R.id.custom_list_view_row_radio_button);
                rButton.setId(position);
                mButtonList.add(rButton);
            }
        }
    }
}
