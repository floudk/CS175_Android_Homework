package yfqing.cs339.hw5_todolist

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton

import android.database.sqlite.SQLiteDatabase
import android.widget.Toast

import androidx.recyclerview.widget.RecyclerView
import yfqing.cs339.hw5_todolist.db.TodoDbHelper
import androidx.recyclerview.widget.DividerItemDecoration

import androidx.recyclerview.widget.LinearLayoutManager
import yfqing.cs339.hw5_todolist.beans.Note
import yfqing.cs339.hw5_todolist.ui.NoteListAdapter
import yfqing.cs339.hw5_todolist.ui.NoteOperator
import yfqing.cs339.hw5_todolist.db.TodoContract.TodoNote

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.provider.BaseColumns
import yfqing.cs339.hw5_todolist.beans.Priority
import yfqing.cs339.hw5_todolist.beans.State
import java.lang.String
import java.util.*
import android.view.Menu
import android.view.MenuItem
import android.app.Activity
import androidx.annotation.Nullable


class MainActivity : AppCompatActivity() {

    private var REQUEST_CODE_ADD = 1002

    private var recyclerView: RecyclerView? = null
    private var notesAdapter: NoteListAdapter? = null

    private var dbHelper: TodoDbHelper? = null
    private var database: SQLiteDatabase? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val fab:FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener{
                startActivityForResult(
                    Intent(this@MainActivity, NoteActivity::class.java),
                    REQUEST_CODE_ADD
                )
//            Toast.makeText(this.applicationContext,"I click it",Toast.LENGTH_SHORT).show()
        }
        dbHelper = TodoDbHelper(this.applicationContext)
        database = dbHelper!!.writableDatabase
        recyclerView = findViewById(R.id.list_todo)

        recyclerView?.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )
        recyclerView?.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
        notesAdapter = NoteListAdapter(object : NoteOperator {
            override fun deleteNote(note: Note?) {
               this@MainActivity.deleteNote(note)
            }
            override fun updateNote(note: Note?) {
                this@MainActivity.updateNode(note)
            }
        })
        recyclerView?.adapter = notesAdapter;

        notesAdapter?.refresh(loadNotesFromDatabase());

    }
    override fun onDestroy() {
        super.onDestroy()
        database!!.close()
        database = null
        dbHelper!!.close()
        dbHelper = null
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.getItemId()
        when (id) {
            R.id.action_settings -> return true
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD
            && resultCode == RESULT_OK
        ) {
            notesAdapter!!.refresh(loadNotesFromDatabase())
        }
    }

    @SuppressLint("Range")
    private fun loadNotesFromDatabase(): List<Note>? {
        if (database == null) {
            return Collections.emptyList()
        }
        val result: MutableList<Note> = LinkedList()
        var cursor: Cursor? = null
        try {
            cursor = database!!.query(
                TodoNote.TABLE_NAME, null,
                null, null,
                null, null,
                TodoNote.COLUMN_PRIORITY + " DESC"
            )
            while (cursor.moveToNext()) {
                val id: Long = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID))
                val content: kotlin.String =
                    cursor.getString(cursor.getColumnIndex(TodoNote.COLUMN_CONTENT))
                val dateMs: Long = cursor.getLong(cursor.getColumnIndex(TodoNote.COLUMN_DATE))
                val intState: Int = cursor.getInt(cursor.getColumnIndex(TodoNote.COLUMN_STATE))
                val intPriority: Int =
                    cursor.getInt(cursor.getColumnIndex(TodoNote.COLUMN_PRIORITY))
                val note = Note(id)
                note.content=(content)
                note.date=(Date(dateMs))
                note.state=(State.from(intState))
                note.priority=(Priority.from(intPriority))
                result.add(note)
            }
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
        return result
    }


    private fun deleteNote(note: Note?) {
        if (database == null) {
            return
        }
        val rows = database!!.delete(
            TodoNote.TABLE_NAME,
            TodoNote._ID + "=?", arrayOf(String.valueOf(note?.id))
        )
        if (rows > 0) {
            notesAdapter?.refresh(loadNotesFromDatabase())
        }
    }

    private fun updateNode(note: Note?) {
        if (database == null) {
            return
        }
        val values = ContentValues()
        values.put(TodoNote.COLUMN_STATE, note?.state?.intValue)
        val rows = database!!.update(
            TodoNote.TABLE_NAME, values,
            TodoNote._ID + "=?", arrayOf(String.valueOf(note?.id))
        )
        if (rows > 0) {
            notesAdapter?.refresh(loadNotesFromDatabase())
        }
    }
}