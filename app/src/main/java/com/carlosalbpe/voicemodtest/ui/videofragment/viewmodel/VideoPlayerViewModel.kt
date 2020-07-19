package com.carlosalbpe.voicemodtest.ui.videofragment.viewmodel

import androidx.lifecycle.liveData
import com.carlosalbpe.voicemodtest.usecase.GetVideoUseCase
import com.carlosalbpe.voicemodtest.framework.utils.BaseViewModel
import com.carlosalbpe.voicemodtest.framework.utils.Result
import com.carlosalbpe.voicemodtest.framework.utils.Status
import com.carlosalbpe.voicemodtest.ui.utils.getDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import javax.inject.Inject

class VideoPlayerViewModel @Inject constructor(private val getVideoUseCase: GetVideoUseCase, private val dispatcher : CoroutineDispatcher? = null): BaseViewModel() {

    init {
        defaultDispatcher = Dispatchers.IO
    }

    fun getVideo(id : Long) = liveData(getDispatcher(dispatcher,defaultDispatcher)) {
        try {
            emit(Result(status = Status.SUCCESS, message = "Ok", data = getVideoUseCase(id)))
        } catch (error : Exception) {
            emit(Result(status = Status.ERROR, message = error.message ?: ""))
            error.printStackTrace()
        }
    }

}