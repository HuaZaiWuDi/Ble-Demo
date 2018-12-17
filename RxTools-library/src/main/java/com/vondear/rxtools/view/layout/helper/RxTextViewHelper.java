package com.vondear.rxtools.view.layout.helper;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.v7.content.res.AppCompatResources;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.vondear.rxtools.R;


/**
 * TextView-Helper
 *
 * @author ZhongDaFeng
 */
public class RxTextViewHelper extends RxBaseHelper<TextView> {

    //default value
    public static final int ICON_DIR_LEFT = 1, ICON_DIR_TOP = 2, ICON_DIR_RIGHT = 3, ICON_DIR_BOTTOM = 4;
    public static final int ORIENTATION_HORIZONTAL = 0, ORIENTATION_VERTICAL = 1;

    //icon
    private int mIconHeight;
    private int mIconWidth;
    private int mIconDirection;

    // Text
    private int mTextColorNormal;
    private int mTextColorPressed;
    private int mTextColorUnable;
    private ColorStateList mTextColorStateList;

    //Icon
    private Drawable mIcon = null;
    private Drawable mIconNormal;
    private Drawable mIconPressed;
    private Drawable mIconUnable;

    //ripple
    private GradientDrawable mRipplePressed;


    //typeface
    private String mTypefacePath;

    //ripple
    private int mRippleColor = 0;

    //text渐变
    public int mTextGradientOrientation = 0;
    private int mTextGradientCenterColor = 0;
    private int mTextGradientEndColor = 0;
    private int mTextGradientSatrtColor = 0;
    public boolean mTextGradientOpen = false;
    public int mTextGradientTileMode = 0;
    public int[] mTextGradientColors;
    public Shader textShader;

    //手势检测
    private GestureDetector mGestureDetector;


    public RxTextViewHelper(Context context, TextView view, AttributeSet attrs) {
        super(context, view, attrs);
        mGestureDetector = new GestureDetector(context, new SimpleOnGesture());
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
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RxTextView);
        //icon
        //Vector兼容处理
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mIconNormal = a.getDrawable(R.styleable.RxTextView_icon_src_normal);
            mIconPressed = a.getDrawable(R.styleable.RxTextView_icon_src_pressed);
            mIconUnable = a.getDrawable(R.styleable.RxTextView_icon_src_unable);
        } else {
            int normalId = a.getResourceId(R.styleable.RxTextView_icon_src_normal, -1);
            int pressedId = a.getResourceId(R.styleable.RxTextView_icon_src_pressed, -1);
            int unableId = a.getResourceId(R.styleable.RxTextView_icon_src_unable, -1);

            if (normalId != -1)
                mIconNormal = AppCompatResources.getDrawable(context, normalId);
            if (pressedId != -1)
                mIconPressed = AppCompatResources.getDrawable(context, pressedId);
            if (unableId != -1)
                mIconUnable = AppCompatResources.getDrawable(context, unableId);
        }
        mIconWidth = a.getDimensionPixelSize(R.styleable.RxTextView_icon_width, 0);
        mIconHeight = a.getDimensionPixelSize(R.styleable.RxTextView_icon_height, 0);
        mIconDirection = a.getInt(R.styleable.RxTextView_icon_direction, ICON_DIR_LEFT);
        //text
        mTextColorNormal = a.getColor(R.styleable.RxTextView_text_color_normal, mView.getCurrentTextColor());
        mTextColorPressed = a.getColor(R.styleable.RxTextView_text_color_pressed, mView.getCurrentTextColor());
        mTextColorUnable = a.getColor(R.styleable.RxTextView_text_color_unable, mView.getCurrentTextColor());
        //typeface
        mTypefacePath = a.getString(R.styleable.RxTextView_text_typeface);

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


        //text渐变
        mTextGradientOrientation = a.getInteger(R.styleable.RxTextView_text_gradient_orientation, 0);
        mTextGradientCenterColor = a.getColor(R.styleable.RxTextView_text_gradient_centerColor, 0);
        mTextGradientEndColor = a.getColor(R.styleable.RxTextView_text_gradient_endColor, 0);
        mTextGradientSatrtColor = a.getColor(R.styleable.RxTextView_text_gradient_startColor, 0);
        mTextGradientOpen = a.getBoolean(R.styleable.RxTextView_text_gradient_open, false);
        mTextGradientTileMode = a.getInt(R.styleable.RxTextView_text_gradient_tileMode, 0);


        if (mTextGradientCenterColor == 0) {
            mTextGradientColors = new int[]{mTextGradientSatrtColor, mTextGradientEndColor};
        } else {
            mTextGradientColors = new int[]{mTextGradientSatrtColor,
                    mTextGradientCenterColor, mTextGradientEndColor};
        }

        a.recycle();

        //setup
        setup();

    }

    /**
     * 设置
     */
    private void setup() {
        mRipplePressed = new GradientDrawable();
        mRipplePressed.setColor(mTextColorNormal);
        /**
         * icon
         */
        if (mView.isEnabled() == false) {
            mIcon = mIconUnable;
        } else {
            mIcon = mIconNormal;
        }

        //设置文本颜色
        setTextColor();

        //设置ICON
        setIcon();

        //设置文本字体样式
        setTypeface();

        //设置渐变
        setGradient();


        mView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                setTextGradient();
            }
        });
    }


    /**
     * 设置渐变模式
     */
    private void setGradient() {
        if (mGradientCenterX != 0 && mGradientCenterY != 0) {
            mRipplePressed.setGradientCenter(mGradientCenterX, mGradientCenterY);
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
            mRipplePressed.setColors(colors);
        }
        gradientOrientation();
        mRipplePressed.setGradientRadius(mGradientRadius);
        if (mGradientType != 0) {
            mRipplePressed.setGradientType(mGradientType);
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
        mRipplePressed.setOrientation(mOrientation);
    }


    /************************
     * Typeface
     ************************/

    public RxTextViewHelper setTypeface(String typefacePath) {
        this.mTypefacePath = typefacePath;
        setTypeface();
        return this;
    }

    public String getTypefacePath() {
        return mTypefacePath;
    }

    private void setTypeface() {
        if (!TextUtils.isEmpty(mTypefacePath)) {
            AssetManager assetManager = mContext.getAssets();
            Typeface typeface = Typeface.createFromAsset(assetManager, mTypefacePath);
            mView.setTypeface(typeface);
        }
    }

    /************************
     * Icon
     ************************/

    public RxTextViewHelper setIconNormal(Drawable icon) {
        this.mIconNormal = icon;
        this.mIcon = icon;
        setIcon();
        return this;
    }

    public Drawable getIconNormal() {
        return mIconNormal;
    }

    public RxTextViewHelper setIconPressed(Drawable icon) {
        this.mIconPressed = icon;
        this.mIcon = icon;
        setIcon();
        return this;
    }

    public Drawable getIconPressed() {
        return mIconPressed;
    }

    public RxTextViewHelper setIconUnable(Drawable icon) {
        this.mIconUnable = icon;
        this.mIcon = icon;
        setIcon();
        return this;
    }

    public Drawable getIconUnable() {
        return mIconUnable;
    }

    public RxTextViewHelper setIconSize(int iconWidth, int iconHeight) {
        this.mIconWidth = iconWidth;
        this.mIconHeight = iconHeight;
        setIcon();
        return this;
    }

    public RxTextViewHelper setIconWidth(int iconWidth) {
        this.mIconWidth = iconWidth;
        setIcon();
        return this;
    }

    public int getIconWidth() {
        return mIconWidth;
    }

    public RxTextViewHelper setIconHeight(int iconHeight) {
        this.mIconHeight = iconHeight;
        setIcon();
        return this;
    }

    public int getIconHeight() {
        return mIconHeight;
    }

    public RxTextViewHelper setIconDirection(int iconDirection) {
        this.mIconDirection = iconDirection;
        setIcon();
        return this;
    }

    public int getIconDirection() {
        return mIconDirection;
    }

    private void setIcon() {
        //未设置图片大小
        if (mIconHeight == 0 && mIconWidth == 0) {
            if (mIcon != null) {
                mIconWidth = mIcon.getIntrinsicWidth();
                mIconHeight = mIcon.getIntrinsicHeight();
            }
        }
        setIcon(mIcon, mIconWidth, mIconHeight, mIconDirection);
    }

    private void setIcon(Drawable drawable, int width, int height, int direction) {
        if (drawable != null) {
            if (width != 0 && height != 0) {
                drawable.setBounds(0, 0, width, height);
            }
            switch (direction) {
                case ICON_DIR_LEFT:
                    mView.setCompoundDrawables(drawable, null, null, null);
                    break;
                case ICON_DIR_TOP:
                    mView.setCompoundDrawables(null, drawable, null, null);
                    break;
                case ICON_DIR_RIGHT:
                    mView.setCompoundDrawables(null, null, drawable, null);
                    break;
                case ICON_DIR_BOTTOM:
                    mView.setCompoundDrawables(null, null, null, drawable);
                    break;
            }
        }
    }

    /************************
     * text color
     ************************/

    public RxTextViewHelper setTextColorNormal(int textColor) {
        this.mTextColorNormal = textColor;
        if (mTextColorPressed == 0) {
            mTextColorPressed = mTextColorNormal;
        }
        if (mTextColorUnable == 0) {
            mTextColorUnable = mTextColorNormal;
        }
        setTextColor();
        return this;
    }

    public int getTextColorNormal() {
        return mTextColorNormal;
    }

    public RxTextViewHelper setPressedTextColor(int textColor) {
        this.mTextColorPressed = textColor;
        setTextColor();
        return this;
    }

    public int getPressedTextColor() {
        return mTextColorPressed;
    }

    public RxTextViewHelper setTextColorUnable(int textColor) {
        this.mTextColorUnable = textColor;
        setTextColor();
        return this;
    }

    public int getTextColorUnable() {
        return mTextColorUnable;
    }

    public void setTextColor(int normal, int pressed, int unable) {
        this.mTextColorNormal = normal;
        this.mTextColorPressed = pressed;
        this.mTextColorUnable = unable;
        setTextColor();
    }

    private void setTextColor() {
        int[] colors = new int[]{mTextColorPressed, mTextColorPressed, mTextColorNormal, mTextColorUnable};
        mTextColorStateList = new ColorStateList(states, colors);
        mView.setTextColor(mTextColorStateList);
    }

    public RxTextViewHelper setTextGradientOrientation(@IntRange(from = 0, to = 1) int orientation) {
        this.mTextGradientOrientation = orientation;
        mTextGradientOpen = true;
        setTextGradient();
        return this;
    }

    public RxTextViewHelper setTextGradientOpen(boolean textGradientOpen) {
        mTextGradientOpen = textGradientOpen;
        setTextGradient();
        return this;
    }

    public RxTextViewHelper setTextGradientColorList(@ColorInt int[] colors) {
        if (colors.length < 3) {
            throw new NullPointerException("colors 长度必须大于2");
        }
        mTextGradientOpen = true;
        mTextGradientColors = colors;
        setTextGradient();
        return this;
    }

    private void setTextGradient() {
        if (!mTextGradientOpen) {
            mView.getPaint().setShader(null);
            mView.invalidate();
            return;
        }
        Shader.TileMode mode = Shader.TileMode.CLAMP;
        switch (mTextGradientTileMode) {
            case 0:
                mode = Shader.TileMode.CLAMP;
                break;
            case 1:
                mode = Shader.TileMode.REPEAT;
                break;
            case 2:
                mode = Shader.TileMode.MIRROR;
                break;
        }

        if (mTextGradientOrientation != 0)
            textShader = new LinearGradient(0, 0, 0, mView.getHeight(), mTextGradientColors, null, mode);
        else {
            textShader = new LinearGradient(0, 0, mView.getWidth(), 0, mTextGradientColors, null, mode);
        }
        mView.getPaint().setShader(textShader);
        mView.invalidate();
    }


    /**
     * 设置是否启用
     *
     * @param enabled
     */
    public void setEnabled(boolean enabled) {
        if (enabled) {
            if (mIconNormal != null) {
                mIcon = mIconNormal;
                setIcon();
            }
        } else {
            if (mIconUnable != null) {
                mIcon = mIconUnable;
                setIcon();
            }
        }
    }

    /**
     * 触摸事件逻辑
     *
     * @param event
     */
    public void onTouchEvent(MotionEvent event) {
        if (!mView.isEnabled()) return;
        mGestureDetector.onTouchEvent(event);
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP://抬起
                if (mIconNormal != null) {
                    mIcon = mIconNormal;
                    setIcon();
                }
                break;
            case MotionEvent.ACTION_MOVE://移动
                int x = (int) event.getX();
                int y = (int) event.getY();
                if (isOutsideView(x, y)) {
                    if (mIconNormal != null) {
                        mIcon = mIconNormal;
                        setIcon();
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL://父级控件获取控制权
                if (mIconNormal != null) {
                    mIcon = mIconNormal;
                    setIcon();
                }
                break;
        }
    }

    /**
     * 手势处理
     */
    class SimpleOnGesture extends GestureDetector.SimpleOnGestureListener {
        @Override
        public void onShowPress(MotionEvent e) {
            if (mIconPressed != null) {
                mIcon = mIconPressed;
                setIcon();
            }
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (mIconNormal != null) {
                mIcon = mIconNormal;
                setIcon();
            }
            return false;
        }
    }

}
