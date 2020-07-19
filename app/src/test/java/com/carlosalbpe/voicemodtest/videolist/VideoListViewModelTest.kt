package com.carlosalbpe.voicemodtest.videolist

import android.os.Build
import androidx.lifecycle.Observer
import com.carlosalbpe.voicemodtest.data.VideoRepository
import com.carlosalbpe.voicemodtest.data.model.VideoInfo
import com.carlosalbpe.voicemodtest.usecase.DeleteVideoUseCase
import com.carlosalbpe.voicemodtest.usecase.GetVideosUseCase
import com.carlosalbpe.voicemodtest.framework.datasources.VideoLocalDataSourceImpl
import com.carlosalbpe.voicemodtest.testutils.observeOnce
import com.carlosalbpe.voicemodtest.ui.videolistfragment.viewmodel.VideoListViewModel
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import com.carlosalbpe.voicemodtest.framework.utils.Result
import com.carlosalbpe.voicemodtest.framework.utils.Status
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class VideoListViewModelTest {

    lateinit var videoListViewModel : VideoListViewModel

    lateinit var getVideosUseCase : GetVideosUseCase
    lateinit var deleteVideoUseCase : DeleteVideoUseCase

    lateinit var videoRepository : VideoRepository

    @Mock
    lateinit var dataSource: VideoLocalDataSourceImpl

    var mockObserver : Observer<Result<Boolean>> = mock()

    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this);
        videoRepository = VideoRepository(dataSource)
        getVideosUseCase = GetVideosUseCase(videoRepository)
        deleteVideoUseCase = DeleteVideoUseCase(videoRepository)
        videoListViewModel = VideoListViewModel(getVideosUseCase,deleteVideoUseCase, Dispatchers.Main)
    }

    @Test
    fun testGetVideos() = runBlocking (Dispatchers.Main) {
        `when`(dataSource.getVideos()).thenReturn(listOf(VideoInfo(path = "testPath1"),VideoInfo(path = "testPath2"),VideoInfo(path = "testPath3")))

        videoListViewModel.getVideos().observeOnce {
            assertNotNull(it.data)
            assertEquals(it.data!!.size,3)
        }
    }

    @Test
    fun testGetVideosEmpty() = runBlocking{
        `when`(dataSource.getVideos()).thenReturn(listOf())

        videoListViewModel.getVideos().observeOnce {
            assertNotNull(it.data)
            assertEquals(it.data!!.size,0)
        }
    }

    @Test
    fun testDeleteVideoSuccess() = runBlocking (Dispatchers.Main) {
        var video = VideoInfo(path = "fake")

        `when`(dataSource.deleteVideo(video)).thenReturn(true)

        videoListViewModel.deleteVideo(video).observeOnce {result ->
            assertTrue(result.status == Status.SUCCESS)
            assertNotNull(result.data)
            assertTrue(result.data!!)
        }
    }

    @Test
    fun testDeleteVideoError() = runBlocking (Dispatchers.Main) {
        var video = VideoInfo(path = "fake")

        `when`(dataSource.deleteVideo(video)).thenThrow(Exception("THIS IS A TEST EXCEPTION"))

        videoListViewModel.deleteVideo(video).observeOnce {result ->
            assertTrue(result.status == Status.ERROR)
            assertNull(result.data)
        }
    }

}