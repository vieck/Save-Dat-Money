package edu.purdue.vieck.budgetapp.DatabaseAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

import edu.purdue.vieck.budgetapp.CustomObjects.RealmBudgetItem;
import edu.purdue.vieck.budgetapp.CustomObjects.RealmCategoryItem;
import edu.purdue.vieck.budgetapp.CustomObjects.RealmDataItem;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Stealth on 12/24/2015.
 */
public class RealmHandler {

    RealmConfiguration config;
    Realm realm;

    Context mContext;

    public RealmHandler(Context context) {
        mContext = context;
        config = new RealmConfiguration.Builder(context)
                .name("savedatmoney.realm")
                .schemaVersion(1)
                .build();
    }

    public void createRealm() {
        realm = Realm.getInstance(config);
    }


    public void add(final RealmCategoryItem categoryItem) {
        categoryItem.setKey(getCategoryCount() + 2);
        Log.d("Category Key", categoryItem.getKey() + "");
        createRealm();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(categoryItem);
        realm.commitTransaction();
        realm.close();
    }


    public void add(final RealmDataItem realmDataItem) {
        realmDataItem.setId(getCount() + 1);
        createRealm();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(realmDataItem);
        realm.commitTransaction();
        realm.close();
    }

    public void add(RealmBudgetItem realmBudgetItem) {
        createRealm();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(realmBudgetItem);
        realm.commitTransaction();
        realm.close();
    }

    public void update(final RealmCategoryItem realmCategoryItem) {
        createRealm();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(realmCategoryItem);
        realm.commitTransaction();
        realm.close();
    }

    public void update(final RealmDataItem realmDataItem) {
        createRealm();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(realmDataItem);
        realm.commitTransaction();
        realm.close();
    }


    public void update(final float budget, final int month, final int year) {
        createRealm();
        realm.beginTransaction();
        RealmQuery query = realm.where(RealmBudgetItem.class).equalTo("key", month + "/" + year);
        RealmResults<RealmBudgetItem> items = query.findAll();
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setBudget(budget);
        }
        realm.copyToRealmOrUpdate(items);
        realm.commitTransaction();
    }

    public RealmResults<RealmCategoryItem> getCategoryParents() {
        createRealm();
        return realm.where(RealmCategoryItem.class).equalTo("isChild", false).findAll();
    }

    public RealmResults<RealmCategoryItem> getCategoryChildren(String category) {
        createRealm();
        RealmQuery query = realm.where(RealmCategoryItem.class).equalTo("category", category).equalTo("isChild", true);
        return query.findAll();
    }

    public int getCategoryCount() {
        createRealm();
        RealmQuery query = realm.where(RealmCategoryItem.class);
        return (int) query.count();
    }

    public float getBudget() {
        createRealm();
        RealmQuery query = realm.where(RealmBudgetItem.class);
        return query.sum("budget").floatValue();
    }

    public float getBudget(int month, int year) {
        createRealm();
        RealmQuery query = realm.where(RealmBudgetItem.class).equalTo("key", month + "/" + year);
        RealmResults<RealmBudgetItem> results = query.findAll();
        if (results.size() < 1) {
            return 500;
        } else {
            return results.get(0).getBudget();
        }
    }

    public boolean isEmpty(int type) {
        createRealm();
        RealmQuery<RealmDataItem> query;
        if (type == 2) {
            return realm.isEmpty();
        } else if (type == 1) {
            query = realm.where(RealmDataItem.class).equalTo("type", true);
        } else {
            query = realm.where(RealmDataItem.class).equalTo("type", false);
        }

        return query.findAll().isEmpty();
    }

    public boolean isCategoriesEmpty() {
        createRealm();
        RealmQuery<RealmCategoryItem> query = realm.where(RealmCategoryItem.class);
        if (query.findAll().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public int getCount() {
        createRealm();
        return realm.where(RealmDataItem.class).findAll().size();
    }


//    public HashMap<Integer, List<RealmDataItem>> getAllYearsAsHashmap(int type) {
//        HashMap<Integer, List<RealmDataItem>> mDataset = new HashMap<>();
//        createRealm();
//        RealmQuery query = realm.where(RealmDataItem.class);
//        if (type == 1) {
//            query.equalTo("type", true);
//        } else {
//            query.equalTo("type", false);
//        }
//
//        RealmResults<RealmDataItem> results = query.findAll().sort("month", Sort.DESCENDING).sort("year", Sort.DESCENDING);
//
//        for (RealmDataItem realmDataItem : results) {
//            if (mDataset.get(realmDataItem.getYear()) == null) {
//                List<RealmDataItem> list = new ArrayList<>();
//                list.add(realmDataItem);
//                mDataset.put(realmDataItem.getYear(), list);
//            } else {
//                List<RealmDataItem> list = mDataset.get(realmDataItem.getYear());
//                list.add(realmDataItem);
//                mDataset.put(realmDataItem.getYear(), list);
//            }
//        }
//        return mDataset;
//    }

    public List<RealmDataItem> getAllUniqueMonthsAsList(int type) {
        List<RealmDataItem> items = new ArrayList<>();
        List<Integer> months = new ArrayList<>();
        final HashMap<Integer, List<Integer>> monthHashmap = new HashMap<>();
        createRealm();
        RealmQuery query;
        if (type == 2) {
            query = realm.where(RealmDataItem.class);
        } else if (type == 1) {
            query = realm.where(RealmDataItem.class).equalTo("type", true);
        } else {
            query = realm.where(RealmDataItem.class).equalTo("type", false);
        }
        RealmResults<RealmDataItem> results = query.findAll();
        results.sort("month", Sort.DESCENDING);
        results.sort("year", Sort.DESCENDING);
        for (RealmDataItem realmDataItem : results) {
            if (monthHashmap.get(realmDataItem.getYear()) == null) {
                List<Integer> list = new ArrayList<Integer>();
                list.add(realmDataItem.getMonth());
                items.add(realmDataItem);
                monthHashmap.put(realmDataItem.getYear(), list);
            } else {
                List<Integer> list = monthHashmap.get(realmDataItem.getYear());
                if (!list.contains(realmDataItem.getMonth())) {
                    list.add(realmDataItem.getMonth());
                    monthHashmap.put(realmDataItem.getYear(), list);
                    months.add(realmDataItem.getMonth());
                    items.add(realmDataItem);
                }
            }
        }
        return items;
    }

    public LinkedList<RealmDataItem> getAllUniqueMonthsAsLinkedList(int type) {
        final LinkedList<RealmDataItem> items = new LinkedList<>();
        final HashMap<Integer, List<Integer>> monthHashmap = new HashMap<>();
        createRealm();
        RealmQuery query;
        if (type == 2) {
            query = realm.where(RealmDataItem.class);
        } else if (type == 1) {
            query = realm.where(RealmDataItem.class).equalTo("type", true);
        } else {
            query = realm.where(RealmDataItem.class).equalTo("type", false);
        }
        RealmResults<RealmDataItem> results = query.findAll();
        results.sort("month", Sort.DESCENDING);
        results.sort("year", Sort.DESCENDING);
        for (RealmDataItem realmDataItem : results) {
            if (monthHashmap.get(realmDataItem.getYear()) == null) {
                List<Integer> list = new ArrayList<Integer>();
                list.add(realmDataItem.getMonth());
                items.add(realmDataItem);
                monthHashmap.put(realmDataItem.getYear(), list);
            } else {
                List<Integer> list = monthHashmap.get(realmDataItem.getYear());
                if (!list.contains(realmDataItem.getMonth())) {
                    list.add(realmDataItem.getMonth());
                    monthHashmap.put(realmDataItem.getYear(), list);
                    items.add(realmDataItem);
                }
            }
        }
        return items;
    }

    public HashMap<String, Float> getAllMonthsAsOneElement(int type) {
        final HashMap<String, Float> hashMap = new HashMap<>();
        createRealm();
        RealmQuery query;
        if (type == 2) {
            query = realm.where(RealmDataItem.class);
        } else if (type == 1) {
            query = realm.where(RealmDataItem.class).equalTo("type", true);
        } else {
            query = realm.where(RealmDataItem.class).equalTo("type", false);
        }
        RealmResults<RealmDataItem> results = query.findAll();
        results.sort("month", Sort.ASCENDING);
        results.sort("year", Sort.ASCENDING);
        for (RealmDataItem realmDataItem : results) {
            if (hashMap.get(realmDataItem.getMonth() + "/" + realmDataItem.getYear()) == null) {
                hashMap.put(realmDataItem.getMonth() + "/" + realmDataItem.getYear(), realmDataItem.getAmount());
            } else {
                Float tempItem = hashMap.get(realmDataItem.getMonth() + "/" + realmDataItem.getYear());
                tempItem += realmDataItem.getAmount();
                hashMap.put(realmDataItem.getMonth() + "/" + realmDataItem.getYear(), tempItem);
            }
        }
        return hashMap;
    }

    public RealmResults<RealmDataItem> getResultsByFilter() {
        createRealm();

        RealmQuery query = realm.where(RealmDataItem.class);

//        if (type == 1) {
//            query.equalTo("type", true);
//        } else {
//            query.equalTo("type", false);
//        }

        return query.findAll().sort("day", Sort.DESCENDING).sort("month", Sort.DESCENDING).sort("year", Sort.DESCENDING);
    }

    public RealmResults<RealmDataItem> getResultsByFilter(int year, int type) {
        createRealm();
        RealmQuery query = realm.where(RealmDataItem.class);
        query.equalTo("year", year);
        if (type == 1) {
            query.equalTo("type", true);
        } else if (type == 0) {
            query.equalTo("type", false);
        }
        return query.findAll().sort("day", Sort.DESCENDING).sort("month", Sort.DESCENDING);
    }

    public RealmResults<RealmDataItem> getResultsByFilter(int month, int year, int type) {
        createRealm();
        RealmQuery query = realm.where(RealmDataItem.class);
        query.equalTo("month", month).equalTo("year", year);
        if (type == 1) {
            query.equalTo("type", true);
        } else if (type == 0) {
            query.equalTo("type", false);
        }
        return query.findAll().sort("day", Sort.DESCENDING).sort("month", Sort.DESCENDING);
    }

    public RealmResults<RealmDataItem> getResultsByFilter(int day, int month, int year, int type) {
        createRealm();
        RealmQuery query = realm.where(RealmDataItem.class);

        query.equalTo("day", day).equalTo("month", month).equalTo("year", year);

        if (type == 1) {
            query.equalTo("type", true);
        } else if (type == 0) {
            query.equalTo("type", false);
        }
        return query.findAll();
    }

    public RealmResults<RealmDataItem> getResultsByFilter(int startDay, int endDay, int firstMonth, int secondMonth, int year, int type) {
        createRealm();
        RealmQuery query = realm.where(RealmDataItem.class);

        if (firstMonth != secondMonth) {
            query.beginGroup()
                    .between("day", startDay - 1, 32)
                    .or()
                    .between("day", 0, endDay + 1)
                    .endGroup();
            query.beginGroup().equalTo("month",firstMonth).or().equalTo("month",secondMonth).endGroup();
        } else {
            query.beginGroup()
                    .greaterThanOrEqualTo("day", startDay)
                    .or()
                    .lessThanOrEqualTo("day", endDay)
                    .endGroup();
            query.equalTo("year",firstMonth);
        }
        query.equalTo("year", year);

        if (type == 1) {
            query.equalTo("type", true);
        } else if (type == 0) {
            query.equalTo("type", false);
        }
        return query.findAll();
    }

    public float getSpecificDateAmount(int month, int year, int type) {
        float amount = 0;
        createRealm();
        RealmQuery query;
        if (type == 2) {
            query = realm.where(RealmDataItem.class).equalTo("month", month).equalTo("year", year);
        } else if (type == 1) {
            query = realm.where(RealmDataItem.class).equalTo("type", true).equalTo("month", month).equalTo("year", year);
        } else {
            query = realm.where(RealmDataItem.class).equalTo("type", false).equalTo("month", month).equalTo("year", year);
        }
        RealmResults<RealmDataItem> results = query.findAll();
        for (RealmDataItem realmDataItem : results) {
            amount += realmDataItem.getAmount();
        }
        return (float) (Math.round(amount * 100) / 100.00);
    }

    public float[] getAllDataAsArray(int type) {
        createRealm();
        RealmQuery query;
        getAllUniqueMonthsAsList(type);
        if (type == 2) {
            query = realm.where(RealmDataItem.class);
        } else if (type == 1) {
            query = realm.where(RealmDataItem.class).equalTo("type", true);
        } else {
            query = realm.where(RealmDataItem.class).equalTo("type", false);
        }
        RealmResults<RealmDataItem> results = query.findAll();
        float[] data = new float[results.size()];
        int i = 0;
        for (RealmDataItem realmDataItem : results) {
            data[i++] = realmDataItem.getAmount();
        }
        return data;
    }

    public float[] getListOfDays(String category, int month, int year, int type) {
        float[] days;
        if (month == 2) {
            days = new float[28];
        } else if (month % 2 == 1) {
            days = new float[30];
        } else {
            days = new float[31];
        }
        createRealm();
        RealmQuery query;
        if (type == 2) {
            query = realm.where(RealmDataItem.class).equalTo("month", month).equalTo("year", year);
        } else if (type == 1) {
            query = realm.where(RealmDataItem.class).equalTo("type", true).equalTo("month", month).equalTo("year", year);
        } else {
            query = realm.where(RealmDataItem.class).equalTo("type", false).equalTo("month", month).equalTo("year", year);
        }
        RealmResults<RealmDataItem> results = query.findAll();
        for (RealmDataItem item : results) {
            days[item.getDay() - 1] += item.getAmount();
        }
        return days;
    }

    public float getSpecificDateAmountByType(String category, int month, int year, int type) {
        float categoryPercent = 0;
        createRealm();
        RealmQuery query;
        if (month != -1 && year != -1) {
            if (type == 2) {
                query = realm.where(RealmDataItem.class).equalTo("category", category).beginGroup().equalTo("month", month).equalTo("year", year).endGroup();
            } else if (type == 1) {
                query = realm.where(RealmDataItem.class).equalTo("type", true).equalTo("category", category).beginGroup().equalTo("month", month).equalTo("year", year).endGroup();
            } else {
                query = realm.where(RealmDataItem.class).equalTo("type", false).equalTo("category", category).beginGroup().equalTo("month", month).equalTo("year", year).endGroup();
            }
        } else {
            if (type == 2) {
                query = realm.where(RealmDataItem.class).equalTo("category", category);
            } else if (type == 1) {
                query = realm.where(RealmDataItem.class).equalTo("type", true).equalTo("category", category);
            } else {
                query = realm.where(RealmDataItem.class).equalTo("type", false).equalTo("category", category);
            }

        }
        RealmResults<RealmDataItem> results = query.findAll();
        results.sort("month", Sort.DESCENDING);
        results.sort("year", Sort.DESCENDING);
        for (RealmDataItem realmDataItem : results) {
            categoryPercent += realmDataItem.getAmount();
        }
        return (float) (Math.round(categoryPercent * 100) / 100.00);
    }

    public void delete(final RealmDataItem realmDataItem) {
        createRealm();
        realm.beginTransaction();
        realm.where(RealmDataItem.class).equalTo("id", realmDataItem.getId()).findFirst().deleteFromRealm();
        if (realm.where(RealmDataItem.class).equalTo("month", realmDataItem.getMonth()).equalTo("year", realmDataItem.getYear()).findAll().isEmpty()) {
            realm.delete(RealmBudgetItem.class);
        }
        realm.commitTransaction();
        realm.close();
    }

    public void delete(final RealmBudgetItem realmBudgetItem) {
        createRealm();
        realm.beginTransaction();
        realm.where(RealmBudgetItem.class).equalTo("key", realmBudgetItem.getKey()).findAll().clear();
        realm.commitTransaction();
        realm.close();
    }

    public void delete(final RealmCategoryItem realmCategoryItem) {
        createRealm();
        realm.beginTransaction();
        realm.where(RealmCategoryItem.class).equalTo("key", realmCategoryItem.getKey()).findAll().clear();
        realm.commitTransaction();
        realm.close();
    }

    public void deleteCategory(String category) {
        createRealm();
        realm.beginTransaction();
        realm.where(RealmCategoryItem.class).equalTo("category", category).findAll().clear();
        realm.commitTransaction();
        realm.close();
    }

    public void deleteSubcategory(final String subcategory) {
        createRealm();
        realm.beginTransaction();
        realm.where(RealmCategoryItem.class).equalTo("subcategory", subcategory).findAll().clear();
        realm.commitTransaction();
        realm.close();
    }

    public void deleteAll() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                createRealm();
                realm.beginTransaction();
                realm.delete(RealmBudgetItem.class);
                realm.delete(RealmDataItem.class);
                realm.delete(RealmCategoryItem.class);
                realm.commitTransaction();
                realm.close();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException ex) {
            Log.e("Thread", "Delete thread interrupted");
        } finally {
            Intent intent = ((Activity) mContext).getIntent();
            ((Activity) mContext).finish();
            mContext.startActivity(intent);
        }
    }


}
