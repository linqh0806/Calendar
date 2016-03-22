package linqh.schedule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.umeng.update.UmengUpdateAgent;

import java.util.Calendar;

import linqh.schedule.ui.Schedule;
import linqh.schedule.utiles.ToastUtils;

public class MainActivity extends AppCompatActivity {
    //声明手势检测器
    private GestureDetector gestureDetector;

    private TextView tv_time;
    private Schedule schedule;

    private String timeText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_time = (TextView) findViewById(R.id.tv_time);
        schedule = (Schedule) findViewById(R.id.schedule);
        setTimeText(schedule.todayYear, schedule.todayMonth);
        tv_time.setText(timeText);
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//                if (Math.abs(e1.getRawY() - e2.getRawY()) > 300) {
//                    return true;
//                }
                if ((schedule.LastMotionX - e2.getRawX()) > 150) {
                    showNext();
                }
                if ((e2.getRawX() - schedule.LastMotionX) > 150) {
                    showPre();

                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
        schedule.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("e", "action------->被处理了！！！" );
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(this);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        // TODO Auto-generated method stub
//        //GestureDetector 接受没人处理的Touch事件
//        gestureDetector.onTouchEvent(event);
//        return super.onTouchEvent(event);
//    }

    private void showPre() {
        TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        ta.setDuration(100);
        schedule.startAnimation(ta);
        ta.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                schedule.show_pre();
                setTimeText(schedule.mYear, schedule.mMonth);
                tv_time.setText(timeText);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    private void showNext() {
        TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        ta.setDuration(100);
        schedule.startAnimation(ta);
        ta.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                schedule.show_next();
                setTimeText(schedule.mYear, schedule.mMonth);
                tv_time.setText(timeText);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void setTimeText(int year, int month) {
        timeText = String.valueOf(year) + "年" + String.valueOf(month) + "月";
    }
}
