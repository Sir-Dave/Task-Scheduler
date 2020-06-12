package com.example.taskscheduler

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import java.io.Serializable

class Tasks(id: Int, text: String, description: String,
            created: String, done: Boolean) : Serializable {
    var id = ObservableInt()
    var text = ObservableField<String>()
    var description = ObservableField<String>()
    var created = ObservableField<String>()
    var done = ObservableBoolean()

    init {
        this.id.set(id)
        this.text.set(text)
        this.description.set(description)
        this.created.set(created)
        this.done.set(done)
    }
}