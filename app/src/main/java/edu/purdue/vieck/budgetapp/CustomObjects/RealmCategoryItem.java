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
    private int color, icon;

    public RealmCategoryItem() {}

    public RealmCategoryItem(String category, String subcategory, int icon, int color, boolean isChild) {
        this.category = category;
        this.subcategory = subcategory;
        this.icon = icon;
        this.color = color;
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

    public int getIcon() {
        return icon;
    }

    public int getColor() {
        return color;
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

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
