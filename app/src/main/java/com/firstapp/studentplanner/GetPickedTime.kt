package com.firstapp.studentplanner

public interface GetPickedTime {
    public fun getTime(hour: Int, minute: Int)
    public fun getDayStart(dayStart: Int, monthStart: Int, yearStart: Int)
    public fun getDayEnd(dayEnd: Int, monthEnd: Int, yearEnd: Int)
}

public interface GetPickedMark {
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

public interface GetPickedDeadline {
    public fun getDay(dayEnd: Int, monthEnd: Int, yearEnd: Int)
}