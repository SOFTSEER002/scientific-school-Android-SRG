package com.jeannypr.scientificstudy.Login.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import java.lang.ref.WeakReference

open class BaseViewModel<T> : ViewModel() {
    private var mNavigator: WeakReference<T>? = null
    val isLoading = ObservableBoolean(false)

    var navigator: T?
        get() = mNavigator!!.get()
        set(value) {
            this.mNavigator = WeakReference<T>(value)
        }

    /**
     * Created to set loader
     * @param isLoading
     */
    fun setIsLoading(isLoading: Boolean) {
        this.isLoading.set(isLoading)
    }
}
