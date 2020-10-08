package com.jeannypr.scientificstudy.Utilities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.jeannypr.scientificstudy.MainActivity

class SplashActivity : AppCompatActivity() {
    private val spinner: ProgressBar? = null
    var txtAppName: TextView? = null
    var appName: String? = null
    var context: Context? = null
    @Override
    protected fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.jeannypr.scientificstudy.R.layout.activity_splash_pro)
        context = this
        txtAppName = findViewById(com.jeannypr.scientificstudy.R.id.appName)
        txtAppName.setText(com.jeannypr.scientificstudy.R.string.flavored_app_name)
        txtAppName.setVisibility(View.VISIBLE)
        txtAppName.setAlpha(0f)
        txtAppName.setScaleX(0f)
        txtAppName.setScaleY(0f)
        txtAppName.animate()
                .alpha(1f)
                .scaleX(1f).scaleY(1f)
                .setDuration(2000)
                .start()
        Handler().postDelayed(object : Runnable() {
            @Override
            fun run() {
                val i = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(i)
                finish()
            }
        }, 3000)
    }
}