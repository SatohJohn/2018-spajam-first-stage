package com.example.satohjohn.kotlinsample.activity

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.PorterDuff.Mode
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceHolder.Callback
import android.view.SurfaceView

class DrawSurfaceView : SurfaceView, Callback {

    private var mHolder: SurfaceHolder? = null
    private var mPaint: Paint? = null
    private var mPath: Path? = null
    private var mLastDrawBitmap: Bitmap? = null
    private var mLastDrawCanvas: Canvas? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        mHolder = holder

        // 透過します。
        setZOrderOnTop(true)
        mHolder!!.setFormat(PixelFormat.TRANSPARENT)

        // コールバックを設定します。
        mHolder!!.addCallback(this)

        // ペイントを設定します。
        mPaint = Paint()
        mPaint!!.color = Color.BLACK
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.strokeCap = Paint.Cap.ROUND
        mPaint!!.isAntiAlias = true
        mPaint!!.strokeWidth = 6f
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        // 描画状態を保持するBitmapを生成します。
        clearLastDrawBitmap()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int,
                                height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        mLastDrawBitmap!!.recycle()
    }

    private fun clearLastDrawBitmap() {
        if (mLastDrawBitmap == null) {
            mLastDrawBitmap = Bitmap.createBitmap(width, height,
                    Config.ARGB_8888)
        }

        if (mLastDrawCanvas == null) {
            mLastDrawCanvas = Canvas(mLastDrawBitmap!!)
        }

        mLastDrawCanvas!!.drawColor(0, Mode.CLEAR)
    }

    public fun onTouchDown(x: Float, y: Float) {
        mPath = Path()
        mPath!!.moveTo(x, y)
    }

    public fun onTouchMove(x: Float, y: Float) {
        mPath!!.lineTo(x, y)
        drawLine(mPath as Path)
    }

    public fun onTouchUp(x: Float, y: Float) {
        mPath!!.lineTo(x, y)
        drawLine(mPath as Path)
        mLastDrawCanvas!!.drawPath(mPath!!, mPaint!!)
    }

    private fun drawLine(path: Path) {
        // ロックしてキャンバスを取得します。
        val canvas = mHolder!!.lockCanvas()

        // キャンバスをクリアします。
        canvas.drawColor(0, Mode.CLEAR)

        // 前回描画したビットマップをキャンバスに描画します。
        canvas.drawBitmap(mLastDrawBitmap!!, 0f, 0f, null)

        // パスを描画します。
        canvas.drawPath(path, mPaint!!)

        // ロックを外します。
        mHolder!!.unlockCanvasAndPost(canvas)
    }

}