package edu.purdue.vieck.budgetapp.DatabaseAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

import edu.purdue.vieck.budgetapp.CustomObjects.RealmBudgetItem;
import edu.purdue.vieck.budgetapp.CustomObjects.RealmCategoryItem;
import edu.purdue.vieck.budgetapp.CustomObjects.RealmDataItem;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Stealth on 12/24/2015.
 */
public class RealmHandler {

    private static AtomicInteger id;
    Realm realm;

    Context mContext;

    public RealmHandler(Context context) {
        mContext = context;
        id = new AtomicInteger();
    }

    public void add(final RealmCategoryItem categoryItem) {
                categoryItem.setKey(getCategoryCount() + 1);
                realm = Realm.getInstance(mContext);
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(categoryItem);
                realm.commitTransaction();
                realm.close();
    }


    public void add(final RealmDataItem realmDataItem) {
        realmDataItem.setId(getCount() + 1);
        realm = Realm.getInstance(mContext);
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(realmDataItem);
        realm.commitTransaction();
        realm.close();
    }

    public void add(RealmBudgetItem realmBudgetItem) {
        realm = Realm.getInstance(mContext);
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(realmBudgetItem);
        realm.commitTransaction();
        realm.close();
    }

    public void update(final RealmCategoryItem realmCategoryItem) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                realm = Realm.getInstance(mContext);
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(realmCategoryItem);
                realm.commitTransaction();
                realm.close();
            }
        });
        thread.start();
    }
    public void update(final RealmDataItem realmDataItem) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                realm = Realm.getInstance(mContext);
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(realmDataItem);
                realm.commitTransaction();
                realm.close();
            }
        });
        thread.start();
    }



    public void update(final float budget, final int month, final int year) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                realm = Realm.getInstance(mContext);
                realm.beginTransaction();
                RealmQuery query = realm.where(RealmBudgetItem.class).equalTo("key", month + "/" + year);
                RealmResults<RealmBudgetItem> items = query.findAll();
                for (int i = 0; i < items.size(); i++) {
                    items.get(i).setBudget(budget);
                }
                realm.copyToRealmOrUpdate(items);
                realm.commitTransaction();
            }
        });
        thread.start();
    }

    public List<RealmCategoryItem> getCategoryParents() {
        realm = Realm.getInstance(mContext);
        RealmQuery query = realm.where(RealmCategoryItem.class).equalTo("isChild",false);
        RealmResults<RealmCategoryItem> results = query.findAll();
        List<RealmCategoryItem> items = new ArrayList<>();
        for (RealmCategoryItem item : results) {
            items.add(item);
        }
        return items;
    }

    public List<RealmCategoryItem> getCategoryChildren(String category) {
        realm = Realm.getInstance(mContext);
        RealmQuery query = realm.where(RealmCategoryItem.class).equalTo("category",category);
        RealmResults<RealmCategoryItem> results = query.findAll();
        List<RealmCategoryItem> items = new ArrayList<>();
        for (RealmCategoryItem item : results) {
            items.add(item);
        }
        return items;
    }

    public int getCategoryCount() {
        realm = Realm.getInstance(mContext);
        RealmQuery query = realm.where(RealmCategoryItem.class);
        return (int)query.count();
    }

    public float getBudget() {
        realm = Realm.getInstance(mContext);
        RealmQuery query = realm.where(RealmBudgetItem.class);
        return query.sum("budget").floatValue();
    }

    public float getBudget(int month, int year) {
        realm = Realm.getInstance(mContext);
        RealmQuery query = realm.where(RealmBudgetItem.class).equalTo("key", month + "/" + year);
        RealmResults<RealmBudgetItem> results = query.findAll();
        if (results.size() < 1) {
            return 500;
        } else {
            return results.get(0).getBudget();
        }
    }

    public boolean isEmpty(int type) {
        realm = Realm.getInstance(mContext);
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
        realm = Realm.getInstance(mContext);
        RealmQuery<RealmCategoryItem> query = realm.where(RealmCategoryItem.class);
        if (query.findAll().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public int getCount() {
        realm = Realm.getInstance(mContext);
        return realm.where(RealmDataItem.class).findAll().size();
    }


    public Stack<RealmDataItem> getAllDataAsStack(int type) {
        Stack<RealmDataItem> mDataset = new Stack<>();
        realm = Realm.getInstance(mContext);
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
        results.sort("year", Sort.DESCENDING);

        for (RealmDataItem item : results) {
            mDataset.push(item);
        }

        return mDataset;
    }

    public HashMap<Integer, List<RealmDataItem>> getAllYearsAsHashmap(int type) {
        HashMap<Integer, List<RealmDataItem>> mDataset = new HashMap<>();
        realm = Realm.getInstance(mContext);
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
            if (mDataset.get(realmDataItem.getYear()) == null) {
                List<RealmDataItem> list = new ArrayList<>();
                list.add(realmDataItem);
                mDataset.put(realmDataItem.getYear(), list);
            } else {
                List<RealmDataItem> list = mDataset.get(realmDataItem.getYear());
                list.add(realmDataItem);
                mDataset.put(realmDataItem.getYear(), list);
            }
        }
        return mDataset;
    }

    public List<RealmDataItem> getAllUniqueMonthsAsList(int type) {
        List<RealmDataItem> items = new ArrayList<>();
        List<Integer> months = new ArrayList<>();
        final HashMap<Integer, List<Integer>> monthHashmap = new HashMap<>();
        realm = Realm.getInstance(mContext);
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
        realm = Realm.getInstance(mContext);
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
        realm = Realm.getInstance(mContext);
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

    public Stack<RealmDataItem> getSpecificMonthYearAsStack(int month, int year, int type) {
        Stack<RealmDataItem> mDataset = new Stack<>();
        realm = Realm.getInstance(mContext);
        RealmQuery query;
        if (type == 2) {
            query = realm.where(RealmDataItem.class).equalTo("month", month).equalTo("year", year);
        } else if (type == 1) {
            query = realm.where(RealmDataItem.class).equalTo("type", true).equalTo("month", month).equalTo("year", year);
        } else {
            query = realm.where(RealmDataItem.class).equalTo("type", false).equalTo("month", month).equalTo("year", year);
        }
        RealmResults<RealmDataItem> results = query.findAll();
        results.sort("month", Sort.DESCENDING);
        results.sort("year", Sort.DESCENDING);
        for (RealmDataItem realmDataItem : results) {
            mDataset.add(realmDataItem);
        }

        return mDataset;
    }

    public float getSpecificDateAmount(int month, int year, int type) {
        float amount = 0;
        realm = Realm.getInstance(mContext);
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
        realm = Realm.getInstance(mContext);
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
        realm = Realm.getInstance(mContext);
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
        realm = Realm.getInstance(mContext);
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
        realm = Realm.getInstance(mContext);
        realm.beginTransaction();
        realm.where(RealmDataItem.class).equalTo("id", realmDataItem.getId()).findFirst().removeFromRealm();
        if (realm.where(RealmDataItem.class).equalTo("month", realmDataItem.getMonth()).equalTo("year", realmDataItem.getYear()).findAll().isEmpty()) {
            realm.clear(RealmBudgetItem.class);
        }
        realm.commitTransaction();
        realm.close();
    }

    public void delete(final RealmBudgetItem realmBudgetItem) {
        realm = Realm.getInstance(mContext);
        realm.beginTransaction();
        realm.where(RealmBudgetItem.class).equalTo("key", realmBudgetItem.getKey()).findAll().clear();
        realm.commitTransaction();
        realm.close();
    }

    public void delete(final RealmCategoryItem realmCategoryItem) {
        realm = Realm.getInstance(mContext);
        realm.beginTransaction();
        realm.where(RealmCategoryItem.class).equalTo("key",realmCategoryItem.getKey()).findAll().clear();
        realm.commitTransaction();
        realm.close();
    }

    public void deleteAll() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                realm = Realm.getInstance(mContext);
                realm.beginTransaction();
                realm.clear(RealmBudgetItem.class);
                realm.clear(RealmDataItem.class);
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
