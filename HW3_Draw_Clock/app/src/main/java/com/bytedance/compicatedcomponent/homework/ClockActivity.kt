package com.bytedance.compicatedcomponent.homework

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.bytedance.compicatedcomponent.R

/**
 *  author : neo
 *  time   : 2021/10/30
 *  desc   :
 */
class ClockActivity : Activity() {
    val mainHandler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val thread: Thread = object : Thread() {
//            override fun run() {
//                try {
//                    while (!this.isInterrupted) {
//                        sleep(1000)
//                        runOnUiThread {
//                            // update TextView here!
//                            setContentView(R.layout.activity_clock)
//                        }
//                    }
//                } catch (e: InterruptedException) {
//                }
//            }
//        }
//        thread.start()
        mainHandler.post(object : Runnable {
            override fun run() {
                setContentView(R.layout.activity_clock)
                mainHandler.postDelayed(this, 1000)
            }
        })
    }
}