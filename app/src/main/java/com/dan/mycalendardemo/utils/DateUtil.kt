package com.dan.mycalendardemo.utils

import com.dan.mycalendardemo.entry.HouseInfo
import com.haibin.calendarview.Calendar

object DateUtil {


    /**
     *  计算价格
     */
    fun getPrice(year: Int, month: Int, day: Int, houseInfo: HouseInfo): String {
        val calendar = Calendar()
        calendar.year = year
        calendar.month = month
        calendar.day = day
        houseInfo.special.forEach {
            if (it.day == "$year$month$day") {
                return "${it.price}"
            }
        }
        when (getWeekFormCalendar(calendar)) {
            0 -> {
//                周天
                return "${houseInfo.weekdayPrice[6]}"
            }
            1 -> {
                return "${houseInfo.weekdayPrice[0]}"
            }
            2 -> {
                return "${houseInfo.weekdayPrice[1]}"
            }
            3 -> {
                return "${houseInfo.weekdayPrice[2]}"
            }
            4 -> {
                return "${houseInfo.weekdayPrice[3]}"
            }
            5 -> {
                return "${houseInfo.weekdayPrice[4]}"
            }
            6 -> {
                return "${houseInfo.weekdayPrice[5]}"
            }
            else -> return "0"
        }
    }

    /**
     * 获取某月的天数
     *
     * @param year  年
     * @param month 月
     * @return 某月的天数
     */
    fun getMonthDaysCount(year: Int, month: Int): Int {
        var count = 0
        //判断大月份
        if (month == 1 || month == 3 || month == 5 || month == 7
                || month == 8 || month == 10 || month == 12) {
            count = 31
        }

        //判断小月
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            count = 30
        }

        //判断平年与闰年
        if (month == 2) {
            if (isLeapYear(year)) {
                count = 29
            } else {
                count = 28
            }
        }
        return count
    }


    /**
     * 是否是闰年
     *
     * @param year year
     * @return 是否是闰年
     */
    fun isLeapYear(year: Int): Boolean {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0
    }

    /**
     * 获取某个日期是星期几
     * 测试通过
     *
     * @param calendar 某个日期
     * @return 返回某个日期是星期几
     */
    fun getWeekFormCalendar(calendar: Calendar): Int {
        val date = java.util.Calendar.getInstance()
        date.set(calendar.year, calendar.month - 1, calendar.day)
        return date.get(java.util.Calendar.DAY_OF_WEEK) - 1
    }

    /**
     * 判断一个日期是否是周末，即周六日
     *
     * @param calendar calendar
     * @return 判断一个日期是否是周末，即周六日
     */
    fun isWeekend(calendar: Calendar): Boolean {
        val week = getWeekFormCalendar(calendar)
        return week == 0 || week == 6
    }


    /**
     * 是否在日期范围內
     * 测试通过 test pass
     *
     * @param calendar     calendar
     * @param minYear      minYear
     * @param minYearDay   最小年份天
     * @param minYearMonth minYearMonth
     * @param maxYear      maxYear
     * @param maxYearMonth maxYearMonth
     * @param maxYearDay   最大年份天
     * @return 是否在日期范围內
     */
    fun isCalendarInRange(calendar: Calendar,
                          minYear: Int, minYearMonth: Int, minYearDay: Int,
                          maxYear: Int, maxYearMonth: Int, maxYearDay: Int): Boolean {
        val c = java.util.Calendar.getInstance()
        c.set(minYear, minYearMonth - 1, minYearDay)
        val minTime = c.timeInMillis
        c.set(maxYear, maxYearMonth - 1, maxYearDay)
        val maxTime = c.timeInMillis
        c.set(calendar.year, calendar.month - 1, calendar.day)
        val curTime = c.timeInMillis
        return curTime in minTime..maxTime
    }
}