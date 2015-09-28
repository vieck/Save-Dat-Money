package edu.purdue.vieck.budgetapp.CustomObjects;

import android.graphics.drawable.Drawable;

/**
 * Created by mvieck on 9/27/2015.
 */
public class CategoryItem {

    Drawable icon;
    String type;

    public CategoryItem() {}

    public CategoryItem(Drawable icon, String type) {
        this.icon = icon;
        this.type = type;
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
}
