package tech.linjiang.suitlines;

import android.graphics.Paint;
import android.graphics.Path;

import java.util.List;

/**
 * @Package tech.linjiang.suitlines
 * @FileName LineBean
 * @Date 2018/9/20 10:22
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit
 */
public class LineBean {


    /**
     * 是否显示点
     */
    private boolean showPoint;

    /**
     * 线条类型：曲线、线段
     */
    private int lineType = SuitLines.SEGMENT;


    /**
     * 线条主题:实线、虚线
     */
    private int lineStyle = SuitLines.SOLID;


    /**
     * 是否填充阴影
     **/
    private boolean isFill;


    /**
     * 线段所有的点
     */
    private List<Unit> mUnits;


    /**
     * 线段的最大最小值
     */
    private float maxValue;
    private float minValue;

    /**
     * 线段颜色，当数组长度大于1时颜色为渐变色
     */
    private int[] color;


    /**
     * 画笔
     */
    private Paint mPaint;


    /**
     * 路劲
     */
    private Path mPath;


    /**
     * 线条宽度
     */
    private int lineWidth;

}
