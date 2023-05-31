package com.example.calendarapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.util.Calendar


lateinit var week: Week
lateinit var adapter : WeekAdapter
lateinit var editDate : TextInputEditText
lateinit var editTime : TextInputEditText

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        week = Week(ArrayList())

        //
        editDate = findViewById(R.id.editDate);
        editTime = findViewById(R.id.editTime);

        editDate.setOnClickListener { showDatePickerDialog() }
        editTime.setOnClickListener { showTimePickerDialog() }


        //

        /*week.days.add(Day("П`ятниця", "26.05.2023", ArrayList()))
        week.days.add(Day("П`ятниця", "27.05.2023", ArrayList()))
        week.days.add(Day("П`ятниця", "28.05.2023", ArrayList()))
        week.days[0].lessons.add(Lesson("Розробка мобільних додатків", "П91", "18:00-20:50", "126"))
        week.days[0].lessons.add(Lesson("Розробка мобільних додатків", "П91", "18:00-20:50", "126"))
        week.days[1].lessons.add(Lesson("Розробка мобільних додатків", "П91", "18:00-20:50", "126"))*/




        adapter = WeekAdapter(week.days)
        val weekView: RecyclerView = findViewById(R.id.weekDays)
        weekView.adapter = adapter
        weekView.layoutManager = layoutManager

        val buttonClearDays: FloatingActionButton = findViewById(R.id.buttonClearDays)
        buttonClearDays.setOnClickListener {
            showDeleteDialog()
        }


        loadDaysFromFile()

        adapter.notifyItemInserted(week.days.lastIndex)


        val fabOpen : FloatingActionButton = findViewById(R.id.fabOpenAdd)
        val bCancel : Button = findViewById(R.id.buttonCancel)
        val bAdd : Button = findViewById(R.id.buttonAdd)


        val mainView : ConstraintLayout = findViewById(R.id.scheduleLayout)
        val addView : ConstraintLayout = findViewById(R.id.addLessonLayout)
        addView.visibility = View.GONE


        fabOpen.setOnClickListener {
            mainView.visibility = View.GONE
            addView.visibility = View.VISIBLE
        }

        bCancel.setOnClickListener {
            mainView.visibility = View.VISIBLE
            addView.visibility = View.GONE
        }

        bAdd.setOnClickListener {
            val nameView : TextView = findViewById(R.id.editName)
            val groupView : TextView = findViewById(R.id.editGroup)
            val roomView : TextView = findViewById(R.id.editRoom)
            val dateView : TextView = findViewById(R.id.editDate)
            val timeView : TextView = findViewById(R.id.editTime)
            val day = week.days.find { it.date == dateView.text.toString() }
            if (day != null) {
                day.addLesson(Lesson(
                    nameView.text.toString(),
                    groupView.text.toString(),
                    timeView.text.toString(),
                    roomView.text.toString()
                ))
                adapter.notifyDataSetChanged()

                mainView.visibility = View.VISIBLE
                addView.visibility = View.GONE
            }
            else {
                val newDay = week.addDay(dateView.text.toString())
                newDay.addLesson(
                    Lesson(
                        nameView.text.toString(),
                        groupView.text.toString(),
                        timeView.text.toString(),
                        roomView.text.toString()
                    )
                )
                adapter.notifyItemInserted(newDay.position)
            }
            mainView.visibility = View.VISIBLE
            addView.visibility = View.GONE
        }

    }
    override fun onStop() {
        super.onStop()
        saveDaysToFile(false)
    }
    private fun clearDaysFile() {
        val dataFile = File(applicationContext.filesDir, "data.json")
        dataFile.delete()
    }
    private fun saveDaysToFile(newOld : Boolean) {
        if(newOld){
            val dataFile = File(applicationContext.filesDir, "data.json")
            dataFile.writeText("{}")
        }else{
            val jsonData = week.createJson()
            val dataFile = File(applicationContext.filesDir, "data.json")
            dataFile.writeText(jsonData)
        }

    }
    private fun loadDaysFromFile() {
        val dataFile = File(applicationContext.filesDir, "data.json")
        if (dataFile.exists()) {
            val jsonString = dataFile.readText()
            week.parseJson(jsonString)
        } else {
            saveDaysToFile(true)
        }
    }

    private fun showDeleteDialog() {
        val options = arrayOf("Видалити всі дні", "Видалити один день[not work]")

        AlertDialog.Builder(this)
            .setTitle("Видалення")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> deleteAllDays()
                    1 -> deleteOneDay()
                }
                adapter.notifyDataSetChanged()
            }
            .setNegativeButton("Скасувати", null)
            .show()
    }

    private fun deleteAllDays() {
        clearDaysFile()
        saveDaysToFile(false)
    }
    private fun deleteOneDay() {

    }

    private fun showDatePickerDialog() {
        val calendar: Calendar = Calendar.getInstance()
        val year: Int = calendar.get(Calendar.YEAR)
        val month: Int = calendar.get(Calendar.MONTH)
        val day: Int = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = dayOfMonth.toString() + "." + (monthOfYear + 1) + "." + year
                editDate.setText(selectedDate)
            }, year, month, day
        )

        datePickerDialog.show()
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]

        val timePickerDialog = TimePickerDialog(this,
            { _, hourOfDay, minute ->
                val selectedTime = "$hourOfDay:$minute"
                editTime.setText(selectedTime)
            }, hour, minute, true
        )

        timePickerDialog.show()
    }


}