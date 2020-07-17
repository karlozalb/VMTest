package com.carlosalbpe.voicemodtest.business.data

import com.carlosalbpe.voicemodtest.business.domain.VideoInfo
import java.io.File

interface VideoDataSource {

    suspend fun getVideos() : List<VideoInfo>
    suspend fun getVideo(id : Long) : VideoInfo
    suspend fun saveVideo(video : File) : VideoInfo
    suspend fun deleteVideo(video : VideoInfo) : Boolean

}