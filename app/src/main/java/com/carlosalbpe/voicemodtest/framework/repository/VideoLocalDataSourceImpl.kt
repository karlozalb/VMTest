package com.carlosalbpe.voicemodtest.framework.repository

import com.carlosalbpe.voicemodtest.business.data.VideoDataSource
import com.carlosalbpe.voicemodtest.framework.database.VideoDatabase
import com.carlosalbpe.voicemodtest.business.domain.VideoInfo
import com.carlosalbpe.voicemodtest.framework.io.FileStorage
import java.io.File
import java.nio.file.FileStore
import javax.inject.Inject

class VideoLocalDataSourceImpl @Inject constructor(val db : VideoDatabase, val fileStorage: FileStorage) : VideoDataSource {

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
        fileStorage.deleteFile(video.path)

        return true
    }

}