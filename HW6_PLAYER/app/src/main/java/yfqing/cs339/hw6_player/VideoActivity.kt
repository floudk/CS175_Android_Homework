package yfqing.cs339.hw6_player

import android.content.res.Configuration
import android.graphics.PixelFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_video.*
import android.media.MediaPlayer

import android.media.MediaPlayer.OnPreparedListener

import android.os.AsyncTask
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import org.w3c.dom.Text
import java.lang.Exception
import android.view.MotionEvent
import android.view.View


class VideoActivity : AppCompatActivity() {
    private lateinit var totaltime:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_video)


        val mRunnable = Runnable {
            run {
                do {
                    var current = 0
                    var tm="00:00/00:00"
                    if(videoView!=null){
                        try {
                            current = videoView.currentPosition
                            totaltime = getVideoTime(videoView.duration.toLong()).toString()
                            tm = getVideoTime(
                                videoView.currentPosition.toLong()
                            ).toString() +
                                    " / " +   totaltime
                        }catch (e: Exception){

                        }
                    }

                    try {
//                        Log.e("time",tm)
                        runOnUiThread {
                            Progressbar.progress = ((current * 100 / videoView.duration) as Int)
                            timeText.text = tm
                        }
                        if (Progressbar.getProgress() >= 100) {
                            break
                        }
                    } catch (e: Exception) {

                    }
                } while (Progressbar!=null&&Progressbar.progress <= 100)
            }
        }

        Progressbar.visibility = ProgressBar.GONE
        timeText.visibility = TextView.GONE
        var initialthread =true
        buttonPlay.setOnClickListener {
            if(!videoView.isPlaying){
                Progressbar.visibility = ProgressBar.VISIBLE
                timeText.visibility=TextView.VISIBLE
                videoView.start()
            }
            if(initialthread){
                Thread(mRunnable).start()
                initialthread=false
            }

        }
        buttonPause.setOnClickListener {
            if(videoView.isPlaying)
                videoView.pause()
//            videoView.suspend()
//            Thread(mRunnable).stop()
        }

        buttonReload.setOnClickListener {

            Thread(mRunnable).interrupt()
            videoView.resume()
            videoView.visibility = VideoView.GONE
            videoView.visibility =  VideoView.VISIBLE
            Progressbar.visibility = ProgressBar.GONE
            timeText.visibility = TextView.GONE
            Progressbar.progress = 0
            timeText.text="00:00/00:00"
            initialthread = true
        }

        Progressbar.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                if (event.action == MotionEvent.ACTION_DOWN) {
                    Log.e("Touch coordinates : " , event.x.toString() + "," + event.y.toString())
                    Log.e("ProgressBar : " , (Progressbar.x+Progressbar.width).toString() + "," + Progressbar.y.toString())
                    val pos = event.x/(Progressbar.x+Progressbar.width)
                    val new_time = pos * videoView.duration as Int
                    videoView.seekTo(new_time.toInt())
                }
                return true
            }
        })


        videoView.holder.setFormat(PixelFormat.TRANSPARENT)
        videoView.setZOrderOnTop(true)
        videoView.setVideoPath(getVideoPath(R.raw.hutao))
        initialProcessBar()

    }
    private fun getVideoPath(resId: Int): String {
        return "android.resource://" + this.packageName + "/" + resId
    }
    private fun initialProcessBar(){
        Progressbar.progress = 0
        Progressbar.max = 100
    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // stuff for set video to proper position..
        newConfig
    }
    fun getVideoTime(ms: Long): String? {
        var ms = ms
        ms /= 1000

        return  ""+ms % 3600 / 60 + ":" + ms % 3600 % 60
    }

}