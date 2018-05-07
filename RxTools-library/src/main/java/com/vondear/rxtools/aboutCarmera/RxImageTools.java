package com.vondear.rxtools.aboutCarmera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Base64;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 处理图片的工具类.
 */
public class RxImageTools {
    /**
     * 图片去色,返回灰度图片
     *
     * @param bmpOriginal 传入的图片
     * @return 去色后的图片
     */
    public static Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        // 获取宽高
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();
        // 按照一定画质新建bitmap
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);
        // 建画布
        Canvas c = new Canvas(bmpGrayscale);
        // 建画笔
        Paint paint = new Paint();
        // 颜色矩阵
        ColorMatrix cm = new ColorMatrix();
        // 设置灰阶
        cm.setSaturation(0);
        // 颜色矩阵颜色过滤对象,传入设置的颜色矩阵
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        // 设置画笔的颜色过滤,传入颜色矩阵颜色过滤对象
        paint.setColorFilter(f);
        // 在画布用画笔画传入的图片
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    /**
     * 去色同时加圆角
     *
     * @param bmpOriginal 原图
     * @param pixels      圆角弧度
     * @return 修改后的图片
     */
    public static Bitmap toGrayscale(Bitmap bmpOriginal, int pixels) {
        return toRoundCorner(toGrayscale(bmpOriginal), pixels);
    }

    /**
     * 把图片变成圆角
     *
     * @param bitmap 需要修改的图片
     * @param pixels 圆角的弧度
     * @return 圆角图片
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        final float roundPx = pixels;// 圆角的弧度

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
    /** */
    /**
     * 使圆角功能支持BitampDrawable
     *
     * @param bitmapDrawable
     * @param pixels
     * @return
     */
    public static BitmapDrawable toRoundCorner(BitmapDrawable bitmapDrawable,
                                               int pixels) {
        Bitmap bitmap = bitmapDrawable.getBitmap();
        bitmapDrawable = new BitmapDrawable(toRoundCorner(bitmap, pixels));
        return bitmapDrawable;
    }

    /**
     * 读取路径中的图片，然后将其转化为缩放后的bitmap
     *
     * @param path
     */
    public static Bitmap saveBefore(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高
        Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回bm为空
        options.inJustDecodeBounds = false;
        // 计算缩放比
        int be = (int) (options.outHeight / (float) 200);
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = 2; // 图片长宽各缩小二分之一
        // 重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false哦
        bitmap = BitmapFactory.decodeFile(path, options);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        System.out.println(w + "    " + h);
        // savePNG_After(bitmap,path);
        saveJPGE_After(bitmap, path);
        return bitmap;
    }

    /**
     * 保存图片为PNG
     *
     * @param bitmap bitmap对象
     * @param name   path
     */
    public static void savePNG_After(Bitmap bitmap, String name) {
        File file = new File(name);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存图片为JPEG
     *
     * @param bitmap
     * @param path
     */
    public static void saveJPGE_After(Bitmap bitmap, String path) {
        File file = new File(path);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static final int TOP_LEFT = 0;
    public static final int TOP_RIGHT = 1;
    public static final int BOTTOM_LEFT = 2;
    public static final int BOTTOM_RIGHT = 3;

    /**
     * 水印
     *
     * @param src       图片
     * @param watermark 水印
     * @return bitmap 处理后的图片
     */
    public static Bitmap createBitmapForWatermark(Bitmap src, Bitmap watermark, int direction, int magin) {

        if (src == null) {
            return null;
        }
        //位图的宽高
        int w = src.getWidth();
        int h = src.getHeight();
        //水印的宽高
        int ww = watermark.getWidth();
        int wh = watermark.getHeight();
        // 创建一个新的和SRC长度宽度一样的位图
        Bitmap newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newb);
        // 在 0，0坐标开始画入src
        cv.drawBitmap(src, 0, 0, null);
        // 在src的右下角画入水印（距离底部和右边距离为5）
        switch (direction) {
            case TOP_LEFT:
                cv.drawBitmap(watermark, magin, magin, null);
                break;
            case TOP_RIGHT:
                cv.drawBitmap(watermark, w - ww + magin, magin, null);
                break;
            case BOTTOM_LEFT:
                cv.drawBitmap(watermark, magin, h - wh + magin, null);
                break;
            case BOTTOM_RIGHT:
                cv.drawBitmap(watermark, w - ww + magin, h - wh + magin, null);
                break;
        }

        // 保存
        cv.save(Canvas.ALL_SAVE_FLAG);
        // 存储
        cv.restore();
        return newb;
    }

    /**
     * 将Bitmap转换成指定大小
     *
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public static Bitmap createBitmapBySize(Bitmap bitmap, int width, int height) {
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    /**
     * 将Bitmap百分百缩放
     *
     * @param bitmap
     * @param wf
     * @param hf
     * @return
     */
    public static Bitmap zoom(Bitmap bitmap, float wf, float hf) {
        Matrix matrix = new Matrix();
        matrix.postScale(wf, hf);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 缩放图片
     *
     * @param sf
     * @return
     */
    public static Bitmap zoom(Bitmap bitmap, float sf) {
        Matrix matrix = new Matrix();
        matrix.postScale(sf, sf);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    /**
     * Drawable 转 Bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmapByBD(Drawable drawable) {
        Bitmap bmp = null;
        //第一步，将Drawable对象转化为Bitmap对象
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) drawable).getBitmap();
        } else
            bmp = drawableToBitamp(drawable);
        return bmp;
    }


    private static Bitmap drawableToBitamp(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        return Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    }

    /**
     * Bitmap 转 Drawable
     *
     * @param bitmap
     * @return
     */
    public static Drawable bitmapToDrawableByBD(Bitmap bitmap) {
        Drawable drawable = new BitmapDrawable(bitmap);
        return drawable;
    }

    /**
     * byte[] 转 bitmap
     *
     * @param b
     * @return
     */
    public static Bitmap bytesToBimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    /**
     * bitmap 转 byte[]
     *
     * @param bm
     * @return
     */
    public static byte[] bitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 图片转成string
     *
     * @param bitmap
     * @return
     */
    public static String convertIconToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);
    }

    /**
     * string转成bitmap
     *
     * @param st
     */
    public static Bitmap convertStringToIcon(String st) {
        // OutputStream out;
        Bitmap bitmap = null;
        try {
            // out = new FileOutputStream("/sdcard/aa.jpg");
            byte[] bitmapArray;
            bitmapArray = Base64.decode(st, Base64.DEFAULT);
            bitmap =
                    BitmapFactory.decodeByteArray(bitmapArray, 0,
                            bitmapArray.length);
            // bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    //给图片上色
    protected Drawable tintDrawableWithColor(Context context, @DrawableRes int drawableRes, @ColorRes int colorRes) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableRes);
        DrawableCompat.setTint(drawable, ContextCompat.getColor(context, colorRes));
        return drawable;
    }


    /**
     * 控件转成bitmap
     *
     * @param view
     */
    public static Bitmap TranslateBitmap(View view) {
        // Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
        // view.getHeight(),
        // Bitmap.Config.RGB_565);
        // // 利用bitmap生成画布
        // Canvas canvas = new Canvas(bitmap);
        // view.draw(canvas);

        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(true);
        view.getDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        view.setDrawingCacheEnabled(false);

        return bitmap;
    }


    /**
     * Check the SD card 检测是否有SD卡
     *
     * @return
     */
    public static boolean checkSDCardAvailable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 将图片存到本地
     */
    public static File saveBitmap(Bitmap bm, String dicName, String picName) {
        try {
            String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + dicName + picName + ".jpg";
            File f = new File(dir);
            if (!f.exists()) {
                f.getParentFile().mkdirs();
                f.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            return f;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Save image to the SD card 保存图片到sd卡
     *
     * @param photoBitmap 位图
     * @param photoName   图片名
     * @param path        存放路径
     */
    public static void savePhotoToSDCard(Bitmap photoBitmap, String path,
                                         String photoName) {
        // 判断是否存在sd卡  
        if (checkSDCardAvailable()) {
            File dir = new File(path);
            // 如果dir 文件不存在,新建  
            if (!dir.exists()) {
                dir.mkdirs();
            }
            // 建图片文件路径,在文件路径下  
            File photoFile = new File(path, photoName + ".png");
            // 新建文件输出流定为空  
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100,
                            fileOutputStream)) {
                        fileOutputStream.flush(); // 这里才是输出数据  
                        fileOutputStream.close();
                    }
                }
            } catch (FileNotFoundException e) {
                photoFile.delete();
                e.printStackTrace();
            } catch (IOException e) {
                photoFile.delete();
                e.printStackTrace();
            } finally {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}  