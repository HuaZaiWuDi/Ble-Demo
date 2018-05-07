package com.vondear.rxtools.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.vondear.rxtools.model.timer.MyTimer;
import com.vondear.rxtools.model.timer.MyTimerListener;

import java.util.Timer;


/**
 * 项目名称：Ali_Sophix
 * 类描述：
 * 创建人：oden
 * 创建时间：2017/12/28
 */
public class CountDownTextView extends TextView {


    public CountDownTextView(Context context) {
        this(context, null);
    }

    public CountDownTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        text = "";
        index = 0;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        mBound = new Rect();
    }

    Rect mBound;
    Paint paint;
    String content = "测试数据";
    int maxLen = 0;
    String text = "";
    int index = 0;
    long TotalTime = 1000;
    long animTime = 100;

    public void setText(String content) {
        this.content = content;
        char[] chars = content.toCharArray();
        maxLen = chars.length;
        Logger.d("maxLen:" + maxLen);
        if (maxLen != 0)
            animTime = TotalTime / maxLen;
        postInvalidate();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取宽-测量规则的模式和大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        // 获取高-测量规则的模式和大小
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // 设置wrap_content的默认宽 / 高值
        // 默认宽/高的设定并无固定依据,根据需要灵活设置
        // 类似TextView,ImageView等针对wrap_content均在onMeasure()对设置默认宽 / 高值有特殊处理,具体读者可以自行查看
        int mWidth = (int) paint.measureText(content);
        int mHeight = 200;


        // 当布局参数设置为wrap_content时，设置默认值
        if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT && getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(mWidth, mHeight);
            // 宽 / 高任意一个布局参数为= wrap_content时，都设置默认值
        } else if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(mWidth, heightSize);
        } else if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(widthSize, mHeight);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {


        paint.setTextSize(getTextSize());

        if (index < maxLen)
            text = text + content.charAt(index);
        index++;
        Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
        int baseLine = fontMetricsInt.descent - fontMetricsInt.ascent;//基线

        int y = baseLine;
        String[] strings = autoSplit(text, paint, getWidth());

        for (String s : strings) {
            canvas.drawText(s, getPaddingLeft(), y, paint);
            y += baseLine + fontMetricsInt.leading;//行间距
        }

        super.onDraw(canvas);

    }


    /**
     * 自动分割文本
     *
     * @param content 需要分割的文本
     * @param p       画笔，用来根据字体测量文本的宽度
     * @param width   最大的可显示像素（一般为控件的宽度）
     * @return 一个字符串数组，保存每行的文本
     */
    private String[] autoSplit(String content, Paint p, float width) {
        int length = content.length();
        float textWidth = p.measureText(content);
        if (textWidth <= width) {
            return new String[]{content};
        }

        int start = 0, end = 1, i = 0;
        int lines = (int) Math.ceil(textWidth / width); //计算行数
        String[] lineTexts = new String[lines];
        while (start < length) {
            if (p.measureText(content, start, end) > width) { //文本宽度超出控件宽度时
                lineTexts[i++] = (String) content.subSequence(start, end);
                start = end;
            }
            if (end == length) { //不足一行的文本
                lineTexts[i] = (String) content.subSequence(start, end);
                break;
            }
            end += 1;
        }
        return lineTexts;
    }


    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus)
            startAnimation();
    }

    MyTimer timer;


    public void startAnimation() {
        index = 0;
        text = "";
        if (timer != null)
            timer.stopTimer();
        timer = new MyTimer(animTime, new MyTimerListener() {
            @Override
            public void enterTimer() {
                if (index < maxLen) {
                    postInvalidate();
                } else {
                    timer.stopTimer();
                }
            }
        });
        timer.startTimer();
    }


}
