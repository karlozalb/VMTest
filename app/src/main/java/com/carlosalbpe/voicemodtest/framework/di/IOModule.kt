package com.carlosalbpe.voicemodtest.framework.di

import android.app.Application
import androidx.room.Room
import com.carlosalbpe.voicemodtest.data.VideoDataSource
import com.carlosalbpe.voicemodtest.data.VideoRepository
import com.carlosalbpe.voicemodtest.framework.database.VideoDatabase
import com.carlosalbpe.voicemodtest.framework.io.FileStorage
import com.carlosalbpe.voicemodtest.framework.io.InternalFileStorage
import com.carlosalbpe.voicemodtest.framework.datasources.VideoLocalDataSourceImpl
import dagger.Module
import dagger.Provides

@Module
class IOModule {

    @AppScoped
    @Provides
    fun provideVideoInfoDatabase(applicationContext : Application) : VideoDatabase{
        return Room.databaseBuilder(applicationContext, VideoDatabase::class.java,"videosdb").build()
    }

    @AppScoped
    @Provides
    fun provideFileStorage(applicationContext : Application) : FileStorage{
        return InternalFileStorage(applicationContext)
    }

    @AppScoped
    @Provides
    fun provideDataSource(database : VideoDatabase) : VideoDataSource{
        return VideoLocalDataSourceImpl(database)
    }

    @AppScoped
    @Provides
    fun provideVideoRepository(dataSource : VideoDataSource) : VideoRepository{
        return VideoRepository(dataSource)
    }

}