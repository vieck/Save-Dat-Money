package edu.purdue.vieck.budgetapp.Fragments;


import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.lukedeighton.wheelview.WheelView;
import com.lukedeighton.wheelview.transformer.SimpleItemTransformer;

import java.util.ArrayList;
import java.util.List;

import edu.purdue.vieck.budgetapp.Activities.CategoryEditingActivity;
import edu.purdue.vieck.budgetapp.Adapters.DrawableAdapter;
import edu.purdue.vieck.budgetapp.CustomObjects.RealmCategoryItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewCategoryFragment extends android.app.Fragment {

    private EditText mCategoryEditText;
    private EditText mSubcategoryEditText;
    private WheelView mWheelView;

    private List<Drawable> mDrawablesList;
    private List<Integer> mDrawableIds;
    private RealmHandler mRealmHandler;
    private SharedPreferences mSharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_category, container, false);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        mCategoryEditText = (EditText) view.findViewById(R.id.edittext_add_category_name);

        mSubcategoryEditText = (EditText) view.findViewById(R.id.edittext_add_subcategory_name);

        mWheelView = (WheelView) view.findViewById(R.id.wheel_view);

        mRealmHandler = new RealmHandler(getActivity());

        setupWheelView();

        return view;
    }

    public void setCategoryEditText(String category) {
        mCategoryEditText.setText(category);
    }

    public void changeSubcategoryVisibility(boolean up) {
        if (up) {
            mSubcategoryEditText.setText("");
            mSubcategoryEditText.setVisibility(View.INVISIBLE);
        } else {
            mSubcategoryEditText.setVisibility(View.VISIBLE);
        }
    }

    private void setupWheelView() {
        int[] drawables = {R.drawable.food_groceries_dark, R.drawable.utility_misc_dark, R.drawable.entertainment_dark, R.drawable.medical_misc_dark, R.drawable.insurance_dark, R.drawable.graph_dark};
        int[] groceryDrawables = {R.drawable.food_groceries_dark, R.drawable.food_groceries_dark, R.drawable.food_groceries_dark};
        int[] utilitiesDrawables = {R.drawable.utility_misc_dark, R.drawable.utility_gas_dark, R.drawable.landline_phone_bill_dark, R.drawable.cell_phone_bill_dark,
                R.drawable.entertainment_web_dark, R.drawable.satellite_bill_dark, R.drawable.utility_water_dark, R.drawable.utility_trash_dark, R.drawable.utility_misc_dark};
        int[] entertainmentDrawables = {R.drawable.entertainment_theater_dark, R.drawable.entertainment_vaction_dark, R.drawable.digital_media_dark, R.drawable.digital_music_dark, R.drawable.ecommerce_dark, R.drawable.entertainment_dark};
        int[] medicalDrawables = {R.drawable.medical_visit_dark, R.drawable.emergence_room_dark, R.drawable.medical_prescription_dark, R.drawable.medical_misc_dark};
        int[] insuranceDrawables = {R.drawable.home_insurance_dark, R.drawable.car_insurance_dark, R.drawable.medical_misc_dark, R.drawable.life_insurance_dark};
        mDrawablesList = new ArrayList<Drawable>();
        mDrawableIds = new ArrayList<>();
        for (int i = 0; i < drawables.length; i++) {
            mDrawablesList.add(ContextCompat.getDrawable(getActivity(), drawables[i]));
            mDrawableIds.add(drawables[i]);
        }
        for (int i = 0; i < utilitiesDrawables.length; i++) {
            mDrawablesList.add(ContextCompat.getDrawable(getActivity(), utilitiesDrawables[i]));
            mDrawableIds.add(utilitiesDrawables[i]);
        }
        for (int i = 0; i < entertainmentDrawables.length; i++) {
            mDrawablesList.add(ContextCompat.getDrawable(getActivity(), entertainmentDrawables[i]));
            mDrawableIds.add(entertainmentDrawables[i]);
        }
        for (int i = 0; i < medicalDrawables.length; i++) {
            mDrawablesList.add(ContextCompat.getDrawable(getActivity(), medicalDrawables[i]));
            mDrawableIds.add(medicalDrawables[i]);
        }
        for (int i = 0; i < insuranceDrawables.length; i++) {
            mDrawablesList.add(ContextCompat.getDrawable(getActivity(), insuranceDrawables[i]));
            mDrawableIds.add(insuranceDrawables[i]);
        }
        final DrawableAdapter drawableAdapter = new DrawableAdapter(mDrawablesList);
        mWheelView.setAdapter(drawableAdapter);

        mWheelView.setOnWheelItemClickListener(new WheelView.OnWheelItemClickListener() {
            @Override
            public void onWheelItemClick(WheelView parent, int position, boolean isSelected) {
                parent.setWheelItemTransformer(new SimpleItemTransformer());
                parent.setPosition(position);
            }
        });
    }

    public void addCategoryItem(boolean isCategory) {
        int icon = mDrawableIds.get(mWheelView.getSelectedPosition());
        RealmCategoryItem item = new RealmCategoryItem(mCategoryEditText.getText().toString(), mSubcategoryEditText.getText().toString(), icon, R.color.md_white_1000, !isCategory);
        mRealmHandler.add(item);
        mCategoryEditText.setText("");
        mSubcategoryEditText.setText("");
        mWheelView.setPosition(0);
        ((CategoryEditingActivity)getActivity()).addedCategory();
    }

}
