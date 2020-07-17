package com.carlosalbpe.voicemodtest.framework.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.carlosalbpe.voicemodtest.framework.di.AppScoped
import javax.inject.Inject
import javax.inject.Provider

@AppScoped
class ViewModelFactory @Inject constructor(private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModels[modelClass]?.get() as T

}
