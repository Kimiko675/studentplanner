package com.firstapp.studentplanner

data class Subject(val subject: String? = null, val field: String? = null, val form: String? = null, val howLong: String? = null, val isCyclical: Boolean = true, val hour: Int = 0, val minute: Int = 0, val dayOfWeek: String? = null, val dayStart: Int = 0, val monthStart: Int = 0, val yearStart: Int = 0, val dayEnd: Int = 0, val monthEnd: Int = 0, val yearEnd: Int = 0, val mark: Int = 0) {

}