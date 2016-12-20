package com.pythoncat.a04.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ScrollView;

import com.apkfuns.logutils.LogUtils;
import com.pythoncat.a04.R;
import com.pythoncat.a04.tools.BitmapUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;

public class ScrollShowActivity extends AppCompatActivity {

    @BindView(R.id.scroll_view)
    ScrollView sv;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_show);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Bitmap bmp = BitmapUtil.convertViewToBitmap(sv);
        final String filename = "test01.jpg";
        final File target = new File("/sdcard/", filename);
        Observable.defer(() ->
                Observable.just(BitmapUtil.saveBitmap2file(bmp, target)))
                .subscribe(ok -> {
                    LogUtils.e("is ok ? " + ok);
                    LogUtils.w("is ok ? " + ok);
                    if (ok) {
                        final Uri parse = Uri.parse(target.getPath());
                        LogUtils.e("file length = "+target.length());
                        share(parse);
                    }

                }, Throwable::printStackTrace);
        LogUtils.e(bmp.getClass());
    }


    private void share(Uri uri) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        if (uri != null) {
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("image/*");
            //当用户选择短信时使用sms_body取得文字
        }
        //自定义选择框的标题
        startActivity(Intent.createChooser(shareIntent, "邀请好友"));
        //系统默认标题

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


}
