package edu.purdue.vieck.budgetapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import edu.purdue.vieck.budgetapp.CustomObjects.BudgetItem;

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

    public void addData(BudgetItem budgetItem) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CATEGORY, budgetItem.getCategory());
        contentValues.put(COLUMN_SUB_CATEGORY, budgetItem.getSubcategory());
        contentValues.put(COLUMN_AMOUNT, budgetItem.getAmount());
        contentValues.put(COLUMN_TYPE, budgetItem.isType());
        contentValues.put(COLUMN_DAY, budgetItem.getDay());
        contentValues.put(COLUMN_MONTH, budgetItem.getMonth());
        contentValues.put(COLUMN_YEAR, budgetItem.getYear());
        contentValues.put(COLUMN_NOTE, budgetItem.getNote());

        database.insert(TABLE_DATA, null, contentValues);
        database.close();
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
        Log.d("Database", "Category" + budgetItem.getSubcategory() +
                "\nAmount " + budgetItem.getAmount() +
                "\nType " + budgetItem.isType());
        return budgetItem;
    }


    public Stack<BudgetItem> getAllData() {
        Stack<BudgetItem> mDataset = new Stack<>();
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

    public List<BudgetItem> getAllMonths() {
        List<BudgetItem> mDataset = new ArrayList<>();
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

    public HashMap<Integer, List<BudgetItem>> getAllYears() {
        HashMap<Integer, List<BudgetItem>> mDataset = new HashMap<>();
        String selectQuery = "SELECT * FROM " + TABLE_DATA
                + " ORDER BY " + COLUMN_MONTH + " DESC," + COLUMN_YEAR + " DESC";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                BudgetItem budgetItem = convertCursorToItem(cursor);
                if (mDataset.get(budgetItem.getYear()) == null) {
                    List<BudgetItem> list = new ArrayList<>();
                    list.add(budgetItem);
                    mDataset.put(budgetItem.getYear(), list);
                } else {
                    List<BudgetItem> list = mDataset.get(budgetItem.getYear());
                    list.add(budgetItem);
                    mDataset.put(budgetItem.getYear(), list);
                }
            } while (cursor.moveToNext());
        }
        return mDataset;
    }

    public Stack<BudgetItem> getSpecificMonthYear(int month, int year) {
        Stack<BudgetItem> mDataset = new Stack<>();
        String selectQuery = "SELECT * FROM " + TABLE_DATA + " WHERE " + COLUMN_MONTH + " = " + month
                + " and " + COLUMN_YEAR + " = " + year
                + " ORDER BY " + COLUMN_MONTH + " DESC," + COLUMN_YEAR + " DESC";
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

    public ArrayList<BudgetItem> getFilteredData(String filter) {
        ArrayList<BudgetItem> mDataset = new ArrayList<>();
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

    public Stack<BudgetItem> searchDatabase(String searchParameters) {
        Stack<BudgetItem> mDataset = new Stack<>();
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

    public void delete(BudgetItem budgetItem) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_DATA, COLUMN_ID + " = " + "'" + budgetItem.getCategory() + "'", null);
        database.close();
    }

    public void deleteAll() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_DATA, null, null);
        database.close();
    }

    public float getSpecificDateAmount(int month, int year) {
        float amount = 0;
        String selectQuery = "SELECT * FROM " + TABLE_DATA + " WHERE " + COLUMN_MONTH + " = " + month
                + " and " + COLUMN_YEAR + " = " + year
                + " ORDER BY " + COLUMN_MONTH + " DESC," + COLUMN_YEAR + " DESC";
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

    public float getSpecificDateAmountByType(String type, int month, int year) {
        float categoryPercent = 0;
        String selectQuery = "SELECT * FROM " + TABLE_DATA + " WHERE category LIKE '%" + type + "%'";
        String totalQuery = "SELECT * FROM " + TABLE_DATA;
        if (month != -1 && year != -1) {
            selectQuery = "SELECT * FROM " + TABLE_DATA + " WHERE category LIKE '%" + type + "%'"
                    + " and " + COLUMN_MONTH + " = " + month
                    + " and " + COLUMN_YEAR + " = " + year
                    + " ORDER BY " + COLUMN_MONTH + " DESC," + COLUMN_YEAR + " DESC";

            totalQuery = "SELECT * FROM " + TABLE_DATA + " WHERE " + COLUMN_MONTH + " = " + month
                    + " and " + COLUMN_YEAR + " = " + year
                    + " ORDER BY " + COLUMN_MONTH + " DESC," + COLUMN_YEAR + " DESC";
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
        if (total > 0) {
            categoryPercent = categoryPercent / total;
        }
        return categoryPercent;
    }
}
