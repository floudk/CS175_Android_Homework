package yfqing.cs339.hw6_player

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initButton()
    }
    private fun initButton() {
        open(R.id.image, PictureActivity::class.java)
        open(R.id.video, VideoActivity::class.java)
    }
    private fun open(buttonId: Int, clz: Class<*>) {
        findViewById<View>(buttonId).setOnClickListener { startActivity(Intent(this@MainActivity, clz)) }
    }
}