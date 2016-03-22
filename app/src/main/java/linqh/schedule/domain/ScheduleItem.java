package linqh.schedule.domain;

import java.util.ArrayList;

/**
 * 包括年、月、日。 总天数、星期几
 * Created by dz1 on 2016/1/11.
 */
public class ScheduleItem {

    private int totaldate; //用来存储某年某月的天数
    private int firstDayWeek;//1-7表示星期几，当月第一天星期几
    private int lastDayWeek;//1-7表示星期几，当月最后一天星期几
    private int preMonthLastDay;

    public int getPreMonthLastDay() {
        return preMonthLastDay;
    }

    public void setPreMonthLastDay(int preMonthLastDay) {
        this.preMonthLastDay = preMonthLastDay;
    }

    public int getLastDayWeek() {
        return lastDayWeek;
    }

    public void setLastDayWeek(int lastDayWeek) {
        this.lastDayWeek = lastDayWeek;
    }

    public int getTotaldate() {
        return totaldate;
    }

    public void setTotaldate(int totaldate) {
        this.totaldate = totaldate;
    }

    public int getFirstDayWeek() {
        return firstDayWeek;
    }

    public void setFirstDayWeek(int firstDayWeek) {
        this.firstDayWeek = firstDayWeek;
    }
}
