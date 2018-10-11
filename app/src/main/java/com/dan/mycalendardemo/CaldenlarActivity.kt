package com.dan.mycalendardemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.blankj.utilcode.util.LogUtils
import com.dan.mycalendardemo.entry.HouseInfo
import com.dan.mycalendardemo.entry.OrderSummart
import com.dan.mycalendardemo.entry.Special
import com.dan.mycalendardemo.weight.SelectCalendarView
import com.haibin.calendarview.Calendar
import kotlinx.android.synthetic.main.activity_caldenlar.*
import org.jetbrains.anko.toast

class CaldenlarActivity : AppCompatActivity(), SelectCalendarView.OnCalendarRangeSelectListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_caldenlar)
        initView()
    }

    private fun initView() {
        mCalendarView.setRange(mCalendarView.getCurYear(), 12, 20)
        mCalendarView.setPrices(HouseInfo(listOf(100, 100, 200, 300, 400, 500, 600), listOf(Special("20181010", 300)
                , Special("20181011", 2000), Special("20181012", 2500))))
        mCalendarView.setCalendarRangeSelectListener(this)

        mClearBtn.setOnClickListener {
            mCalendarView.clearSelectDate()
        }

        mTotalBtn.setOnClickListener {
            toast("${mCalendarView.getSelectDatePrice()}")
        }

        mSelectBtn.setOnClickListener {
            val startTime = java.util.Calendar.getInstance()
            startTime.set(java.util.Calendar.YEAR, 2018)
            startTime.set(java.util.Calendar.MONTH, 10)
            startTime.set(java.util.Calendar.DAY_OF_MONTH, 20)
            val endTime = java.util.Calendar.getInstance()
            endTime.set(java.util.Calendar.YEAR, 2018)
            endTime.set(java.util.Calendar.MONTH, 10)
            endTime.set(java.util.Calendar.DAY_OF_MONTH, 25)
            var start = Calendar()
            start.year = startTime.get(java.util.Calendar.YEAR)
            start.month = startTime.get(java.util.Calendar.MONTH)
            start.day = startTime.get(java.util.Calendar.DAY_OF_MONTH)
            var end = Calendar()
            end.year = endTime.get(java.util.Calendar.YEAR)
            end.month = endTime.get(java.util.Calendar.MONTH)
            end.day = endTime.get(java.util.Calendar.DAY_OF_MONTH)

            mCalendarView.setSelectCalendarRange(start, end)
        }
    }

    override fun setRangeSelectListener(calendar: Calendar?, isEnd: Boolean) {
        if (isEnd) {
            LogUtils.e("$calendar")
        } else {
            LogUtils.e("$calendar")
        }
    }
}
