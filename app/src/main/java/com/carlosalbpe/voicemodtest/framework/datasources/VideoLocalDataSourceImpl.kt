package com.carlosalbpe.voicemodtest.framework.datasources

import com.carlosalbpe.voicemodtest.data.VideoDataSource
import com.carlosalbpe.voicemodtest.framework.database.VideoDatabase
import com.carlosalbpe.voicemodtest.data.model.VideoInfo
import java.io.File
import javax.inject.Inject

open class VideoLocalDataSourceImpl @Inject constructor(val db : VideoDatabase) : VideoDataSource {

    @Throws(Exception::class)
    override suspend fun getVideos(): List<VideoInfo> {
        return db.videoInfoDao().getAll()
    }

    @Throws(Exception::class)
    override suspend fun getVideo(id: Long): VideoInfo = db.videoInfoDao().get(id)

    @Throws(Exception::class)
    override suspend fun saveVideo(video: File): VideoInfo {

        val id = db.videoInfoDao().insert(
            VideoInfo(
                video.absolutePath
            )
        )

        return getVideo(id)
    }

    @Throws(Exception::class)
    override suspend fun deleteVideo(video: VideoInfo): Boolean {
        db.videoInfoDao().delete(video)
        return true
    }

}