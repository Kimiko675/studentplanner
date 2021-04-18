package com.firstapp.studentplanner

data class Subject(val id: String? = null ,val subject: String? = null, val field: String? = null, val form: Int = -1, val howLong: String? = null, val isCyclical: Boolean = true, val hour: Int = 0, val minute: Int = 0, val dayOfWeek: Int = -1, val dayStart: Int = 0, val monthStart: Int = 0, val yearStart: Int = 0, val dayEnd: Int = 0, val monthEnd: Int = 0, val yearEnd: Int = 0, val mark: Int = 0) {

}