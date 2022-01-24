package yfqing.cs339.hw5_todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.database.sqlite.SQLiteDatabase
import android.widget.Button
import yfqing.cs339.hw5_todolist.R
import yfqing.cs339.hw5_todolist.db.TodoDbHelper
import androidx.appcompat.widget.AppCompatRadioButton
import android.widget.RadioGroup
import android.widget.EditText
import android.widget.Toast
import android.content.Context
import android.text.TextUtils
import android.view.inputmethod.InputMethodManager
import yfqing.cs339.hw5_todolist.db.TodoContract.TodoNote
import android.content.ContentValues
import android.view.View
import yfqing.cs339.hw5_todolist.beans.Priority
import yfqing.cs339.hw5_todolist.beans.State




class NoteActivity : AppCompatActivity() {
    private var editText: EditText? = null
    private var addBtn: Button? = null
    private var radioGroup: RadioGroup? = null
    private var lowRadio: AppCompatRadioButton? = null

    private var dbHelper: TodoDbHelper? = null
    private var database: SQLiteDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        setTitle(R.string.take_a_note);
        dbHelper = TodoDbHelper(this)
        database = dbHelper!!.writableDatabase

        editText = findViewById(R.id.edit_text)

        editText?.setFocusable(true)
        editText?.requestFocus()

        val inputManager: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (inputManager != null) {
            inputManager.showSoftInput(editText, 0)
        }
        radioGroup = findViewById(R.id.radio_group)
        lowRadio = findViewById(R.id.btn_low)
        lowRadio?.isChecked = true

        addBtn = findViewById(R.id.btn_add)

        addBtn?.setOnClickListener{
            val content: CharSequence = this.editText?.text.toString()
            if (TextUtils.isEmpty(content)) {
                Toast.makeText(
                    this@NoteActivity,
                    "No content to add", Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            val succeed: Boolean = saveNote2Database(
                content.toString().trim { it <= ' ' },
                getSelectedPriority()
            )
            if (succeed) {
                Toast.makeText(
                    this@NoteActivity,
                    "Note added", Toast.LENGTH_SHORT
                ).show()
                setResult(RESULT_OK)
            } else {
                Toast.makeText(
                    this@NoteActivity,
                    "Error", Toast.LENGTH_SHORT
                ).show()
            }
            finish()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        database!!.close()
        database = null
        dbHelper!!.close()
        dbHelper = null
    }
    private fun saveNote2Database(content: String, priority: Priority): Boolean {
        if (database == null || TextUtils.isEmpty(content)) {
            return false
        }
        val values = ContentValues()
        values.put(TodoNote.COLUMN_CONTENT, content)
        values.put(TodoNote.COLUMN_STATE, State.TODO.intValue)
        values.put(TodoNote.COLUMN_DATE, System.currentTimeMillis())
        values.put(TodoNote.COLUMN_PRIORITY, priority.intValue)
        val rowId = database!!.insert(TodoNote.TABLE_NAME, null, values)
        return rowId != -1L
    }

    private fun getSelectedPriority(): Priority {
        return when (radioGroup!!.checkedRadioButtonId) {
            (R.id.btn_high) -> Priority.High
            (R.id.btn_medium) -> Priority.Medium
            else -> Priority.Low
        }
    }

}