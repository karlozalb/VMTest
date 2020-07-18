package com.carlosalbpe.voicemodtest.framework.di

import android.app.Application
import com.carlosalbpe.voicemodtest.MyApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@AppScoped
@Component(modules = [
    AndroidSupportInjectionModule::class,
    ActivityBindingModule::class,
    FragmentsBindingModule::class,
    DispatcherModule::class,
    IOModule::class,
    ServicesModule::class,
    ViewModelModule::class
])
interface AppComponent : AndroidInjector<MyApplication> {

    override fun inject(application: MyApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }

}