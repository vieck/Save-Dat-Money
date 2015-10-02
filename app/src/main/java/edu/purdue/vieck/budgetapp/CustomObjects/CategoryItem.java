package edu.purdue.vieck.budgetapp.CustomObjects;

import android.graphics.drawable.Drawable;

/**
 * Created by mvieck on 9/27/2015.
 */
public class CategoryItem {

    Drawable icon;
    String type;
    String subType;

    public CategoryItem() {
    }

    public CategoryItem(Drawable icon, String type, String subType) {
        this.icon = icon;
        this.type = type;
        this.subType = subType;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }
}
