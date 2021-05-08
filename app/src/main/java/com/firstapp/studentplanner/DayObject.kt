package com.firstapp.studentplanner

import java.io.Serializable

class DayObject(val day: String? = null, val size: Int? = 0, val list_object1: HourObject? = null, val list_object2: HourObject? = null):
        Serializable {
}
