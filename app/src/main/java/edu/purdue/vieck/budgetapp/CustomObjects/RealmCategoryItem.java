package edu.purdue.vieck.budgetapp.CustomObjects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mvieck on 2/6/16.
 */
public class RealmCategoryItem extends RealmObject {
    @PrimaryKey
    private int key;
    private boolean isChild;
    private String category, subcategory;

    public RealmCategoryItem(String category, String subcategory, boolean isChild) {
        this.category = category;
        this.subcategory = subcategory;
        this.isChild = isChild;
    }

    public int getKey() {
        return key;
    }

    public String getCategory() {
        return category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public boolean isChild() {
        return isChild;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public void setChild(boolean child) {
        isChild = child;
    }
}
