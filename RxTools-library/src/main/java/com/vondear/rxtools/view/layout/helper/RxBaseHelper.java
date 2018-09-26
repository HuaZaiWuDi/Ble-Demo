package com.vondear.rxtools.view.layout.helper;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewConfiguration;

import com.vondear.rxtools.R;
import com.vondear.rxtools.utils.RxLogUtils;


/**
 * BaseHelper
 *
 * @author ZhongDaFeng
 */
public class RxBaseHelper<T extends View> {

    //default value
    public static final int GRADIENT_TYPE_LINEAR = 0, GRADIENT_TYPE_RADIAL = 1, GRADIENT_TYPE_SWEEP = 2;
    public static final int GRADIENT_O_TOP_BOTTOM = 0, GRADIENT_O_TR_BL = 1, GRADIENT_O_RIGHT_LEFT = 2, GRADIENT_O_BR_TL = 3,
            GRADIENT_O_BOTTOM_TOP = 4, GRADIENT_O_BL_TR = 5, GRADIENT_O_LEFT_RIGHT = 6, GRADIENT_O_TL_BR = 7;

    //corner
    private float mCornerRadius;
    private float mCornerRadiusTopLeft;
    private float mCornerRadiusTopRight;
    private float mCornerRadiusBottomLeft;
    private float mCornerRadiusBottomRight;

    //BorderWidth
    private float mBorderDashWidth = 0;
    private float mBorderDashGap = 0;
    private int mBorderWidthNormal = 0;
    private int mBorderWidthPressed = 0;
    private int mBorderWidthUnable = 0;

    //BorderColor
    private int mBorderColorNormal;
    private int mBorderColorPressed;
    private int mBorderColorUnable;

    //Background
    private int mBackgroundColorNormal;
    private int mBackgroundColorPressed;
    private int mBackgroundColorUnable;
    private GradientDrawable mBackgroundNormal;
    private GradientDrawable mBackgroundPressed;
    private GradientDrawable mBackgroundUnable;

    //ripple
    private int mRippleColor = 0;

    //渐变
    int mGradientAngle = 0;
    float mGradientCenterX = 0;
    float mGradientCenterY = 0;
    int mGradientCenterColor = 0;
    int mGradientEndColor = 0;
    int mGradientSatrtColor = 0;
    int mGradientRadius = 0;
    int mGradientType = GRADIENT_TYPE_LINEAR;
    private GradientDrawable mGradient;

    protected int[][] states = new int[4][];
    private StateListDrawable mStateBackground;
    private float mBorderRadii[] = new float[8];


    /**
     * Cache the touch slop from the context that created the view.
     */
    private int mTouchSlop;
    protected Context mContext;

    /**
     * 是否设置对应的属性
     */
    private boolean mHasPressedBgColor = false;
    private boolean mHasUnableBgColor = false;
    private boolean mHasPressedBorderColor = false;
    private boolean mHasUnableBorderColor = false;
    private boolean mHasPressedBorderWidth = false;
    private boolean mHasUnableBorderWidth = false;

    // view
    protected T mView;

    public RxBaseHelper(Context context, T view, AttributeSet attrs) {
        mView = view;
        mContext = context;
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        //初始化控件属性
        initAttributeSet(context, attrs);
    }

    /**
     * 初始化控件属性
     *
     * @param context
     * @param attrs
     */
    private void initAttributeSet(Context context, AttributeSet attrs) {
        if (context == null || attrs == null) {
            setup();
            return;
        }
        TypedArray a = mView.getContext().obtainStyledAttributes(attrs, R.styleable.RBaseView);
        //corner
        mCornerRadius = a.getDimensionPixelSize(R.styleable.RBaseView_corner_radius, -1);
        mCornerRadiusTopLeft = a.getDimensionPixelSize(R.styleable.RBaseView_corner_radius_top_left, 0);
        mCornerRadiusTopRight = a.getDimensionPixelSize(R.styleable.RBaseView_corner_radius_top_right, 0);
        mCornerRadiusBottomLeft = a.getDimensionPixelSize(R.styleable.RBaseView_corner_radius_bottom_left, 0);
        mCornerRadiusBottomRight = a.getDimensionPixelSize(R.styleable.RBaseView_corner_radius_bottom_right, 0);
        //border
        mBorderDashWidth = a.getDimensionPixelSize(R.styleable.RBaseView_border_dash_width, 0);
        mBorderDashGap = a.getDimensionPixelSize(R.styleable.RBaseView_border_dash_gap, 0);
        mBorderWidthNormal = a.getDimensionPixelSize(R.styleable.RBaseView_border_width_normal, 0);
        mBorderWidthPressed = a.getDimensionPixelSize(R.styleable.RBaseView_border_width_pressed, 0);
        mBorderWidthUnable = a.getDimensionPixelSize(R.styleable.RBaseView_border_width_unable, 0);
        mBorderColorNormal = a.getColor(R.styleable.RBaseView_border_color_normal, Color.TRANSPARENT);
        mBorderColorPressed = a.getColor(R.styleable.RBaseView_border_color_pressed, Color.TRANSPARENT);
        mBorderColorUnable = a.getColor(R.styleable.RBaseView_border_color_unable, Color.TRANSPARENT);
        //background
        mBackgroundColorNormal = a.getColor(R.styleable.RBaseView_background_normal, 0);
        mBackgroundColorPressed = a.getColor(R.styleable.RBaseView_background_pressed, 0);
        mBackgroundColorUnable = a.getColor(R.styleable.RBaseView_background_unable, 0);
        //波浪
        mRippleColor = a.getColor(R.styleable.RBaseView_ripple_color, 0);

        //渐变
        mGradientAngle = a.getInteger(R.styleable.RBaseView_gradient_angle, GRADIENT_O_LEFT_RIGHT);
        mGradientCenterX = a.getFloat(R.styleable.RBaseView_gradient_centerX, 0);
        mGradientCenterY = a.getFloat(R.styleable.RBaseView_gradient_centerY, 0);
        mGradientCenterColor = a.getColor(R.styleable.RBaseView_gradient_centerColor, 0);
        mGradientEndColor = a.getColor(R.styleable.RBaseView_gradient_endColor, 0);
        mGradientSatrtColor = a.getColor(R.styleable.RBaseView_gradient_startColor, 0);
        mGradientRadius = a.getDimensionPixelSize(R.styleable.RBaseView_gradient_gradientRadius, 0);
        mGradientType = a.getInt(R.styleable.RBaseView_gradient_type, GRADIENT_TYPE_LINEAR);

        a.recycle();

        mHasPressedBgColor = mBackgroundColorPressed < 0;
        mHasUnableBgColor = mBackgroundColorUnable < 0;
        mHasPressedBorderColor = mBorderColorPressed < 0;
        mHasUnableBorderColor = mBorderColorUnable < 0;
        mHasPressedBorderWidth = mBorderWidthPressed < 0;
        mHasUnableBorderWidth = mBorderWidthUnable < 0;

        //setup
        setup();

    }

    /**
     * 设置
     */
    private void setup() {

        mBackgroundNormal = new GradientDrawable();
        mBackgroundPressed = new GradientDrawable();
        mBackgroundUnable = new GradientDrawable();

        Drawable drawable = mView.getBackground();
        if (drawable != null && drawable instanceof StateListDrawable) {
            mStateBackground = (StateListDrawable) drawable;
        } else {
            mStateBackground = new StateListDrawable();
        }

        /**
         * 设置背景默认值
         */
        if (!mHasPressedBgColor) {
            mBackgroundColorPressed = mBackgroundColorNormal;
        }
        if (!mHasUnableBgColor) {
            mBackgroundColorUnable = mBackgroundColorNormal;
        }

        mBackgroundNormal.setColor(mBackgroundColorNormal);
        mBackgroundPressed.setColor(mBackgroundColorPressed);
        mBackgroundUnable.setColor(mBackgroundColorUnable);

        //pressed, focused, normal, unable
        states[0] = new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed};
        states[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        states[3] = new int[]{-android.R.attr.state_enabled};
        states[2] = new int[]{android.R.attr.state_enabled};
        mStateBackground.addState(states[0], mBackgroundPressed);
        mStateBackground.addState(states[1], mBackgroundPressed);
        mStateBackground.addState(states[3], mBackgroundUnable);
        mStateBackground.addState(states[2], mBackgroundNormal);

        /**
         * 设置边框默认值
         */
        if (!mHasPressedBorderWidth) {
            mBorderWidthPressed = mBorderWidthNormal;
        }
        if (!mHasUnableBorderWidth) {
            mBorderWidthUnable = mBorderWidthNormal;
        }
        if (!mHasPressedBorderColor) {
            mBorderColorPressed = mBorderColorNormal;
        }
        if (!mHasUnableBorderColor) {
            mBorderColorUnable = mBorderColorNormal;
        }

        if (mBackgroundColorNormal == 0 && mBackgroundColorUnable == 0 && mBackgroundColorPressed == 0) {//未设置自定义背景色
           /* if (mBorderColorPressed == 0 && mBorderColorUnable == 0 && mBorderColorNormal == 0) {//未设置自定义边框
                //获取原生背景并设置
                setBackgroundState(true);
            } else {
                setBackgroundState(false);
            }*/
            //获取原生背景并设置
            setBackgroundState(true);
        } else {
            //设置背景资源
            setBackgroundState(false);
        }

        //设置边框
        setBorder();

        //设置圆角
        setRadius();

        //设置ripple
        setRipple();

        //设置渐变
        setGradient();
    }


    private void setGradient() {
        if (mGradientCenterX != 0 && mGradientCenterY != 0) {
            mBackgroundNormal.setGradientCenter(mGradientCenterX, mGradientCenterY);
        }
        if (mGradientSatrtColor != 0 && mGradientEndColor != 0) {
            int[] colors;
            if (mGradientCenterColor != 0) {
                colors = new int[3];
                colors[0] = mGradientSatrtColor;
                colors[1] = mGradientCenterColor;
                colors[2] = mGradientEndColor;
            } else {
                colors = new int[2];
                colors[0] = mGradientSatrtColor;
                colors[1] = mGradientEndColor;
            }
            mBackgroundNormal.setColors(colors);
        }
        gradientOrientation();
        mBackgroundNormal.setGradientRadius(mGradientRadius);
        if (mGradientType != 0) {
            mBackgroundNormal.setGradientType(mGradientType);
        }
    }

    private void gradientOrientation() {
        GradientDrawable.Orientation mOrientation = GradientDrawable.Orientation.LEFT_RIGHT;
        switch (mGradientAngle) {
            case GRADIENT_O_TOP_BOTTOM:
                mOrientation = GradientDrawable.Orientation.TOP_BOTTOM;
                break;
            case GRADIENT_O_TR_BL:
                mOrientation = GradientDrawable.Orientation.TR_BL;
                break;
            case GRADIENT_O_RIGHT_LEFT:
                mOrientation = GradientDrawable.Orientation.RIGHT_LEFT;
                break;
            case GRADIENT_O_BR_TL:
                mOrientation = GradientDrawable.Orientation.BR_TL;
                break;
            case GRADIENT_O_BOTTOM_TOP:
                mOrientation = GradientDrawable.Orientation.BOTTOM_TOP;
                break;
            case GRADIENT_O_BL_TR:
                mOrientation = GradientDrawable.Orientation.BL_TR;
                break;
            case GRADIENT_O_LEFT_RIGHT:
                mOrientation = GradientDrawable.Orientation.LEFT_RIGHT;
                break;
            case GRADIENT_O_TL_BR:
                mOrientation = GradientDrawable.Orientation.TL_BR;
                break;
        }
        mBackgroundNormal.setOrientation(mOrientation);
    }

    /************************
     * Ripple
     ************************/
    private void setRipple() {
        if (mRippleColor != 0) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                RxLogUtils.e("mRippleColor：" + mRippleColor);
                Drawable contentDrawable = (mStateBackground == null ? mBackgroundPressed : mStateBackground);
                RippleDrawable rippleDrawable = new RippleDrawable(ColorStateList.valueOf(mRippleColor), contentDrawable, contentDrawable);
                mView.setBackground(rippleDrawable);
            } else {
                setBackgroundColorPressed(mRippleColor);
            }
        }
    }


    /*********************
     * BackgroundColor
     ********************/

    public RxBaseHelper setStateBackgroundColor(int normal, int pressed, int unable) {
        mBackgroundColorNormal = normal;
        mBackgroundColorPressed = pressed;
        mBackgroundColorUnable = unable;
        mHasPressedBgColor = true;
        mHasUnableBgColor = true;
        mBackgroundNormal.setColor(mBackgroundColorNormal);
        mBackgroundPressed.setColor(mBackgroundColorPressed);
        mBackgroundUnable.setColor(mBackgroundColorUnable);
        setBackgroundState(false);
        return this;
    }

    public int getBackgroundColorNormal() {
        return mBackgroundColorNormal;
    }

    public RxBaseHelper setBackgroundColorNormal(int colorNormal) {
        this.mBackgroundColorNormal = colorNormal;
        /**
         * 设置背景默认值
         */
        if (!mHasPressedBgColor) {
            mBackgroundColorPressed = mBackgroundColorNormal;
            mBackgroundPressed.setColor(mBackgroundColorPressed);
        }
        if (!mHasUnableBgColor) {
            mBackgroundColorUnable = mBackgroundColorNormal;
            mBackgroundUnable.setColor(mBackgroundColorUnable);
        }
        mBackgroundNormal.setColor(mBackgroundColorNormal);
        setBackgroundState(false);
        return this;
    }

    public int getBackgroundColorPressed() {
        return mBackgroundColorPressed;
    }

    public RxBaseHelper setBackgroundColorPressed(int colorPressed) {
        this.mBackgroundColorPressed = colorPressed;
        this.mHasPressedBgColor = true;
        mBackgroundPressed.setColor(mBackgroundColorPressed);
        setBackgroundState(false);
        return this;
    }

    public int getBackgroundColorUnable() {
        return mBackgroundColorUnable;
    }

    public RxBaseHelper setBackgroundColorUnable(int colorUnable) {
        this.mBackgroundColorUnable = colorUnable;
        this.mHasUnableBgColor = true;
        mBackgroundUnable.setColor(mBackgroundColorUnable);
        setBackgroundState(false);
        return this;
    }

    private void setBackgroundState(boolean unset) {

        //未设置自定义属性,并且设置背景颜色时
        Drawable drawable = mView.getBackground();
        if (unset && drawable instanceof ColorDrawable) {
            int color = ((ColorDrawable) drawable).getColor();
            setStateBackgroundColor(color, color, color);//获取背景颜色值设置 StateListDrawable
        }

        //设置背景资源
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            mView.setBackgroundDrawable(unset ? drawable : mStateBackground);
        } else {
            mView.setBackground(unset ? drawable : mStateBackground);
        }
    }


    /*********************
     * border
     *********************/

    public RxBaseHelper setBorderWidthNormal(int width) {
        this.mBorderWidthNormal = width;
        if (!mHasPressedBorderWidth) {
            mBorderWidthPressed = mBorderWidthNormal;
            setBorderPressed();
        }
        if (!mHasUnableBorderWidth) {
            mBorderWidthUnable = mBorderWidthNormal;
            setBorderUnable();
        }
        setBorderNormal();
        return this;
    }

    public int getBorderWidthNormal() {
        return mBorderWidthNormal;
    }

    public RxBaseHelper setBorderColorNormal(int color) {
        this.mBorderColorNormal = color;
        if (!mHasPressedBorderColor) {
            mBorderColorPressed = mBorderColorNormal;
            setBorderPressed();
        }
        if (!mHasUnableBorderColor) {
            mBorderColorUnable = mBorderColorNormal;
            setBorderUnable();
        }
        setBorderNormal();
        return this;
    }

    public int getBorderColorNormal() {
        return mBorderColorNormal;
    }

    public RxBaseHelper setBorderWidthPressed(int width) {
        this.mBorderWidthPressed = width;
        this.mHasPressedBorderWidth = true;
        setBorderPressed();
        return this;
    }

    public int getBorderWidthPressed() {
        return mBorderWidthPressed;
    }

    public RxBaseHelper setBorderColorPressed(int color) {
        this.mBorderColorPressed = color;
        this.mHasPressedBorderColor = true;
        setBorderPressed();
        return this;
    }

    public int getBorderColorPressed() {
        return mBorderColorPressed;
    }

    public RxBaseHelper setBorderWidthUnable(int width) {
        this.mBorderWidthUnable = width;
        this.mHasUnableBorderWidth = true;
        setBorderUnable();
        return this;
    }

    public int getBorderWidthUnable() {
        return mBorderWidthUnable;
    }

    public RxBaseHelper setBorderColorUnable(int color) {
        this.mBorderColorUnable = color;
        this.mHasUnableBorderColor = true;
        setBorderUnable();
        return this;
    }

    public int getBorderColorUnable() {
        return mBorderColorUnable;
    }

    public void setBorderWidth(int normal, int pressed, int unable) {
        this.mBorderWidthNormal = normal;
        this.mBorderWidthPressed = pressed;
        this.mBorderWidthUnable = unable;
        this.mHasPressedBorderWidth = true;
        this.mHasUnableBorderWidth = true;
        setBorder();
    }

    public void setBorderColor(int normal, int pressed, int unable) {
        this.mBorderColorNormal = normal;
        this.mBorderColorPressed = pressed;
        this.mBorderColorUnable = unable;
        this.mHasPressedBorderColor = true;
        this.mHasUnableBorderColor = true;
        setBorder();
    }

    public void setBorderDashWidth(float dashWidth) {
        this.mBorderDashWidth = dashWidth;
        setBorder();
    }

    public float getBorderDashWidth() {
        return mBorderDashWidth;
    }

    public void setBorderDashGap(float dashGap) {
        this.mBorderDashGap = dashGap;
        setBorder();
    }

    public float getBorderDashGap() {
        return mBorderDashGap;
    }

    public void setBorderDash(float dashWidth, float dashGap) {
        this.mBorderDashWidth = dashWidth;
        this.mBorderDashGap = dashGap;
        setBorder();
    }

    private void setBorder() {
        mBackgroundNormal.setStroke(mBorderWidthNormal, mBorderColorNormal, mBorderDashWidth, mBorderDashGap);
        mBackgroundPressed.setStroke(mBorderWidthPressed, mBorderColorPressed, mBorderDashWidth, mBorderDashGap);
        mBackgroundUnable.setStroke(mBorderWidthUnable, mBorderColorUnable, mBorderDashWidth, mBorderDashGap);
        setBackgroundState(false);
    }

    private void setBorderNormal() {
        mBackgroundNormal.setStroke(mBorderWidthNormal, mBorderColorNormal, mBorderDashWidth, mBorderDashGap);
        setBackgroundState(false);
    }

    private void setBorderPressed() {
        mBackgroundPressed.setStroke(mBorderWidthPressed, mBorderColorPressed, mBorderDashWidth, mBorderDashGap);
        setBackgroundState(false);
    }

    private void setBorderUnable() {
        mBackgroundUnable.setStroke(mBorderWidthUnable, mBorderColorUnable, mBorderDashWidth, mBorderDashGap);
        setBackgroundState(false);
    }


    /*********************
     * radius
     ********************/

    public void setCornerRadius(float radius) {
        this.mCornerRadius = radius;
        setRadius();
    }

    public float getCornerRadius() {
        return mCornerRadius;
    }

    public RxBaseHelper setCornerRadiusTopLeft(float topLeft) {
        this.mCornerRadius = -1;
        this.mCornerRadiusTopLeft = topLeft;
        setRadius();
        return this;
    }

    public float getCornerRadiusTopLeft() {
        return mCornerRadiusTopLeft;
    }

    public RxBaseHelper setCornerRadiusTopRight(float topRight) {
        this.mCornerRadius = -1;
        this.mCornerRadiusTopRight = topRight;
        setRadius();
        return this;
    }

    public float getCornerRadiusTopRight() {
        return mCornerRadiusTopRight;
    }

    public RxBaseHelper setCornerRadiusBottomRight(float bottomRight) {
        this.mCornerRadius = -1;
        this.mCornerRadiusBottomRight = bottomRight;
        setRadius();
        return this;
    }

    public float getCornerRadiusBottomRight() {
        return mCornerRadiusBottomRight;
    }

    public RxBaseHelper setCornerRadiusBottomLeft(float bottomLeft) {
        this.mCornerRadius = -1;
        this.mCornerRadiusBottomLeft = bottomLeft;
        setRadius();
        return this;
    }

    public float getCornerRadiusBottomLeft() {
        return mCornerRadiusBottomLeft;
    }

    public void setCornerRadius(float topLeft, float topRight, float bottomRight, float bottomLeft) {
        this.mCornerRadius = -1;
        this.mCornerRadiusTopLeft = topLeft;
        this.mCornerRadiusTopRight = topRight;
        this.mCornerRadiusBottomRight = bottomRight;
        this.mCornerRadiusBottomLeft = bottomLeft;
        setRadius();
    }

    private void setRadiusRadii() {
        mBackgroundNormal.setCornerRadii(mBorderRadii);
        mBackgroundPressed.setCornerRadii(mBorderRadii);
        mBackgroundUnable.setCornerRadii(mBorderRadii);
        setBackgroundState(false);
    }

    private void setRadius() {
        if (mCornerRadius >= 0) {
            mBorderRadii[0] = mCornerRadius;
            mBorderRadii[1] = mCornerRadius;
            mBorderRadii[2] = mCornerRadius;
            mBorderRadii[3] = mCornerRadius;
            mBorderRadii[4] = mCornerRadius;
            mBorderRadii[5] = mCornerRadius;
            mBorderRadii[6] = mCornerRadius;
            mBorderRadii[7] = mCornerRadius;
            setRadiusRadii();
            return;
        }

        if (mCornerRadius < 0) {
            mBorderRadii[0] = mCornerRadiusTopLeft;
            mBorderRadii[1] = mCornerRadiusTopLeft;
            mBorderRadii[2] = mCornerRadiusTopRight;
            mBorderRadii[3] = mCornerRadiusTopRight;
            mBorderRadii[4] = mCornerRadiusBottomRight;
            mBorderRadii[5] = mCornerRadiusBottomRight;
            mBorderRadii[6] = mCornerRadiusBottomLeft;
            mBorderRadii[7] = mCornerRadiusBottomLeft;
            setRadiusRadii();
            return;
        }
    }

    /**
     * 是否移出view
     *
     * @param x
     * @param y
     * @return
     */
    protected boolean isOutsideView(int x, int y) {
        boolean flag = false;
        // Be lenient about moving outside of buttons
        if ((x < 0 - mTouchSlop) || (x >= mView.getWidth() + mTouchSlop) ||
                (y < 0 - mTouchSlop) || (y >= mView.getHeight() + mTouchSlop)) {
            // Outside button
            flag = true;
        }
        return flag;
    }

    private float dp2px(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mContext.getResources().getDisplayMetrics());
    }

}
