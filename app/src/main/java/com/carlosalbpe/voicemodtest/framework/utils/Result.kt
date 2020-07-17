package com.carlosalbpe.voicemodtest.framework.utils

/**
 * Utility class used to hold a specific state+data
 */
data class Result<T>(val status : Status, val message : String = "", val data : T? = null)