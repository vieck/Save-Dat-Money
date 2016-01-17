package edu.purdue.vieck.budgetapp.DatabaseAdapters;

/**
 * Created by vieck on 12/23/15.
 */
public class ParseHandler {

    /*boolean empty;
    double amount;

    public void addData(DataItem budgetItem) {
        /*DataItem budgetObject = new ParseObject("DataItem");
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
        ParseQuery<DataItem> query;
        if (type == 2) {
            query = ParseQuery.getQuery(DataItem.class);
        } else {
            query = ParseQuery.getQuery(DataItem.class).whereEqualTo("type", type);
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


    public Stack<DataItem> getAllDataAsStack(int type) {
        final Stack<DataItem> mDataset = new Stack<>();
        ParseQuery<DataItem> query;
        if (type == 2) {
            query = ParseQuery.getQuery(DataItem.class).addAscendingOrder("month").addDescendingOrder("year");
        } else {
            query = ParseQuery.getQuery(DataItem.class).whereEqualTo("type", type).addAscendingOrder("month").addDescendingOrder("year");
        }
        query.fromLocalDatastore().findInBackground(new FindCallback<DataItem>() {
            @Override
            public void done(List<DataItem> objects, ParseException e) {
                if (e == null) {
                    Log.d("Months",""+objects.size());
                    for (DataItem item : objects) {
                        mDataset.push(item);
                    }
                }
            }
        });
        return mDataset;
    }

    public HashMap<Integer, List<DataItem>> getAllYearsAsHashmap(int type) {
        final HashMap<Integer, List<DataItem>> mDataset = new HashMap<>();
        ParseQuery<DataItem> query;
        if (type == 2) {
            query = ParseQuery.getQuery(DataItem.class).addDescendingOrder("month").addDescendingOrder("year");
        } else {
            query = ParseQuery.getQuery(DataItem.class).whereEqualTo("type", type).addDescendingOrder("month").addDescendingOrder("year");
        }
        query.findInBackground(new FindCallback<DataItem>() {
            @Override
            public void done(List<DataItem> objects, ParseException e) {
                if (e == null) {
                    Log.d("Years",""+objects.size());
                    for (DataItem item : objects) {
                        if (mDataset.get(item.getYear()) == null) {
                            List<DataItem> list = new ArrayList<>();
                            list.add(item);
                            mDataset.put(item.getYear(), list);
                        } else {
                            List<DataItem> list = mDataset.get(item.getYear());
                            list.add(item);
                            mDataset.put(item.getYear(), list);
                        }
                    }
                }
            }
        });
        return mDataset;
    }

    public List<DataItem> getAllUniqueMonthsAsList(int type) {
        final List<DataItem> months = new ArrayList<>();
        final HashMap<Integer, List<Integer>> monthHashmap = new HashMap<>();
        ParseQuery<DataItem> query;
        if (type == 2) {
            query = ParseQuery.getQuery(DataItem.class).addDescendingOrder("month").addDescendingOrder("year");
        } else {
            query = ParseQuery.getQuery(DataItem.class).whereEqualTo("type", type).addDescendingOrder("month").addDescendingOrder("year");
        }
        query.findInBackground(new FindCallback<DataItem>() {
            @Override
            public void done(List<DataItem> objects, ParseException e) {
                if (e == null) {
                    for (DataItem item : objects) {
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

    public LinkedList<DataItem> getAllUniqueMonthsAsLinkedList(int type) {
        final LinkedList<DataItem> months = new LinkedList<>();
        final HashMap<Integer, List<Integer>> monthHashmap = new HashMap<>();
        ParseQuery<DataItem> query;
        if (type == 2) {
            query = ParseQuery.getQuery(DataItem.class).addDescendingOrder("month").addDescendingOrder("year");
        } else {
            query = ParseQuery.getQuery(DataItem.class).whereEqualTo("type", type).addDescendingOrder("month").addDescendingOrder("year");
        }
        query.findInBackground(new FindCallback<DataItem>() {
            @Override
            public void done(List<DataItem> objects, ParseException e) {
                if (e == null) {
                    for (DataItem item : objects) {
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

    public Stack<DataItem> getSpecificMonthYearAsStack(int month, int year, int type) {
        final Stack<DataItem> mDataset = new Stack<>();
        ParseQuery<DataItem> query;
        if (type == 2) {
            query = ParseQuery.getQuery(DataItem.class).whereEqualTo("year", year).whereEqualTo("month", month).addDescendingOrder("month").addDescendingOrder("year");
        } else {
            query = ParseQuery.getQuery(DataItem.class).whereEqualTo("type", type).whereEqualTo("year", year).whereEqualTo("month", month).addDescendingOrder("month").addDescendingOrder("year");
        }
        query.findInBackground(new FindCallback<DataItem>() {
            @Override
            public void done(List<DataItem> objects, ParseException e) {
                if (e == null) {
                    for (DataItem item : objects) {
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
        ParseQuery<DataItem> query;
        if (type == 2) {
            query = ParseQuery.getQuery(DataItem.class).whereEqualTo("year", year).whereEqualTo("month", month).addDescendingOrder("month").addDescendingOrder("year");
        } else {
            query = ParseQuery.getQuery(DataItem.class).whereEqualTo("type", type).whereEqualTo("year", year).whereEqualTo("month", month).addDescendingOrder("month").addDescendingOrder("year");
        }
        query.findInBackground(new FindCallback<DataItem>() {
            @Override
            public void done(List<DataItem> objects, ParseException e) {
                if (e == null) {
                    for (DataItem item : objects) {
                        amount += item.getBudget();
                    }
                }
            }
        });
        return (float) amount;
    }

    public float getSpecificDateAmountByType(String category, int month, int year, int type) {
        amount = 0;
        ParseQuery<DataItem> selectQuery;
        if (month != -1 && year != -1) {
            if (type == 2) {
                selectQuery = ParseQuery.getQuery(DataItem.class).whereEqualTo("year", year).whereEqualTo("month", month).addDescendingOrder("month").addDescendingOrder("year");
            } else {
                selectQuery = ParseQuery.getQuery(DataItem.class).whereEqualTo("type", type).whereEqualTo("year", year).whereEqualTo("month", month).addDescendingOrder("month").addDescendingOrder("year");
            }
        } else {
            if (type == 2) {
                selectQuery = ParseQuery.getQuery(DataItem.class).whereEqualTo("category", category);
            } else {
                selectQuery = ParseQuery.getQuery(DataItem.class).whereEqualTo("category", category).whereEqualTo("type", type);
            }
        }
        selectQuery.findInBackground(new FindCallback<DataItem>() {
            @Override
            public void done(List<DataItem> objects, ParseException e) {
                if (e == null) {
                    for (DataItem item : objects) {
                        amount += item.getBudget();
                    }
                }
            }
        });
        return (float) amount;
    }

    public void delete(DataItem budgetItem) {
    }

    public void deleteAll() {
    }
**/
}
