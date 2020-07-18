package com.carlosalbpe.voicemodtest.framework.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher

@Module
class DispatcherModule {

    @Provides
    fun defaultDispatcher() : CoroutineDispatcher? {
        return null
    }

}