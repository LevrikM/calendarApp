package com.example.calendarapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class Day(name:String, date:String, lessons: ArrayList<Lesson>) : Fragment(R.layout.day){
    private var _name: String = name
    var name: String        get(){ return _name }
    set(value) { _name = value }
    private var _date: String = date
    var date: String        get(){ return _date }
    set(value) { _date = value }


    var lessons: ArrayList<Lesson> = lessons

    fun addLesson(lesson: Lesson) {
        lessons.add(lesson)
        lessons.sortBy { it.time }
    }

    lateinit var dayView : View
    var position : Int = 0
}

class DayAdapter(private val lessonList: ArrayList<Lesson>) : RecyclerView.Adapter<LessonHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.lesson, parent, false)
        return LessonHolder(view)
    }

    override fun onBindViewHolder(holder: LessonHolder, position: Int) {
        val lesson = lessonList[position]
        lesson.position = position
        holder.lessonName.text = lesson.name
        holder.lessonTime.text = lesson.time
        holder.lessonGroup.text = lesson.group
        holder.lessonRoom.text = lesson.room

        for (i in position until lessonList.size){
            lessonList[i].position += 1
        }
    }

    override fun getItemCount(): Int {
        return lessonList.size
    }
}

class DayHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
    val dayName : TextView = itemView.findViewById(R.id.weekDayName)
    val dayDate : TextView = itemView.findViewById(R.id.weekDayDate)
    val dayLessons : RecyclerView = itemView.findViewById(R.id.dayLessons)
}
