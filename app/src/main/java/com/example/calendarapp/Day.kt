package com.example.calendarapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

class DayAdapter: RecyclerView.Adapter<LessonHolder>() {

    var lessons : ArrayList<Lesson>

    init {
        lessons = ArrayList()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.lesson, parent, false)

        val buttonRemove: Button = view.findViewById(R.id.buttonRemove)

        if(buttonRemove.visibility == View.VISIBLE){
            buttonRemove.visibility = View.GONE
        }

        view.setOnClickListener{
            if(buttonRemove.visibility != View.VISIBLE){
                buttonRemove.visibility = View.VISIBLE
            }
            else{
                buttonRemove.visibility = View.GONE
            }
        }




        return LessonHolder(view)
    }

    override fun onBindViewHolder(holder: LessonHolder, position: Int) {
        holder.lessonName.text = lessons[position].name
        holder.lessonTime.text = lessons[position].time
        holder.lessonGroup.text = lessons[position].group
        holder.lessonRoom.text = lessons[position].room
        holder.buttonRemove.setOnClickListener {
            removeLessonAtPosition(holder.adapterPosition)
        }



        for (i in position until lessons.size){
            lessons[i].position += 1
        }


    }

    override fun getItemCount(): Int {
        return lessons.size
    }

    private fun removeLessonAtPosition(position: Int) {
        lessons.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, lessons.size)
    }
}

class DayHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
    val dayName : TextView = itemView.findViewById(R.id.weekDayName)
    val dayDate : TextView = itemView.findViewById(R.id.weekDayDate)
    val dayLessons : RecyclerView = itemView.findViewById(R.id.dayLessons)
}
