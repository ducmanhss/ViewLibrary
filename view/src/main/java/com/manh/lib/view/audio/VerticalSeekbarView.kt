package com.manh.lib.view.audio

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.manh.lib.view.R

class VerticalSeekbarView:View {
    private val TAG="SeekbarView"
//    private var SB_PROGRESS_BG = R.drawable.bg_eq_01_default
//    private var SB_PROGRESS_ON = R.drawable.bg_eq_01_schedule
//    private var SB_PROGRESS_OFF = R.drawable.bg_eq_01_schedule_off
//    private var SB_BUTTON_ON= R.drawable.btn_eq_01_default
//    private var SB_BUTTON_OFF= R.drawable.btn_eq_01_off
//    private var SB_BUTTON_PRESS= R.drawable.btn_eq_01_press
    private var SB_PROGRESS_BG = R.drawable.bg_load
    private var SB_PROGRESS_ON = R.drawable.load
    private var SB_PROGRESS_OFF = R.drawable.bg_eq_01_schedule_off
    private var SB_BUTTON_ON= R.drawable.bt_load
    private var SB_BUTTON_OFF= R.drawable.btn_eq_01_off
    private var SB_BUTTON_PRESS= R.drawable.btn_eq_01_press

    private lateinit var mContext: Context
    private  var mDrawableButtonPress:Int=0
    private  var mDrawableButtonOff:Int=0
    private  var mDrawableButtonOn:Int=0
    private  var mDrawableBgProgress:Int=0
    private  var mDrawableProgressOff:Int=0
    private  var mDrawableProgressOn:Int=0


    private lateinit var mBitmapBgProgress: Bitmap
    private lateinit var mBitmapProgress: Bitmap
//    private lateinit var mBitmapBgButton: Bitmap
    private lateinit var mBitmapButton: Bitmap


    private lateinit var mBmProgressOn: Bitmap
    private lateinit var mBmProgressOff: Bitmap
    private lateinit var mBmBgButtonOn: Bitmap
    private lateinit var mBmBgButtonOff: Bitmap
    private lateinit var mBmButtonOn: Bitmap
    private lateinit var mBmButtonOff: Bitmap

    private var mIsPress=false
    private var mViewEnable=true
    private var mPressEnable=true

    private var currPosition=0
    private var maxCurr=0
    private var minCurr=0
    private var positionOld=0

    private lateinit var mRectF: RectF
    private lateinit var mRectButton: RectF

    private var k=0
    private var MAX=100
     private var MIN=0
    private var progress=0
    //chiều dài progress theo button so vs view
    private var lenghtView=0
//    private var lenghtMaxMin=0

    //Tỷ lệ của button
    private var heightButtonRatio =1
    private var ratioDefault =4
    private var density =0f
    private var ratioDensity =2
    private var mListener:OnVerticalSeekbarViewChangeListener?=null
    constructor(context: Context?) : super(context){
        mContext=context!!
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        mContext=context!!
        init(attrs!!)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        mContext=context!!
        init(attrs!!)
    }

    private fun init(attrs: AttributeSet) {
        mBitmapBgProgress=BitmapFactory.decodeResource(resources,SB_PROGRESS_BG)
        mBitmapProgress=BitmapFactory.decodeResource(resources,SB_PROGRESS_ON)
        mBitmapButton=BitmapFactory.decodeResource(resources,SB_BUTTON_ON)
        mRectF= RectF()
        mRectButton= RectF()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        density=context.resources.displayMetrics.density
        ratioDefault=ratioDefault+(density/ratioDensity).toInt()
        if (ratioDefault-(w/18-ratioDefault)>0){
//        if (w/18>=ratioDefault) {
//            heightButtonRatio = mBitmapButton.height / mBitmapButton.width * (ratioDefault-(w/18-ratioDefault)).toInt()
//        }else{
            heightButtonRatio = mBitmapButton.height / mBitmapButton.width * (ratioDefault+(ratioDefault-w/18)).toInt()
//        }
        }else{
            heightButtonRatio = mBitmapButton.height / mBitmapButton.width * (ratioDefault-(ratioDefault-1)).toInt()
        }

         maxCurr=height-mBitmapButton.height/heightButtonRatio
         minCurr=mBitmapButton.height/heightButtonRatio
        lenghtView=maxCurr-minCurr
        setupParams()
        Log.e(TAG,"w "+width)
        Log.e(TAG,"den "+context.resources.displayMetrics.density)
        Log.e(TAG,"dendpi "+context.resources.displayMetrics.densityDpi)
//        initRatioProgress(minCurr)

    }
    private fun initRatioProgress(minCurr:Int){
//        val curr=currPosition-minCurr
////        progress=(lenght/MAX)*curr.toFloat()
        currPosition=progress*lenghtView/MAX+minCurr

        if (currPosition-mBitmapButton.height/heightButtonRatio<0){
            currPosition=mBitmapButton.height/heightButtonRatio
        }
        if (currPosition+mBitmapButton.height/heightButtonRatio>height){
            currPosition=height-mBitmapButton.height/heightButtonRatio
        }
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        setupParams()
        drawProgressView(canvas)
        drawButtonView(canvas)
    }

    private fun setupParams() {
        initRatioProgress(minCurr)

    }

    private fun drawProgressView(canvas: Canvas?) {
        drawBgProgress(canvas!!)
        drawProgress(canvas!!)
    }

    private fun drawButtonView(canvas: Canvas?) {
        drawButton(canvas!!)
    }

    private fun drawBgProgress(canvas: Canvas){
//        canvas.drawBitmap(mBitmapBgProgress,null,mRectF,null)
        val src = Rect(0,0,mBitmapBgProgress.width+0,mBitmapBgProgress.height+0)
        val dst = Rect(0,0,width+0,height+0)
        canvas.drawBitmap(mBitmapBgProgress,src,dst,null)
    }
    private fun drawProgress(canvas: Canvas){
        canvas.save()
//        mRectF.set(0f,height/2+50f,width+0f,height+0f)
        mRectF.set(0f,currPosition+0f,width+0f,height+0f)
        canvas.clipRect(mRectF)
//        canvas.drawBitmap(mBitmapProgress,k.toFloat(),paddingTop.toFloat(),null)

        val src = Rect(0,0,mBitmapProgress.width+0,mBitmapProgress.height+0)
        val dst = Rect(0,0,width-0,height-0)
//        val dst = Rect(20,20,width-20,height-20)
        canvas.drawBitmap(mBitmapProgress,src,dst,null)
        canvas.restore()
    }
    private fun drawButton(canvas: Canvas){
        val mRectF=RectF()
//        mRectF.set(width/(1/6f)+0f,mCurrPosition+0f,width/(6/5f)+0f,mBitmapButton.height/(5f)+0f)
        mRectF.top=currPosition+0f
//            canvas.drawBitmap(mBitmapButton,(width-mBitmapButton.width)/2f,mRectF.top-(mBitmapButton.height/2f),null)

        val src = Rect((10*density).toInt(),(10*density).toInt(),mBitmapButton.width-(10*density).toInt(),mBitmapButton.height-(10*density).toInt())
        val dst =RectF(0f,mRectF.top-(mBitmapButton.height/heightButtonRatio),width+0f,mRectF.top+mBitmapButton.height/heightButtonRatio+0f)
            canvas.drawBitmap(mBitmapButton,src,dst,null)
//        canvas.restore()
//        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!mViewEnable){
            return false
        }
        when(event!!.action){
            MotionEvent.ACTION_DOWN->{
//mPositionOld=event!!.y.toInt()
                if (mListener!=null){
                    mListener!!.onSeekbarStart()
                }
            }
            MotionEvent.ACTION_MOVE->{

                if (currPosition-mBitmapButton.height/heightButtonRatio>=0&&currPosition+mBitmapButton.height/heightButtonRatio<=height) {
                   val mCurrPosition = event!!.y.toInt() - positionOld
                    progress=((mCurrPosition-minCurr)*MAX/lenghtView).toInt()
                    if (progress>MAX){
                        progress=MAX
                    }
                    if (progress<MIN){
                        progress=MIN
                    }
                if (mListener!=null){
                    mListener!!.onSeekbarChanged((progress-MAX/2)*-1)

                }
                }
                if (progress>MAX){
                    progress=MAX
                }
                if (progress<MIN){
                    progress=0
                }
            }
            MotionEvent.ACTION_UP->{
                if (mListener!=null){
                    mListener!!.onSeekbarStop()
                }
            }
        }

        invalidate()
        return true
    }

    fun setMax(max:Int):VerticalSeekbarView{
        MAX=max

        return this
    }
    fun getMax():Int{

        return MAX
    }
    fun setProgress(progress:Int):VerticalSeekbarView{
//        this.progress=(progress-lenghtMaxMin/2)*-1
        this.progress=progress
        invalidate()
        return this
    }
    fun getProgress():Int{
        return progress
    }

    fun setEnable(enable:Boolean){
        mViewEnable=enable
    }
//    fun getHeightRatio():Int{
//        return buttonRatio
//    }
//
//    fun setHeightButtonRatio(ratio:Int){
//        buttonRatio=ratio
//    }

    fun setOnVerticalSeekbarViewChangeListener(listener: OnVerticalSeekbarViewChangeListener){
        mListener=listener
    }
fun onDeleteCacheBitmap(){
    mBitmapButton.recycle()
    mBitmapBgProgress.recycle()
    mBitmapProgress.recycle()
}
    interface OnVerticalSeekbarViewChangeListener{
        fun onSeekbarChanged(progress: Int)
        fun onSeekbarStart()
        fun onSeekbarStop()
    }
}