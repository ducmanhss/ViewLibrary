package com.manh.lib.view.audio

import android.content.Context
import android.graphics.*
import android.graphics.Matrix.ScaleToFit
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.MeasureSpec
import com.manh.lib.view.R


class ArcProgressView: View {
    private val TAG="ArcProgressView"
private var ARC_PROGRESS_BG = R.drawable.bg_bass_01_default
private var ARC_PROGRESS_ON = R.drawable.bg_bass_01_schedule
private var ARC_PROGRESS_OFF = R.drawable.bg_bass_01_off
//private var ARC_PROGRESS_BG = R.drawable.max_on
//    private var ARC_PROGRESS_ON = R.drawable.max_off
//private var ARC_PROGRESS_OFF = R.drawable.max_on
private var ARC_BG_BUTTON_ON=R.drawable.btn_bass_01_bottom_schedule
private var ARC_BG_BUTTON_OFF=R.drawable.btn_bass_01_bottom
private var ARC_BUTTON_ON=R.drawable.btn_bass_01_top
//private var ARC_BUTTON_ON=R.drawable.volume_3
private var ARC_BUTTON_OFF=R.drawable.btn_bass_01_top_off

    private lateinit var mContext:Context
private  var mDrawableBgButtonOff:Int=0
private  var mDrawableBgButtonOn:Int=0
private  var mDrawableButtonOff:Int=0
private  var mDrawableButtonOn:Int=0
private  var mDrawableBgProgress:Int=0
private  var mDrawableProgressOff:Int=0
private  var mDrawableProgressOn:Int=0


private lateinit var mBitmapBgProgress: Bitmap
private lateinit var mBitmapProgress: Bitmap
private lateinit var mBitmapBgButton: Bitmap
private lateinit var mBitmapButton: Bitmap


//private lateinit var mBitmapBgProgress: Bitmap

private lateinit var mBmProgressOn: Bitmap
private lateinit var mBmProgressOff: Bitmap
private lateinit var mBmBgButtonOn: Bitmap
private lateinit var mBmBgButtonOff: Bitmap
private lateinit var mBmButtonOn: Bitmap
private lateinit var mBmButtonOff: Bitmap

    private var mViewEnable = true
    private var mPressEnable = false
    private var mIsPress = false
    private var mIsRotateBgButton = true

    private lateinit var mPaintFlags:PaintFlagsDrawFilter
    private lateinit var mPaint:Paint
    private lateinit var mRectF: RectF
    private var degrees=0f
    private var downDegrees=0f
    private var currDegrees=0f

    private var startAngle=0f
    //sweepAngle max = 282f để lấy hết bitmap ,bình thường = 270f
    private var sweepAngle=0f

    private var centerX=0f
    private var centerY=0f

    private var mLeft =0f
    private var mTop =0f

    private var mHeight=0
    private var mScale=0

    private var MAX=200
    private var MIN=0

    private var mListener: OnSeekBarChangeListener?=null

    private lateinit var mMatrix: Matrix

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


    private fun init(attrs: AttributeSet){
    var typeArray = mContext.obtainStyledAttributes(attrs,R.styleable.ArcProgressView)
        mDrawableBgButtonOff=typeArray.getResourceId(R.styleable.ArcProgressView_arc_bg_button_off,ARC_BG_BUTTON_OFF)!!
        mDrawableBgButtonOn=typeArray.getResourceId(R.styleable.ArcProgressView_arc_bg_button_on,ARC_BG_BUTTON_ON)!!
        mDrawableButtonOff=typeArray.getResourceId(R.styleable.ArcProgressView_arc_button_off,ARC_BUTTON_OFF)!!
        mDrawableButtonOn=typeArray.getResourceId(R.styleable.ArcProgressView_arc_button_on,ARC_BUTTON_ON)!!

        mDrawableBgProgress=typeArray.getResourceId(R.styleable.ArcProgressView_arc_progress_bg,ARC_PROGRESS_BG)!!
        mDrawableProgressOff=typeArray.getResourceId(R.styleable.ArcProgressView_arc_progress_off,ARC_PROGRESS_OFF)!!
        mDrawableProgressOn=typeArray.getResourceId(R.styleable.ArcProgressView_arc_progress_on,ARC_PROGRESS_ON)!!

        val enablePress=typeArray.getInt(R.styleable.ArcProgressView_arc_enable_press,0)
        mPressEnable = enablePress==1
        mIsRotateBgButton=typeArray.getBoolean(R.styleable.ArcProgressView_arc_rotate_bg_button,true)

        setupBitmap()
        mPaintFlags= PaintFlagsDrawFilter(0,3)
        mPaint= Paint()
        mRectF=RectF()

    }

    private fun setupBitmap() {
        mBmBgButtonOn=BitmapFactory.decodeResource(resources,mDrawableBgButtonOn)
        mBmBgButtonOff=BitmapFactory.decodeResource(resources,mDrawableBgButtonOff)

        mBmButtonOn=BitmapFactory.decodeResource(resources,mDrawableButtonOn)
        mBmButtonOff=BitmapFactory.decodeResource(resources,mDrawableButtonOff)

        mBmProgressOn=BitmapFactory.decodeResource(resources,mDrawableProgressOn)
        mBmProgressOff=BitmapFactory.decodeResource(resources,mDrawableProgressOff)


        mBitmapBgProgress=BitmapFactory.decodeResource(resources,mDrawableBgProgress)
        mBitmapProgress=BitmapFactory.decodeResource(resources,mDrawableProgressOn)
        mBitmapBgButton=BitmapFactory.decodeResource(resources,mDrawableBgButtonOff)
        mBitmapButton=BitmapFactory.decodeResource(resources,mDrawableButtonOn)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val min = Math.min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec))
//        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
        super.onMeasure(MeasureSpec.makeMeasureSpec(min, MeasureSpec.getMode(widthMeasureSpec)), MeasureSpec.makeMeasureSpec(min, MeasureSpec.getMode(heightMeasureSpec)))

    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mScale= Math.min(width, height) - paddingLeft - paddingRight
        mHeight=(h-paddingTop)-paddingBottom
//        mRectF.set(10f,10f,width-10f,height-10f)
        centerX=width/2f
        centerY=height/2f

setupBitmapFromSetting()
        setupPaintShaderProgress(w,height)

    }

    private fun setupPaintShaderProgress(viewWidth: Int, viewHeight: Int) {
        val matrix = Matrix()
        val bmWidth=mBitmapProgress.getWidth()
        val bmHeight=mBitmapProgress.getHeight()
//vì khi vẽ bitmap ta lấy tất cả chiều cao chiều rộng nên ở đây cũng phải lấy hết
        val src = RectF(0f, 0f, bmWidth.toFloat(), bmHeight.toFloat())
//vì khi view progress ta set mRectF.set(10f,10f,width-10f,height-10f) nên ở đây cũng vậy.không thì khi quay progress sẽ bị lệch
        val dst = RectF(10f, 10f, viewWidth.toFloat()-10, viewHeight.toFloat()-10)
//        val dst = RectF(0f, 0f, w.toFloat()-0, h.toFloat()-0)

        matrix.setRectToRect(src, dst, ScaleToFit.CENTER)
        val shader: Shader = BitmapShader(mBitmapProgress, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        shader.setLocalMatrix(matrix)
        mPaint.setShader(shader)
        matrix.mapRect(mRectF, src)
    }
    private fun setupBitmapFromSetting(){
        if (mIsPress&&mPressEnable){
            mBitmapBgButton=mBmBgButtonOn
        }else{
            mBitmapBgButton=mBmBgButtonOff
        }
    if (mViewEnable){
        mBitmapButton=mBmButtonOn
        mBitmapProgress=mBmProgressOn
    }else{
        mBitmapButton=mBmButtonOff
        mBitmapProgress=mBmProgressOff
    }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!.drawFilter=mPaintFlags
        canvas.save()
        setupParams()
        drawViewProgress(canvas)
        drawViewButton(canvas)
        canvas.restore()
        Log.e(TAG,"onDraw")
    }
    private fun setupParams(){
        if (degrees>MAX){
            degrees=MAX.toFloat()
        }else if (degrees<MIN){
            degrees=MIN.toFloat()
        }
        //tính toán sweepAngle dựa trên mỗi tiến trình
        sweepAngle = 282f * (degrees / MAX)
        Log.e(TAG,"degrees1 "+degrees)

        if (mListener!=null){
            mListener!!.onTouchChange(degrees.toInt())
        }

    }
    private fun drawViewProgress(canvas: Canvas) {
        //ở đây mRectF set như nào thì dst phần matrix.setRectToRect cũng phải set như thế k thì view progress khi quay sẽ bị lệch
        mRectF.set(10f,10f,width-10f,height-10f)
//        mRectF.set(0f,0f,width-0f,height-0f)
        drawBgProgress(canvas)
        drawProgress(canvas)
    }

    private fun drawViewButton(canvas: Canvas) {
        //ở đây +219 hay -141 hay bất kì 1 số khác là do chúng ta cần đưa nấc trên button về đúng vị trí ban đầu vì mỗi design vẽ nấc button ở vị trí khác nhau
//        val degrees = 219 + sweepAngle
        val degrees = sweepAngle-141
        //vì cái button bên trong bóng đè lên progress nên set lại khoảng cách vẽ
        mRectF.set(20f,20f,width-20f,height-20f)
        if (!mIsRotateBgButton) {
            drawBgButton(canvas)
            canvas.rotate(degrees, centerX, centerY)
            drawButton(canvas)
        }else{
            canvas.rotate(degrees, centerX, centerY)
            drawBgButton(canvas)
            drawButton(canvas)
        }
    }


    private fun drawBgProgress(canvas: Canvas){
        canvas.drawBitmap(mBitmapBgProgress,null,mRectF,null)
    }

    private fun drawProgress(canvas: Canvas){
        canvas.drawArc(mRectF,129f,sweepAngle,true,mPaint)
    }
    private fun drawBgButton(canvas: Canvas){
//        canvas.drawBitmap(mBitmapBgButton,mLeft,mTop,null)
//        mRectF.set(20f,20f,width-20f,height-20f)
        Log.e(TAG,"mIsPress :"+mIsPress+" mPressEnable :"+mPressEnable)
        if (mIsPress&&mPressEnable){
            mBitmapBgButton=mBmBgButtonOn
        }else{
            mBitmapBgButton=mBmBgButtonOff
        }
        canvas.drawBitmap(mBitmapBgButton,null,mRectF,null)
    }
    private fun drawButton(canvas: Canvas){
//        mRectF.set(20f,20f,width-20f,height-20f)
        canvas.drawBitmap(mBitmapButton,null,mRectF,null)
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources
            .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    fun convertPixelsToDp(px: Float, context: Context): Float {
        return px / (context.resources
            .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!mViewEnable){
            return false
        }
        if (event!!.action==MotionEvent.ACTION_UP){
            mIsPress=false
            if (mListener!=null){
                mListener!!.onTouchStop()
            }
        }
        if (event.action==MotionEvent.ACTION_DOWN){
            if (mListener!=null){
                mListener!!.onTouchStart()
            }
        }

        if (event!!.action==MotionEvent.ACTION_DOWN){
            val dx: Float = event.x - centerX
            val dy: Float = event.y - centerY
            val radians = Math.atan2(dy.toDouble(), dx.toDouble())
            downDegrees = (radians * 180 / Math.PI).toFloat()
            downDegrees -= 90f
            if (downDegrees < 0) {
                downDegrees += 360f
            }
            downDegrees =
                Math.floor(downDegrees / 360 * (MAX + 5).toDouble()).toFloat()
            return true
            Log.e(TAG,"down")
        }

        if (event!!.action==MotionEvent.ACTION_MOVE) {
            mIsPress = true
            onMoveView(event)
        }
        invalidate()
        return true
    }

    private fun onMoveView(event: MotionEvent){
        val dx: Float = event.x - centerX
        val dy: Float = event.y - centerY
        Log.e(TAG,"x "+event.x+" y "+event.y)
        val radians = Math.atan2(dy.toDouble(), dx.toDouble())
        currDegrees = (radians * 180 / Math.PI).toFloat()
        currDegrees -= 90f
        if (currDegrees < 0) {
            currDegrees += 360f
        }
        currDegrees =
            Math.floor(currDegrees / 360 * (MAX + 5).toDouble()).toFloat()

        if (currDegrees / (MAX + 4) > 0.75f && (downDegrees - 0) / (MAX + 4) < 0.25f) {
            degrees--
            if (degrees < 0) {
                degrees = 0f
            }
            downDegrees = currDegrees
        } else if (downDegrees / (MAX + 4) > 0.75f && (currDegrees - 0) / (MAX + 4) < 0.25f) {
            degrees++
            if (degrees > MAX) {
                degrees = MAX.toFloat()
            }
            downDegrees = currDegrees
        } else {
            degrees += currDegrees - downDegrees
            if (degrees > MAX) {
                degrees = MAX.toFloat()

            }
            if (degrees < 0) {
                degrees = 0f
            }
            downDegrees = currDegrees
        }
    }

    private fun setBackgroundProgressBitmap(bitmap:Bitmap){
        mBitmapBgProgress=bitmap
    }
    private fun setProgressOnBitmap(bitmap: Bitmap){
        mBmProgressOn=bitmap
    }
    private fun setProgressOffBitmap(bitmap: Bitmap){
        mBmProgressOff=bitmap
    }
    private fun setBackgroundButtonOnBitmap(bitmap: Bitmap){
        mBmBgButtonOn=bitmap
    }
    private fun setBackgroundButtonOffBitmap(bitmap: Bitmap){
        mBmBgButtonOff=bitmap
    }
    private fun setButtonOnBitmap(bitmap: Bitmap){
        mBmButtonOn=bitmap
    }
    private fun setButtonOffBitmap(bitmap: Bitmap){
        mBmButtonOff=bitmap
    }

    fun setBackgroundProgressDrawable(img:Int):ArcProgressView{
        val decodeResource=BitmapFactory.decodeResource(resources,img)
        setBackgroundProgressBitmap(decodeResource)
        return this
    }

    fun setProgressOnDrawable(img: Int):ArcProgressView{
        val decodeResource=BitmapFactory.decodeResource(resources,img)
        setProgressOnBitmap(decodeResource)
        return this
    }
    fun setProgressOffDrawable(img: Int):ArcProgressView{
        val decodeResource=BitmapFactory.decodeResource(resources,img)
        setProgressOffBitmap(decodeResource)
        return this
    }
    fun setBackgroundButtonOnDrawable(img: Int):ArcProgressView{
        val decodeResource=BitmapFactory.decodeResource(resources,img)
        setBackgroundButtonOnBitmap(decodeResource)
        return this
    }
    fun setBackgroundButtonOffDrawable(img: Int):ArcProgressView{
        val decodeResource=BitmapFactory.decodeResource(resources,img)
        setBackgroundButtonOffBitmap(decodeResource)
        return this
    }
    fun setButtonOnDrawable(img: Int):ArcProgressView{
        val decodeResource=BitmapFactory.decodeResource(resources,img)
        setButtonOnBitmap(decodeResource)
        return this
    }
    fun setButtonOffDrawable(img: Int):ArcProgressView{
        val decodeResource=BitmapFactory.decodeResource(resources,img)
        setButtonOffBitmap(decodeResource)
        return this
    }

    fun onResetBitmapView():ArcProgressView{
        setupBitmapFromSetting()
        setupPaintShaderProgress(width,height)
        return this
    }
    fun setViewEnable(enable:Boolean):ArcProgressView{
        mViewEnable=enable
        invalidate()
        return this
    }
    fun setPressEnable(enable:Boolean):ArcProgressView{
        mPressEnable=enable
//        invalidate()
        return this
    }
    fun setRotateBgButtonEnable(enable:Boolean):ArcProgressView{
        mIsRotateBgButton=enable
        return this
//        invalidate()
    }

    fun setMax(max:Int):ArcProgressView{
        MAX=max
        return this
    }
    fun getMax():Int{
        return MAX
    }
    fun setProgress(progress: Int):ArcProgressView{
       degrees=progress.toFloat()
        invalidate()
        return this

    }

    fun setOnSeekBarChangeListener(listener:OnSeekBarChangeListener){
    mListener=listener
    }
    fun getProgress():Int{
        return degrees.toInt()
    }

    interface OnSeekBarChangeListener{
        fun onTouchStart()
        fun onTouchStop()
        fun onTouchChange(progress:Int)
    }
}