package edu.purdue.vieck.budgetapp.Adapters;

import android.graphics.drawable.Drawable;

import com.lukedeighton.wheelview.adapter.WheelAdapter;

import java.util.List;

/**
 * Created by vieck on 2/20/16.
 */
public class DrawableAdapter implements WheelAdapter {

    List<Drawable> drawables;

    public DrawableAdapter(List<Drawable> drawables) {
        this.drawables = drawables;
    }

    @Override
    public Drawable getDrawable(int position) {
        return drawables.get(position);
    }

    @Override
    public int getCount() {
        return drawables.size();
    }

    @Override
    public Object getItem(int position) {
        return drawables.get(position);
    }
}
