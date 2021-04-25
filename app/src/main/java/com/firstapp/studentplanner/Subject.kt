package com.firstapp.studentplanner

import java.io.Serializable

data class Subject(val id: String? = null, val subject: String? = null, val field: String? = null, val forms: ArrayList<Form>? = null, val mark: Int = 0): Serializable {

}