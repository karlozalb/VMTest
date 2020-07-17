package com.carlosalbpe.voicemodtest.framework.di

import com.carlosalbpe.voicemodtest.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector()
    internal abstract fun mainActivity(): MainActivity

}