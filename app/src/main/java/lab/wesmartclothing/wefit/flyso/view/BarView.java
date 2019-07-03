package lab.wesmartclothing.wefit.flyso.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Dacer on 11/11/13.
 */
public class BarView extends View {
    private final int BAR_SIDE_MARGIN;
    private final int TEXT_TOP_MARGIN;
    private final int TEXT_COLOR = Color.parseColor("#9B9A9B");
    private final int BACKGROUND_COLOR = Color.parseColor("#FFD9D8E1");
    private final int FOREGROUND_COLOR = Color.parseColor("#FFD9D8E1");
    private ArrayList<Float> percentList;
    private ArrayList<Float> targetPercentList;
    private Paint textPaint;
    private Paint bgPaint;
    private Paint fgPaint;
    private Paint selectPaint;


    private RectF rect;
    private int barWidth;
    private int bottomTextDescent;
    private boolean autoSetWidth = true;
    private int topMargin;
    private int bottomTextHeight;
    private int longestWidth = 0;
    private int itemWidth = 0;
    private int indexToSelect, selectIndex;
    private ArrayList<String> bottomTextList = new ArrayList<String>();
    private Runnable animator = new Runnable() {
        @Override
        public void run() {
            boolean needNewFrame = false;
            for (int i = 0; i < targetPercentList.size(); i++) {
                if (percentList.get(i) < targetPercentList.get(i)) {
                    percentList.set(i, percentList.get(i) + 0.02f);
                    needNewFrame = true;
                } else if (percentList.get(i) > targetPercentList.get(i)) {
                    percentList.set(i, percentList.get(i) - 0.02f);
                    needNewFrame = true;
                }
                if (Math.abs(targetPercentList.get(i) - percentList.get(i)) < 0.02f) {
                    percentList.set(i, targetPercentList.get(i));
                }
            }
            if (needNewFrame) {
                postDelayed(this, 20);
            }
            invalidate();
        }
    };

    public BarView(Context context) {
        this(context, null);
    }

    public BarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setColor(BACKGROUND_COLOR);

        selectPaint = new Paint();
        selectPaint.setAntiAlias(true);
        selectPaint.setColor(Color.parseColor("#FFFF7200"));

        fgPaint = new Paint(bgPaint);
        fgPaint.setColor(FOREGROUND_COLOR);

        rect = new RectF();
        topMargin = dp2px(5);
        barWidth = dp2px(10);
        BAR_SIDE_MARGIN = dp2px(40);
        TEXT_TOP_MARGIN = dp2px(5);
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(TEXT_COLOR);
        textPaint.setTextSize(dp2px(10));
        textPaint.setTextAlign(Paint.Align.CENTER);
        percentList = new ArrayList<Float>();
    }

    public Paint getTextPaint() {
        return textPaint;
    }

    public Paint getFgPaint() {
        return fgPaint;
    }

    public Paint getSelectPaint() {
        return selectPaint;
    }

    public void setBarWidth(int barWidth) {
        this.barWidth = barWidth;
    }


    /**
     * dataList will be reset when called is method.
     *
     * @param bottomStringList The String ArrayList in the bottom.
     */
    public void setBottomTextList(ArrayList<String> bottomStringList) {
        //        this.dataList = null;
        this.bottomTextList = bottomStringList;
        Rect r = new Rect();
        bottomTextDescent = 0;
        for (String s : bottomTextList) {
            textPaint.getTextBounds(s, 0, s.length(), r);
            if (bottomTextHeight < r.height()) {
                bottomTextHeight = r.height();
            }
            longestWidth = Math.max(longestWidth, r.width());

            if (bottomTextDescent < (Math.abs(r.bottom))) {
                bottomTextDescent = Math.abs(r.bottom);
            }
        }
        itemWidth = (int) (longestWidth * 1.2f);
        setMinimumWidth(2);
        postInvalidate();
    }

    /**
     * @param list The ArrayList of Integer with the range of [0-max].
     */
    public void setDataList(ArrayList<Float> list, float max) {
        targetPercentList = new ArrayList<Float>();
        if (max == 0) max = 1;

        for (Float f : list) {
            targetPercentList.add(1 - f / (float) max);
        }

        // Make sure percentList.size() == targetPercentList.size()
        if (percentList.isEmpty() || percentList.size() < targetPercentList.size()) {
            int temp = targetPercentList.size() - percentList.size();
            for (int i = 0; i < temp; i++) {
                percentList.add(1f);
            }
        } else if (percentList.size() > targetPercentList.size()) {
            int temp = percentList.size() - targetPercentList.size();
            for (int i = 0; i < temp; i++) {
                percentList.remove(percentList.size() - 1);
            }
        }
        setMinimumWidth(2);
        removeCallbacks(animator);
        post(animator);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int i = 1;
        if (percentList != null && !percentList.isEmpty()) {
            for (Float f : percentList) {
                /**
                 * The correct total height is "getHeight()-topMargin-bottomTextHeight-TEXT_TOP_MARGIN",not "getHeight()-topMargin".
                 * fix by zhenghuiy@gmail.com on 11/11/13.
                 */
                rect.set(BAR_SIDE_MARGIN * i + barWidth * (i - 1), topMargin + (int) ((getHeight()
                                - topMargin
                                - bottomTextHeight
                                - TEXT_TOP_MARGIN) * percentList.get(i - 1)),
                        (BAR_SIDE_MARGIN + barWidth) * i,
                        getHeight() - bottomTextHeight - TEXT_TOP_MARGIN);

                if (selectIndex != -1 && selectIndex == i) {
                    canvas.drawRoundRect(rect, barWidth / 2, barWidth / 2, selectPaint);
                } else {
                    canvas.drawRoundRect(rect, barWidth / 2, barWidth / 2, fgPaint);
                }
                i++;
            }
        }

        if (bottomTextList != null && !bottomTextList.isEmpty()) {
            i = 1;
            for (String s : bottomTextList) {
                if (selectIndex != -1 && selectIndex == i) {
                    textPaint.setColor(Color.WHITE);

                    Log.d("bottomTextDescent", bottomTextDescent + "");
                    Log.d("getHeight()", getHeight() + "");
                    Log.d("bottomTextHeight", bottomTextHeight + "");

                    int startX = BAR_SIDE_MARGIN * i + barWidth * (i - 1) + barWidth / 2;
                    canvas.drawRoundRect(new RectF(startX - longestWidth * 0.6f,
                                    getHeight(),
                                    startX + longestWidth * 0.6f,
                                    getHeight() - bottomTextHeight - bottomTextDescent),
                            bottomTextHeight / 2, bottomTextHeight / 2, selectPaint);
                } else {
                    textPaint.setColor(TEXT_COLOR);
                }

                canvas.drawText(s, BAR_SIDE_MARGIN * i + barWidth * (i - 1) + barWidth / 2,
                        getHeight() - bottomTextDescent, textPaint);
                i++;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            indexToSelect = findPointAt((int) event.getX(), (int) event.getY());
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (indexToSelect > 0) {
                selectIndex = indexToSelect;
                indexToSelect = -1;
                postInvalidate();
            }
        }
        return true;
    }

    private int findPointAt(int x, int y) {
        if (percentList.isEmpty()) {
            return -1;
        }
        final Region r = new Region();
        int i = 1;

        if (percentList != null && !percentList.isEmpty()) {
            for (Float f : percentList) {

                r.set(BAR_SIDE_MARGIN * i - BAR_SIDE_MARGIN / 2,
                        0,
                        (BAR_SIDE_MARGIN + barWidth) * i + BAR_SIDE_MARGIN / 2,
                        getHeight());
                if (r.contains(x, y)) return i;
                i++;
            }
        }
        return -1;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mViewWidth = measureWidth(widthMeasureSpec);
        int mViewHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    private int measureWidth(int measureSpec) {
        int preferred = 0;
        if (bottomTextList != null) {
            preferred = bottomTextList.size() * (barWidth + BAR_SIDE_MARGIN) + BAR_SIDE_MARGIN;
        }
        return getMeasurement(measureSpec, preferred);
    }

    private int measureHeight(int measureSpec) {
        int preferred = 222;
        return getMeasurement(measureSpec, preferred);
    }

    private int getMeasurement(int measureSpec, int preferred) {
        int specSize = MeasureSpec.getSize(measureSpec);
        int measurement;
        switch (MeasureSpec.getMode(measureSpec)) {
            case MeasureSpec.EXACTLY:
                measurement = specSize;
                break;
            case MeasureSpec.AT_MOST:
                measurement = Math.min(preferred, specSize);
                break;
            default:
                measurement = preferred;
                break;
        }
        return measurement;
    }

    public static int dp2px(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }
}
