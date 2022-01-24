package yfqing.cs175.hw1

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.security.AccessController.getContext
import android.widget.Toast

import android.view.MotionEvent

import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import yfqing.cs175.hw1.MainActivity.RecyclerItemClickListener





class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rvContacts = findViewById<View>(R.id.items) as RecyclerView
        // Initialize contacts
        val Img_files:Array<String> = arrayOf(
            "ic_bordercollie",
            "ic_germanshepherd",
            "ic_husky",
            "ic_labrador",
            "ic_mongrel1",
            "ic_mongrel2",
            "ic_shiba",
            "ic_teddy"
        )
        val Names:Array<String> = arrayOf(
            "\"Jhon\"",
            "\"Snow\"",
            "\"Night\"",
            "\"Watchman\"",
            "\"Dragon\"",
            "\"Assassin\"",
            "\"Warning\"",
            "\"Error\""
        )
        val intros:Array<String> = arrayOf(
            "A lovely puppy, not afraid of people",
            "A retired police dog,adoption requires return visit every month",
            "A beautiful puppy with complete vaccine certificates",
            "A very clever dog can even calculate addition and subtraction within ten",
            "A timid dog often hides in the corner",
            "A dog with beautiful eyes and always brings happiness",
            "A dog that can write code",
            "A little naughty and brave, very outgoing"
        )
        var contacts = Contact.createContactsList(8,Img_files,Names,intros)
        // Create adapter passing in the sample user data
        val adapter = ContactsAdapter(contacts,this@MainActivity)
        // Attach the adapter to the recyclerview to populate items
        rvContacts.adapter = adapter
        // Set layout manager to position the items
        rvContacts.layoutManager = LinearLayoutManager(this)

        val recyclerView = findViewById<RecyclerView>(R.id.items)
        recyclerView.addOnItemTouchListener(
            RecyclerItemClickListener(
                this,
                recyclerView,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        // do whatever
                        Toast.makeText(this@MainActivity, "Long Click to enter detail page", Toast.LENGTH_LONG).show()
                        //Toast.makeText(this@MainActivity,position.toString(), Toast.LENGTH_LONG).show()
                    }

                    override fun onLongItemClick(view: View?, position: Int) {
                        // do whatever
                        val intent = Intent(this@MainActivity,DetailActivity::class.java)
                        var tmp_id = this@MainActivity.resources.getIdentifier(Img_files[position], "drawable",this@MainActivity.packageName)
                        intent.putExtra("resid",tmp_id.toString())
                        intent.putExtra("dogname",Names[position])
                        var tmp_breed=Img_files[position].substring(3)
                        if(tmp_breed == "mongrel1"||tmp_breed == "mongrel2") {tmp_breed="mongrel"}
                        intent.putExtra("breed",tmp_breed)
                        startActivity(intent)
                    }
                })
        )
    }
    class RecyclerItemClickListener(
        context: Context?,
        recyclerView: RecyclerView,
        private val mListener: OnItemClickListener?
    ) :
        OnItemTouchListener {
        interface OnItemClickListener {
            fun onItemClick(view: View?, position: Int)
            fun onLongItemClick(view: View?, position: Int)
        }

        var mGestureDetector: GestureDetector
        override fun onInterceptTouchEvent(view: RecyclerView, e: MotionEvent): Boolean {
            val childView = view.findChildViewUnder(e.x, e.y)
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView))
                return true
            }
            return false
        }

        override fun onTouchEvent(view: RecyclerView, motionEvent: MotionEvent) {}
        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

        init {
            mGestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }

                override fun onLongPress(e: MotionEvent) {
                    val child = recyclerView.findChildViewUnder(e.x, e.y)
                    if (child != null && mListener != null) {
                        mListener.onLongItemClick(
                            child,
                            recyclerView.getChildAdapterPosition(child)
                        )
                    }
                }
            })
        }
    }

    class Contact(val img: String, val name: String,
                  val breed:String,val age:Int,val Intro:String) {
        companion object {
            private var lastContactId = 0
            fun createContactsList(numContacts: Int,
                                   img_files: Array<String>,names:Array<String>,intros:Array<String>): ArrayList<Contact> {
                val contacts = ArrayList<Contact>()
                for (i in 1..numContacts) {
                    var name = img_files[i-1].substring(3)
                    if(name == "mongrel1"||name == "mongrel2") {name="mongrel"}
                    contacts.add(Contact(img_files[i-1],names[i-1],name,
                        (1..3).random(),intros[i-1]))
                }
                return contacts
            }
        }
    }

    class ContactsAdapter (private val mContacts: List<Contact>,val context:Context) : RecyclerView.Adapter<ContactsAdapter.ViewHolder>()
    {
        // Provide a direct reference to each of the views within a data item
        // Used to cache the views within the item layout for fast access
        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            // Your holder should contain and initialize a member variable
            // for any view that will be set as you render a row
            val ageTextView = itemView.findViewById<TextView>(R.id.age)
            val breedTextView = itemView.findViewById<TextView>(R.id.breed)
            val nameTextView = itemView.findViewById<TextView>(R.id.name)
            val introTextView = itemView.findViewById<TextView>(R.id.intro)
            val PictureImageView = itemView.findViewById<ImageView>(R.id.picture)
        }


        // ... constructor and member variables
        // Usually involves inflating a layout from XML and returning the holder
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsAdapter.ViewHolder {
            val context = parent.context
            val inflater = LayoutInflater.from(context)
            // Inflate the custom layout
            val contactView = inflater.inflate(R.layout.dynamic_lists, parent, false)
            // Return a new holder instance
            return ViewHolder(contactView)
        }

        // Involves populating data into the item through holder
        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            //Get the data model based on position
            val contact: Contact = mContacts.get(position)
            // Set item views based on your views and data model
            val nametextView = viewHolder.nameTextView
            nametextView.text = contact.name
            val agetextView = viewHolder.ageTextView
            agetextView.text = contact.age.toString()+"-year-old"
            val breedtextView = viewHolder.breedTextView
            breedtextView.text = contact.breed
            val introtextView = viewHolder.introTextView
            introtextView.text = contact.Intro
            val image = viewHolder.PictureImageView
            Log.d("resid",contact.img)
            val id = context.resources.getIdentifier(contact.img, "drawable",context.packageName)
            Log.d("resid",id.toString())
            image.setImageResource(id)
        }

        // Returns the total count of items in the list
        override fun getItemCount(): Int {
            return mContacts.size
        }
    }
}




