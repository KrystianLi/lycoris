package com.hello.tools;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期操作
 */
public class DateUtil {

    /**
     * 增加多少年的时间
     * @param strDate
     * @param year
     * @return
     */
    public static String addDate(String strDate, int year){
        LocalDate date = LocalDate.parse(strDate);
        LocalDate newDate = date.plusYears(year);
        String newDateString = newDate.toString();
        return newDateString;
    }

    /**
     * 将时间格式化成yyyy-MM-dd的格式
     * @param date
     * @return 返回时间字符串
     */
    public static String formatDate(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * 传入一个时间字符串数组，格式为yyyy-MM-dd，降序排列
     * @param strDate
     * @return
     */
    public static List<String> descDate(List<String> strDate){
        strDate.sort((t1, t2) -> t2.compareTo(t1));
        return strDate;
    }
    /**
     * 传入一个时间字符串数组，格式为yyyy-MM-dd，升序排列
     * @param strDate
     * @return
     */
    public static List<String> ascDate(List<String> strDate){
        strDate.sort((t1, t2) -> t1.compareTo(t2));
        return strDate;

    }

    /**
     * 判断date1是否在date2之后的日期
     * @param date1 字符串日期格式yyyy-MM-dd
     * @param date2 字符串日期格式yyyy-MM-dd
     * @return
     */
    public static Boolean DateComparisonAfter(String date1,String date2){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate1 = LocalDate.parse(date1, formatter);
        LocalDate localDate2 = LocalDate.parse(date2, formatter);
        return localDate1.isAfter(localDate2);
    }

}
