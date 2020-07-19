package com.carlosalbpe.voicemodtest.data

import com.carlosalbpe.voicemodtest.data.model.VideoInfo
import java.io.File

class VideoRepository (private val dataSource: VideoDataSource) {

    suspend fun getVideos() : List<VideoInfo> = dataSource.getVideos()

    suspend fun getVideo(id : Long) : VideoInfo = dataSource.getVideo(id)

    suspend fun saveVideo(video: File) : VideoInfo = dataSource.saveVideo(video)

    suspend fun deleteVideo(video : VideoInfo) : Boolean = dataSource.deleteVideo(video)

}