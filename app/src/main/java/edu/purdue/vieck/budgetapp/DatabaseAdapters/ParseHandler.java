package edu.purdue.vieck.budgetapp.DatabaseAdapters;

/**
 * Created by vieck on 12/23/15.
 */
public class ParseHandler {

    /*boolean empty;
    double amount;

    public void addData(RealmDataItem budgetItem) {
        /*RealmDataItem budgetObject = new ParseObject("RealmDataItem");
        budgetObject.put("amount", budgetItem.getBudget());
        budgetObject.put("category", budgetItem.getCategory());
        budgetObject.put("subcategory", budgetItem.getSubcategory());
        budgetObject.put("type", budgetItem.isType());
        budgetObject.put("day", budgetItem.getDay());
        budgetObject.put("month", budgetItem.getMonth());
        budgetObject.put("year", budgetItem.getYear());
        budgetObject.put("note", budgetItem.getNote());*/
/*
    public boolean isEmpty(int type) {
        empty = true;
        ParseQuery<RealmDataItem> query;
        if (type == 2) {
            query = ParseQuery.getQuery(RealmDataItem.class);
        } else {
            query = ParseQuery.getQuery(RealmDataItem.class).whereEqualTo("type", type);
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


    public Stack<RealmDataItem> getAllDataAsStack(int type) {
        final Stack<RealmDataItem> mDataset = new Stack<>();
        ParseQuery<RealmDataItem> query;
        if (type == 2) {
            query = ParseQuery.getQuery(RealmDataItem.class).addAscendingOrder("month").addDescendingOrder("year");
        } else {
            query = ParseQuery.getQuery(RealmDataItem.class).whereEqualTo("type", type).addAscendingOrder("month").addDescendingOrder("year");
        }
        query.fromLocalDatastore().findInBackground(new FindCallback<RealmDataItem>() {
            @Override
            public void done(List<RealmDataItem> objects, ParseException e) {
                if (e == null) {
                    Log.d("Months",""+objects.size());
                    for (RealmDataItem item : objects) {
                        mDataset.push(item);
                    }
                }
            }
        });
        return mDataset;
    }

    public HashMap<Integer, List<RealmDataItem>> getAllYearsAsHashmap(int type) {
        final HashMap<Integer, List<RealmDataItem>> mDataset = new HashMap<>();
        ParseQuery<RealmDataItem> query;
        if (type == 2) {
            query = ParseQuery.getQuery(RealmDataItem.class).addDescendingOrder("month").addDescendingOrder("year");
        } else {
            query = ParseQuery.getQuery(RealmDataItem.class).whereEqualTo("type", type).addDescendingOrder("month").addDescendingOrder("year");
        }
        query.findInBackground(new FindCallback<RealmDataItem>() {
            @Override
            public void done(List<RealmDataItem> objects, ParseException e) {
                if (e == null) {
                    Log.d("Years",""+objects.size());
                    for (RealmDataItem item : objects) {
                        if (mDataset.get(item.getYear()) == null) {
                            List<RealmDataItem> list = new ArrayList<>();
                            list.add(item);
                            mDataset.put(item.getYear(), list);
                        } else {
                            List<RealmDataItem> list = mDataset.get(item.getYear());
                            list.add(item);
                            mDataset.put(item.getYear(), list);
                        }
                    }
                }
            }
        });
        return mDataset;
    }

    public List<RealmDataItem> getAllUniqueMonthsAsList(int type) {
        final List<RealmDataItem> months = new ArrayList<>();
        final HashMap<Integer, List<Integer>> monthHashmap = new HashMap<>();
        ParseQuery<RealmDataItem> query;
        if (type == 2) {
            query = ParseQuery.getQuery(RealmDataItem.class).addDescendingOrder("month").addDescendingOrder("year");
        } else {
            query = ParseQuery.getQuery(RealmDataItem.class).whereEqualTo("type", type).addDescendingOrder("month").addDescendingOrder("year");
        }
        query.findInBackground(new FindCallback<RealmDataItem>() {
            @Override
            public void done(List<RealmDataItem> objects, ParseException e) {
                if (e == null) {
                    for (RealmDataItem item : objects) {
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

    public LinkedList<RealmDataItem> getAllUniqueMonthsAsLinkedList(int type) {
        final LinkedList<RealmDataItem> months = new LinkedList<>();
        final HashMap<Integer, List<Integer>> monthHashmap = new HashMap<>();
        ParseQuery<RealmDataItem> query;
        if (type == 2) {
            query = ParseQuery.getQuery(RealmDataItem.class).addDescendingOrder("month").addDescendingOrder("year");
        } else {
            query = ParseQuery.getQuery(RealmDataItem.class).whereEqualTo("type", type).addDescendingOrder("month").addDescendingOrder("year");
        }
        query.findInBackground(new FindCallback<RealmDataItem>() {
            @Override
            public void done(List<RealmDataItem> objects, ParseException e) {
                if (e == null) {
                    for (RealmDataItem item : objects) {
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

    public Stack<RealmDataItem> getSpecificMonthYearAsStack(int month, int year, int type) {
        final Stack<RealmDataItem> mDataset = new Stack<>();
        ParseQuery<RealmDataItem> query;
        if (type == 2) {
            query = ParseQuery.getQuery(RealmDataItem.class).whereEqualTo("year", year).whereEqualTo("month", month).addDescendingOrder("month").addDescendingOrder("year");
        } else {
            query = ParseQuery.getQuery(RealmDataItem.class).whereEqualTo("type", type).whereEqualTo("year", year).whereEqualTo("month", month).addDescendingOrder("month").addDescendingOrder("year");
        }
        query.findInBackground(new FindCallback<RealmDataItem>() {
            @Override
            public void done(List<RealmDataItem> objects, ParseException e) {
                if (e == null) {
                    for (RealmDataItem item : objects) {
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
        ParseQuery<RealmDataItem> query;
        if (type == 2) {
            query = ParseQuery.getQuery(RealmDataItem.class).whereEqualTo("year", year).whereEqualTo("month", month).addDescendingOrder("month").addDescendingOrder("year");
        } else {
            query = ParseQuery.getQuery(RealmDataItem.class).whereEqualTo("type", type).whereEqualTo("year", year).whereEqualTo("month", month).addDescendingOrder("month").addDescendingOrder("year");
        }
        query.findInBackground(new FindCallback<RealmDataItem>() {
            @Override
            public void done(List<RealmDataItem> objects, ParseException e) {
                if (e == null) {
                    for (RealmDataItem item : objects) {
                        amount += item.getBudget();
                    }
                }
            }
        });
        return (float) amount;
    }

    public float getSpecificDateAmountByType(String category, int month, int year, int type) {
        amount = 0;
        ParseQuery<RealmDataItem> selectQuery;
        if (month != -1 && year != -1) {
            if (type == 2) {
                selectQuery = ParseQuery.getQuery(RealmDataItem.class).whereEqualTo("year", year).whereEqualTo("month", month).addDescendingOrder("month").addDescendingOrder("year");
            } else {
                selectQuery = ParseQuery.getQuery(RealmDataItem.class).whereEqualTo("type", type).whereEqualTo("year", year).whereEqualTo("month", month).addDescendingOrder("month").addDescendingOrder("year");
            }
        } else {
            if (type == 2) {
                selectQuery = ParseQuery.getQuery(RealmDataItem.class).whereEqualTo("category", category);
            } else {
                selectQuery = ParseQuery.getQuery(RealmDataItem.class).whereEqualTo("category", category).whereEqualTo("type", type);
            }
        }
        selectQuery.findInBackground(new FindCallback<RealmDataItem>() {
            @Override
            public void done(List<RealmDataItem> objects, ParseException e) {
                if (e == null) {
                    for (RealmDataItem item : objects) {
                        amount += item.getBudget();
                    }
                }
            }
        });
        return (float) amount;
    }

    public void delete(RealmDataItem budgetItem) {
    }

    public void deleteAll() {
    }
**/
}
