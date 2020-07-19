package com.carlosalbpe.voicemodtest.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class VideoInfo(
    @ColumnInfo(name = "path")
    val path: String
){
    @PrimaryKey(autoGenerate = true)
    var uid: Long = 0
}