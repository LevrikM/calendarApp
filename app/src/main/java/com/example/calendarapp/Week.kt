package com.example.calendarapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar

class Week (days : ArrayList<Day>) : Fragment(R.layout.activity_main){
    var days : ArrayList<Day> = days

    fun parseJson(file : String){
        val obj = JSONObject(file)
        val week = obj.getJSONObject("week")
        val daysArray = week.getJSONArray("days")

        for (i in 0 until daysArray.length())
        {
            val dayObj = daysArray.getJSONObject(i)
            val name = dayObj.getString("name")
            val date = dayObj.getString("date")

            val lessonsArray = dayObj.getJSONArray("lessons")
            val lessons = ArrayList<Lesson>()
            for (j in 0 until lessonsArray.length())
            {
                val lessonObj = lessonsArray.getJSONObject(j)
                val lessonName = lessonObj.getString("name")
                val lessonGroup = lessonObj.getString("group")
                val lessonTime = lessonObj.getString("time")
                val lessonRoom = lessonObj.getString("room")
                val lesson = Lesson(lessonName, lessonGroup, lessonTime, lessonRoom)
                lessons.add(lesson)
            }

            val dayClass = Day(name, date, lessons)
            days.add(dayClass)
        }
    }

    fun createJson(): String {
        val jsonObject = JSONObject()
        val weekObject = JSONObject()
        val daysArray = JSONArray()

        for (day in week.days) {
            val dayObject = JSONObject()
            dayObject.put("name", day.name)
            dayObject.put("date", day.date)

            val lessonsArray = JSONArray()
            for (lesson in day.lessons) {
                val lessonObject = JSONObject()
                lessonObject.put("name", lesson.name)
                lessonObject.put("group", lesson.group)
                lessonObject.put("time", lesson.time)
                lessonObject.put("room", lesson.room)
                lessonsArray.put(lessonObject)
            }
            dayObject.put("lessons", lessonsArray)
            daysArray.put(dayObject)
        }

        weekObject.put("days", daysArray)
        jsonObject.put("week", weekObject)

        return jsonObject.toString()
    }


    fun addDay(date : String) : Day{
        val calendar = Calendar.getInstance()
        calendar.time = SimpleDateFormat("dd.MM.yyyy").parse(date)

        val dayOfWeek = calendar[Calendar.DAY_OF_WEEK]
        val dayName = getDayName(dayOfWeek)

        val day = Day(dayName, date, ArrayList())
        days.add(day)
        days.sortBy { it.date }

        for(i in 0 until days.size){
            days[i].position = i
        }

        return day
    }

    private fun getDayName(dayOfWeek: Int): String {
        return when (dayOfWeek) {
            Calendar.MONDAY -> "Понеділок"
            Calendar.TUESDAY -> "Вівторок"
            Calendar.WEDNESDAY -> "Середа"
            Calendar.THURSDAY -> "Четвер"
            Calendar.FRIDAY -> "П'ятниця"
            Calendar.SATURDAY -> "Субота"
            Calendar.SUNDAY -> "Неділя"
            else -> "Помилка"
        }
    }
}


class WeekAdapter(private val daysList : ArrayList<Day>) : RecyclerView.Adapter<DayHolder>() {
    lateinit var parent: ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayHolder {
        this.parent = parent
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.day, parent, false)
        return DayHolder(view)
    }

    override fun onBindViewHolder(holder: DayHolder, position: Int) {
        val day = daysList[position]
        day.position = position
        holder.dayName.text = day.name
        holder.dayDate.text = day.date
        day.dayView = holder.itemView

        val adapter2 = DayAdapter()

        adapter2.lessons = week.days[position].lessons

        holder.dayLessons.adapter = adapter2
        holder.dayLessons.layoutManager = LinearLayoutManager(parent.context)

        adapter2.notifyItemInserted(week.days[position].lessons.lastIndex)
        for (i in position + 1 until daysList.size) {
            daysList[i].position += 1
        }
    }
    override fun getItemCount(): Int {
        return daysList.size
    }
}