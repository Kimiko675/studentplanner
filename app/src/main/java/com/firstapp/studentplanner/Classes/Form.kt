package com.firstapp.studentplanner.Classes

import java.io.Serializable

class Form(var form: Int = -1, var howLong: String = "", var hour: Int = -1, var minute: Int = -1, var dayOfWeek: Int = -1, var dayStart: Int = -1, var monthStart: Int = -1, var yearStart: Int = -1, var dayEnd: Int = -1, var monthEnd: Int = -1, var yearEnd: Int = -1, val mark: Int = 0): Serializable {
}