package com.carlosalbpe.voicemodtest.framework.utils

data class Result<T>(val status : Status, val message : String, val data : T? = null)