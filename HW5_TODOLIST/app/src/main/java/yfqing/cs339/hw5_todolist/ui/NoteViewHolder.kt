package yfqing.cs339.hw5_todolist.ui

import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import yfqing.cs339.hw5_todolist.R
import yfqing.cs339.hw5_todolist.beans.Note
import yfqing.cs339.hw5_todolist.beans.State
import java.text.SimpleDateFormat
import java.util.*

class NoteViewHolder(itemView: View, private val operator: NoteOperator) :
    RecyclerView.ViewHolder(itemView) {
    private val checkBox: CheckBox = itemView.findViewById(R.id.checkbox)
    private val contentText: TextView = itemView.findViewById(R.id.text_content)
    private val dateText: TextView = itemView.findViewById(R.id.text_date)
    private val deleteBtn: View = itemView.findViewById(R.id.btn_delete)

    fun bind(note: Note) {
        contentText.text = note.content
        dateText.setText(SIMPLE_DATE_FORMAT.format(note.date))
        checkBox.setOnCheckedChangeListener(null)
        checkBox.isChecked =( note.state == State.DONE)
        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            note.state=(if (isChecked) State.DONE else State.TODO)
            operator.updateNote(note)
        }
        deleteBtn.setOnClickListener{
            operator.deleteNote(note)

        }
        if (note.state === State.DONE) {
            contentText.setTextColor(Color.GRAY)
            contentText.paintFlags = contentText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            contentText.setTextColor(Color.BLACK)
            contentText.paintFlags = contentText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
        note.priority?.color?.let { itemView.setBackgroundColor(it) }
    }

    companion object {
        private val SIMPLE_DATE_FORMAT: SimpleDateFormat =
            SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH)
    }

}