package linqh.schedule.ui;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.StyleableRes;
import android.support.v4.view.MotionEventCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import linqh.schedule.R;
import linqh.schedule.domain.ScheduleItem;
import linqh.schedule.utiles.ChinaDate;
import linqh.schedule.utiles.ToastUtils;
import linqh.schedule.utiles.getScheduleItem;

/**
 * Created by dz1 on 2016/1/11.
 */
public class Schedule extends RelativeLayout implements View.OnClickListener {
    private int year;
    private int month;
    private int totalDate;

    private int descTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 6, getResources().getDisplayMetrics());
    ;
    private int numberSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,14,getResources().getDisplayMetrics());
    private int smallnumberSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,5, getResources().getDisplayMetrics());
    //存放星期的数组
    private TextView[] weekItems = new TextView[7];
    private float itemWidth;
    //存放现在的时间
    private ScheduleItem item; //当前月的数据
    public int todayYear;
    public int todayMonth;
    private int todayDate;
    private int todayWeek;

    public int mYear;
    public int mMonth;
    private int mDate;
    private int mWeek;

    private int mWidth;
    private int mHeight;

    private boolean once;

    private GestureDetector gestureDetector;
    public float LastMotionX;

    public interface ScheduleListener {
        void nextSchedule(String timeText);

        void preSchedule(String timeText);
    }

    public ScheduleListener mListener;

    public void setOnScheduleListener(ScheduleListener mListener) {
        this.mListener = mListener;
    }

    public Schedule(Context context) {
        super(context, null);
    }

    public Schedule(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a=
        getTodayData();
        this.setFocusable(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        if (!once) {
            once = true;

            initDate(mYear, mMonth, mDate, mWeek);
        }
        System.out.println("onMeasure------mWidth:" + mWidth);
        setMeasuredDimension(mWidth, mHeight);
    }

    //拦截事件，当Touch事件返回true，表示该ViewGroup所有下发操作拦截掉。
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        LastMotionX=ev.getRawX();
        Log.e("e","action------->"+String.valueOf(action));
        if (action == MotionEvent.ACTION_SCROLL||action==MotionEvent.ACTION_MOVE) {
            Log.e("e", "action------->被拦截了！！" );
            return true;
        }
        return false;
    }

    private void initDate(int year, int month, int date, int week) {
        itemWidth = Math.round(mWidth / 7 + 0.5f);
        item = getScheduleItem.getScheduleFromDate(year, month, date, week);
        int preDays;
        if (month == 1) {
            preDays = getScheduleItem.getMonthDays(year - 1, 12);
        } else {
            preDays = getScheduleItem.getMonthDays(year, month - 1);
        }
        //每页固定42天
        TextView[] days = new TextView[42];
        for (int i = 0; i < 42; i++) {
            days[i] = new TextView(getContext());
            days[i].setGravity(Gravity.CENTER);
            days[i].setId(i + 1);
            days[i].setOnClickListener(this);
            days[i].setFocusable(false);
            System.out.println("这个月第一天是星期" + item.getFirstDayWeek());
            if (i < item.getFirstDayWeek()) {
                days[i].setText(String.valueOf(preDays - item.getFirstDayWeek() + i + 1));
                days[i].setTextColor(0x44000000);
                days[i].setTextSize(smallnumberSize);
            } else {
                if (i < item.getFirstDayWeek() + item.getTotaldate()) {
                    String text;
                    SpannableString sp;
                    String noli = ChinaDate.oneDay(mYear, mMonth, i - item.getFirstDayWeek() + 1);
                    if (mYear == todayYear && mMonth == todayMonth && todayDate == i - item.getFirstDayWeek() + 1) {
                        text = " 今天 " + "\n" + noli;
                        days[i].setBackgroundResource(R.drawable.today);
                        sp = new SpannableString(text);
                        sp.setSpan(new ForegroundColorSpan(0xffff3300), 0, text.lastIndexOf(" "), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        sp.setSpan(new ForegroundColorSpan(0xffff3300), text.lastIndexOf(" ") + 1, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else {
                        text = " " + String.valueOf(i - item.getFirstDayWeek() + 1) + " " + "\n" + noli;
                        sp = new SpannableString(text);
                        sp.setSpan(new ForegroundColorSpan(0xff000000), 0, text.lastIndexOf(" "), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        sp.setSpan(new ForegroundColorSpan(0x55000000), text.lastIndexOf(" ") + 1, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    sp.setSpan(new AbsoluteSizeSpan(numberSize), 0, text.lastIndexOf(" "), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    sp.setSpan(new AbsoluteSizeSpan(numberSize), text.lastIndexOf(" ") + 1, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    days[i].setLineSpacing(2, 1.2f);
                    days[i].setText(sp);
                } else {
                    days[i].setText(String.valueOf(i - item.getFirstDayWeek() - item.getTotaldate() + 1));
                    days[i].setTextColor(0x44000000);
                    days[i].setTextSize(smallnumberSize);
                }
            }
            LayoutParams lp = new LayoutParams((int) itemWidth, (int) itemWidth);
            if (days[i].getId() > 7) {
                lp.addRule(BELOW, days[i].getId() - 7);
            }
            if (days[i].getId() % 7 != 1) {
                lp.addRule(RIGHT_OF, days[i].getId() - 1);
            }
            addView(days[i], lp);
        }
    }

    public void getTodayData() {
        Calendar c = Calendar.getInstance();
        mYear = todayYear = c.get(Calendar.YEAR);
        mMonth = todayMonth = c.get(Calendar.MONTH) + 1;
        mDate = todayDate = c.get(Calendar.DAY_OF_MONTH);
        mWeek = todayWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        System.out.println(todayYear + "年" + todayMonth + "月" + todayDate + "日" + "  星期" + todayWeek);
    }

    /**
     * 每个日期的点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        Toast.makeText(getContext(), v.getId() + "被点击了", Toast.LENGTH_SHORT).show();
//        switch (v.getTag().toString()){
//            case "1":
//                v.setTag("2");
//                v.setBackground(null);
//                break;
//            case "2":
//                v.setTag("1");
//                v.setBackgroundResource(R.drawable.today);
//                break;
//            default:v.setTag("1");
//                v.setBackgroundResource(R.drawable.today);
//                break;
//        }

    }

    public void show_next() {
        if (mMonth == 12) {
            mMonth = 1;
            mYear += 1;
        } else {
            mMonth += 1;
        }
        mWeek = item.getLastDayWeek() + 1;
        mWeek = mWeek < 8 ? mWeek : (mWeek - 7);
        mDate = 1;
        remove();
        initDate(mYear, mMonth, mDate, mWeek);
    }

    public void show_pre() {
        if (mMonth == 1) {
            mMonth = 12;
            mYear -= 1;
        } else {
            mMonth -= 1;
        }
        mWeek = item.getFirstDayWeek() - 1;
        mWeek = mWeek < 0 ? 7 : mWeek;
        mDate = getScheduleItem.getMonthDays(mYear, mMonth);
        remove();
        initDate(mYear, mMonth, mDate, mWeek);
    }

    private void remove() {
        for (int i = 1; i <= 42; i++) {
            removeView(findViewById(i));
        }
    }
}
