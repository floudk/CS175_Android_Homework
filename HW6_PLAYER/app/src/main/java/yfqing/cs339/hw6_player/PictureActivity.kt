package yfqing.cs339.hw6_player

import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_picture.*
import java.util.ArrayList
import yfqing.cs339.hw6_player.R.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View.OnTouchListener
import android.view.ViewParent
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_image_item.view.*


//https://c-ssl.duitang.com/uploads/item/201906/11/20190611135736_fsdsh.thumb.1000_0.gif
class PictureActivity : AppCompatActivity() {
    private val pages: MutableList<View> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)
        view_pager.post {
            loadNetImage(view_pager.width, view_pager.height,"https://imgres.iefans.net/iefans/16/78609-202007170504135f10c0cdca72b.jpg")
            loadNetGifImage("https://c-ssl.duitang.com/uploads/item/201906/11/20190611135736_fsdsh.thumb.1000_0.gif")
            val adapter = ViewAdapter()
            adapter.setDatas(pages)
            view_pager.adapter = adapter


            view_pager.setOnTouchListener(object : OnTouchListener {
                //记录上一次两个手指的位置
                var origFirstPoint = Point()
                var oriSecondPoint = Point()
                override fun onTouch(v: View, event: MotionEvent): Boolean {
                    //以index为0和1的手指进行缩放
                    val action = event.actionMasked
                    var x = event.x.toInt()
                    var y = event.y.toInt()
                    val view = pages[ view_pager.currentItem]
                    val img = view.findViewById<ImageView>(id.image)
                    val matrix = Matrix()
                    when (action) {
                        MotionEvent.ACTION_DOWN ->
                            img.scaleType = ImageView.ScaleType.MATRIX
                        MotionEvent.ACTION_POINTER_DOWN -> {
                            //当第二根手指按下时，记录两个点的位置
                            origFirstPoint[x] = y
                            x = event.getX(1).toInt()
                            y = event.getY(1).toInt()
                            oriSecondPoint[x] = y
                        }
                        MotionEvent.ACTION_MOVE -> if (event.pointerCount >= 2) {
                            val x1 = event.getX(1).toInt()
                            val y1 = event.getY(1).toInt()
                            //获取当前两点的距离
                            val dis: Int = getDistance(x, y, x1, y1)
                            //获取上一次两点距离
                            val distance: Int = getDistance(
                                origFirstPoint.x,
                                origFirstPoint.y,
                                oriSecondPoint.x,
                                oriSecondPoint.y
                            )
                            val scale = dis * 1.0f / distance
                            matrix.set(img.getImageMatrix())
                            //以现在的两点中心进行缩放
                            matrix.postScale(scale, scale, ((x + x1) / 2).toFloat(),
                                ((y + y1) / 2).toFloat()
                            )
                            img.setImageMatrix(matrix)
                            //将当前两点记录下来，以供下次参考
                            origFirstPoint[x] = y
                            oriSecondPoint[x1] = y1
                        }
                        MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP ->
                            //恢复到中间位置
                            restoreCenter(img,matrix)
                        else -> {
                        }
                    }
                    return false

                }
            })

        }




    }
    private fun restoreCenter(img:ImageView,matrix:Matrix) {
        val m: Matrix = img.getImageMatrix()
        val rectF = RectF(
            0F,
            0F,
            img.getDrawable().getIntrinsicWidth().toFloat(),
            img.getDrawable().getIntrinsicHeight().toFloat()
        )
        m.mapRect(rectF)
        val postX = (img.getWidth() / 2 - (rectF.right + rectF.left) / 2)
        val postY = (img.getHeight() / 2 - (rectF.bottom + rectF.top) / 2)
        matrix.set(m)
        matrix.postTranslate(postX, postY)
        img.setImageMatrix(matrix)
    }
    private fun getDistance(x1: Int, y1: Int, x2: Int, y2: Int): Int {
        return Math.sqrt(((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)).toDouble()).toInt()
    }


    private fun loadNetImage(width: Int, height: Int,url: String) {
        val imageView = layoutInflater.inflate(layout.activity_image_item, null) as ImageView
        pages.add(imageView)
        Thread {
            val bitmap = decodeBitmapFromNet(url,
                width,
                height)
            runOnUiThread { addImageAsyn(imageView, bitmap) }
        }.start()
    }
    private fun loadNetGifImage(path: String) {
        val imageView = layoutInflater.inflate(R.layout.activity_image_item, null) as ImageView
        Glide.with(this)
            .load(path)
            .apply(RequestOptions().circleCrop().diskCacheStrategy(DiskCacheStrategy.ALL))
            .error(R.drawable.error) //.transition(withCrossFade(4000))
            //.override(100, 100)
            .into(imageView)
        pages.add(imageView)
    }
    private fun addImageAsyn(imageView: ImageView, bitmap: Bitmap?) {
        imageView.setImageBitmap(bitmap)
    }
    private fun decodeBitmapFromNet(url: String, reqWidth: Int, reqHeight: Int): Bitmap? {
        var input: InputStream? = null
        var data: ByteArray? = null
        try {
            val imgUrl = URL(url)
            val conn = imgUrl.openConnection() as HttpURLConnection
            conn.doInput = true
            conn.connect()
            input = conn.inputStream
            data = inputStreamToByteArray(input)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            return null
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } finally {
            try {
                input?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return if (data != null) {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeByteArray(data, 0, data.size, options)
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
            options.inJustDecodeBounds = false
            BitmapFactory.decodeByteArray(data, 0, data.size, options)
        } else {
            null
        }
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if(height > reqHeight || width > reqWidth){
            val halfHeight = height /2
            val halfWidth = width /2
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
    companion object {
        fun inputStreamToByteArray(input: InputStream?): ByteArray {
            val outputStream = ByteArrayOutputStream()
            input ?: return outputStream.toByteArray()
            val buffer = ByteArray(1024)
            var len: Int
            try {
                while (input.read(buffer).also { len = it } != -1) {
                    outputStream.write(buffer, 0, len)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    input.close()
                    outputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return outputStream.toByteArray()
        }
    }
}