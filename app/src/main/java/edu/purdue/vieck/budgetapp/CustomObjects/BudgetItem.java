package edu.purdue.vieck.budgetapp.CustomObjects;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by vieck on 7/14/15.
 */

public class BudgetItem extends RealmObject {

    @PrimaryKey
    private int id;
    private String category, subcategory, note, monthString, typeString;
    private boolean type;
    private int day, month, year, image;
    private float amount;

    public BudgetItem() {

    }

    public BudgetItem(float amount, String category, String subcategory, boolean type, int day, int month, int year, String note, int image) {
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        this.amount = amount;
        this.category = category;
        this.subcategory = subcategory;
        this.type = type;
        this.day = day;
        this.month = month;
        this.year = year;
        this.note = note;
        this.image = image;
        this.monthString = months[month - 1];

        if (type) {
            typeString = "Income";
        } else {
            typeString = "Expense";
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean getType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public String getTypeString() {
        return typeString;
    }

    public void setTypeString(String typeString) {
        this.typeString = typeString;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getMonthString() {
        return monthString;
    }

    public void setMonthString(String monthString) {
        this.monthString = monthString;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

}
