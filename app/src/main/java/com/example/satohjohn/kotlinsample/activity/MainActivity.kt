package com.example.satohjohn.kotlinsample.activity

import android.content.Intent
import android.graphics.Point
import android.media.SoundPool
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Display
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.Button
import com.example.satohjohn.kotlinsample.R

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

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

        sample_text.setOnClickListener { view ->
            startActivity(Intent(this, TestActivity::class.java))
        }

        piano = piano.map { soundPool.load(this, it, 1) }

        defaultDisplay = getWindowManager().getDefaultDisplay();

        Log.d("mainActivity", "end on create")
    }

    private val soundPool = SoundPool.Builder().setMaxStreams(7).build()
    private var defaultDisplay: Display? = null


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

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return event.run {
            this?.let {
                //                val position: Int = (it.getY() % piano.size + 0.5f).toInt()
                // windowの高さ / piano.size
                val point = Point()
                defaultDisplay?.getSize(point)
                val position: Int = (it.getY() / (point.y / piano.size)).toInt()
                //val positionXRate =(it.getX() / (point.x));
                //val positionXRate =(abs(it.getX() - (point.x/2.0f))/(point.x/2.0f));
                val positionXRate =1.0f-(abs(it.getX() - (point.x/2.0f))/(point.x/2.0f));
                Log.d("mainActivity", "point x: ${it.getX()}, y: ${it.getY()}, point: ${position}, ")
                soundPool.play(piano[position], 1.0f, 1.0f,
                        0, 0,1.5f*positionXRate+0.5f)
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


}
