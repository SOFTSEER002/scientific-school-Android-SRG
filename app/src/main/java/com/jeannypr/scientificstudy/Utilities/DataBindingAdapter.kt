package com.jeannypr.scientificstudy.Utilities

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.jeannypr.scientificstudy.R

/**
 *Created by kannuk on
 **/
object DataBindingAdapter {
    @BindingAdapter("textToBeFormat")
    fun TextView.setTextToBeFormat(value: String?) {
        this.text = String.format(resources.getString(R.string.youAreA), value)
    }

    /*@BindingAdapter("frontImageBitmap")
    @JvmStatic
    var ImageView: `fun` = null.open

    fun setFrontImageBitmap() {
        this.setImageBitmap(frontImage)
    }*/
}