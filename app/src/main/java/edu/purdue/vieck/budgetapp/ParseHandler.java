package edu.purdue.vieck.budgetapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.parse.FindCallback;
import com.parse.Parse;
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
        ParseObject budgetObject = new ParseObject("BudgetItem");
        budgetObject.put("amount",budgetItem.getAmount());
        budgetObject.put("category",budgetItem.getCategory());
        budgetObject.put("subcategory",budgetItem.getSubcategory());
        budgetObject.put("type", budgetItem.isType());
        budgetObject.put("day",budgetItem.getDay());
        budgetObject.put("month", budgetItem.getMonth());
        budgetObject.put("year", budgetItem.getYear());
        budgetObject.put("note", budgetItem.getNote());
    }


    private BudgetItem convertCursorToItem(Cursor cursor) {
        BudgetItem budgetItem = new BudgetItem();
        cursor.getLong(0);
        budgetItem.setCategory(cursor.getString(1));
        budgetItem.setSubcategory(cursor.getString(2));
        budgetItem.setAmount(cursor.getFloat(3));
        if (cursor.getInt(4) == 0) {
            budgetItem.setType(false);
        } else {
            budgetItem.setType(true);
        }
        budgetItem.setDay(cursor.getInt(5));
        budgetItem.setMonth(cursor.getInt(6));
        budgetItem.setYear(cursor.getInt(7));
        budgetItem.setNote(cursor.getString(8));
        return budgetItem;
    }

    public boolean isEmpty(int type) {
        ParseQuery query;
        if (type == 2) {
            query = ParseQuery.getQuery("BudgetItem");
        } else {
           query = ParseQuery.getQuery("BudgetItem").whereEqualTo("type",type);
        }
        query.findInBackground(new FindCallback<BudgetItem>(){
            @Override
            public void done(List<BudgetItem> objects, ParseException e) {
                empty = objects.isEmpty();
            }
        });
        return empty;
    }


    public Stack<BudgetItem> getAllDataAsStack(int type) {
        final Stack<BudgetItem> mDataset = new Stack<>();
        ParseQuery query;
        if (type == 2) {
            query = ParseQuery.getQuery("BudgetItem").addAscendingOrder("month").addDescendingOrder("year");
        } else {
            query = ParseQuery.getQuery("BudgetItem").whereEqualTo("type",type).addAscendingOrder("month").addDescendingOrder("year");
        }
        query.findInBackground(new FindCallback<BudgetItem>(){
            @Override
            public void done(List<BudgetItem> objects, ParseException e) {
                for (BudgetItem item : objects) {
                    mDataset.push(item);
                }
            }
        });
        return mDataset;
    }

    public HashMap<Integer, List<BudgetItem>> getAllYearsAsHashmap(int type) {
        final HashMap<Integer, List<BudgetItem>> mDataset = new HashMap<>();
        ParseQuery query;
        if (type == 2) {
            query = ParseQuery.getQuery("BudgetItem").addDescendingOrder("month").addDescendingOrder("year");
        } else {
            query = ParseQuery.getQuery("BudgetItem").whereEqualTo("type",type).addDescendingOrder("month").addDescendingOrder("year");
        }
        query.findInBackground(new FindCallback<BudgetItem>(){
            @Override
            public void done(List<BudgetItem> objects, ParseException e) {
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
        });
        return mDataset;
    }

    public List<BudgetItem> getAllUniqueMonthsAsList(int type) {
        final List<BudgetItem> months = new ArrayList<>();
        final HashMap<Integer, List<Integer>> monthHashmap = new HashMap<>();
        ParseQuery query;
        if (type == 2) {
            query = ParseQuery.getQuery("BudgetItem").addDescendingOrder("month").addDescendingOrder("year");
        } else {
            query = ParseQuery.getQuery("BudgetItem").whereEqualTo("type",type).addDescendingOrder("month").addDescendingOrder("year");
        }
        query.findInBackground(new FindCallback<BudgetItem>(){
            @Override
            public void done(List<BudgetItem> objects, ParseException e) {
                for (BudgetItem item : objects) {
                    if (monthHashmap.get(item.getYear()) == null) {
                        List<Integer> list = new ArrayList<Integer>();
                        list.add(item.getMonth());
                        monthHashmap.put(item.getYear(),list);
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
        });
        return months;
    }

    public LinkedList<BudgetItem> getAllUniqueMonthsAsLinkedList(int type) {
        final LinkedList<BudgetItem> months = new LinkedList<>();
        final HashMap<Integer, List<Integer>> monthHashmap = new HashMap<>();
        ParseQuery query;
        if (type == 2) {
            query = ParseQuery.getQuery("BudgetItem").addDescendingOrder("month").addDescendingOrder("year");
        } else {
            query = ParseQuery.getQuery("BudgetItem").whereEqualTo("type",type).addDescendingOrder("month").addDescendingOrder("year");
        }
        query.findInBackground(new FindCallback<BudgetItem>(){
            @Override
            public void done(List<BudgetItem> objects, ParseException e) {
                for (BudgetItem item : objects) {
                    if (monthHashmap.get(item.getYear()) == null) {
                        List<Integer> list = new ArrayList<Integer>();
                        list.add(item.getMonth());
                        monthHashmap.put(item.getYear(),list);
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
        });
        return months;
    }

    public Stack<BudgetItem> getSpecificMonthYearAsStack(int month, int year, int type) {
        final Stack<BudgetItem> mDataset = new Stack<>();
        ParseQuery query;
        if (type == 2) {
            query = ParseQuery.getQuery("BudgetItem").whereEqualTo("year",year).whereEqualTo("month",month).addDescendingOrder("month").addDescendingOrder("year");
        } else {
            query = ParseQuery.getQuery("BudgetItem").whereEqualTo("type",type).whereEqualTo("year",year).whereEqualTo("month",month).addDescendingOrder("month").addDescendingOrder("year");
        }
        query.findInBackground(new FindCallback<BudgetItem>(){
            @Override
            public void done(List<BudgetItem> objects, ParseException e) {
                for (BudgetItem item : objects) {
                    mDataset.push(item);
                }
            }
        });
        return mDataset;
    }

    public double getSpecificDateAmount(int month, int year, int type) {
        amount = 0;
        ParseQuery query;
        if (type == 2) {
            query = ParseQuery.getQuery("BudgetItem").whereEqualTo("year",year).whereEqualTo("month",month).addDescendingOrder("month").addDescendingOrder("year");
        } else {
            query = ParseQuery.getQuery("BudgetItem").whereEqualTo("type",type).whereEqualTo("year",year).whereEqualTo("month",month).addDescendingOrder("month").addDescendingOrder("year");
        }
        query.findInBackground(new FindCallback<BudgetItem>(){
            @Override
            public void done(List<BudgetItem> objects, ParseException e) {
                for (BudgetItem item : objects) {
                    amount += item.getAmount();
                }
            }
        });
        return amount;
    }

    public double getSpecificDateAmountByType(String category, int month, int year, int type) {
        amount = 0;
        ParseQuery selectQuery;
        if (month != -1 && year != -1) {
            if (type == 2) {
                selectQuery = ParseQuery.getQuery("BudgetItem").whereEqualTo("year", year).whereEqualTo("month", month).addDescendingOrder("month").addDescendingOrder("year");
            } else {
                selectQuery = ParseQuery.getQuery("BudgetItem").whereEqualTo("type", type).whereEqualTo("year", year).whereEqualTo("month", month).addDescendingOrder("month").addDescendingOrder("year");
            }
        } else {
            if(type == 2) {
                selectQuery = ParseQuery.getQuery("BudgetItem").whereEqualTo("category", category);
            } else {
                selectQuery = ParseQuery.getQuery("BudgetItem").whereEqualTo("category", category).whereEqualTo("type",type);
            }
        }
        selectQuery.findInBackground(new FindCallback<BudgetItem>() {
            @Override
            public void done(List<BudgetItem> objects, ParseException e) {
                for (BudgetItem item : objects) {
                    amount += item.getAmount();
                }
            }
        });
        return amount;
    }

    public void delete(BudgetItem budgetItem) {
    }

    public void deleteAll() {
    }

}
