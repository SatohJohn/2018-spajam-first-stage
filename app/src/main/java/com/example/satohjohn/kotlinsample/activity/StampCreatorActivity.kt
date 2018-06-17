package com.example.satohjohn.kotlinsample.activity

import android.content.Context
import android.content.res.Configuration
import android.hardware.Camera
import android.hardware.Camera.CameraInfo
import android.media.CamcorderProfile
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.Toast
import com.example.satohjohn.kotlinsample.R
import kotlinx.android.synthetic.main.activity_stamp_creator.*
import java.io.File
import java.io.IOException
import java.nio.file.DirectoryStream
import java.text.SimpleDateFormat
import java.util.*


class StampCreatorActivity : AppCompatActivity(), SurfaceHolder.Callback {

    private var surfaceHolder: SurfaceHolder? = null
    private var surfaceView: SurfaceView? = null
    var mrec: MediaRecorder? = MediaRecorder()
    private var isRecording: Boolean = false;
    var video: File? = null
    private var mCamera: Camera? = null
    private val TAG = "VideoActivity"
    private var context: Context? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stamp_creator)

        button.setOnClickListener {
            if (isRecording == true) {
                mrec?.stop();
                mrec?.release();
                mrec = null;

                isRecording = false
            } else {
                startRecording()

                isRecording = true
            }
        }

        context = this

    }

    public override fun onResume() {
        super.onResume()

        val numCams = Camera.getNumberOfCameras()
        if (numCams > 0) {
            try {

                mCamera = Camera.open()
                surfaceView = findViewById(R.id.surface_camera) as SurfaceView
                surfaceHolder = surfaceView!!.holder
                surfaceHolder!!.addCallback(this)

            } catch (ex: RuntimeException) {
                Toast.makeText(context, "カメラがありません", Toast.LENGTH_LONG).show()
            }

        }

    }


    override fun onPause() {
        if (mCamera != null) {
            mCamera!!.stopPreview()
            mCamera!!.release()
            mCamera = null
        }
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(0, 0, 0, "StartRecording")
        menu.add(0, 1, 0, "StopRecording")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //この辺は若干適当なので利用する際はアレンジして頂きたいですが、
        //オプションのメニューアイテムで録画開始とストップをします。
        when (item.itemId) {
            0 -> try {
                startRecording()
            } catch (e: Exception) {
                val message = e.message
                Log.i(null, "Problem Start$message")
                mrec!!.release()
            }

            1 //GoToAllNotes
            -> {

                mrec!!.stop()
                mrec!!.release()
                mrec = null
            }

            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @Throws(IOException::class)
    protected fun startRecording() {
        mrec = MediaRecorder()  // Works well
        mCamera!!.unlock()

        mrec!!.setCamera(mCamera)

        mrec!!.setPreviewDisplay(surfaceHolder!!.surface)
        mrec!!.setVideoSource(MediaRecorder.VideoSource.CAMERA)
        mrec!!.setAudioSource(MediaRecorder.AudioSource.MIC)

        //録画される画面の縦横を決める
        val degrees = getSurfaceDegrees()

        val camInfo = Camera.CameraInfo()
        val camera_id = findFrontFacingCameraID()
        Camera.getCameraInfo(camera_id, camInfo)

        val cameraRotationOffset = camInfo.orientation
        val displayRotation = getDisplayRotation(camInfo, cameraRotationOffset, degrees)

        var profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH)
        profile.audioCodec = MediaRecorder.AudioEncoder.DEFAULT
        profile.videoCodec = MediaRecorder.VideoEncoder.DEFAULT
        profile.fileFormat = MediaRecorder.OutputFormat.MPEG_4

        mrec!!.setOrientationHint(displayRotation)
        mrec!!.setProfile(profile)

        val date = Date()
        val sdf1 = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")

        val file_name = sdf1.format(date) + ".mp4"

        mrec!!.setOutputFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).path + "/$file_name")

        mrec!!.prepare()
        mrec!!.start()
    }

    protected fun stopRecording() {
        mrec!!.stop()
        mrec!!.release()
    }


    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

        startPreview(holder)
    }


    override fun surfaceCreated(holder: SurfaceHolder) {
        if (mCamera != null) {

        } else {
            Toast.makeText(applicationContext, "Camera not available!", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {


    }


    fun startPreview(holder: SurfaceHolder) {
        try {
            Log.i(TAG, "starting preview")

            val camInfo = Camera.CameraInfo()
            val params = mCamera!!.parameters
            val camera_id = findFrontFacingCameraID()
            Camera.getCameraInfo(camera_id, camInfo)
            val cameraRotationOffset = camInfo.orientation

            val parameters = mCamera!!.parameters
            val previewSizes = parameters.supportedPreviewSizes
            var previewSize: Camera.Size? = null
            var closestRatio = java.lang.Float.MAX_VALUE

            val targetPreviewWidth = if (isLandscape()) getWidth() else getHeight()
            val targetPreviewHeight = if (isLandscape()) getHeight() else getWidth()
            val targetRatio = targetPreviewWidth / targetPreviewHeight.toFloat()

            Log.v(TAG, "target size: $targetPreviewWidth / $targetPreviewHeight ratio:$targetRatio")

            for (candidateSize in previewSizes) {
                val whRatio = candidateSize.width / candidateSize.height.toFloat()
                if (previewSize == null || Math.abs(targetRatio - whRatio) < Math.abs(targetRatio - closestRatio)) {
                    closestRatio = whRatio
                    previewSize = candidateSize
                }
            }


            val degrees = getSurfaceDegrees()


            val displayRotation = getDisplayRotation(camInfo, cameraRotationOffset, degrees)

            Log.d(TAG, "displayRotation:$displayRotation")

            mCamera!!.setDisplayOrientation(displayRotation)

            val rotate = getRotation(camInfo, cameraRotationOffset, degrees)

            Log.v(TAG, "rotate: $rotate")

            Log.v(TAG, "preview size: " + previewSize!!.width + " / " + previewSize.height)
            parameters.setPreviewSize(previewSize.width, previewSize.height)
            parameters.setRotation(rotate)

            mCamera!!.parameters = parameters
            mCamera!!.setPreviewDisplay(holder)
            mCamera!!.startPreview()

            Log.d(TAG, "preview started")


        } catch (e: IOException) {
            Log.d(TAG, "Error setting camera preview: " + e.message)
        }

    }

    private fun isFrontFacingCam(camInfo: Camera.CameraInfo): Boolean {

        val isFrontFacingCam: Boolean

        if (camInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            isFrontFacingCam = true
        } else {
            isFrontFacingCam = false
        }

        return isFrontFacingCam
    }

    private fun getWidth(): Int {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        return displayMetrics.widthPixels
    }

    private fun getHeight(): Int {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        return displayMetrics.heightPixels
    }

    private fun isLandscape(): Boolean {
        return if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            true
        } else {
            false
        }
    }

    private fun getSurfaceDegrees(): Int {

        val rotation = windowManager.defaultDisplay.rotation
        var degrees = 0

        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 90
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 270
        }// Natural orientation
        // Landscape left
        // Upside down
        // Landscape right
        return degrees
    }

    private fun getRotation(camInfo: Camera.CameraInfo, cameraRotationOffset: Int, degrees: Int): Int {
        val rotate: Int
        if (isFrontFacingCam(camInfo)) {
            rotate = (360 + cameraRotationOffset + degrees) % 360
        } else {
            rotate = (360 + cameraRotationOffset - degrees) % 360
        }
        return rotate
    }

    private fun getDisplayRotation(camInfo: Camera.CameraInfo, cameraRotationOffset: Int, degrees: Int): Int {
        var displayRotation: Int
        if (isFrontFacingCam(camInfo)) {

            displayRotation = (cameraRotationOffset + degrees) % 360
            displayRotation = (360 - displayRotation) % 360

        } else { // back-facing

            displayRotation = (cameraRotationOffset - degrees + 360) % 360
        }
        return displayRotation
    }

    private fun findFrontFacingCameraID(): Int {
        var cameraId = -1
        // Search for the front facing camera
        val numberOfCameras = Camera.getNumberOfCameras()
        for (i in 0 until numberOfCameras) {
            val info = CameraInfo()
            Camera.getCameraInfo(i, info)
            if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
                Log.d(TAG, "Camera found")
                cameraId = i
                break
            }
        }
        return cameraId
    }
}
