package yfqing.cs175.hw1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val pic = this.findViewById<ImageView>(R.id.detail_img)
        //Toast.makeText(this, intent.extras?.getString("resid"), Toast.LENGTH_LONG).show()
        pic.setImageResource(intent.extras?.getString("resid")!!.toInt())
        this.findViewById<TextView>(R.id.detail_name).setText(intent.extras?.getString("dogname"))
        var str_intro:String= "Breed:"+intent.extras?.getString("breed")+"\n\n"
        str_intro += "Source:The newly born baby in the original owner's family is allergic to hair, so it has to be transferred and has all epidemic prevention related certificates\n\n"
        str_intro += "Conditions for application for adoption:\n"
        str_intro += "The applicant must be at least 18 years old and legally register the dog according to the regulations of the local government.\n"
        str_intro += "If you are sure to adopt this dog, please contact us with 54749110xxxx :)\n"
        this.findViewById<TextView>(R.id.detail_intro).setText(str_intro)
    }
}