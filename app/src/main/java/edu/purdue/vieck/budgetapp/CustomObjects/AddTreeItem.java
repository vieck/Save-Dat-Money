package edu.purdue.vieck.budgetapp.CustomObjects;

/**
 * Created by mvieck on 10/9/2015.
 */
public class AddTreeItem {

    String name;
    float amount;
    int drawableId;

    public AddTreeItem() {
    }

    ;

    public AddTreeItem(int drawableId, String name, float amount) {
        this.drawableId = drawableId;
        this.name = name;
        this.amount = amount;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
