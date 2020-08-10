package com.manh.lib.view.audio

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.RectF
import android.provider.SyncStateContract
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.manh.lib.view.R
import com.manh.lib.view.util.PREF_NAME_DEFAULT

class SwitchButton : androidx.appcompat.widget.AppCompatImageView, View.OnTouchListener {
    private var TAG = "SwitchButton"
    private var mEnable = false
    private var mContext: Context? = null

    private var PREF_NAME = ""

    private var mImageOn = 0
    private var mImageOff = 0

    private var DEFAULT_IMAGE_ON = R.drawable.sw_dark_on
    private var DEFAULT_IMAGE_OFF = R.drawable.sw_dark_off

    private var mRectF: RectF? = null

    private var KEY = ""
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private var mListener: OnSwitchButtonListener? = null

    constructor(context: Context?) : super(context) {

    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context!!, attrs!!)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context!!, attrs!!)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        val typeArray = getContext().obtainStyledAttributes(attrs, R.styleable.SwitchButton)
        mImageOn = typeArray.getInteger(R.styleable.SwitchButton_sb_image_on, DEFAULT_IMAGE_ON)
        mImageOff = typeArray.getInteger(R.styleable.SwitchButton_sb_image_off, DEFAULT_IMAGE_OFF)
        typeArray.recycle()
        PREF_NAME = PREF_NAME_DEFAULT
        mRectF = RectF()
        setupSharedPreferences()
        setImageResource()
        setOnTouchListener(this)

    }

    private fun setupSharedPreferences() {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    private fun setImageResource() {
        if (KEY != "") {
            mEnable = getBoolean(KEY)

        }
        if (mEnable) {

            setImageResource(mImageOn)

        } else {
            setImageResource(mImageOff)
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        try {
            if (event!!.action == MotionEvent.ACTION_UP) {
                this.mEnable = !mEnable
                Log.d(TAG, "touch ${mEnable}")
                setBoolean(KEY, mEnable)
                setImageResource()
                if (mListener != null) {
//                    mListener!!.onState(this.mEnable)
                    mListener!!.onChange(this)
                }
                invalidate()
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        return true
    }


    private fun drawImage(canvas: Canvas, image: Bitmap) {
        canvas.drawBitmap(image, null, mRectF!!, null)
    }

    private fun getBitmap(image: Int): Bitmap {
        return BitmapFactory.decodeResource(resources, image)
    }

    private fun setEnabledSwitchButton(enable: Boolean) {
        this.mEnable = enable
        invalidate()
    }

    private fun getBoolean(key: String): Boolean {
        return sharedPreferences!!.getBoolean(key, false)
    }

    private fun setBoolean(key: String, boolean: Boolean) {
        editor!!.putBoolean(key, boolean)
        editor.apply()
    }

    fun setKey(key: String) {
        KEY = key
        setImageResource()
    }

    fun setDbPrefName(dbName: String) {
        PREF_NAME = dbName
        setupSharedPreferences()
    }

    fun onDestroy() {

    }

    fun setOnListener(listener: OnSwitchButtonListener) {
        mListener = listener

    }

    interface OnSwitchButtonListener {
        fun onState(enable: Boolean) {

        }

        fun onChange(v: View) {

        }

    }
}