package yfqing.cs339.hw5_todolist.beans

import android.graphics.Color
import java.util.*


class Note(val id: Long) {
    var date: Date? = null
    var state: State? = null
    var content: String? = null
    var priority: Priority? = null
}

