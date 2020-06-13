package com.example.taskscheduler

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import java.io.Serializable

class Tasks(id: Int, text: String, description: String,
            hour: Int, min: Int, done: Boolean) : Serializable {
    var id = ObservableInt()
    var text = ObservableField<String>()
    var description = ObservableField<String>()
    var hour = ObservableInt()
    var min = ObservableInt()
    var done = ObservableBoolean()

    init {
        this.id.set(id)
        this.text.set(text)
        this.description.set(description)
        this.hour.set(hour)
        this.min.set(min)
        this.done.set(done)
    }
}