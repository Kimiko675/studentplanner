package com.firstapp.studentplanner

import java.io.Serializable

class ListObject(val subjects: Subject? = null, val forms: Form? = null, val hour: Int? = 0, val minute: Int? = 0):
    Serializable {
}