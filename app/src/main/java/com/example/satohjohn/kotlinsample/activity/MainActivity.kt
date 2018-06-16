package com.example.satohjohn.kotlinsample.activity

import android.content.Intent
import android.graphics.Point
import android.hardware.Camera
import android.media.SoundPool
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import android.widget.Button
import com.example.satohjohn.kotlinsample.R

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import java.io.IOException
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity(), SurfaceHolder.Callback {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("mainActivity", "on create")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        // Example of a call to a native method
        test_text.text = testJNI("yamaken")

        // activityを他に使いたくなればどうぞ
//        sample_text.setOnClickListener { view ->
//            startActivity(Intent(this, TestActivity::class.java))
//        }

        dramTimer = Timer().schedule(1000, 500) {
            if (tomTiming >= 3) {
                tomTiming = 0
                soundPool.play(dram[1], 1.0f, 1.0f, 0, 0, 1.0f)
            } else {
                soundPool.play(dram[0], 1.0f, 1.0f, 0, 0, 1.0f)
                tomTiming++
            }
        }

        dram = dram.map { soundPool.load(this, it, 1) }
        piano = piano.map { soundPool.load(this, it, 1) }

        defaultDisplay = getWindowManager().getDefaultDisplay();

        Log.d("mainActivity", "end on create")
    }

    private var tomTiming = 0
    private var dramTimer:TimerTask? = null
    private val soundPool = SoundPool.Builder().setMaxStreams(7).build()
    private var defaultDisplay: Display? = null

    private var dram = listOf(
            R.raw.se_maoudamashii_instruments_drum2_tom1,
            R.raw.se_maoudamashii_instruments_drum2_snare
    )

    private var piano = listOf(
            R.raw.se_maoudamashii_instruments_piano2_1do,
            R.raw.se_maoudamashii_instruments_piano2_2re,
            R.raw.se_maoudamashii_instruments_piano2_3mi,
            R.raw.se_maoudamashii_instruments_piano2_4fa,
            R.raw.se_maoudamashii_instruments_piano2_5so,
            R.raw.se_maoudamashii_instruments_piano2_6ra,
            R.raw.se_maoudamashii_instruments_piano2_7si)


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    var beforePosition: Int = -1
    var prevTimeMillis: Long = System.currentTimeMillis();

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return event.run {
            this?.let {
                //                val position: Int = (it.getY() % piano.size + 0.5f).toInt()
                // windowの高さ / piano.size
                val point = Point()
                defaultDisplay?.getSize(point)
                val position: Int = (it.getY() / (point.y / piano.size)).toInt()
                Log.d("mainActivity", "point x: ${it.getX()}, y: ${it.getY()}, point: ${position}, ")

                if ((event as MotionEvent).action == MotionEvent.ACTION_DOWN) {
                    canvasView.onTouchDown(event.x, event.y)
                }
                else if ((event as MotionEvent).action == MotionEvent.ACTION_MOVE) {
                    canvasView.onTouchMove(event.x, event.y)
                }
                else if ((event as MotionEvent).action == MotionEvent.ACTION_UP) {
                    canvasView.onTouchUp(event.x, event.y)
                }

                val current = System.currentTimeMillis()
                if (current - prevTimeMillis > 100) {
                    soundPool.play(piano[position], 1.0f, 1.0f, 0, 0, 1.0f)
                    prevTimeMillis = current
                }
//                if (position != beforePosition) {
//                    soundPool.play(piano[position], 1.0f, 1.0f, 0, 0, 1.0f)
//                    beforePosition = position
//                }
            }

            super.onTouchEvent(event)
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun testJNI(word: String): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }


    // Camera2 API が推奨されているが、コードが多くなるらしいので今回はこれを利用する。
    private var camera: Camera? = null

    override fun onResume() {
        super.onResume()

        camera = getBackCamera()
        camera!!.setDisplayOrientation(90)

        val surfaceView = findViewById(R.id.surfaceView) as SurfaceView
        val holder = surfaceView.holder
        holder.addCallback(this)
    }

    override fun onStop() {
        super.onStop()

        if (camera != null) {
            camera!!.release()
            camera = null
        }
    }

    /**
     * バックカメラ（外向きのカメラ）を取得する
     *
     * @return
     */
    private fun getBackCamera(): Camera? {
        for (i in 0 until Camera.getNumberOfCameras()) {
            val cameraInfo = Camera.CameraInfo()
            Camera.getCameraInfo(i, cameraInfo)
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                return Camera.open(i)
            }
        }
        return null
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        try {
            camera?.setPreviewDisplay(holder)
            camera?.startPreview()
        } catch (e: IOException) {
            Log.e("surfaceCreated", "Failed to init camera preview.", e)
        }

    }

    override fun onPause() {
        dramTimer?.cancel()
        dramTimer = null
        super.onPause()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {}
}
