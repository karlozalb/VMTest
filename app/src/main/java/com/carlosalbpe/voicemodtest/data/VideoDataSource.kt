package com.carlosalbpe.voicemodtest.data

import com.carlosalbpe.voicemodtest.data.model.VideoInfo
import java.io.File

interface VideoDataSource {

    @Throws(Exception::class)
    suspend fun getVideos() : List<VideoInfo>

    @Throws(Exception::class)
    suspend fun getVideo(id : Long) : VideoInfo

    @Throws(Exception::class)
    suspend fun saveVideo(video : File) : VideoInfo

    @Throws(Exception::class)
    suspend fun deleteVideo(video : VideoInfo) : Boolean

}