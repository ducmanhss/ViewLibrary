package com.manh.lib.viewlibrary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.manh.lib.view.audio.ArcProgressView
import com.manh.lib.view.audio.VerticalSeekbarView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
val TAG="MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        arc.setBackgroundProgressDrawable(R.drawable.bg_bass_04_default)
//            .setProgressOffDrawable(R.drawable.bg_bass_04_off)
//            .setProgressOnDrawable(R.drawable.bg_bass_04_schedule)
//            .setButtonOffDrawable(R.drawable.btn_bass_04_top_off)
//            .setButtonOnDrawable(R.drawable.btn_bass_04_top)
//            .setBackgroundButtonOffDrawable(R.drawable.btn_bass_04_bottom)
//            .setBackgroundButtonOnDrawable(R.drawable.btn_bass_04_bottom_schedule)
//            .onResetBitmapView()
//            .setMax(300)
//            .setProgress(200)
//            .setOnSeekBarChangeListener(object :ArcProgressView.OnSeekBarChangeListener{
//                override fun onTouchStop() {
//                    Log.e(TAG,"onTouchStop ")
//                }
////
//                override fun onTouchChange(progress: Int) {
//                    Log.e(TAG,"progress "+progress)
//                }
//
//            })
////            .setViewEnable(true)

sbv
    .setMax(30)

    .setProgress(10)
    .setOnVerticalSeekbarViewChangeListener(object :VerticalSeekbarView.OnVerticalSeekbarViewChangeListener{
        override fun onSeekbarChanged(progress: Int) {
            Log.e(TAG,"VerticalSB "+progress)
        }

        override fun onSeekbarStart() {

        }

        override fun onSeekbarStop() {

        }

    })
    }

}
