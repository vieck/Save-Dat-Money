package edu.purdue.vieck.budgetapp.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lukedeighton.wheelview.WheelView;
import com.lukedeighton.wheelview.adapter.WheelAdapter;
import com.lukedeighton.wheelview.transformer.SimpleItemTransformer;

import java.util.ArrayList;
import java.util.List;

import edu.purdue.vieck.budgetapp.Adapters.AddCategoryAdapter;
import edu.purdue.vieck.budgetapp.Adapters.DrawableAdapter;
import edu.purdue.vieck.budgetapp.CustomObjects.RealmCategoryItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by vieck on 2/19/16.
 */
public class CategoryEditingActivity extends AppCompatActivity {
    private AddCategoryAdapter mAdapter;
    private FloatingActionButton mAddButton;
    private Toolbar mToolbar;
    private ListView mListView;
    List<Drawable> mDrawablesList;
    private List<RealmCategoryItem> mCategoryList;
    private RealmHandler mRealmHandler;
    private SharedPreferences mSharedPreferences;
    private Context mContext;
    private int actionBarColor;
    TypedValue primaryColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_editing);
        mListView = (ListView) findViewById(R.id.listview);
        mAddButton = (FloatingActionButton) findViewById(R.id.fab_add);

        mContext = this;

        setupToolbar();

        mRealmHandler = new RealmHandler(this);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        actionBarColor = mSharedPreferences.getInt("actionBarColor",getResources().getColor(R.color.md_black_1000));
        primaryColor = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimaryDark, primaryColor, true);

        mCategoryList = mRealmHandler.getCategoryParents();
        Log.d("Categories",Integer.toString(mCategoryList.size()));
        mAdapter = new AddCategoryAdapter(this, mRealmHandler, mCategoryList, actionBarColor, primaryColor.data);

        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mAdapter.updateSelection(position);
            }
        });

        setupAddButton();
    }

    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (mToolbar != null) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

        }
    }

    public void setupAddButton() {
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getParent().getApplicationContext(), "Show Dialog Popup for Creating Category.", Toast.LENGTH_LONG).show();
                final Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.add_category_dialog);
                dialog.setTitle("Add A Category");
                final EditText categoryEditText = (EditText) dialog.findViewById(R.id.edittext_add_category_name);
                final WheelView wheelView = (WheelView) dialog.findViewById(R.id.wheel_view);

                int[] drawables = {R.drawable.food_groceries_dark, R.drawable.utility_misc_dark, R.drawable.entertainment_dark, R.drawable.medical_misc_dark, R.drawable.insurance_dark, R.drawable.graph_dark};
                int[] groceryDrawables = {R.drawable.food_groceries_dark, R.drawable.food_groceries_dark, R.drawable.food_groceries_dark};
                int[] utilitiesDrawables = {R.drawable.utility_misc_dark, R.drawable.utility_gas_dark, R.drawable.landline_phone_bill_dark, R.drawable.cell_phone_bill_dark,
                        R.drawable.entertainment_web_dark, R.drawable.satellite_bill_dark, R.drawable.utility_water_dark, R.drawable.utility_trash_dark, R.drawable.utility_misc_dark};
                int[] entertainmentDrawables = {R.drawable.entertainment_theater_dark, R.drawable.entertainment_vaction_dark, R.drawable.digital_media_dark, R.drawable.digital_music_dark, R.drawable.ecommerce_dark, R.drawable.entertainment_dark};
                int[] medicalDrawables = {R.drawable.medical_visit_dark, R.drawable.emergence_room_dark, R.drawable.medical_prescription_dark, R.drawable.medical_misc_dark};
                int[] insuranceDrawables = {R.drawable.home_insurance_dark, R.drawable.car_insurance_dark, R.drawable.medical_misc_dark, R.drawable.life_insurance_dark};

                mDrawablesList = new ArrayList<Drawable>();
                for (int i = 0; i < drawables.length; i++) {
                    mDrawablesList.add(ContextCompat.getDrawable(mContext, drawables[i]));
                }
                for (int i = 0; i < utilitiesDrawables.length; i++) {
                    mDrawablesList.add(ContextCompat.getDrawable(mContext, utilitiesDrawables[i]));
                }
                for (int i = 0; i < entertainmentDrawables.length; i++) {
                    mDrawablesList.add(ContextCompat.getDrawable(mContext, entertainmentDrawables[i]));
                }
                for (int i = 0; i < medicalDrawables.length; i++) {
                    mDrawablesList.add(ContextCompat.getDrawable(mContext, medicalDrawables[i]));
                }
                for (int i = 0; i < insuranceDrawables.length; i++) {
                    mDrawablesList.add(ContextCompat.getDrawable(mContext, insuranceDrawables[i]));
                }
                final DrawableAdapter drawableAdapter = new DrawableAdapter(mDrawablesList);
                wheelView.setAdapter(drawableAdapter);

                wheelView.setOnWheelItemClickListener(new WheelView.OnWheelItemClickListener() {
                    @Override
                    public void onWheelItemClick(WheelView parent, int position, boolean isSelected) {
                        parent.setWheelItemTransformer(new SimpleItemTransformer());
                        parent.setPosition(position);

                    }
                });

                Button confirm = (Button) dialog.findViewById(R.id.confirm_button);
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mRealmHandler.add(new RealmCategoryItem(categoryEditText.getText().toString(),"",R.drawable.cell_phone_bill_dark,R.color.md_light_green_A400, false));
                        mContext.startActivity(getIntent());
                        finish();
                        dialog.dismiss();
                    }
                });

                Button cancel = (Button) dialog.findViewById(R.id.cancel_button);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }
}
