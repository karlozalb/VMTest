package com.carlosalbpe.voicemodtest.ui.camera.viewmodel

import androidx.lifecycle.*
import com.carlosalbpe.voicemodtest.business.usecase.SaveVideoUseCase
import java.io.File
import java.lang.Exception
import javax.inject.Inject
import com.carlosalbpe.voicemodtest.framework.utils.Result
import com.carlosalbpe.voicemodtest.framework.utils.Status
import kotlinx.coroutines.Dispatchers

class CameraViewModel @Inject constructor(private val saveVideoUseCase : SaveVideoUseCase) : ViewModel() {

    /**
     * Saves a video in our repository
     */
    fun saveVideo(file : File) = liveData<Result<Unit>>(Dispatchers.IO) {
        try {
            emit(Result(status = Status.LOADING))
            saveVideoUseCase(file)
            emit(Result(status = Status.SUCCESS, message = "Ok"))
        } catch (error : Exception) {
            emit(Result(status = Status.ERROR, message = error.message ?: ""))
            error.printStackTrace()
        }
    }


}