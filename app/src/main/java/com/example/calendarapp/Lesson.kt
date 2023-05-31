package com.example.calendarapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
class Lesson(name:String, group:String, time:String, room:String) : Fragment(R.layout.lesson){
    private var layoutManager : RecyclerView.LayoutManager? = null
    private var adapter : DayAdapter ? = null


    private var _name : String = name
    var name : String  get(){return _name} set(value){_name = value}
    private var _group : String = group
    var group : String  get(){return _group} set(value){_group = value}
    private var _time : String = time
    var time : String   get(){return _time} set(value){_time = value}
    private var _room : String = room
    var room : String  get(){return _room} set(value){_room = value}

    var position : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        layoutManager = LinearLayoutManager(activity)
        adapter = DayAdapter();


    }

}
class LessonHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
    val lessonName: TextView = itemView.findViewById(R.id.lessonNameTextView)
    val lessonGroup : TextView = itemView.findViewById(R.id.lessonGroupTextView)
    val lessonTime : TextView = itemView.findViewById(R.id.lessonTimeTextView)
    val lessonRoom : TextView = itemView.findViewById(R.id.lessonRoomTextView)
    val buttonRemove: Button = itemView.findViewById(R.id.buttonRemove)
}