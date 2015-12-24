package edu.purdue.vieck.budgetapp.CustomObjects;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by vieck on 7/14/15.
 */

@ParseClassName("BudgetItem")
public class BudgetItem extends ParseObject {

    String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    public BudgetItem() { }

    public double getAmount() {
        return getDouble("amount");
    }

    public void setAmount(double amount) {
        put("amount", amount);
    }

    public String getCategory() {
        return getString("category");
    }

    public void setCategory(String category) {
        put("category", category);
    }

    public String getSubcategory() {
        return getString("subcategory");
    }

    public void setSubcategory(String subcategory) {
        put("subcategory", subcategory);
    }

    public String getNote() {
        return getString("note");
    }

    public void setNote(String note) {
        put("note",note);
    }

    public boolean isType() {
        return getBoolean("type");
    }

    public String getTypeAsString() {
        boolean type = isType();
        if (type) {
            return "Income";
        } else {
            return "Expense";
        }
    }

    public void setType(boolean type) {
        put("type", type);
    }

    public int getDay() {
        return getInt("day");
    }

    public void setDay(int day) {
        put("day",day);
    }

    public int getMonth() {
        return getInt("month");
    }

    public String getMonthName() {
        return months[getMonth()-1];
    }

    public void setMonth(int month) {
        put("month",month);
    }

    public int getYear() {
        return getInt("year");
    }

    public void setYear(int year) {
        put("year",year);
    }

    public int getImage() {
        return getInt("image");
    }

    public void setImage(int image) {
        put("image",image);
    }
}
