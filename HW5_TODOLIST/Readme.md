# HW5_TodoList
Homework5 for course CS175 in SJTU.  
Use database operations to build a todo-list app.   

----  
Basically, the whole project is based on [bytedance-sjtu-android](https://github.com/bytedance-sjtu-android/Chapter6/tree/master/Homework).  
What I have done is **understanding JAVA programs** provided and **reproducing them in kotlin**, 
which actually can be done automatically by IDE in most cases.   
However, considering to use Android X
packages, there is still a lot of codes that need to be changed manually and carefully.

In general, the following work has been completed:
- [x] Created a database for application.(Details in `TodoDbHelper.kt`)
- [x] Using recyclerView to show my todo list database.(Details in `NoteListAdapter.kt`)
- [x] Create Basic UIs, including '+' Button and note related UIs.
- [x] A new activity to add new item.(Details in `NoteActivity`)
- [x] Change state or delete items and refresh database.(Details in `NoteActivity`)

This is also the first time I have completely "borrowed" project structure from other project. 
I can indeed find that the teacher's code implementation is very different from mine,
which taught me a lot.

**N.B** This homework and all resources come from *Bytedance*.
