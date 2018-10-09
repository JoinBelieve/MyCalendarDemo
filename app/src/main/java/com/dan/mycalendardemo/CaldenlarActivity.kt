package com.dan.mycalendardemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_caldenlar.*

class CaldenlarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_caldenlar)
        initView()
    }

    private fun initView() {
        mCalendarView.setRange(mCalendarView.getCurYear(), 12, 20)
    }
}
