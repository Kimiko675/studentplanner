package com.firstapp.studentplanner

import com.firstapp.studentplanner.Classes.Achievement
import com.firstapp.studentplanner.Classes.Homework

 interface GetPickedTime {
     fun getTime(hour: Int, minute: Int)
     fun getDayStart(dayStart: Int, monthStart: Int, yearStart: Int)
     fun getDayEnd(dayEnd: Int, monthEnd: Int, yearEnd: Int)
}

 interface GetPickedMark {
    fun getMark(mark: Float)
}

interface GetAchievement{
    fun getAchievement(achievement: Achievement)
}
interface ConvertToAchievement{
    fun convertToAchievement(achievement: Achievement, subjectId: String)
}


interface GetHomework{
    fun getHomework(homework: Homework)
}

 interface GetPickedDeadline {
     fun getDay(dayEnd: Int, monthEnd: Int, yearEnd: Int)
}