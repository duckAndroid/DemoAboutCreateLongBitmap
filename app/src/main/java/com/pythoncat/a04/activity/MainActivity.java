package com.pythoncat.a04.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.widget.EditText;

import com.apkfuns.logutils.LogUtils;
import com.pythoncat.a04.R;
import com.pythoncat.a04.base.BaseActivity;
import com.pythoncat.a04.tools.ToastHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends BaseActivity {

    public static final int PICK_CONTACT = 1; // 拾取联系人 ！
    public static final int REQUEST_IMAGE_CAPTURE = 213; // 拾取拍照图片
    @BindView(R.id.et_pn)
    EditText etPhoneNumber;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_call)
    public void callPhone() {

        String phone = etPhoneNumber.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastHelper.show(getApplication(), "phone is empty !");
        } else {
            Intent in = new Intent();
            in.setData(Uri.parse("tel:" + phone));
            in.setAction(Intent.ACTION_DIAL);

            if (checkIntentFilter(in)) {
                startActivity(in);
            }
        }
    }

    /**
     * 确认是否存在可响应 Intent 的可用 Activity
     *
     * @param in Intent
     * @return check
     */
    private boolean checkIntentFilter(Intent in) {
        PackageManager packageManager = getPackageManager();
        List activities = packageManager.queryIntentActivities(in,
                PackageManager.MATCH_DEFAULT_ONLY);
        LogUtils.e("kkkkkkkkkkk  " + activities.size());
        return activities.size() > 0;
    }

    @OnClick(R.id.btn_map)
    public void openMap() {
        // Build the intent
        Uri location = Uri.parse("geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+California");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);

        // Verify it resolves
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(mapIntent, 0);
        boolean isIntentSafe = activities.size() > 0;

        // Start an activity if it's safe
        if (isIntentSafe) {
            startActivity(mapIntent);
        }
    }

    @OnClick(R.id.btn_contact)
    public void pickContact() {
        // 打开联系人拾取界面
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, PICK_CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request it is that we're responding to
        if (requestCode == PICK_CONTACT) {
            // 拾取联系人 number
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Get the URI that points to the selected contact
                Uri contactUri = data.getData();
                // We only need the NUMBER column, because there will be only one row in the result
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};

                // Perform the query on the contact to get the NUMBER column
                // We don't need a selection or sort order (there's only one result for the given URI)
                // CAUTION: The query() method should be called from a separate thread to avoid blocking
                // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
                // Consider using CursorLoader to perform the query.
                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    // Retrieve the phone number from the NUMBER column
                    int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    String number = cursor.getString(column);
                    etPhoneNumber.setText(number);
                    etPhoneNumber.setSelection(number.length());
                    // Do something with the phone number...
                    cursor.close();
                }
            }
        }
    }

    @OnClick(R.id.btn_alarm)
    public void createAlarm() {
        Integer[] alarmDay = {
                Calendar.MONDAY,
                Calendar.TUESDAY,
                Calendar.WEDNESDAY,
                Calendar.THURSDAY,
                Calendar.FRIDAY
        };
        createAlarm("明天入职9:30前到达", 7, 10, new ArrayList<>(Arrays.asList(alarmDay)));
    }

    /**
     * 创建一个周一到周五的闹铃
     *
     * @param message 标签
     * @param hour    几点闹铃
     * @param minutes 几分
     * @param days    哪些天闹
     */
    public void createAlarm(String message, int hour, int minutes, ArrayList<Integer> days) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent
                    .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                    .putExtra(AlarmClock.EXTRA_HOUR, hour)
                    .putExtra(AlarmClock.EXTRA_DAYS, days)
                    .putExtra(AlarmClock.EXTRA_MINUTES, minutes);

        } else {
            intent
                    .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                    .putExtra(AlarmClock.EXTRA_HOUR, hour)
                    // .putExtra(AlarmClock.EXTRA_DAYS, days) // api level 19
                    .putExtra(AlarmClock.EXTRA_MINUTES, minutes);
        }
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @OnClick(R.id.btn_timer)
    public void timer() {
        startTimer("定时器来了....", 60); // 60s

    }

    /**
     * 设置一个定时器
     *
     * @param message 标签
     * @param seconds 倒计时
     */
    public void startTimer(String message, int seconds) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent(AlarmClock.ACTION_SET_TIMER)
                    .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                    .putExtra(AlarmClock.EXTRA_LENGTH, seconds)
                    .putExtra(AlarmClock.EXTRA_SKIP_UI, false); // 不要去设置定时器的UI
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }

    @OnClick(R.id.btn_calender)
    public void createCalenderEvent() {
        try {
            Calendar begin = Calendar.getInstance(TimeZone.getTimeZone("GMT+:08:00"));
            begin.setTimeInMillis(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
                    .parse("2016-12-12 09:30:00").getTime());
            Calendar end = Calendar.getInstance(TimeZone.getTimeZone("GMT+:08:00"));
            end.setTimeInMillis(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
                    .parse("2016-12-12 11:30:00").getTime());
            LogUtils.e(begin.getTime() + " , " + end.getTime());
            addEvent("python猫 是谁？",
                    "深圳 南山 蛇口网谷",
                    "去欢乐谷吃玉米谷啊",
                    true,
                    begin,
                    end
            );
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一个日历事件
     *
     * @param begin 开始时间 --> fixme: 貌似不行,貌似会变成当前时间！
     * @param end   结束时间 --> fixme： 也不行，就是这两个不行，其他都可以！
     */
    public void addEvent(String title, String location, String desc,
                         boolean allDay, Calendar begin, Calendar end) {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, location)
                .putExtra(CalendarContract.Events.DESCRIPTION, desc)
                .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, allDay)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @OnClick(R.id.btn_camera)
    public void openCamera() {

    }
    @OnClick(R.id.btn_scroll)
    public void toScroll(){
        startActivity(new Intent(this,ScrollShowActivity.class));
    }


}
