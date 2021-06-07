package com.firstapp.studentplanner

import com.firstapp.studentplanner.Classes.*

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

interface OnAchievementItemClickListener{
    fun onDeleteClick(achievement: Achievement)
}

interface OnDayItemClickListener{
    fun onItemClick(day_object: DayObject, position: Int)
}

interface OnFieldItemClickListener{
    fun onItemClick(field: String)
    fun onDeleteClick(field: String)
}

interface OnFormItemClickListener{
    fun onDeleteClick(form: Form)
    fun updateFields(): ArrayList<String>
    fun showTimePicker()
    fun showDayStartPicker()
    fun showDayEndPicker()
    fun getPosition(position: Int)
    fun sendFormInfo(info: Int)
    fun sendDayOfWeekInfo(info: Int)
    fun sendHowLong(info: String)
    fun sendPickedTime(hour: Int, minute: Int)
}

interface OnHomeworkItemClickListener {
    fun onDeleteHomeworkClick(homework: Homework)
    fun onCompleteHomeworkClick(homework: Homework)
    fun onItemClick(homework: Homework, position: Int)
}

interface OnSubjectItemClickListener{
    fun onItemClick(subjects: Subject, position: Int)
    fun onEditClick(subjects: Subject)
    fun onDeleteClick(id: String)
}

interface OnSubjectMarkItemClickListener {
    fun onItemClick(subjects: Subject, position: Int)
}

interface OnSubItemClickListener{
    fun onItemClick(listObject: ListObject, position: Int)
}