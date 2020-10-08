package com.jeannypr.scientificstudy.Utilities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.jeannypr.scientificstudy.Base.activity.MainActivity
import com.jeannypr.scientificstudy.R

class SplashActivity : AppCompatActivity() {
    private val spinner: ProgressBar? = null
    lateinit var txtAppName: RelativeLayout
    var appName: String? = null
    var context: Context? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_free)
        context = this
        txtAppName = findViewById(R.id.appName)
        txtAppName.setVisibility(View.VISIBLE)
        txtAppName.setAlpha(0f)
        txtAppName.setScaleX(0f)
        txtAppName.setScaleY(0f)
        txtAppName.animate()
                .alpha(1f)
                .scaleX(1f).scaleY(1f)
                .setDuration(2000)
                .start()
        Handler().postDelayed({
            val i = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(i)
            finish()
        }, 3000)
    }
}