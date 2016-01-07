package edu.purdue.vieck.budgetapp.CustomObjects;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Set;

import edu.purdue.vieck.budgetapp.R;

/**
 * Created by Stealth on 1/5/2016.
 */
public class CurrencyPreference extends ListPreference {

    CurrencyPreferenceAdapter mPreferenceAdapter = null;
    Context mContext;
    private LayoutInflater mInflater;
    Currency[] mEntries;
    ArrayList<RadioButton> mButtonList;
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;


    public CurrencyPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mButtonList = new ArrayList<RadioButton>();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mEditor = mSharedPreferences.edit();

        Set<Currency> currencies = Currency.getAvailableCurrencies();
        mEntries = currencies.toArray(new Currency[currencies.size()]);
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {

        int selectedPosition = mSharedPreferences.getInt("currencyPosition", -1);

        if (mEntries == null) {
            throw new IllegalStateException(
                    "ListPreference requires an entries array and an entryValues array which are both the same length");
        }

        mPreferenceAdapter = new CurrencyPreferenceAdapter(mContext, selectedPosition);

        builder.setAdapter(mPreferenceAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

    }

    private class CurrencyPreferenceAdapter extends BaseAdapter {

        private int mSelection;

        public CurrencyPreferenceAdapter(Context context, int mSelection) {
            this.mSelection = mSelection;
        }

        @Override
        public int getCount() {
            return mEntries.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View row = convertView;
            CustomHolder holder = null;

            if (row == null) {
                row = mInflater.inflate(R.layout.currency_list_preference_row, parent, false);
                holder = new CustomHolder(row, position);
                row.setTag(holder);
                row.setClickable(true);
            } else {
                holder = (CustomHolder) row.getTag();
            }
            holder.rButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (RadioButton rb : mButtonList) {
                        if (rb.getId() != position)
                            rb.setChecked(false);
                    }

                    int index = position;
                    String currencySymbol = mEntries[index].getSymbol().toString();
                    mEditor.putString("currencySymbol", currencySymbol);
                    mEditor.putInt("currencyPosition", index);
                    mEditor.commit();

                    Dialog mDialog = getDialog();
                    mDialog.dismiss();
                }
            });
            holder.text.setText(mEntries[position].getCurrencyCode() + " " + mEntries[position].getDisplayName());
            if (mSelection == -1 && mEntries[position].getCurrencyCode().compareTo("USD") == 0) {
                holder.rButton.setChecked(true);
            } else if (position == mSelection) {
                holder.rButton.setChecked(true);
            } else {
                holder.rButton.setChecked(false);
            }

            return row;
        }

        class CustomHolder {
            private TextView text = null;
            private RadioButton rButton = null;

            CustomHolder(View row, int position) {
                text = (TextView) row.findViewById(R.id.currency_textview);
                rButton = (RadioButton) row.findViewById(R.id.custom_list_view_row_radio_button);
                rButton.setId(position);
                mButtonList.add(rButton);
            }
        }
    }
}
