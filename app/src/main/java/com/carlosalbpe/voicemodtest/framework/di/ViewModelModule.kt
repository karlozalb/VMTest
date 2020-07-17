package com.carlosalbpe.voicemodtest.framework.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.carlosalbpe.voicemodtest.framework.utils.ViewModelFactory
import com.carlosalbpe.voicemodtest.ui.camera.viewmodel.CameraViewModel
import com.carlosalbpe.voicemodtest.ui.videofragment.viewmodel.VideoPlayerViewModel
import com.carlosalbpe.voicemodtest.ui.videolistfragment.viewmodel.VideoListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @AppScoped
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CameraViewModel::class)
    abstract fun bindCameraViewModel(viewModel: CameraViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VideoListViewModel::class)
    abstract fun bindVideoListViewModel(viewModel: VideoListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VideoPlayerViewModel::class)
    abstract fun bindVideoPlayerViewModel(viewModel: VideoPlayerViewModel): ViewModel


}