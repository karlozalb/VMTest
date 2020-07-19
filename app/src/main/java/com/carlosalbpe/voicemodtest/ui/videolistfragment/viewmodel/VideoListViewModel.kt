package com.carlosalbpe.voicemodtest.ui.videolistfragment.viewmodel

import androidx.lifecycle.*
import com.carlosalbpe.voicemodtest.data.model.VideoInfo
import com.carlosalbpe.voicemodtest.usecase.DeleteVideoUseCase
import com.carlosalbpe.voicemodtest.usecase.GetVideosUseCase
import com.carlosalbpe.voicemodtest.framework.utils.BaseViewModel
import com.carlosalbpe.voicemodtest.framework.utils.Result
import com.carlosalbpe.voicemodtest.framework.utils.Status
import com.carlosalbpe.voicemodtest.ui.utils.getDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class VideoListViewModel @Inject constructor(private val getVideosUseCase: GetVideosUseCase, private val deleteVideoUseCase: DeleteVideoUseCase, private val dispatcher : CoroutineDispatcher? = null): BaseViewModel() {

    init {
        defaultDispatcher = Dispatchers.IO
    }

    private val videosLd = MutableLiveData<Result<List<VideoInfo>>>()

    fun getVideos() : LiveData<Result<List<VideoInfo>>> {
        fetchVideos()
        return videosLd
    }

    fun deleteVideo(video : VideoInfo) = liveData(getDispatcher(dispatcher,defaultDispatcher)) {
        try {
            emit(Result(status = Status.SUCCESS, message = "Ok", data = deleteVideoUseCase(video)))
            fetchVideos()
        } catch (error : Exception) {
            emit(Result(status = Status.ERROR, message = error.message ?: ""))
            error.printStackTrace()
        }
    }

    private fun fetchVideos() {
        viewModelScope.launch(getDispatcher(dispatcher,defaultDispatcher)) {
            try {
                videosLd.postValue(Result(status = Status.SUCCESS, message = "Ok", data = getVideosUseCase()))
            } catch (error : Exception) {
                videosLd.postValue(Result(status = Status.ERROR, message = error.message ?: ""))
                error.printStackTrace()
            }
        }
    }

}