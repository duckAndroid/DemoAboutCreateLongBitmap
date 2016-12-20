package com.pythoncat.a04.tools;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;

import com.apkfuns.logutils.LogUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class BitmapUtil {
   public static boolean saveBitmap2file(Bitmap bmp, File target) {
        Bitmap.CompressFormat format = Bitmap.CompressFormat.PNG; // png very good！
        int quality = 100;
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(target);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bmp.compress(format, quality, stream);
    }
    public static Bitmap convertViewToBitmap(View view) {
        view.setBackgroundColor(Color.WHITE); // 加这一句话试试 very good !
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        LogUtils.e("w = " + view.getMeasuredWidth() + " ,  h = " + view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();

        return bitmap;
    }
}  