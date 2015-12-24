package edu.purdue.vieck.budgetapp;

import android.database.Cursor;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import edu.purdue.vieck.budgetapp.CustomObjects.BudgetItem;

/**
 * Created by vieck on 12/23/15.
 */
public class ParseHandler {

    boolean empty;
    double amount;

    public void addData(BudgetItem budgetItem) {
        /*BudgetItem budgetObject = new ParseObject("BudgetItem");
        budgetObject.put("amount", budgetItem.getAmount());
        budgetObject.put("category", budgetItem.getCategory());
        budgetObject.put("subcategory", budgetItem.getSubcategory());
        budgetObject.put("type", budgetItem.isType());
        budgetObject.put("day", budgetItem.getDay());
        budgetObject.put("month", budgetItem.getMonth());
        budgetObject.put("year", budgetItem.getYear());
        budgetObject.put("note", budgetItem.getNote());*/
        budgetItem.saveEventually();
    }

    public boolean isEmpty(int type) {
        empty = true;
        ParseQuery<BudgetItem> query;
        if (type == 2) {
            query = ParseQuery.getQuery(BudgetItem.class);
        } else {
            query = ParseQuery.getQuery(BudgetItem.class).whereEqualTo("type", type);
        }

        try {
            if(query.count() == 0) {
                Log.d("IsEmpty","true");
                return true;
            } else {
                Log.d("IsEmpty","false");
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return true;
    }


    public Stack<BudgetItem> getAllDataAsStack(int type) {
        final Stack<BudgetItem> mDataset = new Stack<>();
        ParseQuery<BudgetItem> query;
        if (type == 2) {
            query = ParseQuery.getQuery(BudgetItem.class).addAscendingOrder("month").addDescendingOrder("year");
        } else {
            query = ParseQuery.getQuery(BudgetItem.class).whereEqualTo("type", type).addAscendingOrder("month").addDescendingOrder("year");
        }
        query.fromLocalDatastore().findInBackground(new FindCallback<BudgetItem>() {
            @Override
            public void done(List<BudgetItem> objects, ParseException e) {
                if (e == null) {
                    Log.d("Months",""+objects.size());
                    for (BudgetItem item : objects) {
                        mDataset.push(item);
                    }
                }
            }
        });
        return mDataset;
    }

    public HashMap<Integer, List<BudgetItem>> getAllYearsAsHashmap(int type) {
        final HashMap<Integer, List<BudgetItem>> mDataset = new HashMap<>();
        ParseQuery<BudgetItem> query;
        if (type == 2) {
            query = ParseQuery.getQuery(BudgetItem.class).addDescendingOrder("month").addDescendingOrder("year");
        } else {
            query = ParseQuery.getQuery(BudgetItem.class).whereEqualTo("type", type).addDescendingOrder("month").addDescendingOrder("year");
        }
        query.findInBackground(new FindCallback<BudgetItem>() {
            @Override
            public void done(List<BudgetItem> objects, ParseException e) {
                if (e == null) {
                    Log.d("Years",""+objects.size());
                    for (BudgetItem item : objects) {
                        if (mDataset.get(item.getYear()) == null) {
                            List<BudgetItem> list = new ArrayList<>();
                            list.add(item);
                            mDataset.put(item.getYear(), list);
                        } else {
                            List<BudgetItem> list = mDataset.get(item.getYear());
                            list.add(item);
                            mDataset.put(item.getYear(), list);
                        }
                    }
                }
            }
        });
        return mDataset;
    }

    public List<BudgetItem> getAllUniqueMonthsAsList(int type) {
        final List<BudgetItem> months = new ArrayList<>();
        final HashMap<Integer, List<Integer>> monthHashmap = new HashMap<>();
        ParseQuery<BudgetItem> query;
        if (type == 2) {
            query = ParseQuery.getQuery(BudgetItem.class).addDescendingOrder("month").addDescendingOrder("year");
        } else {
            query = ParseQuery.getQuery(BudgetItem.class).whereEqualTo("type", type).addDescendingOrder("month").addDescendingOrder("year");
        }
        query.findInBackground(new FindCallback<BudgetItem>() {
            @Override
            public void done(List<BudgetItem> objects, ParseException e) {
                if (e == null) {
                    for (BudgetItem item : objects) {
                        if (monthHashmap.get(item.getYear()) == null) {
                            List<Integer> list = new ArrayList<Integer>();
                            list.add(item.getMonth());
                            monthHashmap.put(item.getYear(), list);
                            months.add(item);
                        } else {
                            List<Integer> list = monthHashmap.get(item.getYear());
                            if (!months.contains(item.getMonth())) {
                                list.add(item.getMonth());
                                monthHashmap.put(item.getYear(), list);
                                months.add(item);
                            }
                        }
                    }
                }
            }
        });
        Log.d("UniqueMonths",""+months.size());
        return months;
    }

    public LinkedList<BudgetItem> getAllUniqueMonthsAsLinkedList(int type) {
        final LinkedList<BudgetItem> months = new LinkedList<>();
        final HashMap<Integer, List<Integer>> monthHashmap = new HashMap<>();
        ParseQuery<BudgetItem> query;
        if (type == 2) {
            query = ParseQuery.getQuery(BudgetItem.class).addDescendingOrder("month").addDescendingOrder("year");
        } else {
            query = ParseQuery.getQuery(BudgetItem.class).whereEqualTo("type", type).addDescendingOrder("month").addDescendingOrder("year");
        }
        query.findInBackground(new FindCallback<BudgetItem>() {
            @Override
            public void done(List<BudgetItem> objects, ParseException e) {
                if (e == null) {
                    for (BudgetItem item : objects) {
                        if (monthHashmap.get(item.getYear()) == null) {
                            List<Integer> list = new ArrayList<Integer>();
                            list.add(item.getMonth());
                            monthHashmap.put(item.getYear(), list);
                            months.add(item);
                        } else {
                            List<Integer> list = monthHashmap.get(item.getYear());
                            if (!months.contains(item.getMonth())) {
                                list.add(item.getMonth());
                                monthHashmap.put(item.getYear(), list);
                                months.add(item);
                            }
                        }
                    }
                }
            }
        });
        Log.d("UniqueMonthsLL",""+months.size());
        return months;
    }

    public Stack<BudgetItem> getSpecificMonthYearAsStack(int month, int year, int type) {
        final Stack<BudgetItem> mDataset = new Stack<>();
        ParseQuery<BudgetItem> query;
        if (type == 2) {
            query = ParseQuery.getQuery(BudgetItem.class).whereEqualTo("year", year).whereEqualTo("month", month).addDescendingOrder("month").addDescendingOrder("year");
        } else {
            query = ParseQuery.getQuery(BudgetItem.class).whereEqualTo("type", type).whereEqualTo("year", year).whereEqualTo("month", month).addDescendingOrder("month").addDescendingOrder("year");
        }
        query.findInBackground(new FindCallback<BudgetItem>() {
            @Override
            public void done(List<BudgetItem> objects, ParseException e) {
                if (e == null) {
                    for (BudgetItem item : objects) {
                        mDataset.push(item);
                    }
                }
            }
        });
        Log.d("MonthStack",""+mDataset.size());
        return mDataset;
    }

    public float getSpecificDateAmount(int month, int year, int type) {
        amount = 0;
        ParseQuery<BudgetItem> query;
        if (type == 2) {
            query = ParseQuery.getQuery(BudgetItem.class).whereEqualTo("year", year).whereEqualTo("month", month).addDescendingOrder("month").addDescendingOrder("year");
        } else {
            query = ParseQuery.getQuery(BudgetItem.class).whereEqualTo("type", type).whereEqualTo("year", year).whereEqualTo("month", month).addDescendingOrder("month").addDescendingOrder("year");
        }
        query.findInBackground(new FindCallback<BudgetItem>() {
            @Override
            public void done(List<BudgetItem> objects, ParseException e) {
                if (e == null) {
                    for (BudgetItem item : objects) {
                        amount += item.getAmount();
                    }
                }
            }
        });
        return (float) amount;
    }

    public float getSpecificDateAmountByType(String category, int month, int year, int type) {
        amount = 0;
        ParseQuery<BudgetItem> selectQuery;
        if (month != -1 && year != -1) {
            if (type == 2) {
                selectQuery = ParseQuery.getQuery(BudgetItem.class).whereEqualTo("year", year).whereEqualTo("month", month).addDescendingOrder("month").addDescendingOrder("year");
            } else {
                selectQuery = ParseQuery.getQuery(BudgetItem.class).whereEqualTo("type", type).whereEqualTo("year", year).whereEqualTo("month", month).addDescendingOrder("month").addDescendingOrder("year");
            }
        } else {
            if (type == 2) {
                selectQuery = ParseQuery.getQuery(BudgetItem.class).whereEqualTo("category", category);
            } else {
                selectQuery = ParseQuery.getQuery(BudgetItem.class).whereEqualTo("category", category).whereEqualTo("type", type);
            }
        }
        selectQuery.findInBackground(new FindCallback<BudgetItem>() {
            @Override
            public void done(List<BudgetItem> objects, ParseException e) {
                if (e == null) {
                    for (BudgetItem item : objects) {
                        amount += item.getAmount();
                    }
                }
            }
        });
        return (float) amount;
    }

    public void delete(BudgetItem budgetItem) {
    }

    public void deleteAll() {
    }

}
