package com.carlosalbpe.voicemodtest.ui.camera.viewmodel

import androidx.lifecycle.*
import com.carlosalbpe.voicemodtest.business.usecase.SaveVideoUseCase
import com.carlosalbpe.voicemodtest.framework.utils.BaseViewModel
import java.io.File
import java.lang.Exception
import javax.inject.Inject
import com.carlosalbpe.voicemodtest.framework.utils.Result
import com.carlosalbpe.voicemodtest.framework.utils.Status
import com.carlosalbpe.voicemodtest.ui.utils.getDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class CameraViewModel @Inject constructor(private val saveVideoUseCase : SaveVideoUseCase, private val dispatcher : CoroutineDispatcher? = null) : BaseViewModel() {

    init {
        defaultDispatcher = Dispatchers.IO
    }

    /**
     * Saves a video in our repository
     */
    fun saveVideo(file : File) = liveData(getDispatcher(dispatcher,defaultDispatcher)) {
        try {
            saveVideoUseCase(file)
            emit(Result<Unit>(status = Status.SUCCESS, message = "Ok"))
        } catch (error : Exception) {
            emit(Result<Unit>(status = Status.ERROR, message = error.message ?: ""))
            error.printStackTrace()
        }
    }


}