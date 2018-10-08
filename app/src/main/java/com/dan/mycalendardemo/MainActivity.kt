package com.dan.mycalendardemo

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.blankj.utilcode.util.TimeUtils
import com.dan.mycalendardemo.entry.HouseInfo
import com.dan.mycalendardemo.entry.OrderSummart
import com.dan.mycalendardemo.entry.Special
import com.dan.mycalendardemo.utils.DateUtil
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity(), CalendarView.OnCalendarInterceptListener
        , CalendarView.OnCalendarRangeSelectListener {


    private var houseInfo: HouseInfo? = null

    private var orderSummart: OrderSummart? = null

    private var start: Calendar? = null

    private var orderSumMap: HashMap<String, Any>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        mCalendarView.setRange(mCalendarView.curYear, mCalendarView.curMonth, mCalendarView.curDay,
                mCalendarView.curYear, 12, 31)
        loadData()

        mCalendarView.setOnCalendarInterceptListener(this)
        mCalendarView.setOnCalendarRangeSelectListener(this)

        mClearBtn.setOnClickListener {
            start = null
            mCalendarView.clearSelectRange()
        }
    }

    private fun loadData() {
        houseInfo = HouseInfo(listOf(100, 100, 200, 300, 400, 500, 600), listOf(Special("20181010", 300)
                , Special("20181011", 2000), Special("20181012", 2500)))

        //预定信息
        orderSummart = OrderSummart(mutableListOf("20181030", "20181025", "20181024"))

        orderSumMap = hashMapOf()
        orderSummart!!.orderSummary.forEach {
            orderSumMap!![it] = "已租"
        }


        val map = HashMap<String, Calendar>()
        val maxYear = mCalendarView.maxRangeCalendar.year
        val maxMonth = mCalendarView.maxRangeCalendar.month
        val maxDay = mCalendarView.maxRangeCalendar.day
        val minYear = mCalendarView.minRangeCalendar.year
        val minMonth = mCalendarView.minRangeCalendar.month
        val minDay = mCalendarView.minRangeCalendar.day

        //遍历年月日
        for (year in minYear..maxYear) {
            for (month in minMonth..maxMonth) {
                if (month == minMonth) {
                    for (day in minDay..maxDay) {
                        map[getSchemeCalendar(year, month, day, DateUtil.getPrice(year, month, day, houseInfo!!)).toString()] =
                                getSchemeCalendar(year, month, day, DateUtil.getPrice(year, month, day, houseInfo!!))
                    }
                } else {
                    for (day in 1..DateUtil.getMonthDaysCount(year, month)) {
                        map[getSchemeCalendar(year, month, day, DateUtil.getPrice(year, month, day, houseInfo!!)).toString()] =
                                getSchemeCalendar(year, month, day, DateUtil.getPrice(year, month, day, houseInfo!!))
                    }
                }
            }
        }
        mCalendarView.setSchemeDate(map)
    }

    private fun getSchemeCalendar(year: Int, month: Int, day: Int, text: String): Calendar {
        val calendar = Calendar()
        calendar.year = year
        calendar.month = month
        calendar.day = day
//        calendar.schemeColor = color//如果单独标记颜色、则会使用这个颜色
        calendar.scheme = text
        calendar.addScheme(Calendar.Scheme())
        return calendar
    }

    //拦截
    override fun onCalendarInterceptClick(calendar: Calendar, isClick: Boolean) {
        Toast.makeText(this, "这天已经租出去啦", Toast.LENGTH_SHORT).show()
    }

    override fun onCalendarIntercept(calendar: Calendar): Boolean {
        //判断是否在日期范围之内
//        if (DateUtil.isCalendarInRange(calendar, mCalendarView.minRangeCalendar.year,
//                        mCalendarView.minRangeCalendar.month, mCalendarView.minRangeCalendar.day,
//                        mCalendarView.maxRangeCalendar.year, mCalendarView.maxRangeCalendar.month, mCalendarView.maxRangeCalendar.day)) {
//            val year = calendar.year
//            val month = calendar.month
//            val day = calendar.day
////        mCalendarView.minRangeCalendar
//            if (start == null) {
//                return orderSummart?.orderSummary?.any {
//                    year == it.substring(0..3).toInt() &&
//                            month == it.substring(4..5).toInt() &&
//                            day == it.substring(6..7).toInt()
//                } ?: false
//            } else {
//                val sYear = start!!.year
//                val sMonth = start!!.month
//                val sDay = start!!.day
//                val startCalendar = java.util.Calendar.getInstance()
//                startCalendar.set(java.util.Calendar.YEAR, sYear)
//                startCalendar.set(java.util.Calendar.MONTH, sMonth)
//                startCalendar.set(java.util.Calendar.DAY_OF_MONTH, sDay)
//
//                val allCalendar = java.util.Calendar.getInstance()
//                allCalendar.set(java.util.Calendar.YEAR, year)
//                allCalendar.set(java.util.Calendar.MONTH, month)
//                allCalendar.set(java.util.Calendar.DAY_OF_MONTH, day)
//
//                if (startCalendar.timeInMillis > allCalendar.timeInMillis) {
//                    Log.e("TAG", "比入住之前的日期 $year-$month-$day")
//                } else if (startCalendar.timeInMillis < allCalendar.timeInMillis) {
//                    Log.e("TAG", "比入住之后的日期 $year-$month-$day")
//                }
//            }
//        }
        return orderSumMap == null || orderSumMap?.size == 0 || orderSumMap?.containsKey(calendar.toString())!!
    }

    override fun onCalendarSelectOutOfRange(calendar: Calendar) {
        Log.e("TAG", "onCalendarSelectOutOfRange : ${calendar.year}-${calendar.month}-${calendar.day}")
    }

    override fun onCalendarRangeSelect(calendar: Calendar, isEnd: Boolean) {
        if (isEnd.not()) {
            start = calendar
        }
        Log.e("TAG", "onCalendarRangeSelect : ${calendar.year}-${calendar.month}-${calendar.day} isEnd = $isEnd")
    }

    override fun onSelectOutOfRange(calendar: Calendar, isOutOfMinRange: Boolean) {
        Log.e("TAG", "onSelectOutOfRange : ${calendar.year}-${calendar.month}-${calendar.day} isOutOfMinRange = $isOutOfMinRange")
    }
}
