package com.carlosalbpe.voicemodtest.framework.di

import javax.inject.Scope

@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScoped

@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class AppScoped

@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@kotlin.annotation.Target(
    AnnotationTarget.TYPE,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.CLASS
)
annotation class FragmentScoped