package edu.purdue.vieck.budgetapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.parse.Parse;
import com.parse.ParseObject;

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
        return true;
    }


    public Stack<BudgetItem> getAllDataAsStack(int type) {
        Stack<BudgetItem> mDataset = new Stack<>();
        return mDataset;
    }

    public List<BudgetItem> getAllMonthsAsList() {
        List<BudgetItem> mDataset = new ArrayList<>();
        return mDataset;
    }

    public HashMap<Integer, List<BudgetItem>> getAllYearsAsHashmap(int type) {
        HashMap<Integer, List<BudgetItem>> mDataset = new HashMap<>();
        return mDataset;
    }

    public List<BudgetItem> getAllUniqueMonthsAsList(int type) {
        List<BudgetItem> months = new ArrayList<>();
        return months;
    }

    public LinkedList<BudgetItem> getAllUniqueMonthsAsLinkedList(int type) {
        LinkedList<BudgetItem> months = new LinkedList<>();
        return months;
    }

    public int getNumberOfMonths() {
        return 0;
    }

    public Stack<BudgetItem> getSpecificMonthYearAsStack(int month, int year, int type) {
        Stack<BudgetItem> mDataset = new Stack<>();
        return mDataset;
    }

    public float getSpecificDateAmount(int month, int year, int type) {
        float amount = 0;
        return amount;
    }

    public float getSpecificDateAmountByType(String category, int month, int year, int type) {
        float categoryPercent = 0;
        return categoryPercent;
    }

    public ArrayList<BudgetItem> getFilteredDataAsArrayList(String filter) {
        ArrayList<BudgetItem> mDataset = new ArrayList<>();
        return mDataset;
    }

    public Stack<BudgetItem> searchDatabase(String searchParameters) {
        Stack<BudgetItem> mDataset = new Stack<>();
        return mDataset;
    }

    public void delete(BudgetItem budgetItem) {
    }

    public void deleteAll() {
    }

}
