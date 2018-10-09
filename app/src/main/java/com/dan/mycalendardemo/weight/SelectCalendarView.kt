package com.dan.mycalendardemo.weight

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.dan.mycalendardemo.R
import kotlinx.android.synthetic.main.view_select_calendar_layout.view.*

class SelectCalendarView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    init {
        initView()
    }

    private fun initView() {
        View.inflate(context, R.layout.view_select_calendar_layout, this)
        setDateTitle()
    }

    private fun setDateTitle() {
        
    }
}