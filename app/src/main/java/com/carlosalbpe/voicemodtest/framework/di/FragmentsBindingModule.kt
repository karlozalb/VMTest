package com.carlosalbpe.voicemodtest.framework.di

import com.carlosalbpe.voicemodtest.ui.videolistfragment.VideoListFragment
import com.carlosalbpe.voicemodtest.ui.camera.CameraFragment
import com.carlosalbpe.voicemodtest.ui.videofragment.VideoPlayerFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentsBindingModule {

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun videoListFragment(): VideoListFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun videoPlayerFragment(): VideoPlayerFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun cameraFragment(): CameraFragment

}