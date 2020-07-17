package com.carlosalbpe.voicemodtest.ui.videolistfragment.viewmodel

import androidx.lifecycle.*
import com.carlosalbpe.voicemodtest.business.domain.VideoInfo
import com.carlosalbpe.voicemodtest.business.usecase.DeleteVideoUseCase
import com.carlosalbpe.voicemodtest.business.usecase.GetVideosUseCase
import com.carlosalbpe.voicemodtest.framework.utils.Result
import com.carlosalbpe.voicemodtest.framework.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject
import kotlin.random.Random

class VideoListViewModel @Inject constructor(private val getVideosUseCase: GetVideosUseCase, private val deleteVideoUseCase: DeleteVideoUseCase): ViewModel() {

    var random : Int = 0

    init {
        random = Random.nextInt(0, 100)
    }

    private val videosLd = MutableLiveData<Result<List<VideoInfo>>>()

    fun getVideos() : LiveData<Result<List<VideoInfo>>> {
        System.out.println(random)
        fetchVideos()
        return videosLd
    }

    fun deleteVideo(video : VideoInfo) = liveData(Dispatchers.IO) {
        try {
            emit(Result(status = Status.SUCCESS, message = "Ok", data = deleteVideoUseCase(video)))
            fetchVideos()
        } catch (error : Exception) {
            emit(Result(status = Status.ERROR, message = error.message ?: ""))
            error.printStackTrace()
        }
    }

    private fun fetchVideos() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                videosLd.postValue(Result(status = Status.SUCCESS, message = "Ok", data = getVideosUseCase()))
            } catch (error : Exception) {
                videosLd.postValue(Result(status = Status.ERROR, message = error.message ?: ""))
                error.printStackTrace()
            }
        }
    }

}