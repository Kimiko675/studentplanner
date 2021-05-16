package com.firstapp.studentplanner

import java.io.Serializable

data class Homework(val id: String ="", val title: String = "", val description: String = "", val subject: String = "", val subjectId: String = "", val day: Int = 0, val month: Int = 0, val year: Int = 0): Serializable {
}