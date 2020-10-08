package com.jeannypr.scientificstudy.Utilities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.Nullable
import com.jeannypr.scientificstudy.Base.activity.MainActivity

class SplashActivity : AppCompatActivity() {
    private val spinner: ProgressBar? = null
    //    var txtAppName: TextView? = null
    var appName: String? = null
    var context: Context? = null

    protected override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.jeannypr.scientificstudy.R.layout.activity_splash_pro)
        context = this
        val txtAppName = findViewById<TextView>(com.jeannypr.scientificstudy.R.id.appName)
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
        Handler().postDelayed({
            val i = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(i)
            finish()
        }, 3000)
    }
}