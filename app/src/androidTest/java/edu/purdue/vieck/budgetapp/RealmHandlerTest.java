package edu.purdue.vieck.budgetapp;

import android.app.Instrumentation;
import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.AndroidJUnitRunner;
import android.test.AndroidTestCase;

import org.junit.Test;
import org.junit.runner.RunWith;

import edu.purdue.vieck.budgetapp.CustomObjects.RealmCategoryItem;
import edu.purdue.vieck.budgetapp.CustomObjects.RealmDataItem;
import edu.purdue.vieck.budgetapp.DatabaseAdapters.RealmHandler;
import io.realm.OrderedRealmCollection;
import io.realm.RealmResults;

@RunWith(AndroidJUnit4.class)
public class RealmHandlerTest extends AndroidJUnitRunner {

    Resources res;
    private String[] categories;
    private String[] foodCategories;
    private String[] utilityCategories;
    private String[] entertainmentCategories;
    private String[] medicalCategories;
    private String[] insuranceCategories;

    public RealmHandlerTest() {
        super();
        res = InstrumentationRegistry.getTargetContext().getResources();
        categories = res.getStringArray(R.array.categoryarray);
        foodCategories = res.getStringArray(R.array.subgroceryarray);
        utilityCategories = res.getStringArray(R.array.subutilityarray);
        entertainmentCategories = res.getStringArray(R.array.subentertainmentarray);
        medicalCategories = res.getStringArray(R.array.submedicalarray);
        insuranceCategories = res.getStringArray(R.array.subinsurancearray);
    }

    @Test
    public void fillRealmWithAllData() {
        fillRealmWithFakeIncomeData();
        fillRealmWithFakeExpenseData();
    }

    private void fillRealmWithFakeIncomeData() {
        RealmHandler realm = new RealmHandler(InstrumentationRegistry.getTargetContext());
        RealmDataItem realmDataItem;
        RealmResults<RealmCategoryItem> subCategories;
        // inflate the food category
        subCategories = realm.getCategoryChildren(categories[0]);
        for (int i = 0; i < foodCategories.length; i++) {
            realmDataItem = new RealmDataItem(150f, categories[0], foodCategories[i], true, 17, 7, 2016, "Food Category Test", subCategories.get(i).getIcon());
            realm.add(realmDataItem);
        }
        // inflate the utility category
        subCategories = realm.getCategoryChildren(categories[1]);
        for (int i = 0; i < utilityCategories.length; i++) {
            realmDataItem = new RealmDataItem(150f, categories[1], utilityCategories[i], true, 17, 7, 2016, "Utility Category Test", subCategories.get(i).getIcon());
            realm.add(realmDataItem);
        }
        // inflate the entertainment category
        subCategories = realm.getCategoryChildren(categories[2]);
        for (int i = 0; i < entertainmentCategories.length; i++) {
            realmDataItem = new RealmDataItem(150f, categories[2], entertainmentCategories[i], true, 17, 7, 2016, "Entertainment Category Test", subCategories.get(i).getIcon());
            realm.add(realmDataItem);
        }
        // inflate the medical category
        subCategories = realm.getCategoryChildren(categories[3]);
        for (int i = 0; i < medicalCategories.length; i++) {
            realmDataItem = new RealmDataItem(150f, categories[3], medicalCategories[i], true, 17, 7, 2016, "Medical Category Test", subCategories.get(i).getIcon());
            realm.add(realmDataItem);
        }
        // inflate the insurance category
        subCategories = realm.getCategoryChildren(categories[4]);
        for (int i = 0; i < insuranceCategories.length; i++) {
            realmDataItem = new RealmDataItem(150f, categories[4], insuranceCategories[i], true, 17, 7, 2016, "Insurance Category Test", subCategories.get(i).getIcon());
            realm.add(realmDataItem);
        }
    }

    @Test
    public void fillRealmWithFakeExpenseData() {
        RealmHandler realm = new RealmHandler(InstrumentationRegistry.getTargetContext());
        RealmDataItem realmDataItem;
        RealmResults<RealmCategoryItem> subCategories;
        // inflate the food category
        subCategories = realm.getCategoryChildren(categories[0]);
        for (int i = 0; i < foodCategories.length; i++) {
            realmDataItem = new RealmDataItem(150f, categories[0], foodCategories[i], false, 17, 7, 2016, "Food Category Test", subCategories.get(i).getIcon());
            realm.add(realmDataItem);
        }
        // inflate the utility category
        subCategories = realm.getCategoryChildren(categories[1]);
        for (int i = 0; i < utilityCategories.length; i++) {
            realmDataItem = new RealmDataItem(150f, categories[1], utilityCategories[i], false, 17, 7, 2016, "Utility Category Test", subCategories.get(i).getIcon());
            realm.add(realmDataItem);
        }
        // inflate the entertainment category
        subCategories = realm.getCategoryChildren(categories[2]);
        for (int i = 0; i < entertainmentCategories.length; i++) {
            realmDataItem = new RealmDataItem(150f, categories[2], entertainmentCategories[i], false, 17, 7, 2016, "Entertainment Category Test", subCategories.get(i).getIcon());
            realm.add(realmDataItem);
        }
        // inflate the medical category
        subCategories = realm.getCategoryChildren(categories[3]);
        for (int i = 0; i < medicalCategories.length; i++) {
            realmDataItem = new RealmDataItem(150f, categories[3], medicalCategories[i], false, 17, 7, 2016, "Medical Category Test", subCategories.get(i).getIcon());
            realm.add(realmDataItem);
        }
        // inflate the insurance category
        subCategories = realm.getCategoryChildren(categories[4]);
        for (int i = 0; i < insuranceCategories.length; i++) {
            realmDataItem = new RealmDataItem(150f, categories[4], insuranceCategories[i], false, 17, 7, 2016, "Insurance Category Test", subCategories.get(i).getIcon());
            realm.add(realmDataItem);
        }
    }

    @Test
    public void removeAllFakeData() {
        RealmHandler realm = new RealmHandler(InstrumentationRegistry.getTargetContext());
        realm.deleteAll();
    }
}
