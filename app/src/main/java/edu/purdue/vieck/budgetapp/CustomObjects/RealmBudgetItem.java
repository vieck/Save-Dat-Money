package edu.purdue.vieck.budgetapp.CustomObjects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by vieck on 1/15/16.
 */
public class RealmBudgetItem extends RealmObject {
    @PrimaryKey
    private String key;
    private int month, year;
    private float budget;

    public RealmBudgetItem() {}

    public RealmBudgetItem(int month, int year, float budget) {
        this.key = month + "/" + year;
        this.month = month;
        this.year = year;
        this.budget = budget;
    }

    public String getKey() {
        return key;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public float getBudget() {
        return budget;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setBudget(float budget) {
        this.budget = budget;
    }
}
