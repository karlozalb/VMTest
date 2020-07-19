package com.carlosalbpe.voicemodtest.framework.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.carlosalbpe.voicemodtest.data.model.VideoInfo

@Database(entities = [VideoInfo::class], version = 1)
abstract class VideoDatabase : RoomDatabase() {
    abstract fun videoInfoDao(): VideoInfoDao
}