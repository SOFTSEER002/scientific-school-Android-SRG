package com.jeannypr.scientificstudy.Base.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Created by Kannu
 * A provider factory that persists ViewModels [ViewModel].
 * Used if the view model has a parameterized constructor.
 */
class ViewModelProviderFactory<V>(private val viewModel: V) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(modelClass)) return viewModel as T
        throw IllegalArgumentException("Unknown class productCode")
    }

}