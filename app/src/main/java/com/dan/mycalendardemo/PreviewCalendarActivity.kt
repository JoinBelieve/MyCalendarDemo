package com.dan.mycalendardemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.dan.mycalendardemo.entry.Detail
import com.dan.mycalendardemo.weight.PreviewRangeMonthView
import kotlinx.android.synthetic.main.activity_preview_calendar.*

class PreviewCalendarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview_calendar)
        initView()
    }

    private fun initView() {
        mCalendarView.setMonthView(PreviewRangeMonthView::class.java)
        val details = mutableListOf(
                Detail("20181020", "weekday", 5, 200),
                Detail("20181021", "weekday", 5, 300)
        )
        mCalendarView.setPrices(details)
    }

}
