package com.firstapp.studentplanner

import java.time.Month
import java.time.Year

public interface GetPickedTime {
    public fun getTime(hour: Int, minute: Int)
    public fun getDayStart(dayStart: Int, monthStart: Int, yearStart: Int)
    public fun getDayEnd(dayEnd: Int, monthEnd: Int, yearEnd: Int)
}