package com.carlosalbpe.voicemodtest.ui.videofragment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosalbpe.voicemodtest.business.domain.VideoInfo
import com.carlosalbpe.voicemodtest.business.usecase.GetVideoUseCase
import com.carlosalbpe.voicemodtest.framework.utils.Result
import com.carlosalbpe.voicemodtest.framework.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class VideoPlayerViewModel @Inject constructor(private val getVideoUseCase: GetVideoUseCase): ViewModel() {

    private val videoLd = MutableLiveData<Result<VideoInfo>>()

    fun getVideo(id : Long) : LiveData<Result<VideoInfo>> {
        fetchVideo(id)
        return videoLd
    }

    private fun fetchVideo(id : Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                videoLd.postValue(Result(status = Status.LOADING))
                videoLd.postValue(Result(status = Status.SUCCESS, message = "Ok", data = getVideoUseCase(id)))
            } catch (error : Exception) {
                videoLd.postValue(Result(status = Status.ERROR, message = error.message ?: ""))
                error.printStackTrace()
            }
        }
    }

}