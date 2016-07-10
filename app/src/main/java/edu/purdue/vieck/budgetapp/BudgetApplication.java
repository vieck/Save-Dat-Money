package edu.purdue.vieck.budgetapp;

import android.app.Application;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import edu.purdue.vieck.budgetapp.CustomObjects.RealmCategoryItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import io.realm.RealmResults;
import okhttp3.OkHttpClient;

/**
 * Created by mvieck on 7/2/16.
 */

public class BudgetApplication extends Application {
    private RealmHandler mRealmHandler;

    public void onCreate() {
        super.onCreate();
        // Create an InitializerBuilder
        Stetho.InitializerBuilder initializerBuilder =
                Stetho.newInitializerBuilder(this);

// Enable Chrome DevTools
        initializerBuilder.enableWebKitInspector(
                Stetho.defaultInspectorModulesProvider(this)
        );

// Enable command line interface
        initializerBuilder.enableDumpapp(
                Stetho.defaultDumperPluginsProvider(this)
        );

// Use the InitializerBuilder to generate an Initializer
        Stetho.Initializer initializer = initializerBuilder.build();

// Initialize Stetho with the Initializer
        Stetho.initialize(initializer);

        mRealmHandler = new RealmHandler(this);
        createTree();
    }

    private void createTree() {

        if (mRealmHandler.isCategoriesEmpty()) {
            String[] categoryNames = getResources().getStringArray(R.array.categoryarray);
            String[] subCategoryFood = getResources().getStringArray(R.array.subgroceryarray);
            String[] subCategoryUtility = getResources().getStringArray(R.array.subutilityarray);
            String[] subCategoryEntertainment = getResources().getStringArray(R.array.subentertainmentarray);
            String[] subCategoryMedical = getResources().getStringArray(R.array.submedicalarray);
            String[] subCategoryInsurance = getResources().getStringArray(R.array.subinsurancearray);
            int[] drawables = {R.drawable.food_groceries_dark, R.drawable.utility_misc_dark, R.drawable.entertainment_dark, R.drawable.medical_misc_dark, R.drawable.insurance_dark, R.drawable.graph_dark};
            int[] groceryDrawables = {R.drawable.food_groceries_dark, R.drawable.food_groceries_dark, R.drawable.food_groceries_dark};
            int[] utilitiesDrawables = {R.drawable.utility_misc_dark, R.drawable.utility_gas_dark, R.drawable.landline_phone_bill_dark, R.drawable.cell_phone_bill_dark,
                    R.drawable.entertainment_web_dark, R.drawable.satellite_bill_dark, R.drawable.utility_water_dark, R.drawable.utility_trash_dark, R.drawable.utility_misc_dark};
            int[] entertainmentDrawables = {R.drawable.entertainment_theater_dark, R.drawable.entertainment_vaction_dark, R.drawable.digital_media_dark, R.drawable.digital_music_dark, R.drawable.ecommerce_dark, R.drawable.entertainment_dark};
            int[] medicalDrawables = {R.drawable.medical_visit_dark, R.drawable.emergence_room_dark, R.drawable.medical_prescription_dark, R.drawable.medical_misc_dark};
            int[] insuranceDrawables = {R.drawable.home_insurance_dark, R.drawable.car_insurance_dark, R.drawable.medical_misc_dark, R.drawable.life_insurance_dark};
            RealmCategoryItem categoryItem, subCategoryItem;
            for (int i = 0; i < drawables.length; i++) {
                switch (i) {
                    case 0:
                        categoryItem = new RealmCategoryItem(categoryNames[i], categoryNames[0], drawables[i], getResources().getColor(R.color.md_green_300), false);
                        // Grocery Sub List
                        for (int j = 0; j < subCategoryFood.length; j++) {
                            subCategoryItem = new RealmCategoryItem(categoryNames[0], subCategoryFood[j], groceryDrawables[j], getResources().getColor(R.color.md_green_300), true);
                            mRealmHandler.add(subCategoryItem);
                        }
                        mRealmHandler.add(categoryItem);
                        break;
                    case 1:
                        categoryItem = new RealmCategoryItem(categoryNames[1], categoryNames[i], drawables[i], getResources().getColor(R.color.md_blue_300), false);

                        // Utility Sub List
                        for (int j = 0; j < subCategoryUtility.length; j++) {
                            subCategoryItem = new RealmCategoryItem(categoryNames[i], subCategoryUtility[j], utilitiesDrawables[j], getResources().getColor(R.color.md_blue_300), true);
                            mRealmHandler.add(subCategoryItem);
                        }
                        mRealmHandler.add(categoryItem);
                        break;
                    case 2:
                        categoryItem = new RealmCategoryItem(categoryNames[i], categoryNames[i], drawables[i], getResources().getColor(R.color.md_yellow_300), false);

                        // Entertainment Sub List
                        for (int j = 0; j < subCategoryEntertainment.length; j++) {
                            subCategoryItem = new RealmCategoryItem(categoryNames[i], subCategoryEntertainment[j], entertainmentDrawables[j], getResources().getColor(R.color.md_yellow_300), true);
                            mRealmHandler.add(subCategoryItem);
                        }
                        mRealmHandler.add(categoryItem);
                        break;
                    case 3:
                        categoryItem = new RealmCategoryItem(categoryNames[i], categoryNames[i], drawables[i], getResources().getColor(R.color.md_red_300), false);

                        // Medical Sub List
                        for (int j = 0; j < subCategoryMedical.length; j++) {
                            subCategoryItem = new RealmCategoryItem(categoryNames[i], subCategoryMedical[j], medicalDrawables[j], getResources().getColor(R.color.md_red_300), true);
                            mRealmHandler.add(subCategoryItem);
                        }
                        mRealmHandler.add(categoryItem);
                        break;
                    case 4:
                        categoryItem = new RealmCategoryItem(categoryNames[i], categoryNames[i], drawables[i], getResources().getColor(R.color.md_purple_300), false);
                        // Insurance Sub List
                        for (int j = 0; j < subCategoryInsurance.length; j++) {
                            subCategoryItem = new RealmCategoryItem(categoryNames[i], subCategoryInsurance[j], insuranceDrawables[j], getResources().getColor(R.color.md_purple_300), true);
                            mRealmHandler.add(subCategoryItem);
                        }
                        mRealmHandler.add(categoryItem);

                }
            }
        }

        Log.d("Count", "" + mRealmHandler.getCategoryCount());
    }
}
