package com.pythoncat.a04.tools;

import android.content.Context;
import android.widget.Toast;

/**
 * @author Administrator
 *         2016/12/11
 *         com.pythoncat.a04.tools
 */

public class ToastHelper {


    private static Toast toast;

    public static void show(Context c, String text) {
        cancel();
        toast = Toast.makeText(c, text, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void cancel() {
        if (toast != null)
            toast.cancel();
    }
}
