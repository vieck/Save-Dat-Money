package edu.purdue.vieck.budgetapp.Fragments;


import android.app.Fragment;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.DecoDrawEffect;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import edu.purdue.vieck.budgetapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class IncomeExpensesGraphFragment extends GraphParentFragment {

    final private float trackBackWidth = 60f;
    final private float trackWidth = 30f;
    final private boolean[] mClockwise = {true, true, true, false, true};
    final private boolean[] mRounded = {true, true, true, true, true};
    final private boolean[] mPie = {false, false, false, false, true};
    final private int[] totalAngle = {180, 180, 180, 180, 180};
    final private int[] rotateAngle = {0, 180, 180, 0, 180};
    private int backIndex;
    private int seriesIndexOne;
    private int seriesIndexTwo;
    private int styleIndex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_graph, container, false);
    }

    @Override
    protected void createTracks() {
        final float seriesMax = 50f;
        final DecoView decoView = getDecoView();
        final View view = getView();
        if (decoView == null || view == null) {
            return;
        }
        decoView.deleteAll();
        decoView.configureAngles(totalAngle[styleIndex], rotateAngle[styleIndex]);

        SeriesItem arcBackTrack = new SeriesItem.Builder(Color.argb(255, 228, 228, 228))
                .setRange(0, seriesMax, seriesMax)
                .setInitialVisibility(false)
                .setLineWidth(getDimension(trackBackWidth))
                .setChartStyle(mPie[styleIndex] ? SeriesItem.ChartStyle.STYLE_PIE : SeriesItem.ChartStyle.STYLE_DONUT)
                .build();

        backIndex = decoView.addSeries(arcBackTrack);

        float inset = 0;
        if (trackBackWidth != trackWidth) {
            inset = getDimension((trackBackWidth - trackWidth) / 2);
        }
        SeriesItem incomeSeriesItem = new SeriesItem.Builder(getResources().getColor(R.color.Red))
                .setRange(0, seriesMax, 0)
                .setInitialVisibility(false)
                .setLineWidth(getDimension(trackWidth))
                .setInset(new PointF(-inset, -inset))
                .setSpinClockwise(mClockwise[styleIndex])
                .setCapRounded(mRounded[styleIndex])
                .build();

        seriesIndexOne = decoView.addSeries(incomeSeriesItem);

        SeriesItem expenseSeriesItem = new SeriesItem.Builder(getResources().getColor(R.color.Lime))
                .setRange(0, seriesMax, 0)
                .setInitialVisibility(false)
                .setCapRounded(true)
                .setLineWidth(getDimension(trackWidth))
                .setInset(new PointF(inset, inset))
                .setCapRounded(mRounded[styleIndex])
                .build();

        seriesIndexTwo = decoView.addSeries(expenseSeriesItem);

        final TextView textPercent = (TextView) view.findViewById(R.id.textPercentage);
        if (textPercent != null) {
            textPercent.setText("");
            addProgressListener(incomeSeriesItem, textPercent, "%.0f%%");
        }

        final TextView textToGo = (TextView) view.findViewById(R.id.textRemaining);
        textToGo.setText("");
        addProgressRemainingListener(incomeSeriesItem, textToGo, "%.0f min to goal", seriesMax);

        // View layout = getView().findViewById(R.id.layoutActivities);
        //layout.setVisibility(View.INVISIBLE);

        final TextView textActivity1 = (TextView) getView().findViewById(R.id.textPercentage);
        addProgressListener(incomeSeriesItem, textActivity1, "%.0f Km");
        textActivity1.setText("");

        final TextView textActivity2 = (TextView) getView().findViewById(R.id.textRemaining);
        textActivity2.setText("");
        addProgressListener(expenseSeriesItem, textActivity2, "%.0f Km");
    }

    @Override
    protected void setupEvents() {
        final DecoView decoView = getDecoView();
        final View view = getView();
        if (decoView == null || decoView.isEmpty() || view == null) {
            return;
        }

        mUpdateListeners = true;
        final TextView textPercent = (TextView) view.findViewById(R.id.textPercentage);
        final TextView textToGo = (TextView) view.findViewById(R.id.textRemaining);
        //final View layout = view.findViewById(R.id.layoutActivities);
        final View[] linkedViews = {textPercent, textToGo};
        final int fadeDuration = 2000;

        decoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT_FILL)
                .setIndex(backIndex)
                .setDuration(3000)
                .build());

        decoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(seriesIndexOne)
                .setFadeDuration(fadeDuration)
                .setDuration(2000)
                .setDelay(1000)
                .build());

        decoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(seriesIndexTwo)
                .setLinkedViews(linkedViews)
                .setDuration(2000)
                .setDelay(1100)
                .build());

        decoView.addEvent(new DecoEvent.Builder(15).setIndex(seriesIndexTwo).setDelay(3900).build());
        decoView.addEvent(new DecoEvent.Builder(50).setIndex(seriesIndexTwo).setDelay(7000).build());

        decoView.addEvent(new DecoEvent.Builder(10).setIndex(seriesIndexOne).setDelay(3300).build());
        decoView.addEvent(new DecoEvent.Builder(50).setIndex(seriesIndexOne).setDuration(1500).setDelay(9000).build());
        decoView.addEvent(new DecoEvent.Builder(0).setIndex(seriesIndexOne).setDuration(500).setDelay(10500)
                .setListener(new DecoEvent.ExecuteEventListener() {
                    @Override
                    public void onEventStart(DecoEvent event) {
                        mUpdateListeners = false;
                    }

                    @Override
                    public void onEventEnd(DecoEvent event) {

                    }
                })
                .setInterpolator(new AccelerateInterpolator()).build());
    }

}
