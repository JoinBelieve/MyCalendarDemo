package com.dan.mycalendardemo.entry

import java.util.*

data class HouseInfo(
        val weekdayPrice: List<Int>,
        val special: List<Special>
)

data class Special(
        val day: String,
        val price: Int
)

