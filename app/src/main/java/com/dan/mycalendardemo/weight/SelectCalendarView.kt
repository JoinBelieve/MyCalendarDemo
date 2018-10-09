package com.dan.mycalendardemo.weight

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.dan.mycalendardemo.R
import com.haibin.calendarview.CalendarView
import kotlinx.android.synthetic.main.view_select_calendar_layout.view.*

class SelectCalendarView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr)
        , CalendarView.OnMonthChangeListener {


    private lateinit var mCalendarView: CalendarView


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
        mCalendarView
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
    

    override fun onMonthChange(year: Int, month: Int) {
        mTitleDateTv.text = "${year}年${month}月"
        isShowDoubleBtn(year, month)
    }


}