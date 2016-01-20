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

import edu.purdue.vieck.budgetapp.CustomObjects.BudgetItem;
import edu.purdue.vieck.budgetapp.CustomObjects.DataItem;
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

    public void addData(final DataItem dataItem) {
                dataItem.setId(getCount()+1);
                realm = Realm.getInstance(mContext);
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(dataItem);
                realm.commitTransaction();
                realm.close();
    }

    public void updateData(final DataItem dataItem) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                realm = Realm.getInstance(mContext);
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(dataItem);
                realm.commitTransaction();
                realm.close();
            }
        });
        thread.start();
    }

    public void addBudget(BudgetItem budgetItem) {
        realm = Realm.getInstance(mContext);
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(budgetItem);
        realm.commitTransaction();
        realm.close();
    }

    public void updateBudget(final float budget, final int month, final int year) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                realm = Realm.getInstance(mContext);
                realm.beginTransaction();
                RealmQuery query = realm.where(BudgetItem.class).equalTo("key",month+"/"+year);
                RealmResults<BudgetItem> items = query.findAll();
                for (int i = 0; i < items.size(); i++) {
                    items.get(i).setBudget(budget);
                }
                realm.copyToRealmOrUpdate(items);
                realm.commitTransaction();
            }
        });
        thread.start();
    }

    public float getBudget() {
        realm = Realm.getInstance(mContext);
        RealmQuery query = realm.where(BudgetItem.class);
        return query.sum("budget").floatValue();
    }

    public float getBudget(int month, int year) {
                realm = Realm.getInstance(mContext);
                RealmQuery query = realm.where(BudgetItem.class).equalTo("key",month+"/"+year);
                RealmResults<BudgetItem> results = query.findAll();
                if (results.size() < 1) {
                    return 500;
                } else {
                    return results.get(0).getBudget();
                }
    }

    public boolean isEmpty(int type) {
        realm = Realm.getInstance(mContext);
        RealmQuery<DataItem> query;
        if (type == 2) {
            return realm.isEmpty();
        } else if (type == 1) {
            query = realm.where(DataItem.class).equalTo("type", true);
        } else {
            query = realm.where(DataItem.class).equalTo("type", false);
        }

        return query.findAll().isEmpty();
    }

    public int getCount() {
        realm = Realm.getInstance(mContext);
        return realm.where(DataItem.class).findAll().size();
    }


    public Stack<DataItem> getAllDataAsStack(int type) {
        Stack<DataItem> mDataset = new Stack<>();
        realm = Realm.getInstance(mContext);
        RealmQuery query;
        if (type == 2) {
            query = realm.where(DataItem.class);
        } else if (type == 1) {
            query = realm.where(DataItem.class).equalTo("type", true);
        } else {
            query = realm.where(DataItem.class).equalTo("type", false);
        }

        RealmResults<DataItem> results = query.findAll();
        results.sort("month", Sort.ASCENDING);
        results.sort("year", Sort.DESCENDING);

        for (DataItem item : results) {
            mDataset.push(item);
        }

        return mDataset;
    }

    public HashMap<Integer, List<DataItem>> getAllYearsAsHashmap(int type) {
        HashMap<Integer, List<DataItem>> mDataset = new HashMap<>();
        realm = Realm.getInstance(mContext);
        RealmQuery query;
        if (type == 2) {
            query = realm.where(DataItem.class);
        } else if (type == 1) {
            query = realm.where(DataItem.class).equalTo("type", true);
        } else {
            query = realm.where(DataItem.class).equalTo("type", false);
        }

        RealmResults<DataItem> results = query.findAll();
        results.sort("month", Sort.DESCENDING);
        results.sort("year", Sort.DESCENDING);

        for (DataItem dataItem : results) {
            if (mDataset.get(dataItem.getYear()) == null) {
                List<DataItem> list = new ArrayList<>();
                list.add(dataItem);
                mDataset.put(dataItem.getYear(), list);
            } else {
                List<DataItem> list = mDataset.get(dataItem.getYear());
                list.add(dataItem);
                mDataset.put(dataItem.getYear(), list);
            }
        }
        return mDataset;
    }

    public List<DataItem> getAllUniqueMonthsAsList(int type) {
        List<DataItem> items = new ArrayList<>();
        List<Integer> months = new ArrayList<>();
        final HashMap<Integer, List<Integer>> monthHashmap = new HashMap<>();
        realm = Realm.getInstance(mContext);
        RealmQuery query;
        if (type == 2) {
            query = realm.where(DataItem.class);
        } else if (type == 1) {
            query = realm.where(DataItem.class).equalTo("type", true);
        } else {
            query = realm.where(DataItem.class).equalTo("type", false);
        }
        RealmResults<DataItem> results = query.findAll();
        results.sort("month", Sort.DESCENDING);
        results.sort("year", Sort.DESCENDING);
        for (DataItem dataItem : results) {
            if (monthHashmap.get(dataItem.getYear()) == null) {
                List<Integer> list = new ArrayList<Integer>();
                list.add(dataItem.getMonth());
                items.add(dataItem);
                monthHashmap.put(dataItem.getYear(), list);
            } else {
                List<Integer> list = monthHashmap.get(dataItem.getYear());
                if (!list.contains(dataItem.getMonth())) {
                    list.add(dataItem.getMonth());
                    monthHashmap.put(dataItem.getYear(), list);
                    months.add(dataItem.getMonth());
                    items.add(dataItem);
                }
            }
        }
        return items;
    }

    public LinkedList<DataItem> getAllUniqueMonthsAsLinkedList(int type) {
        final LinkedList<DataItem> items = new LinkedList<>();
        final HashMap<Integer, List<Integer>> monthHashmap = new HashMap<>();
        realm = Realm.getInstance(mContext);
        RealmQuery query;
        if (type == 2) {
            query = realm.where(DataItem.class);
        } else if (type == 1) {
            query = realm.where(DataItem.class).equalTo("type", true);
        } else {
            query = realm.where(DataItem.class).equalTo("type", false);
        }
        RealmResults<DataItem> results = query.findAll();
        results.sort("month", Sort.DESCENDING);
        results.sort("year", Sort.DESCENDING);
        for (DataItem dataItem : results) {
            if (monthHashmap.get(dataItem.getYear()) == null) {
                List<Integer> list = new ArrayList<Integer>();
                list.add(dataItem.getMonth());
                items.add(dataItem);
                monthHashmap.put(dataItem.getYear(), list);
            } else {
                List<Integer> list = monthHashmap.get(dataItem.getYear());
                if (!list.contains(dataItem.getMonth())) {
                    list.add(dataItem.getMonth());
                    monthHashmap.put(dataItem.getYear(), list);
                    items.add(dataItem);
                }
            }
        }
        return items;
    }

    public Stack<DataItem> getSpecificMonthYearAsStack(int month, int year, int type) {
        Stack<DataItem> mDataset = new Stack<>();
        realm = Realm.getInstance(mContext);
        RealmQuery query;
        if (type == 2) {
            query = realm.where(DataItem.class).equalTo("month", month).equalTo("year", year);
        } else if (type == 1) {
            query = realm.where(DataItem.class).equalTo("type", true).equalTo("month", month).equalTo("year", year);
        } else {
            query = realm.where(DataItem.class).equalTo("type", false).equalTo("month", month).equalTo("year", year);
        }
        RealmResults<DataItem> results = query.findAll();
        results.sort("month", Sort.DESCENDING);
        results.sort("year", Sort.DESCENDING);
        for (DataItem dataItem : results) {
            mDataset.add(dataItem);
        }

        return mDataset;
    }

    public int getGraphBounds() {
        realm = Realm.getInstance(mContext);
        float amount = 0;
        RealmQuery query = realm.where(DataItem.class).equalTo("type",true);
        int income = query.sum("amount").intValue();
        query = realm.where(DataItem.class).equalTo("type",false);
        int expense = query.sum("amount").intValue();
        if (income < expense) {
            return income;
        } else {
            return expense;
        }
    }

    public float getSpecificDateAmount(int month, int year, int type) {
        float amount = 0;
        realm = Realm.getInstance(mContext);
        RealmQuery query;
        if (type == 2) {
            query = realm.where(DataItem.class).equalTo("month", month).equalTo("year", year);
        } else if (type == 1) {
            query = realm.where(DataItem.class).equalTo("type", true).equalTo("month", month).equalTo("year", year);
        } else {
            query = realm.where(DataItem.class).equalTo("type", false).equalTo("month", month).equalTo("year", year);
        }
        RealmResults<DataItem> results = query.findAll();
        for (DataItem dataItem : results) {
            amount += dataItem.getAmount();
        }
        return (float) (Math.round(amount * 100) / 100.00);
    }

    public float[] getAllDataAsArray(int type) {
        realm = Realm.getInstance(mContext);
        RealmQuery query;
        getAllUniqueMonthsAsList(type);
        if (type == 2) {
            query = realm.where(DataItem.class);
        } else if (type == 1) {
            query = realm.where(DataItem.class).equalTo("type", true);
        } else {
            query = realm.where(DataItem.class).equalTo("type", false);
        }
        RealmResults<DataItem> results = query.findAll();
        float[] data = new float[results.size()];
        int i = 0;
        for (DataItem dataItem : results) {
            data[i++] = dataItem.getAmount();
        }
        return data;
    }

    public List<DataItem> getUniqueMonthGraphData(int type) {
        List<DataItem> items = getAllUniqueMonthsAsList(type);
        for (int i = 0; i < items.size(); i++) {

        }
        return items;
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
            query = realm.where(DataItem.class).equalTo("month", month).equalTo("year", year);
        } else if (type == 1) {
            query = realm.where(DataItem.class).equalTo("type", true).equalTo("month", month).equalTo("year", year);
        } else {
            query = realm.where(DataItem.class).equalTo("type", false).equalTo("month", month).equalTo("year", year);
        }
        RealmResults<DataItem> results = query.findAll();
        for (DataItem item : results) {
            days[item.getDay()-1] += item.getAmount();
        }
        return days;
    }

    public float getSpecificDateAmountByType(String category, int month, int year, int type) {
        float categoryPercent = 0;
        realm = Realm.getInstance(mContext);
        RealmQuery query;
        if (month != -1 && year != -1) {
            if (type == 2) {
                query = realm.where(DataItem.class).equalTo("category", category).beginGroup().equalTo("month", month).equalTo("year", year).endGroup();
            } else if (type == 1) {
                query = realm.where(DataItem.class).equalTo("type", true).equalTo("category", category).beginGroup().equalTo("month", month).equalTo("year", year).endGroup();
            } else {
                query = realm.where(DataItem.class).equalTo("type", false).equalTo("category", category).beginGroup().equalTo("month", month).equalTo("year", year).endGroup();
            }
        } else {
            if (type == 2) {
                query = realm.where(DataItem.class).equalTo("category", category);
            } else if (type == 1) {
                query = realm.where(DataItem.class).equalTo("type", true).equalTo("category", category);
            } else {
                query = realm.where(DataItem.class).equalTo("type", false).equalTo("category", category);
            }

        }
        RealmResults<DataItem> results = query.findAll();
        results.sort("month", Sort.DESCENDING);
        results.sort("year", Sort.DESCENDING);
        for (DataItem dataItem : results) {
            categoryPercent += dataItem.getAmount();
        }
        return (float) (Math.round(categoryPercent * 100) / 100.00);
    }

    public void delete(final DataItem dataItem) {
                realm = Realm.getInstance(mContext);
                realm.beginTransaction();
                realm.where(DataItem.class).equalTo("id", dataItem.getId()).findFirst().removeFromRealm();
                if (realm.where(DataItem.class).equalTo("month",dataItem.getMonth()).equalTo("year",dataItem.getYear()).findAll().isEmpty()) {
                    realm.clear(BudgetItem.class);
                }
                realm.commitTransaction();
                realm.close();
    }

    public void delete(final BudgetItem budgetItem) {
        realm = Realm.getInstance(mContext);
        realm.beginTransaction();
        realm.where(BudgetItem.class).equalTo("key",budgetItem.getKey()).findAll().clear();
        realm.commitTransaction();
        realm.close();
    }

    public void deleteAll() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                realm = Realm.getInstance(mContext);
                realm.beginTransaction();
                realm.clear(BudgetItem.class);
                realm.clear(DataItem.class);
                realm.commitTransaction();
                realm.close();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException ex) {
            Log.e("Thread", "Delete thread interrupted");
        }
        Intent intent = ((Activity) mContext).getIntent();
        ((Activity)mContext).finish();
        mContext.startActivity(intent);
    }
}
