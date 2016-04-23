package edu.purdue.vieck.budgetapp.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lukedeighton.wheelview.WheelView;
import com.lukedeighton.wheelview.transformer.SimpleItemTransformer;

import java.util.ArrayList;
import java.util.List;

import edu.purdue.vieck.budgetapp.Adapters.AddCategoryAdapter;
import edu.purdue.vieck.budgetapp.Adapters.DrawableAdapter;
import edu.purdue.vieck.budgetapp.CustomObjects.RealmCategoryItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.Fragments.AddCategoryFragment;
import edu.purdue.vieck.budgetapp.Fragments.AddSubCategoryFragment;
import edu.purdue.vieck.budgetapp.Fragments.NewCategoryFragment;
import edu.purdue.vieck.budgetapp.Fragments.ShowCategoriesFragment;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by vieck on 2/19/16.
 */
public class CategoryEditingActivity extends AppCompatActivity {
    private FloatingActionButton mAddButton;
    private CoordinatorLayout coordinatorLayout;
    private Toolbar mToolbar;
    private ShowCategoriesFragment mCategoryFragment;
    private NewCategoryFragment mNewCategoryFragment;

    TypedValue primaryColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_editing);



        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.header_container);

        setupToolbar();

        setupAddButton();

        mCategoryFragment = new ShowCategoriesFragment();
        mNewCategoryFragment = new NewCategoryFragment();

        getFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_view_add, mNewCategoryFragment, "NewCategory")
                .add(R.id.fragment_view_categories, mCategoryFragment, "ViewCategory")
        .commit();
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

    public void setCategoryText(String categoryText) {
        mNewCategoryFragment.setCategoryEditText(categoryText);
    }

    private void setupAddButton() {
        mAddButton = (FloatingActionButton) findViewById(R.id.fab_add);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mCategoryFragment.isCategory) {
                    mNewCategoryFragment.addCategoryItem(false);
                } else {
                    mNewCategoryFragment.addCategoryItem(true);
                    Snackbar.make(coordinatorLayout, "Didn't select a subcategory",Snackbar.LENGTH_LONG).show();
                }
                mCategoryFragment.refreshAdapter();
            }
        });
    }

    public void changeSubcategoryVisibility(boolean up) {
        mNewCategoryFragment = (NewCategoryFragment) getFragmentManager().findFragmentByTag("NewCategory");
        mNewCategoryFragment.changeSubcategoryVisibility(up);
    }

    public void changeCategory(String category) {
        mNewCategoryFragment = (NewCategoryFragment) getFragmentManager().findFragmentByTag("NewCategory");
        mNewCategoryFragment.setCategoryEditText(category);
    }

    public void addedCategory() {
        mCategoryFragment = (ShowCategoriesFragment) getFragmentManager().findFragmentByTag("ViewCategory");
        mCategoryFragment.refreshAdapter();
        //getFragmentManager().beginTransaction().detach(mCategoryFragment).attach(new ShowCategoriesFragment()).detach(mNewCategoryFragment).attach(new NewCategoryFragment()).commit();;
    }

}
