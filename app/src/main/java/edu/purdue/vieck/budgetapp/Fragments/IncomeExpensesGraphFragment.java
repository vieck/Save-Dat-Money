package edu.purdue.vieck.budgetapp.Fragments;


import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.DecoDrawEffect;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import edu.purdue.vieck.budgetapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class IncomeExpensesGraphFragment extends Fragment {

    final private float[] mTrackBackWidth = {30f,60f};
    final private float[] mTrackWidth = {30f,80f};
    final private int[] mTotalAngle = {360,320};
    final private int[] mRotateAngle = {0,180};
    final private boolean[] mClockwise = {true, true, true, false, true};
    private int mBackIndex;
    private int mSeriesIndexOne;
    private int mSeriesIndexTwo;
    private int mStyleIndex;

    private DecoView quickchart;


    protected final String TAG = getClass().getSimpleName();

    final protected int COLOR_BLUE = Color.parseColor("#AA1D76D2");
    final protected int COLOR_PINK = Color.parseColor("#AAFF4081");
    final protected int COLOR_YELLOW = Color.parseColor("#AAFFC107");
    final protected int COLOR_GREEN = Color.parseColor("#AA07CC07");
    final protected int COLOR_EDGE = Color.parseColor("#22000000");
    final protected int COLOR_BACK = Color.parseColor("#22888888");
    final protected int COLOR_NEUTRAL = Color.parseColor("#FF999999");
    protected boolean mUpdateListeners = true;
    private boolean mInitialized = false;

    /**
     * Add a listener to update the progress on a TextView
     *
     * @param seriesItem ArcItem to listen for update changes
     * @param view       View to update
     * @param format     String.format to display the progress
     *                   <p/>
     *                   If the string format includes a percentage character we assume that we should set
     *                   a percentage into the string, otherwise the current position is added into the string
     *                   For example if the arc has a min of 0 and a max of 50 and the current position is 20
     *                   Format -> "%.0f%% Complete" -> "40% Complete"
     *                   Format -> "%.1f Km" -> "20.0 Km"
     *                   Format -> "%.0f/40 Levels Complete" -> "20/40 Levels Complete"
     */
    protected void addProgressListener(final SeriesItem seriesItem, final TextView view, final String format) {
        if (format.length() <= 0) {
            throw new IllegalArgumentException("String formatter can not be empty");
        }

        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                if (mUpdateListeners) {
                    if (format.contains("%%")) {
                        // We found a percentage so we insert a percentage
                        float percentFilled = ((currentPosition - seriesItem.getMinValue()) / (seriesItem.getMaxValue() - seriesItem.getMinValue()));
                        view.setText(String.format(format, percentFilled * 100f));
                    } else {
                        view.setText(String.format(format, currentPosition));
                    }
                }
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });
    }

    protected void addProgressRemainingListener(final SeriesItem seriesItem, final TextView view, final String format, final float maxValue) {
        if (format.length() <= 0) {
            throw new IllegalArgumentException("String formatter can not be empty");
        }

        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {

            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                if (mUpdateListeners) {
                    if (format.contains("%%")) {
                        // We found a percentage so we insert a percentage
                        view.setText(String.format(format, (1.0f - (currentPosition / seriesItem.getMaxValue())) * 100f));
                    } else {
                        view.setText(String.format(format, maxValue - currentPosition));
                    }
                }
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });
    }

    private boolean createAnimation() {
        if (mInitialized) {
            if (super.getUserVisibleHint()) {
                setupEvents();
            }
            return true;
        }
        return false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mInitialized = true;
        createAnimation();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        quickchart = (DecoView) view.findViewById(R.id.quick_chart);
        final float seriesMax = 50f;
        quickchart.deleteAll();
        quickchart.configureAngles(mTotalAngle[mStyleIndex], mRotateAngle[mStyleIndex]);

        SeriesItem arcBackTrack = new SeriesItem.Builder(Color.argb(255, 228, 228, 228))
                .setRange(0, seriesMax, seriesMax)
                .setInitialVisibility(false)
                .setLineWidth(getDimension(mTrackBackWidth[mStyleIndex]))
                .build();

        mBackIndex = quickchart.addSeries(arcBackTrack);

        float inset = 0;
        if (mTrackBackWidth[mStyleIndex] != mTrackWidth[mStyleIndex]) {
            inset = getDimension((mTrackBackWidth[mStyleIndex] - mTrackWidth[mStyleIndex]) / 2);
        }
        SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 255, 165, 0))
                .setRange(0, seriesMax, 0)
                .setInitialVisibility(false)
                .setLineWidth(getDimension(mTrackWidth[mStyleIndex]))
                .setInset(new PointF(-inset, -inset))
                .setSpinClockwise(mClockwise[mStyleIndex])
                .setCapRounded(true)
                .build();

        mSeriesIndexOne = quickchart.addSeries(seriesItem1);

        SeriesItem seriesItem2 = new SeriesItem.Builder(Color.argb(255, 255, 51, 51))
                .setRange(0, seriesMax, 0)
                .setInitialVisibility(false)
                .setCapRounded(true)
                .setLineWidth(getDimension(mTrackWidth[mStyleIndex]))
                .setInset(new PointF(inset, inset))
                .setCapRounded(true)
                .build();

        mSeriesIndexTwo = quickchart.addSeries(seriesItem2);

        /*final TextView textPercent = (TextView) view.findViewById(R.id.textPercentage);
        if (textPercent != null) {
            textPercent.setText("");
            addProgressListener(seriesItem1, textPercent, "%.0f%%");
        }

        final TextView textToGo = (TextView) view.findViewById(R.id.textRemaining);
        textToGo.setText("");
        addProgressRemainingListener(seriesItem1, textToGo, "%.0f min to goal", seriesMax);

        View layout = getView().findViewById(R.id.layoutActivities);
        layout.setVisibility(View.INVISIBLE);

        final TextView textActivity1 = (TextView) getView().findViewById(R.id.textActivity1);
        addProgressListener(seriesItem1, textActivity1, "%.0f Km");
        textActivity1.setText("");

        final TextView textActivity2 = (TextView) getView().findViewById(R.id.textActivity2);
        textActivity2.setText("");
        addProgressListener(seriesItem2, textActivity2, "%.0f Km");*/
        return view;
    }

    protected void setupEvents() {
        final DecoView decoView = quickchart;
        final View view = getView();
        if (decoView == null || decoView.isEmpty() || view == null) {
            return;
        }

        mUpdateListeners = true;
        final TextView textPercent = (TextView) view.findViewById(R.id.textPercentage);
        final TextView textToGo = (TextView) view.findViewById(R.id.textRemaining);
        final View[] linkedViews = {textPercent, textToGo};
        final int fadeDuration = 2000;

            decoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT_FILL)
                    .setIndex(mBackIndex)
                    .setDuration(3000)
                    .build());

            decoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                    .setIndex(mSeriesIndexOne)
                    .setFadeDuration(fadeDuration)
                    .setDuration(2000)
                    .setDelay(1000)
                    .build());

        decoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(mSeriesIndexTwo)
                .setLinkedViews(linkedViews)
                .setDuration(2000)
                .setDelay(1100)
                .build());

        decoView.addEvent(new DecoEvent.Builder(10).setIndex(mSeriesIndexTwo).setDelay(3900).build());
        decoView.addEvent(new DecoEvent.Builder(22).setIndex(mSeriesIndexTwo).setDelay(7000).build());

        decoView.addEvent(new DecoEvent.Builder(25).setIndex(mSeriesIndexOne).setDelay(3300).build());
        decoView.addEvent(new DecoEvent.Builder(50).setIndex(mSeriesIndexOne).setDuration(1500).setDelay(9000).build());
        decoView.addEvent(new DecoEvent.Builder(0).setIndex(mSeriesIndexOne).setDuration(500).setDelay(10500)
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

        decoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_EXPLODE)
                .setLinkedViews(linkedViews)
                .setIndex(mSeriesIndexOne)
                .setDelay(11000)
                .setDuration(3000)
                .setDisplayText("GOAL!")
                .setListener(new DecoEvent.ExecuteEventListener() {
                    @Override
                    public void onEventStart(DecoEvent event) {

                    }

                    @Override
                    public void onEventEnd(DecoEvent event) {
                        mStyleIndex++;
                        if (mStyleIndex >= mTrackBackWidth.length) {
                            mStyleIndex = 0;
                            return;
                        }

                        setupEvents();
                    }
                })
                .build());
    }

    protected float getDimension(float base) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, base, getResources().getDisplayMetrics());
    }

}
