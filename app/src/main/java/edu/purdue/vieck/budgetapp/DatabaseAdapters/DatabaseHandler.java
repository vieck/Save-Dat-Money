package edu.purdue.vieck.budgetapp.DatabaseAdapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import edu.purdue.vieck.budgetapp.CustomObjects.DataItem;

/**
 * Created by vieck on 7/22/15.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "budget_database";
    private static final String TABLE_DATA = "data";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_SUB_CATEGORY = "subcategory";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_DAY = "day";
    private static final String COLUMN_MONTH = "month";
    private static final String COLUMN_YEAR = "year";
    private static final String COLUMN_NOTE = "note";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_COLOR_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_DATA + " ( "
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_CATEGORY + " TEXT,"
                + COLUMN_SUB_CATEGORY + " TEXT,"
                + COLUMN_AMOUNT + " REAL,"
                + COLUMN_TYPE + " INTEGER,"
                + COLUMN_DAY + " INTEGER,"
                + COLUMN_MONTH + " INTEGER,"
                + COLUMN_YEAR + " INTEGER,"
                + COLUMN_NOTE + " TEXT" + ");";
        db.execSQL(CREATE_COLOR_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA);
        onCreate(db);
    }

    public void addData(DataItem dataItem) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CATEGORY, dataItem.getCategory());
        contentValues.put(COLUMN_SUB_CATEGORY, dataItem.getSubcategory());
        contentValues.put(COLUMN_AMOUNT, dataItem.getAmount());
        contentValues.put(COLUMN_TYPE, dataItem.getType());
        contentValues.put(COLUMN_DAY, dataItem.getDay());
        contentValues.put(COLUMN_MONTH, dataItem.getMonth());
        contentValues.put(COLUMN_YEAR, dataItem.getYear());
        contentValues.put(COLUMN_NOTE, dataItem.getNote());

        database.insert(TABLE_DATA, null, contentValues);
        database.close();
    }

    private DataItem convertCursorToItem(Cursor cursor) {
        DataItem dataItem = new DataItem();
        cursor.getLong(0);
        dataItem.setCategory(cursor.getString(1));
        dataItem.setSubcategory(cursor.getString(2));
        dataItem.setAmount(cursor.getFloat(3));
        if (cursor.getInt(4) == 0) {
            dataItem.setType(false);
        } else {
            dataItem.setType(true);
        }
        dataItem.setDay(cursor.getInt(5));
        dataItem.setMonth(cursor.getInt(6));
        dataItem.setYear(cursor.getInt(7));
        dataItem.setNote(cursor.getString(8));
        return dataItem;
    }

    public boolean isEmpty(int type) {
        Cursor mCursor;
        if (type == 2) {
            mCursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_DATA, null);
        } else {
            mCursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_DATA + " WHERE " + COLUMN_TYPE + " = " + type, null);
        }
        Boolean rowExists;

        if (mCursor.moveToFirst()) {
            // Contains elements
            this.getReadableDatabase().close();
            return false;

        } else {
            // Empty
            this.getReadableDatabase().close();
            return true;
        }
    }


    public Stack<DataItem> getAllDataAsStack(int type) {
        Stack<DataItem> mDataset = new Stack<>();
        String selectQuery;
        if (type == 2) {
            selectQuery = "SELECT  * FROM " + TABLE_DATA
                    + " ORDER BY " + COLUMN_MONTH + " ASC," + COLUMN_YEAR + " DESC";
        } else {
            selectQuery = "SELECT  * FROM " + TABLE_DATA + " WHERE " + COLUMN_TYPE + " = " + type
                    + " ORDER BY " + COLUMN_MONTH + " ASC," + COLUMN_YEAR + " DESC";
        }
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                mDataset.add(convertCursorToItem(cursor));
            } while (cursor.moveToNext());
        }
        return mDataset;
    }

    public List<DataItem> getAllMonthsAsList() {
        List<DataItem> mDataset = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_DATA
                + " ORDER BY " + COLUMN_MONTH + " ASC," + COLUMN_YEAR + " DESC";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                mDataset.add(convertCursorToItem(cursor));
            } while (cursor.moveToNext());
        }
        return mDataset;
    }

    public HashMap<Integer, List<DataItem>> getAllYearsAsHashmap(int type) {
        HashMap<Integer, List<DataItem>> mDataset = new HashMap<>();
        String selectQuery;
        if (type == 2) {
            selectQuery = "SELECT * FROM " + TABLE_DATA
                    + " ORDER BY " + COLUMN_MONTH + " DESC," + COLUMN_YEAR + " DESC";
        } else {
            selectQuery = "SELECT * FROM " + TABLE_DATA + " WHERE " + COLUMN_TYPE + " = " + type
                    + " ORDER BY " + COLUMN_MONTH + " DESC," + COLUMN_YEAR + " DESC";
        }
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                DataItem dataItem = convertCursorToItem(cursor);
                if (mDataset.get(dataItem.getYear()) == null) {
                    List<DataItem> list = new ArrayList<>();
                    list.add(dataItem);
                    mDataset.put(dataItem.getYear(), list);
                } else {
                    List<DataItem> list = mDataset.get(dataItem.getYear());
                    list.add(dataItem);
                    mDataset.put(dataItem.getYear(), list);
                }
            } while (cursor.moveToNext());
        }
        return mDataset;
    }

    public List<DataItem> getAllUniqueMonthsAsList(int type) {
        List<DataItem> months = new ArrayList<>();
        String selectQuery;
        if (type == 2) {
            selectQuery = "SELECT " + COLUMN_MONTH + "," + COLUMN_YEAR + ",COUNT(*)"
                    + " FROM " + TABLE_DATA
                    + " GROUP BY " + COLUMN_MONTH + "," + COLUMN_YEAR;
        } else {
            selectQuery = "SELECT " + COLUMN_MONTH + "," + COLUMN_YEAR + ",COUNT(*)"
                    + " FROM " + TABLE_DATA + " WHERE " + COLUMN_TYPE + " = " + type
                    + " GROUP BY " + COLUMN_MONTH + "," + COLUMN_YEAR;
        }
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                DataItem item = new DataItem();
                item.setMonth(cursor.getInt(0));
                item.setYear(cursor.getInt(1));
                months.add(item);
            } while (cursor.moveToNext());
        }
        return months;
    }

    public LinkedList<DataItem> getAllUniqueMonthsAsLinkedList(int type) {
        LinkedList<DataItem> months = new LinkedList<>();
        String selectQuery;
        if (type == 2) {
            selectQuery = "SELECT " + COLUMN_MONTH + "," + COLUMN_YEAR + ",COUNT(*)"
                    + " FROM " + TABLE_DATA
                    + " GROUP BY " + COLUMN_MONTH + "," + COLUMN_YEAR;
        } else {
            selectQuery = "SELECT " + COLUMN_MONTH + "," + COLUMN_YEAR + ",COUNT(*)"
                    + " FROM " + TABLE_DATA + " WHERE " + COLUMN_TYPE + " = " + type
                    + " GROUP BY " + COLUMN_MONTH + "," + COLUMN_YEAR;
        }
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                DataItem item = new DataItem();
                item.setMonth(cursor.getInt(0));
                item.setYear(cursor.getInt(1));
                months.add(item);
            } while (cursor.moveToNext());
        }
        return months;
    }

    public int getNumberOfMonths() {
        String selectQuery = "SELECT " + COLUMN_MONTH + " , " + COLUMN_YEAR + ",COUNT(*)"
                + " FROM " + TABLE_DATA
                + " ORDER BY " + COLUMN_MONTH + " DESC," + COLUMN_YEAR + " DESC";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        return cursor.getCount();
    }

    public Stack<DataItem> getSpecificMonthYearAsStack(int month, int year, int type) {
        Stack<DataItem> mDataset = new Stack<>();
        String selectQuery;
        if (type == 2) {
            selectQuery = "SELECT * FROM " + TABLE_DATA + " WHERE " + COLUMN_MONTH + " = " + month
                    + " and " + COLUMN_YEAR + " = " + year
                    + " ORDER BY " + COLUMN_MONTH + " DESC," + COLUMN_YEAR + " DESC";
        } else {
            selectQuery = "SELECT * FROM " + TABLE_DATA + " WHERE " + COLUMN_MONTH + " = " + month
                    + " and " + COLUMN_YEAR + " = " + year + " and " + COLUMN_TYPE + " = " + type
                    + " ORDER BY " + COLUMN_MONTH + " DESC," + COLUMN_YEAR + " DESC";
        }
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                mDataset.add(convertCursorToItem(cursor));
            } while (cursor.moveToNext());
        }
        sqLiteDatabase.close();
        return mDataset;
    }

    public float getSpecificDateAmount(int month, int year, int type) {
        float amount = 0;
        String selectQuery;
        if (type == 2) {
            selectQuery = "SELECT * FROM " + TABLE_DATA + " WHERE " + COLUMN_MONTH + " = " + month
                    + " and " + COLUMN_YEAR + " = " + year
                    + " ORDER BY " + COLUMN_MONTH + " DESC," + COLUMN_YEAR + " DESC";
        } else {
            selectQuery = "SELECT * FROM " + TABLE_DATA + " WHERE " + COLUMN_MONTH + " = " + month
                    + " and " + COLUMN_YEAR + " = " + year + " and " + COLUMN_TYPE + " = " + type
                    + " ORDER BY " + COLUMN_MONTH + " DESC," + COLUMN_YEAR + " DESC";
        }
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                amount += cursor.getFloat(3);
            } while (cursor.moveToNext());
        }

        sqLiteDatabase.close();
        return amount;
    }

    public float getSpecificDateAmountByType(String category, int month, int year, int type) {
        float categoryPercent = 0;
        String selectQuery;
        String totalQuery;
        if (month != -1 && year != -1) {
            if (type == 2) {
                selectQuery = "SELECT * FROM " + TABLE_DATA + " WHERE category LIKE '%" + category + "%'"
                        + " and " + COLUMN_MONTH + " = " + month
                        + " and " + COLUMN_YEAR + " = " + year
                        + " ORDER BY " + COLUMN_MONTH + " DESC," + COLUMN_YEAR + " DESC";

                totalQuery = "SELECT * FROM " + TABLE_DATA + " WHERE " + COLUMN_MONTH + " = " + month
                        + " and " + COLUMN_YEAR + " = " + year
                        + " ORDER BY " + COLUMN_MONTH + " DESC," + COLUMN_YEAR + " DESC";
            } else {
                selectQuery = "SELECT * FROM " + TABLE_DATA + " WHERE category LIKE '%" + category + "%'"
                        + " and " + COLUMN_MONTH + " = " + month
                        + " and " + COLUMN_YEAR + " = " + year
                        + " and " + COLUMN_TYPE + " = " + type
                        + " ORDER BY " + COLUMN_MONTH + " DESC," + COLUMN_YEAR + " DESC";

                totalQuery = "SELECT * FROM " + TABLE_DATA + " WHERE " + COLUMN_MONTH + " = " + month
                        + " and " + COLUMN_YEAR + " = " + year + " and " + COLUMN_TYPE + " = " + type
                        + " ORDER BY " + COLUMN_MONTH + " DESC," + COLUMN_YEAR + " DESC";
            }
        } else {
            if (type == 2) {
                selectQuery = "SELECT * FROM " + TABLE_DATA + " WHERE category LIKE '%" + category + "%'";
                totalQuery = "SELECT * FROM " + TABLE_DATA;
            } else {
                selectQuery = "SELECT * FROM " + TABLE_DATA + " WHERE category LIKE '%" + category + "%'"
                        + " and " + COLUMN_TYPE + " = " + type;
                totalQuery = "SELECT * FROM " + TABLE_DATA + " WHERE " + COLUMN_TYPE + " = " + type;
            }
        }
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                categoryPercent += cursor.getFloat(3);
            } while (cursor.moveToNext());
        }
        cursor = sqLiteDatabase.rawQuery(totalQuery, null);

        int total = cursor.getCount();

        sqLiteDatabase.close();
        return categoryPercent;
    }

    public ArrayList<DataItem> getFilteredDataAsArrayList(String filter) {
        ArrayList<DataItem> mDataset = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_DATA + " WHERE category LIKE '%" + filter + "%'";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                mDataset.add(convertCursorToItem(cursor));
            } while (cursor.moveToNext());
        }
        sqLiteDatabase.close();
        return mDataset;
    }

    public Stack<DataItem> searchDatabase(String searchParameters) {
        Stack<DataItem> mDataset = new Stack<>();
        String selectQuery = "SELECT * FROM " + TABLE_DATA + " WHERE " + COLUMN_CATEGORY + "LIKE '%" + searchParameters + "%' or "
                + COLUMN_AMOUNT + " LIKE '" + searchParameters + "' or "
                + COLUMN_DAY + " LIKE '%" + searchParameters + "%' or "
                + COLUMN_MONTH + " LIKE '%" + searchParameters + "%' or "
                + COLUMN_YEAR + " LIKE '%" + searchParameters + "%' or "
                + COLUMN_NOTE + " LIKE '%" + searchParameters + "%'";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                mDataset.add(convertCursorToItem(cursor));
            } while (cursor.moveToNext());
        }
        sqLiteDatabase.close();
        return mDataset;
    }

    public void delete(DataItem dataItem) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_DATA, COLUMN_ID + " = " + "'" + dataItem.getCategory() + "'", null);
        database.close();
    }

    public void deleteAll() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_DATA, null, null);
        database.close();
    }


}
