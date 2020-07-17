package com.carlosalbpe.voicemodtest.framework.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.carlosalbpe.voicemodtest.business.domain.VideoInfo

@Dao
interface VideoInfoDao {

    @Query("SELECT * FROM videoinfo")
    fun getAll(): List<VideoInfo>

    @Query("SELECT * FROM videoinfo WHERE uid == :id")
    fun get(id : Long) : VideoInfo

    @Insert
    fun insert(video : VideoInfo) : Long

    @Delete
    fun delete(video : VideoInfo)

}