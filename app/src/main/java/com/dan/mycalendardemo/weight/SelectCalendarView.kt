package com.dan.mycalendardemo.weight

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.blankj.utilcode.util.LogUtils
import com.dan.mycalendardemo.R
import com.dan.mycalendardemo.entry.HouseInfo
import com.dan.mycalendardemo.entry.OrderSummart
import com.dan.mycalendardemo.utils.DateUtil
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import kotlinx.android.synthetic.main.view_select_calendar_layout.view.*
import java.util.*

class SelectCalendarView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr)
        , CalendarView.OnMonthChangeListener, CalendarView.OnCalendarInterceptListener
        , CalendarView.OnCalendarRangeSelectListener {


    private lateinit var mCalendarView: CalendarView
    //存储预定信息
    private var orderSumMap: HashMap<String, Any>? = null
    //存储每天的价格
    private var sumMap: HashMap<String, Int>? = null
    private var mBuyDays: MutableList<Calendar>? = null
    private var start: Calendar? = null
    private var end: Calendar? = null

    private var listener: OnCalendarRangeSelectListener? = null


    init {
        initView()
    }

    private fun initView() {
        val view = View.inflate(context, R.layout.view_select_calendar_layout, this)
        setDateTitle(view)
    }

    private fun setDateTitle(view: View) {
        mCalendarView = view.findViewById(R.id.mCv)
        mCalendarView.setOnMonthChangeListener(this)
        mCalendarView.setOnCalendarInterceptListener(this)
        mCalendarView.setOnCalendarRangeSelectListener(this)

//        左边按钮
        mLeftIconTv.setOnClickListener {
            mCalendarView.scrollToPre()
        }

//        右边按钮
        mRightIconTv.setOnClickListener {
            mCalendarView.scrollToNext()
        }

        mTitleDateTv.text = "${mCalendarView.curYear}年${mCalendarView.curMonth}月"
        isShowDoubleBtn(mCalendarView.curYear, mCalendarView.curMonth)

        start = Calendar()
        end = Calendar()
    }

    /**
     *  设置日期有效范围
     *  @param maxYear 最大年份
     *  @param maxMonth 最大月份
     *  @param maxDay 最大日
     */
    fun setRange(maxYear: Int, maxMonth: Int, maxDay: Int) {
        mCalendarView.setRange(mCalendarView.curYear, mCalendarView.curMonth, mCalendarView.curDay,
                maxYear, maxMonth, maxDay)
    }

    /**
     * 设置选择日期范围
     */
    fun setSelectCalendarRange(start: Calendar, end: Calendar) {
        mCalendarView.setSelectCalendarRange(start, end)
    }

    /**
     * 获取当前年份
     */
    fun getCurYear(): Int {
        return mCalendarView.curYear
    }

    /**
     * 获取当前月份
     */
    fun getCurMonth(): Int {
        return mCalendarView.curMonth
    }

    /**
     * 获取当前日期
     */
    fun getCurDay(): Int {
        return mCalendarView.curDay
    }


    /**
     * 设置日历控件的高度
     */
    fun setCalendarHeight() {
    }

    /**
     * 隐藏选择日期的左右按钮
     */
    fun isShowDoubleBtn(year: Int, month: Int) {
        mLeftIconTv.visibility = if (mCalendarView.minRangeCalendar.year == year && mCalendarView.minRangeCalendar.month == month) View.GONE else View.VISIBLE
        mRightIconTv.visibility = if (mCalendarView.maxRangeCalendar.year == year && mCalendarView.maxRangeCalendar.month == month) View.GONE else View.VISIBLE
    }

    /**
     * 设置每日的价格
     */
    fun setPrices(houseInfo: HouseInfo, orderSum: OrderSummart? = null) {
        orderSumMap = hashMapOf()
        orderSum?.let {
            mBuyDays = mutableListOf()
            orderSum.orderSummary.forEach {
                orderSumMap!![it] = "无房"
                val cal = Calendar()
                cal.year = it.substring(0..3).toInt()
                cal.month = it.substring(4..5).toInt()
                cal.day = it.substring(6..7).toInt()
                mBuyDays!!.add(cal)
            }
        }
        val map = HashMap<String, Calendar>()
        val maxYear = mCalendarView.maxRangeCalendar.year
        val maxMonth = mCalendarView.maxRangeCalendar.month
        val maxDay = mCalendarView.maxRangeCalendar.day
        val minYear = mCalendarView.minRangeCalendar.year
        val minMonth = mCalendarView.minRangeCalendar.month
        val minDay = mCalendarView.minRangeCalendar.day

        val startTime = Calendar()
        startTime.year = minYear
        startTime.month = minMonth
        startTime.day = minDay
        val endTime = Calendar()
        endTime.year = maxYear
        endTime.month = maxMonth
        endTime.day = maxDay

        val days = arrayListOf<Calendar>()

        val date1 = Date()
        date1.time = startTime.timeInMillis
        val date2 = Date()
        date2.time = endTime.timeInMillis
        //当前日期
        val counter = java.util.Calendar.getInstance()
        counter.time = date1
        LogUtils.e("start ${counter.timeInMillis}")
        val end = java.util.Calendar.getInstance()
        end.time = date2
        LogUtils.e("end  ${end.timeInMillis}")
        while (counter.before(end) || counter == end) {
            val calendar = Calendar()
            calendar.year = counter.get(java.util.Calendar.YEAR)
            calendar.month = counter.get(java.util.Calendar.MONTH) + 1
            calendar.day = counter.get(java.util.Calendar.DAY_OF_MONTH)
            days.add(calendar)
            counter.add(java.util.Calendar.DATE, 1)
        }
        sumMap = hashMapOf()
        for (day in days) {
            map[getSchemeCalendar(day.year, day.month, day.day, DateUtil.getPrice(day.year, day.month, day.day, houseInfo)).toString()] =
                    getSchemeCalendar(day.year, day.month, day.day, DateUtil.getPrice(day.year, day.month, day.day, houseInfo))
            sumMap!!["${day.year}${day.month}${day.day}"] =
                    DateUtil.getPrice(day.year, day.month, day.day, houseInfo).toInt()
        }
        mCalendarView.setSchemeDate(map)
    }

    /**
     * 获取选中日期的价格 计算出总价
     */
    fun getSelectDatePrice(): Int {
        var price = 0
        LogUtils.e(mCalendarView.selectCalendarRange.toString())
        sumMap?.let {
            for (i in 0 until mCalendarView.selectCalendarRange.size - 1) {
                price += sumMap!!["${mCalendarView.selectCalendarRange[i].year}" +
                        "${mCalendarView.selectCalendarRange[i].month}" +
                        "${mCalendarView.selectCalendarRange[i].day}"]!!
            }
        }
        return price
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

    private fun getSchemeCalendar(year: Int, month: Int, day: Int, text: String): Calendar {
        val calendar = Calendar()
        calendar.year = year
        calendar.month = month
        calendar.day = day
//        calendar.schemeColor = color//如果单独标记颜色、则会使用这个颜色
        if (orderSumMap != null) {
            if (orderSumMap!!.containsKey(calendar.toString())) {
                calendar.scheme = "无房"
            } else {
                calendar.scheme = text
            }
        } else {
            calendar.scheme = text
        }
        calendar.addScheme(Calendar.Scheme())
        return calendar
    }

    /**
     * 清除选择日期
     */
    fun clearSelectDate() {
        start = null
        end = null
        mCalendarView.clearSelectRange()
    }

    override fun onMonthChange(year: Int, month: Int) {
        mTitleDateTv.text = "${year}年${month}月"
        isShowDoubleBtn(year, month)
    }

    override fun onCalendarIntercept(calendar: Calendar): Boolean {
        return setInterceptData(calendar)
    }

    private fun setInterceptData(calendar: Calendar): Boolean {
        orderSumMap?.let {
            if (orderSumMap!!.size > 0) {
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
                        if (calendar >= end) {
                            val map = orderSumMap!!.filterNot { res -> res.key == end.toString() }
                            return map.isEmpty() || map.containsKey(calendar.toString())
                        }
                    }
                }
                return orderSumMap?.containsKey(calendar.toString())!!
            }
        }
        return false
    }

    override fun onCalendarRangeSelect(calendar: Calendar?, isEnd: Boolean) {
        end = null
        if (isEnd.not()) {
            start = calendar
        } else {
            end = calendar
        }
        listener?.let {
            listener!!.setRangeSelectListener(calendar, isEnd)
        }
    }

    interface OnCalendarRangeSelectListener {
        fun setRangeSelectListener(calendar: Calendar?, isEnd: Boolean)
    }

    fun setCalendarRangeSelectListener(listener: OnCalendarRangeSelectListener) {
        this.listener = listener
    }


    override fun onCalendarInterceptClick(calendar: Calendar?, isClick: Boolean) {}
    override fun onCalendarSelectOutOfRange(calendar: Calendar?) {}
    override fun onSelectOutOfRange(calendar: Calendar?, isOutOfMinRange: Boolean) {}
}