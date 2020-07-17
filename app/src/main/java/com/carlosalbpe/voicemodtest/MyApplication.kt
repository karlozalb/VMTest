package com.carlosalbpe.voicemodtest

import com.carlosalbpe.voicemodtest.framework.di.DaggerAppComponent
import dagger.android.support.DaggerApplication

class MyApplication : DaggerApplication() {

    private val applicationInjector = DaggerAppComponent.builder()
        .application(this)
        .build()

    override fun applicationInjector() = applicationInjector

}