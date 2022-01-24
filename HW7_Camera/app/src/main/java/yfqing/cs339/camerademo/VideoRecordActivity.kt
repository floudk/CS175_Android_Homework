package yfqing.cs339 .camerademo

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.Camera
import android.hardware.Camera.CameraInfo
import android.hardware.Camera.PictureCallback
import android.media.CamcorderProfile
import android.media.MediaMetadataRetriever
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class VideoRecordActivity : AppCompatActivity() {
    private var mTakePhoto: Button? = null
    private var mInputFilename:EditText? =null
    private var mSurfaceView: SurfaceView? = null
    private var mSurfaceHolder: SurfaceHolder? = null
    private var mCamera: Camera? = null
    private var mMediaRecorder: MediaRecorder? = null
    private var mIsRecording = false
    private var mCameraAspect = CameraInfo.CAMERA_FACING_BACK
    private var mCameraSwitcher : Button ? = null
    private var mRecentInfo : TextView ?=null
    private var mShowLastImg: ImageView ?=null
    private var downtime: Long?=null
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_record)
        mSurfaceView = findViewById(R.id.surface_view)
        mTakePhoto = findViewById(R.id.take_photo)
        mCameraSwitcher = findViewById(R.id.switcher)
        mInputFilename = findViewById(R.id.filename)
        mRecentInfo = findViewById(R.id.recent_info)
        mShowLastImg = findViewById(R.id.last_image)

        startCamera()


        mTakePhoto?.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    if(!mIsRecording&&Date().time - downtime!!>300){
                        Toast.makeText(applicationContext,"录像",Toast.LENGTH_SHORT).show()
                        if (prepareVideoRecorder()) {
                            mMediaRecorder!!.start()
                            mIsRecording = true
                        } else {
                            releaseMediaRecorder()
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if(Date().time - downtime!!<300) {
                        Toast.makeText(applicationContext,"拍照",Toast.LENGTH_SHORT).show()
                        takePicture()
                    }else{
                        if(mIsRecording){
                            mMediaRecorder!!.stop()
                            releaseMediaRecorder()
                            mCamera!!.lock()
                            mIsRecording = false
                        }
                    }
                }
                MotionEvent.ACTION_DOWN -> {
                    downtime= Date().time
//                    Toast.makeText(applicationContext,downtime.toString(),Toast.LENGTH_SHORT).show()
                }
            }
            return@setOnTouchListener true
        }

        mCameraSwitcher?.setOnClickListener { switchCamera() }
    }

    private fun startCamera() {
        try {
            mCamera = Camera.open(CameraInfo.CAMERA_FACING_BACK)
            setCameraDisplayOrientation()
            LoadLastImg()
        } catch (e: Exception) {
            // error
        }
        mSurfaceHolder = mSurfaceView!!.holder
        mSurfaceHolder?.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                   mCamera!!.setPreviewDisplay(holder)
                    mCamera!!.startPreview()
                } catch (e: IOException) {
                    // error
                }
            }

            override fun surfaceChanged(holder: SurfaceHolder, i: Int, i1: Int, i2: Int) {
                try {
                    mCamera!!.stopPreview()
                } catch (e: Exception) {
                    // error
                }
                try {
                    mCamera!!.setPreviewDisplay(holder)
                    mCamera!!.startPreview()
                } catch (e: Exception) {
                    //error
                }
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {}
        })
    }

    private fun setCameraDisplayOrientation() {
        val rotation = windowManager.defaultDisplay.rotation
        var degrees = 0
        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 90
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 270
        }
        val info = CameraInfo()
        Camera.getCameraInfo(CameraInfo.CAMERA_FACING_BACK, info)
        val result = (info.orientation - degrees + 360) % 360
        mCamera!!.setDisplayOrientation(result)
    }

    private fun takePicture() {
        mCamera!!.takePicture(null, null, PictureCallback { bytes, camera ->
            val pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE, mInputFilename!!.text.toString()) ?: return@PictureCallback
            try {
                val fos = FileOutputStream(pictureFile)
                fos.write(bytes)
                fos.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            mCamera!!.startPreview()
        })
        LoadLastImg()

    }

    private fun getOutputMediaFile(type: Int, name:String): File? {
        // Android/data/com.bytedance.camera.demo/files/Pictures
        var filename = name
        val mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (!mediaStorageDir!!.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null
            }
        }
        Log.e("infos",filename)
        var info : String
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        if(filename.isEmpty())
            filename = timeStamp
        val mediaFile: File
        mediaFile = if (type == MEDIA_TYPE_IMAGE) {
            info = mediaStorageDir.path + File.separator + filename+".jpg"
            File(mediaStorageDir.path + File.separator + filename+".jpg")
        } else if (type == MEDIA_TYPE_VIDEO) {
            info = mediaStorageDir.path + File.separator + filename+".mp4"
            File(mediaStorageDir.path + File.separator +filename + ".mp4")
        } else {
            return null
        }
        info+= "[Photoed in $timeStamp]"
        Log.e("infos",info)
        mRecentInfo!!.text = info
        return mediaFile
    }

    private fun prepareVideoRecorder(): Boolean {
        mMediaRecorder = MediaRecorder()
        mCamera!!.unlock()
        mMediaRecorder!!.setCamera(mCamera)
        mMediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.CAMCORDER)
        mMediaRecorder!!.setVideoSource(MediaRecorder.VideoSource.CAMERA)
        mMediaRecorder!!.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH))
        mMediaRecorder!!.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO,mInputFilename!!.text.toString()).toString())
        mMediaRecorder!!.setPreviewDisplay(mSurfaceHolder!!.surface)
        try {
            mMediaRecorder!!.prepare()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            releaseMediaRecorder()
            return false
        } catch (e: IOException) {
            releaseMediaRecorder()
            return false
        }
        return true
    }



    override fun onDestroy() {
        super.onDestroy()
        releaseMediaRecorder()
        releaseCamera()
    }

    private fun releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            mMediaRecorder!!.reset()
            mMediaRecorder!!.release()
            mMediaRecorder = null
            mCamera!!.lock()
        }
    }

    private fun releaseCamera() {
        if (mCamera != null) {
            mCamera!!.release()
            mCamera = null
        }
    }

    companion object {
        private const val MEDIA_TYPE_IMAGE = 1
        private const val MEDIA_TYPE_VIDEO = 2
    }

    private fun switchCamera(){
        mCameraAspect = if(mCameraAspect==CameraInfo.CAMERA_FACING_BACK){
            CameraInfo.CAMERA_FACING_FRONT
        }else{
            CameraInfo.CAMERA_FACING_BACK
        }
        mCamera!!.stopPreview()
        releaseCamera()
        mCamera = Camera.open(mCameraAspect)
        setCameraDisplayOrientation()
        mCamera!!.setPreviewDisplay(mSurfaceHolder)
        mCamera!!.startPreview()
    }

    private fun LoadLastImg() {

        val mStore = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (!mStore!!.exists())
            mStore.mkdirs()
        var lastModifiedTime : Long =0
        var lastImg:File? = null
        for(file:File in mStore.listFiles()){
            if(file.lastModified() > lastModifiedTime){
                lastModifiedTime = file.lastModified()
                lastImg = file
            }
        }

        val fileName:String = lastImg?.name ?: "noFiles"
        var bitmap:Bitmap = Bitmap.createBitmap(128,128, Bitmap.Config.ARGB_8888)
        var options = BitmapFactory.Options()
        options.inJustDecodeBounds = false
        options.inSampleSize = 1


        if(lastImg != null){

            if(fileName.substring(fileName.length-4,fileName.length) == ".jpg"){
                bitmap = BitmapFactory.decodeFile(lastImg.absolutePath, options)
            } else if(fileName.substring(fileName.length-4,fileName.length) == ".mp4"){
                var lastvideo = MediaMetadataRetriever()
                lastvideo.setDataSource(lastImg.absolutePath)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                    bitmap= lastvideo.getScaledFrameAtTime(-1,MediaMetadataRetriever.OPTION_CLOSEST, mShowLastImg!!.width, mShowLastImg!!.height)!!
                }
            }
        }

        mShowLastImg?.setImageBitmap(bitmap)
    }

}