package edu.purdue.vieck.budgetapp.CustomObjects;

/**
 * Created by vieck on 7/14/15.
 */
public class BudgetItem {
    private String category, subcategory, note;
    private boolean type;
    private int day, month, year, image;
    private float amount;

    String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};


    public BudgetItem() {

    }

    public BudgetItem(float amount, String category, String subcategory, boolean type, int day, int month, int year, String note, int image) {
        this.amount = amount;
        this.category = category;
        this.subcategory = subcategory;
        this.type = type;
        this.day = day;
        this.month = month;
        this.year = year;
        this.note = note;
        this.image = image;
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

    public boolean isType() {
        return type;
    }

    public String getTypeAsString() {
        if (type) {
            return "Income";
        } else {
            return "Expense";
        }
    }

    public void setType(boolean type) {
        this.type = type;
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

    public String getMonthName() {
        return months[month-1];
    }

    public void setMonth(int month) {
        this.month = month;
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
