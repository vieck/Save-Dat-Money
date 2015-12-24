package edu.purdue.vieck.budgetapp.DatabaseAdapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

import edu.purdue.vieck.budgetapp.CustomObjects.BudgetItem;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Stealth on 12/24/2015.
 */
public class RealmHandler {

    private static AtomicInteger id = new AtomicInteger();
    Realm realm;

    Context mContext;

    public RealmHandler(Context context) {
        mContext = context;
    }

    public void addData(BudgetItem budgetItem) {
        budgetItem.setId(id.incrementAndGet());
        realm = Realm.getInstance(mContext);
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(budgetItem);
        realm.commitTransaction();
        realm.close();
    }

    public boolean isEmpty(int type) {
        realm = Realm.getInstance(mContext);
        RealmQuery<BudgetItem> query;
        if (type == 2) {
            query = realm.where(BudgetItem.class);
        } else {
            query = realm.where(BudgetItem.class).equalTo("type", type);
        }

        if (query.count() == 0) {
            // Contains elements
            realm.close();
            return false;

        } else {
            // Empty
            realm.close();
            return true;
        }
    }


    public Stack<BudgetItem> getAllDataAsStack(int type) {
        Stack<BudgetItem> mDataset = new Stack<>();
        realm = Realm.getInstance(mContext);
        RealmQuery query;
        if (type == 2) {
            query = realm.where(BudgetItem.class);
        } else {
            query = realm.where(BudgetItem.class).equalTo("type",);
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
        } else {
            query = realm.where(BudgetItem.class).equalTo("type", type);
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
        List<BudgetItem> months = new ArrayList<>();
        realm = Realm.getInstance(mContext);
        RealmQuery query;
        if (type == 2) {
            query = realm.where(BudgetItem.class);
        } else {
            query = realm.where(BudgetItem.class).equalTo("type", type);
        }
        RealmResults<BudgetItem> results = query.findAll();
        results.sort("month", Sort.DESCENDING);
        results.sort("year", Sort.DESCENDING);
        for (BudgetItem budgetItem : results) {
            months.add(budgetItem);
        }
        return months;
    }

    public LinkedList<BudgetItem> getAllUniqueMonthsAsLinkedList(int type) {
        LinkedList<BudgetItem> months = new LinkedList<>();
        realm = Realm.getInstance(mContext);
        RealmQuery query;
        if (type == 2) {
            query = realm.where(BudgetItem.class);
        } else {
            query = realm.where(BudgetItem.class).equalTo("type", type);
        }
        RealmResults<BudgetItem> results = query.findAll();
        results.sort("month", Sort.DESCENDING);
        results.sort("year", Sort.DESCENDING);
        for (BudgetItem budgetItem : results) {
            months.add(budgetItem);
        }
        return months;
    }

    public Stack<BudgetItem> getSpecificMonthYearAsStack(int month, int year, int type) {
        Stack<BudgetItem> mDataset = new Stack<>();
        realm = Realm.getInstance(mContext);
        RealmQuery query;
        if (type == 2) {
            query = realm.where(BudgetItem.class).equalTo("month", month).equalTo("year", year);
        } else {
            query = realm.where(BudgetItem.class).equalTo("type", type).equalTo("month", month).equalTo("year", year);
        }
        RealmResults<BudgetItem> results = query.findAll();
        results.sort("month", Sort.DESCENDING);
        results.sort("year", Sort.DESCENDING);
        for (BudgetItem budgetItem : results) {
            mDataset.add(budgetItem);
        }

        return mDataset;
    }

    public float getSpecificDateAmount(int month, int year, int type) {
        float amount = 0;
        realm = Realm.getInstance(mContext);
        RealmQuery query;
        if (type == 2) {
            query = realm.where(BudgetItem.class).equalTo("month", month).equalTo("year", year);
        } else {
            query = realm.where(BudgetItem.class).equalTo("type", type).equalTo("month", month).equalTo("year", year);
        }
        RealmResults<BudgetItem> results = query.findAll();
        results.sort("month", Sort.DESCENDING);
        results.sort("year", Sort.DESCENDING);
        for (BudgetItem budgetItem : results) {
            amount += budgetItem.getAmount();
        }
        return amount;
    }

    public float getSpecificDateAmountByType(String category, int month, int year, int type) {
        float categoryPercent = 0;
        realm = Realm.getInstance(mContext);
        RealmQuery query;
        if (month != -1 && year != -1) {
            if (type == 2) {
                query = realm.where(BudgetItem.class).equalTo("category", category);
            } else {
                query = realm.where(BudgetItem.class).equalTo("type", type).equalTo("category", category);
            }
        } else {
            if (type == 2) {
                query = realm.where(BudgetItem.class).equalTo("month", month).equalTo("year", year);
            } else {
                query = realm.where(BudgetItem.class).equalTo("type", type).equalTo("month", month).equalTo("year", year);
            }
        }
        RealmResults<BudgetItem> results = query.findAll();
        results.sort("month", Sort.DESCENDING);
        results.sort("year", Sort.DESCENDING);
        for (BudgetItem budgetItem : results) {
            categoryPercent += budgetItem.getAmount();
        }
        return categoryPercent;
    }

    public void delete(BudgetItem budgetItem) {
        realm = Realm.getInstance(mContext);
        budgetItem.removeFromRealm();
        realm.close();
    }

    public void deleteAll() {
        realm = Realm.getInstance(mContext);
        realm.clear(BudgetItem.class);
        realm.close();
    }
}
