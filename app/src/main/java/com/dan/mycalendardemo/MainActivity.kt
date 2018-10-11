package com.dan.mycalendardemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dan.mycalendardemo.entry.HouseInfo
import com.dan.mycalendardemo.entry.OrderSummart
import com.dan.mycalendardemo.entry.Special
import com.dan.mycalendardemo.utils.DateUtil
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity(), CalendarView.OnCalendarInterceptListener
        , CalendarView.OnCalendarRangeSelectListener {


    private var houseInfo: HouseInfo? = null

    private var orderSummart: OrderSummart? = null

    private var start: Calendar? = null
    private var end: Calendar? = null

    private var orderSumMap: HashMap<String, Any>? = null

    private var mBuyDays: MutableList<Calendar>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//
//        mCalendarView.setRange(mCalendarView.curYear, mCalendarView.curMonth, mCalendarView.curDay,
//                mCalendarView.curYear, 12, 31)
//        loadData()
//
//        mCalendarView.setOnCalendarInterceptListener(this)
//        mCalendarView.setOnCalendarRangeSelectListener(this)

        mClearBtn.setOnClickListener {
            clearSelectDate()
        }

        mToSelectCalBtn.setOnClickListener {
            startActivity<CaldenlarActivity>()
        }
    }

    private fun loadData() {
        houseInfo = HouseInfo(listOf(100, 100, 200, 300, 400, 500, 600), listOf(Special("20181010", 300)
                , Special("20181011", 2000), Special("20181012", 2500)))

        //预定信息
        orderSummart = OrderSummart(mutableListOf("20181030", "20181025", "20181024","20181124"))
//        orderSummart = OrderSummart(mutableListOf())

        if (orderSummart!!.orderSummary.isNotEmpty()) {
            mBuyDays = mutableListOf()
            orderSummart!!.orderSummary.forEach {
                val cal = Calendar()
                cal.year = it.substring(0..3).toInt()
                cal.month = it.substring(4..5).toInt()
                cal.day = it.substring(6..7).toInt()
                mBuyDays!!.add(cal)
            }

            orderSumMap = hashMapOf()
            orderSummart!!.orderSummary.forEach {
                orderSumMap!![it] = "已租"
            }

        }


        val map = HashMap<String, Calendar>()
//        val maxYear = mCalendarView.maxRangeCalendar.year
//        val maxMonth = mCalendarView.maxRangeCalendar.month
//        val maxDay = mCalendarView.maxRangeCalendar.day
//        val minYear = mCalendarView.minRangeCalendar.year
//        val minMonth = mCalendarView.minRangeCalendar.month
//        val minDay = mCalendarView.minRangeCalendar.day

        //遍历年月日
//        for (year in minYear..maxYear) {
//            for (month in minMonth..maxMonth) {
//                if (month == minMonth) {
//                    for (day in minDay..maxDay) {
//                        map[getSchemeCalendar(year, month, day, DateUtil.getPrice(year, month, day, houseInfo!!)).toString()] =
//                                getSchemeCalendar(year, month, day, DateUtil.getPrice(year, month, day, houseInfo!!))
//                    }
//                } else {
//                    for (day in 1..DateUtil.getMonthDaysCount(year, month)) {
//                        map[getSchemeCalendar(year, month, day, DateUtil.getPrice(year, month, day, houseInfo!!)).toString()] =
//                                getSchemeCalendar(year, month, day, DateUtil.getPrice(year, month, day, houseInfo!!))
//                    }
//                }
//            }
//        }
//        mCalendarView.setSchemeDate(map)
    }

    private fun getSchemeCalendar(year: Int, month: Int, day: Int, text: String): Calendar {
        val calendar = Calendar()
        calendar.year = year
        calendar.month = month
        calendar.day = day
//        calendar.schemeColor = color//如果单独标记颜色、则会使用这个颜色
        orderSumMap?.let {
            if (orderSumMap!!.containsKey(calendar.toString())) {
                calendar.scheme = "无房"
            } else {
                calendar.scheme = text
            }
        }
        calendar.addScheme(Calendar.Scheme())
        return calendar
    }

    //拦截
    override fun onCalendarInterceptClick(calendar: Calendar, isClick: Boolean) {
//        Toast.makeText(this, "这天已经租出去啦", Toast.LENGTH_SHORT).show()
    }

    override fun onCalendarIntercept(calendar: Calendar): Boolean {
        return setInterceptData(calendar)
    }

    private fun setInterceptData(calendar: Calendar): Boolean {
        orderSumMap?.let {
            if (orderSumMap!!.containsKey(start.toString())) {
                clearSelectDate()
            }
            if (start != null && end == null) {
                if (getNearestDay(start!!) != null) {
                    if (calendar >= getNearestDay(start!!)) {
                        return calendar > getNearestDay(start!!)
                    }
                }
            }
            if (end != null) {
                if (orderSumMap!!.containsKey(end.toString())) {
                    if (calendar >= getNearestDay(start!!)) {
                        return calendar > getNearestDay(start!!)
                    }
                }
            }
            return orderSumMap == null || orderSumMap?.size == 0 || orderSumMap?.containsKey(calendar.toString())!!
        }
        return false
    }

    private fun clearSelectDate() {
        start = null
        end = null
//        mCalendarView.clearSelectRange()
    }

    /**
    找离入住日期最近的已租日期
     *  @param
     */
    private fun getNearestDay(calendar: Calendar): Calendar? {
        val list = mutableListOf<Calendar>()
        list.addAll(mBuyDays!!)
        list.sortBy { it.timeInMillis }
        for (day in list) {
            if (calendar < day) {
                return day
            }
        }
        return null
    }

    override fun onCalendarSelectOutOfRange(calendar: Calendar) {
        Log.e("TAG", "onCalendarSelectOutOfRange : ${calendar.year}-${calendar.month}-${calendar.day}")
    }

    override fun onCalendarRangeSelect(calendar: Calendar, isEnd: Boolean) {
        end = null
        if (isEnd.not()) {
            start = calendar
        } else {
            end = calendar
        }
        Log.e("TAG", "onCalendarRangeSelect : ${calendar.year}-${calendar.month}-${calendar.day} isEnd = $isEnd")
    }

    override fun onSelectOutOfRange(calendar: Calendar, isOutOfMinRange: Boolean) {
        Log.e("TAG", "onSelectOutOfRange : ${calendar.year}-${calendar.month}-${calendar.day} isOutOfMinRange = $isOutOfMinRange")
    }
}
