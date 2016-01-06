package edu.purdue.vieck.budgetapp.CustomObjects;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Set;

/**
 * Created by Stealth on 1/5/2016.
 */
public class CurrencyPreference extends ListPreference {

    CurrencyPreferenceAdapter mPreferenceAdapter = null;
    Context mContext;
    private LayoutInflater mInflater;
    CharSequence[] mEntries;
    CharSequence[] mEntryValues;
    ArrayList<RadioButton> mButtonList;
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;


    public CurrencyPreference(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mButtonList = new ArrayList<RadioButton>();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mEditor = mSharedPreferences.edit();

        Set<Currency> currencies = Currency.getAvailableCurrencies();
        mEntries = new CharSequence[currencies.size()];
        mEntryValues = new CharSequence[currencies.size()];

        int i = 0;
        for (Currency currency : currencies) {
            mEntries[i] = currency.getDisplayName();
            mEntryValues[i] = currency.getSymbol();
        }
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);

        if (mEntries == null || mEntryValues == null || mEntries.length != mEntryValues.length )
        {
            throw new IllegalStateException(
                    "ListPreference requires an entries array and an entryValues array which are both the same length");
        }

        mPreferenceAdapter = new CurrencyPreferenceAdapter(mContext);

        builder.setAdapter(mPreferenceAdapter, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });

    }

    private class CurrencyPreferenceAdapter extends BaseAdapter {
        public CurrencyPreferenceAdapter(Context context) {

        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View row = convertView;
            CustomHolder holder = null;

            if(row == null)
            {
                row = mInflater.inflate(R.layout.custom_list_preference_row, parent, false);
                holder = new CustomHolder(row, position);
                row.setTag(holder);

                // do whatever you need here, for me I wanted the last item to be greyed out and unclickable
                if(position != 3)
                {
                    row.setClickable(true);
                    row.setOnClickListener(new View.OnClickListener()
                    {
                        public void onClick(View v)
                        {
                            for(RadioButton rb : mButtonList)
                            {
                                if(rb.getId() != position)
                                    rb.setChecked(false);
                            }

                            int index = position;
                            int value = Integer.valueOf((String) mEntryValues[index]);
                            mEditor.putInt("yourPref", value);

                            Dialog mDialog = getDialog();
                            mDialog.dismiss();
                        }
                    });
                }
            }

            return row;
        }

        class CustomHolder
        {
            private TextView text = null;
            private RadioButton rButton = null;

            CustomHolder(View row, int position)
            {
                text = (TextView)row.findViewById(R.id.custom_list_view_row_text_view);
                text.setText(mEntries[position]);
                rButton = (RadioButton)row.findViewById(R.id.custom_list_view_row_radio_button);
                rButton.setId(position);

                // again do whatever you need to, for me I wanted this item to be greyed out and unclickable
                if(position == 3)
                {
                    text.setTextColor(Color.LTGRAY);
                    rButton.setClickable(false);
                }

                // also need to do something to check your preference and set the right button as checked

                mButtonList.add(rButton);
                rButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
                {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                    {
                        if(isChecked)
                        {
                            for(RadioButton rb : mButtonList)
                            {
                                if(rb != buttonView)
                                    rb.setChecked(false);
                            }

                            int index = buttonView.getId();
                            int value = Integer.valueOf((String) mEntryValues[index]);
                            mEditor.putInt("yourPref", value);

                            Dialog mDialog = getDialog();
                            mDialog.dismiss();
                        }
                    }
                });
            }
        }
    }
}
