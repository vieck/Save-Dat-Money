package edu.purdue.vieck.budgetapp.DatabaseAdapters;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

import edu.purdue.vieck.budgetapp.CustomObjects.BudgetItem;
import io.realm.Realm;
import io.realm.RealmObject;
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

    public void addData(final BudgetItem budgetItem) {
                budgetItem.setId(getCount()+1);
                realm = Realm.getInstance(mContext);
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(budgetItem);
                realm.commitTransaction();
                realm.close();
    }

    public void updateData(final BudgetItem budgetItem) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                realm = Realm.getInstance(mContext);
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(budgetItem);
                realm.commitTransaction();
                realm.close();
            }
        });
        thread.start();
    }

    public void updateBudgetForTheMonth(final float budget, final int month, final int year) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                realm = Realm.getInstance(mContext);
                realm.beginTransaction();
                RealmQuery query = realm.where(BudgetItem.class).equalTo("month",month).equalTo("year",year);
                RealmResults<BudgetItem> items = query.findAll();
                for (int i = 0; i < items.size(); i++) {
                    items.get(i).setBudget(budget);
                }
                realm.copyToRealmOrUpdate(items);
                realm.commitTransaction();
            }
        });
    }

    public float getBudget(int month, int year) {
                realm = Realm.getInstance(mContext);
                RealmQuery query = realm.where(BudgetItem.class).equalTo("month",month).equalTo("year",year);
                BudgetItem item = (BudgetItem) query.findFirst();
                return item.getBudget();
    }

    public boolean isEmpty(int type) {
        realm = Realm.getInstance(mContext);
        RealmQuery<BudgetItem> query;
        if (type == 2) {
            return realm.isEmpty();
        } else if (type == 1) {
            query = realm.where(BudgetItem.class).equalTo("type", true);
        } else {
            query = realm.where(BudgetItem.class).equalTo("type", false);
        }

        return query.findAll().isEmpty();
    }

    public int getCount() {
        realm = Realm.getInstance(mContext);
        return realm.where(BudgetItem.class).findAll().size();
    }


    public Stack<BudgetItem> getAllDataAsStack(int type) {
        Stack<BudgetItem> mDataset = new Stack<>();
        realm = Realm.getInstance(mContext);
        RealmQuery query;
        if (type == 2) {
            query = realm.where(BudgetItem.class);
        } else if (type == 1) {
            query = realm.where(BudgetItem.class).equalTo("type", true);
        } else {
            query = realm.where(BudgetItem.class).equalTo("type", false);
        }

        RealmResults<BudgetItem> results = query.findAll();
        results.sort("month", Sort.ASCENDING);
        results.sort("year", Sort.DESCENDING);

        for (BudgetItem item : results) {
            mDataset.push(item);
        }

        return mDataset;
    }

    public HashMap<Integer, List<BudgetItem>> getAllYearsAsHashmap(int type) {
        HashMap<Integer, List<BudgetItem>> mDataset = new HashMap<>();
        realm = Realm.getInstance(mContext);
        RealmQuery query;
        if (type == 2) {
            query = realm.where(BudgetItem.class);
        } else if (type == 1) {
            query = realm.where(BudgetItem.class).equalTo("type", true);
        } else {
            query = realm.where(BudgetItem.class).equalTo("type", false);
        }

        RealmResults<BudgetItem> results = query.findAll();
        results.sort("month", Sort.DESCENDING);
        results.sort("year", Sort.DESCENDING);

        for (BudgetItem budgetItem : results) {
            if (mDataset.get(budgetItem.getYear()) == null) {
                List<BudgetItem> list = new ArrayList<>();
                list.add(budgetItem);
                mDataset.put(budgetItem.getYear(), list);
            } else {
                List<BudgetItem> list = mDataset.get(budgetItem.getYear());
                list.add(budgetItem);
                mDataset.put(budgetItem.getYear(), list);
            }
        }
        return mDataset;
    }

    public List<BudgetItem> getAllUniqueMonthsAsList(int type) {
        List<BudgetItem> items = new ArrayList<>();
        List<Integer> months = new ArrayList<>();
        final HashMap<Integer, List<Integer>> monthHashmap = new HashMap<>();
        realm = Realm.getInstance(mContext);
        RealmQuery query;
        if (type == 2) {
            query = realm.where(BudgetItem.class);
        } else if (type == 1) {
            query = realm.where(BudgetItem.class).equalTo("type", true);
        } else {
            query = realm.where(BudgetItem.class).equalTo("type", false);
        }
        RealmResults<BudgetItem> results = query.findAll();
        results.sort("month", Sort.DESCENDING);
        results.sort("year", Sort.DESCENDING);
        for (BudgetItem budgetItem : results) {
            if (monthHashmap.get(budgetItem.getYear()) == null) {
                List<Integer> list = new ArrayList<Integer>();
                list.add(budgetItem.getMonth());
                items.add(budgetItem);
                monthHashmap.put(budgetItem.getYear(), list);
            } else {
                List<Integer> list = monthHashmap.get(budgetItem.getYear());
                if (!list.contains(budgetItem.getMonth())) {
                    list.add(budgetItem.getMonth());
                    monthHashmap.put(budgetItem.getYear(), list);
                    months.add(budgetItem.getMonth());
                    items.add(budgetItem);
                }
            }
        }
        return items;
    }

    public LinkedList<BudgetItem> getAllUniqueMonthsAsLinkedList(int type) {
        final LinkedList<BudgetItem> items = new LinkedList<>();
        final HashMap<Integer, List<Integer>> monthHashmap = new HashMap<>();
        realm = Realm.getInstance(mContext);
        RealmQuery query;
        if (type == 2) {
            query = realm.where(BudgetItem.class);
        } else if (type == 1) {
            query = realm.where(BudgetItem.class).equalTo("type", true);
        } else {
            query = realm.where(BudgetItem.class).equalTo("type", false);
        }
        RealmResults<BudgetItem> results = query.findAll();
        results.sort("month", Sort.DESCENDING);
        results.sort("year", Sort.DESCENDING);
        for (BudgetItem budgetItem : results) {
            if (monthHashmap.get(budgetItem.getYear()) == null) {
                List<Integer> list = new ArrayList<Integer>();
                list.add(budgetItem.getMonth());
                items.add(budgetItem);
                monthHashmap.put(budgetItem.getYear(), list);
            } else {
                List<Integer> list = monthHashmap.get(budgetItem.getYear());
                if (!list.contains(budgetItem.getMonth())) {
                    list.add(budgetItem.getMonth());
                    monthHashmap.put(budgetItem.getYear(), list);
                    items.add(budgetItem);
                }
            }
        }
        return items;
    }

    public Stack<BudgetItem> getSpecificMonthYearAsStack(int month, int year, int type) {
        Stack<BudgetItem> mDataset = new Stack<>();
        realm = Realm.getInstance(mContext);
        RealmQuery query;
        if (type == 2) {
            query = realm.where(BudgetItem.class).equalTo("month", month).equalTo("year", year);
        } else if (type == 1) {
            query = realm.where(BudgetItem.class).equalTo("type", true).equalTo("month", month).equalTo("year", year);
        } else {
            query = realm.where(BudgetItem.class).equalTo("type", false).equalTo("month", month).equalTo("year", year);
        }
        RealmResults<BudgetItem> results = query.findAll();
        results.sort("month", Sort.DESCENDING);
        results.sort("year", Sort.DESCENDING);
        for (BudgetItem budgetItem : results) {
            mDataset.add(budgetItem);
        }

        return mDataset;
    }

    public int getGraphBounds() {
        realm = Realm.getInstance(mContext);
        float amount = 0;
        RealmQuery query = realm.where(BudgetItem.class).equalTo("type",true);
        int income = query.sum("amount").intValue();
        query = realm.where(BudgetItem.class).equalTo("type",false);
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
            query = realm.where(BudgetItem.class).equalTo("month", month).equalTo("year", year);
        } else if (type == 1) {
            query = realm.where(BudgetItem.class).equalTo("type", true).equalTo("month", month).equalTo("year", year);
        } else {
            query = realm.where(BudgetItem.class).equalTo("type", false).equalTo("month", month).equalTo("year", year);
        }
        RealmResults<BudgetItem> results = query.findAll();
        for (BudgetItem budgetItem : results) {
            amount += budgetItem.getAmount();
        }
        return (float) (Math.round(amount * 100) / 100.00);
    }

    public float[] getAllDataAsArray(int type) {
        realm = Realm.getInstance(mContext);
        RealmQuery query;
        getAllUniqueMonthsAsList(type);
        if (type == 2) {
            query = realm.where(BudgetItem.class);
        } else if (type == 1) {
            query = realm.where(BudgetItem.class).equalTo("type", true);
        } else {
            query = realm.where(BudgetItem.class).equalTo("type", false);
        }
        RealmResults<BudgetItem> results = query.findAll();
        float[] data = new float[results.size()];
        int i = 0;
        for (BudgetItem budgetItem : results) {
            data[i++] = budgetItem.getAmount();
        }
        return data;
    }

    public List<BudgetItem> getUniqueMonthGraphData(int type) {
        List<BudgetItem> items = getAllUniqueMonthsAsList(type);
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
            query = realm.where(BudgetItem.class).equalTo("month", month).equalTo("year", year);
        } else if (type == 1) {
            query = realm.where(BudgetItem.class).equalTo("type", true).equalTo("month", month).equalTo("year", year);
        } else {
            query = realm.where(BudgetItem.class).equalTo("type", false).equalTo("month", month).equalTo("year", year);
        }
        RealmResults<BudgetItem> results = query.findAll();
        for (BudgetItem item : results) {
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
                query = realm.where(BudgetItem.class).equalTo("category", category).beginGroup().equalTo("month", month).equalTo("year", year).endGroup();
            } else if (type == 1) {
                query = realm.where(BudgetItem.class).equalTo("type", true).equalTo("category", category).beginGroup().equalTo("month", month).equalTo("year", year).endGroup();
            } else {
                query = realm.where(BudgetItem.class).equalTo("type", false).equalTo("category", category).beginGroup().equalTo("month", month).equalTo("year", year).endGroup();
            }
        } else {
            if (type == 2) {
                query = realm.where(BudgetItem.class).equalTo("category", category);
            } else if (type == 1) {
                query = realm.where(BudgetItem.class).equalTo("type", true).equalTo("category", category);
            } else {
                query = realm.where(BudgetItem.class).equalTo("type", false).equalTo("category", category);
            }

        }
        RealmResults<BudgetItem> results = query.findAll();
        results.sort("month", Sort.DESCENDING);
        results.sort("year", Sort.DESCENDING);
        for (BudgetItem budgetItem : results) {
            categoryPercent += budgetItem.getAmount();
        }
        return (float) (Math.round(categoryPercent * 100) / 100.00);
    }

    public void delete(final BudgetItem budgetItem) {
                realm = Realm.getInstance(mContext);
                realm.beginTransaction();
                realm.where(BudgetItem.class).equalTo("id",budgetItem.getId()).findFirst().removeFromRealm();
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
                realm.commitTransaction();
                realm.close();
            }
        });
        thread.start();

    }
}
