package com.carlosalbpe.voicemodtest.framework.utils

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineDispatcher

open class BaseViewModel : ViewModel() {

    protected lateinit var defaultDispatcher : CoroutineDispatcher

}