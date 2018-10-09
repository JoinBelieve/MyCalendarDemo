package com.dan.mycalendardemo.weight

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint

import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import com.haibin.calendarview.RangeMonthView

/**
 * 范围选择月视图
 * Created by huanghaibin on 2018/9/13.
 */

class CustomRangeMonthView(context: Context) : RangeMonthView(context) {


    private var mRadius: Int = 0
    private val mH: Int
    //    是否显示价格
    private var isShowPrice = false

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


    init {


        mDisablePaint.color = -0x606061
        mDisablePaint.isAntiAlias = true
        mDisablePaint.strokeWidth = dipToPx(context, 2f).toFloat()
        mDisablePaint.isFakeBoldText = false
        mH = dipToPx(context, 18f)

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


        mDateMiddlePaint.isAntiAlias = true
        mDateMiddlePaint.color = -0x771189df
        mDateMiddlePaint.style = Paint.Style.FILL

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

        if (isSelectedPre) {
            if (isSelectedNext) {
                canvas.drawRect(x.toFloat(), (cy - mRadius).toFloat(), (x + mItemWidth).toFloat(), (cy + mRadius).toFloat(), mDateMiddlePaint)
            } else {//最后一个，the last
                canvas.drawRect(x.toFloat(), (cy - mRadius).toFloat(), (x + mItemWidth).toFloat(), (cy + mRadius).toFloat(), mSelectedPaint)
                //                canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
            }
        } else {
            if (isSelectedNext) {
                canvas.drawRect(cx.toFloat(), (cy - mRadius).toFloat(), (x + mItemWidth).toFloat(), (cy + mRadius).toFloat(), mSelectedPaint)
            }
            canvas.drawRect(x.toFloat(), (cy - mRadius).toFloat(), (x + mItemWidth).toFloat(), (cy + mRadius).toFloat(), mSelectedPaint)
            //            canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
        }

        if (isEnable) {
            canvas.drawRect(x.toFloat(), (cy - mRadius).toFloat(), (x + mItemWidth).toFloat(), (cy + mRadius).toFloat(), mSelectedPaint)
        }

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


        if (!isInRange) {
            isShowPrice = false
            canvas.drawLine((x + mH).toFloat(), (y + mH).toFloat(), (x + mItemWidth - mH).toFloat(), (y + mItemHeight - mH).toFloat(), mDisablePaint)
        } else {
            isShowPrice = true
        }

        if (isSelected) {
            //            选择状态
            if (isSelectPre) {
                if (!isSelectNext) {
                    //                    最后一个
                    canvas.drawText(if (calendar.isCurrentDay) "今" else calendar.day.toString(),
                            cx.toFloat(),
                            mTextBaseLine + top,
                            mSelectTextPaint)
                    canvas.drawText("离店", cx.toFloat(), mTextBaseLine + y.toFloat() + (mItemHeight / 10).toFloat(),
                            mSelectPriceTextPaint)
                } else {
                    //                    中间的
                    if (hasScheme) {
                        canvas.drawText(if (calendar.isCurrentDay) "今" else calendar.day.toString(),
                                cx.toFloat(),
                                mTextBaseLine + top,
                                mSelectTextPaint)
                        canvas.drawText("￥" + calendar.scheme.toString(), cx.toFloat(), mTextBaseLine + y.toFloat() + (mItemHeight / 10).toFloat(),
                                mSelectPriceTextPaint)
                    } else {
                        //                        没有价格
                        canvas.drawText(calendar.day.toString(),
                                cx.toFloat(),
                                baselineY,
                                mSelectTextPaint)
                    }
                }
            } else {
                //                开始
                canvas.drawText(if (calendar.isCurrentDay) "今" else calendar.day.toString(),
                        cx.toFloat(),
                        mTextBaseLine + top,
                        mSelectTextPaint)
                canvas.drawText("入住", cx.toFloat(), mTextBaseLine + y.toFloat() + (mItemHeight / 10).toFloat(),
                        mSelectPriceTextPaint)
            }
        } else if (hasScheme) {
            canvas.drawText(if (calendar.isCurrentDay) "今" else calendar.day.toString(), cx.toFloat(), mTextBaseLine + top,
                    if (calendar.isCurrentDay)
                        mCurDayTextPaint
                    else if (calendar.isCurrentMonth && isInRange && isEnable) mCurMonthTextPaint else mOtherMonthTextPaint)
            canvas.drawText(if (calendar.scheme.toString() != "无房") "￥" + calendar.scheme.toString() else "无房", cx.toFloat(), mTextBaseLine + y.toFloat() + (mItemHeight / 10).toFloat(),
                    if (calendar.isCurrentMonth && isInRange && isEnable) mCurPriceTextPaint else mRatedPaint)
        } else {
            if (calendar.isCurrentDay) {
                canvas.drawCircle(cx.toFloat(), cy.toFloat(), mRadius.toFloat(), mSchemePaint)
            }

            canvas.drawText(if (calendar.isCurrentDay) "今" else calendar.day.toString(), cx.toFloat(), baselineY,
                    if (calendar.isCurrentDay)
                        mCurDayTextPaint
                    else if (calendar.isCurrentMonth && isInRange && isEnable) mCurMonthTextPaint else mOtherMonthTextPaint)
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
