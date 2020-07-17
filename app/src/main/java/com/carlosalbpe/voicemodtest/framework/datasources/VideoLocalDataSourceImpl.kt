package com.carlosalbpe.voicemodtest.framework.datasources

import com.carlosalbpe.voicemodtest.business.data.VideoDataSource
import com.carlosalbpe.voicemodtest.framework.database.VideoDatabase
import com.carlosalbpe.voicemodtest.business.domain.VideoInfo
import com.carlosalbpe.voicemodtest.framework.io.FileStorage
import java.io.File
import javax.inject.Inject

class VideoLocalDataSourceImpl @Inject constructor(val db : VideoDatabase) : VideoDataSource {

    override suspend fun getVideos(): List<VideoInfo> {
        return db.videoInfoDao().getAll()
    }

    override suspend fun getVideo(id: Long): VideoInfo = db.videoInfoDao().get(id)

    override suspend fun saveVideo(video: File): VideoInfo {

        val id = db.videoInfoDao().insert(
            VideoInfo(
                video.absolutePath
            )
        )

        return getVideo(id)
    }

    override suspend fun deleteVideo(video: VideoInfo): Boolean {
        db.videoInfoDao().delete(video)
        return true
    }

}