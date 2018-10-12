package com.dan.mycalendardemo.weight

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import com.dan.mycalendardemo.utils.DateUtil

import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import com.haibin.calendarview.RangeMonthView

/**
 * 日历预览效果
 * Created by huanghaibin on 2018/9/13.
 */

class PreviewRangeMonthView(context: Context) : RangeMonthView(context) {


    private var mRadius: Int = 0
    private val mH: Int

    /**
     * 不可用画笔
     */
    private val mDisablePaint = Paint()

    /**
     * 价格画笔
     */
    private val mCurPriceTextPaint = Paint()
    /**
     * 选中价格的文本画笔
     */
    private val mSelectPriceTextPaint = Paint()

    /**
     * 日期选择范围之间背景颜色的画笔
     *
     * @param context
     */
    private val mDateMiddlePaint = Paint()

    /**
     * 已租的画笔
     *
     * @param context
     */
    private val mRatedPaint = Paint()


    /**
     * 周末的画笔
     */
    private val mWeekPaint = Paint()

    init {

        mOtherMonthTextPaint.isFakeBoldText = false
        mOtherMonthTextPaint.color = -0x77606061

        mCurMonthTextPaint.isFakeBoldText = false

        mSelectTextPaint.isFakeBoldText = false

        mDisablePaint.color = -0x77606061
        mDisablePaint.isAntiAlias = true
        mDisablePaint.strokeWidth = dipToPx(context, 1.5f).toFloat()
        mDisablePaint.isFakeBoldText = false
        mH = dipToPx(context, 16f)

        mCurPriceTextPaint.color = -0x1189df
        mCurPriceTextPaint.isAntiAlias = true
        mCurPriceTextPaint.textAlign = Paint.Align.CENTER
        mCurPriceTextPaint.isFakeBoldText = false
        mCurPriceTextPaint.textSize = dipToPx(context, 10f).toFloat()

        mSelectPriceTextPaint.color = -0x1
        mSelectPriceTextPaint.isAntiAlias = true
        mSelectPriceTextPaint.textAlign = Paint.Align.CENTER
        mSelectPriceTextPaint.isFakeBoldText = false
        mSelectPriceTextPaint.textSize = dipToPx(context, 10f).toFloat()


        mWeekPaint.isAntiAlias = true
        mWeekPaint.color = mSelectTextPaint.color
        mWeekPaint.textAlign = Paint.Align.CENTER
        mWeekPaint.textSize = dipToPx(context, 14f).toFloat()

        mRatedPaint.isAntiAlias = true
        mRatedPaint.textAlign = Paint.Align.CENTER
        mRatedPaint.color = -0x77606061
        mRatedPaint.isFakeBoldText = false
        mRatedPaint.textSize = dipToPx(context, 10f).toFloat()


    }


    override fun onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) / 5 * 2
        mSchemePaint.style = Paint.Style.STROKE
    }

    override fun onDrawSelected(canvas: Canvas, calendar: Calendar, x: Int, y: Int, hasScheme: Boolean,
                                isSelectedPre: Boolean, isSelectedNext: Boolean): Boolean {
        val cx = x + mItemWidth / 2
        val cy = y + mItemHeight / 2
        val isEnable = !onCalendarIntercept(calendar)

//        if (hasScheme) {
//            canvas.drawRect(x.toFloat(), (cy - mRadius).toFloat(), (x + mItemWidth).toFloat(), (cy + mRadius).toFloat(), mSelectedPaint)
//        }
        return false
    }

    override fun onDrawScheme(canvas: Canvas, calendar: Calendar, x: Int, y: Int, isSelected: Boolean) {
        val cx = x + mItemWidth / 2
        val cy = y + mItemHeight / 2
        val top = y - mItemHeight / 6

        val isInRange = isInRange(calendar)
        val isEnable = !onCalendarIntercept(calendar)
    }

    override fun onDrawText(canvas: Canvas, calendar: Calendar, x: Int, y: Int, hasScheme: Boolean, isSelected: Boolean) {
        val cx = x + mItemWidth / 2
        val cy = y + mItemHeight / 2
        val top = y - mItemHeight / 6
        val baselineY = mTextBaseLine + y

        val isInRange = isInRange(calendar)
        val isEnable = !onCalendarIntercept(calendar)
        val isSelectNext = isSelectNextCalendar(calendar)
        val isSelectPre = isSelectPreCalendar(calendar)

//        if (!isInRange) {
////            canvas.drawLine((x + mH).toFloat(), (y + mH).toFloat(), (x + mItemWidth - mH).toFloat(), (y + mItemHeight - mH).toFloat(), mDisablePaint)
//            canvas.drawLine((x + mItemWidth - mH).toFloat(), (y + mH).toFloat(), (x + mH).toFloat(), (y + mItemHeight - mH).toFloat(), mDisablePaint)
//        }
        if (isInRange) {
            if (hasScheme) {
                canvas.drawRect(x.toFloat(), (cy - mRadius).toFloat(), (x + mItemWidth).toFloat(), (cy + mRadius).toFloat(), mSelectedPaint)
                canvas.drawText(calendar.day.toString(), cx.toFloat(), mTextBaseLine + top,
                        mSelectTextPaint)
                canvas.drawText(if (calendar.scheme.toString() != "无房") "￥" + calendar.scheme.toString() else "无房", cx.toFloat(), mTextBaseLine + y.toFloat() + (mItemHeight / 10).toFloat(),
                        mSelectPriceTextPaint)
            } else {
                if (calendar.isCurrentDay) {
                    canvas.drawCircle(cx.toFloat(), cy.toFloat(), mRadius.toFloat(), mSchemePaint)
                }
                canvas.drawText(calendar.day.toString(), cx.toFloat(), baselineY,
                        if (calendar.isCurrentDay)
                            mCurDayTextPaint
                        else if (DateUtil.isWeekend(calendar)) mWeekPaint else mCurMonthTextPaint)
            }
        } else {
            canvas.drawText(calendar.day.toString(), cx.toFloat(), baselineY,
                    mOtherMonthTextPaint)
        }

    }


    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    private fun dipToPx(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
}
