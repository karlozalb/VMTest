package com.carlosalbpe.voicemodtest.framework.di

import android.app.Application
import android.content.Context
import android.hardware.camera2.CameraManager
import dagger.Module
import dagger.Provides

@Module
class ServicesModule {

    @AppScoped
    @Provides
    fun provideCameraManager(application: Application) : CameraManager {
        return application.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }


}