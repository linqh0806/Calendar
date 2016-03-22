package linqh.schedule.utiles;

import linqh.schedule.domain.ScheduleItem;

/**
 * Created by dz1 on 2016/1/11.
 */
public class getScheduleItem {
    public static ScheduleItem getScheduleFromDate(int year, int month,int date,int week) {
        Boolean isLeapYear=false;
        if(year%4==0&&year%100!=0)isLeapYear=true;
        if(year%400==0&&year%100==0)isLeapYear=true;
        ScheduleItem item = new ScheduleItem();
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            item.setTotaldate(31);
        } else {
            item.setTotaldate(30);
        }
        if(month==2){
            if(isLeapYear){
                item.setTotaldate(29);
            }else {
                item.setTotaldate(28);
            }
        }
        switch (date%7)
        {
            case 0:item.setFirstDayWeek(week+1);
                break;
            case 1:item.setFirstDayWeek(week);
                break;
            case 2:item.setFirstDayWeek((week-1)>0?(week-1):7);
                break;
            case 3:item.setFirstDayWeek((week-2)>0?(week-2):(7+week-2));
                break;
            case 4:item.setFirstDayWeek((week-3)>0?(week-3):(7+week-3));
                break;
            case 5:item.setFirstDayWeek((week-4)>0?(week-4):(7+week-4));
                break;
            case 6:item.setFirstDayWeek((week-5)>0?(week-5):(7+week-5));
                break;
        }
        item.setLastDayWeek((item.getFirstDayWeek()+7-1+item.getTotaldate()%7)%7);
        return item;
    }

    public static int getMonthDays(int year,int month){
        Boolean isLeapYear=false;
        int totalDays;
        if(year%4==0&&year%100!=0)isLeapYear=true;
        if(year%400==0&&year%100==0)isLeapYear=true;
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            totalDays=31;
        } else {
            totalDays=30;
        }
        if(month==2){
            if(isLeapYear){
                totalDays=29;
            }else {
                totalDays=28;
            }
        }
        return totalDays;
    }
}
