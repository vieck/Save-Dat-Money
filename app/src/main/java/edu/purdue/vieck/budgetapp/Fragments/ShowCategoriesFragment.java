package edu.purdue.vieck.budgetapp.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import edu.purdue.vieck.budgetapp.Activities.CategoryEditingActivity;
import edu.purdue.vieck.budgetapp.Adapters.ShowCategoryAdapter;
import edu.purdue.vieck.budgetapp.CustomObjects.AddTree;
import edu.purdue.vieck.budgetapp.CustomObjects.RealmCategoryItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import edu.purdue.vieck.budgetapp.R;

/**
 * Created by vieck on 2/21/16.
 */
public class ShowCategoriesFragment extends Fragment {
    private ListView listView;
    private ShowCategoryAdapter categoryAdapter;
    private FloatingActionButton backButton, nextButton;
    private TypedValue primaryColorDark;
    private RealmHandler mRealmHandler;
    private Context context;
    private SharedPreferences mSharedPreferences;
    private Bundle bundle;
    private List<RealmCategoryItem> categoryItems;
    public boolean isCategory;
    private int prevSelection;
    public String category, subcategory;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_categories, container, false);
        if (getArguments() == null) {
            bundle = new Bundle();
        } else {
            bundle = getArguments();
        }
        context = getActivity();
        listView = (ListView) view.findViewById(R.id.listview);
        backButton = (FloatingActionButton) view.findViewById(R.id.fab_back);
        nextButton = (FloatingActionButton) view.findViewById(R.id.fab_next);
        mRealmHandler = new RealmHandler(getActivity());
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        isCategory = true;
        categoryItems = createTree();

        categoryAdapter = new ShowCategoryAdapter(getActivity(), categoryItems, bundle, ShowCategoriesFragment.this);

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(categoryAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                if (isCategory) {
                    alertDialogBuilder.setTitle("Are you sure you want to delete this category?");
                } else {
                    alertDialogBuilder.setTitle("Are you sure you want to delete this subcategory?");
                }
                    alertDialogBuilder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int pos) {
                        if (isCategory) {
                            mRealmHandler.deleteCategory(categoryItems.get(i).getCategory());
                            categoryItems = mRealmHandler.getCategoryParents();
                            categoryAdapter.setCategoryItems(categoryItems);
                        } else {
                            String subcategory = categoryItems.get(i).getSubcategory();
                            mRealmHandler.deleteSubcategory(subcategory);
                            categoryItems = mRealmHandler.getCategoryChildren(category);
                            categoryAdapter.setCategoryItems(categoryItems);
                        }
                        Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertDialogBuilder.create().show();
                return false;
            }
        });

        setupButtons();

        return view;
    }

    public void setupButtons() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryItems = mRealmHandler.getCategoryParents();
                categoryAdapter.setSelection(prevSelection);
                categoryAdapter.setCategoryItems(categoryItems);
                backButton.setVisibility(View.INVISIBLE);
                nextButton.setVisibility(View.VISIBLE);
                ((CategoryEditingActivity)getActivity()).changeSubcategoryVisibility(true);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = categoryAdapter.getSelection();
                if (position != -1) {
                    RealmCategoryItem item = categoryItems.get(position);
                    categoryItems = mRealmHandler.getCategoryChildren(item.getSubcategory());
                    categoryAdapter.setCategoryItems(categoryItems);
                    category = item.getCategory();
                    ((CategoryEditingActivity)getActivity()).setCategoryText(category);
                    categoryAdapter.setSelection(-1);
                    prevSelection = position;
                    isCategory = false;
                    nextButton.setVisibility(View.INVISIBLE);
                    backButton.setVisibility(View.VISIBLE);
                    ((CategoryEditingActivity)getActivity()).changeSubcategoryVisibility(false);
                } else {
                    Toast.makeText(getActivity(), "No Item Checked", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void refreshAdapter() {
        if (isCategory) {
            categoryItems = mRealmHandler.getCategoryParents();
            categoryAdapter.setCategoryItems(categoryItems);
        } else {
            categoryItems = mRealmHandler.getCategoryChildren(category);
            categoryAdapter.setCategoryItems(categoryItems);

        }
        categoryAdapter.notifyDataSetChanged();

        Log.d("CategoryFragment","Refreshed CategoryList");
    }

    public void changeCategory(String category) {
        ((CategoryEditingActivity)getActivity()).changeCategory(category);
    }

    private List<RealmCategoryItem> createTree() {

        Log.d("Count", "" + mRealmHandler.getCategoryCount());

        List<RealmCategoryItem> categoryParents = mRealmHandler.getCategoryParents();

        return categoryParents;
    }
}
