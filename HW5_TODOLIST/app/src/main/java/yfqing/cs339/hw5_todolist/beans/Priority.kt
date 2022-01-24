package yfqing.cs339.hw5_todolist.beans

import android.graphics.Color

enum class Priority(val intValue: Int, val color: Int) {
    High(2, Color.parseColor("#A73842")), Medium(1, Color.parseColor("#3681A3")), Low(0, Color.parseColor("#ACE3AF"));
    companion object {
        fun from(intValue: Int): Priority {
            for (priority in values()) {
                if (priority.intValue == intValue) {
                    return priority
                }
            }
            return Low // default
        }
    }
}