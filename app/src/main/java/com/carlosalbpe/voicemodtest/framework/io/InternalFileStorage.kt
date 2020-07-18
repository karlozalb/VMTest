package com.carlosalbpe.voicemodtest.framework.io

import android.content.Context
import androidx.lifecycle.liveData
import com.carlosalbpe.voicemodtest.framework.utils.Result
import com.carlosalbpe.voicemodtest.framework.utils.Status
import com.carlosalbpe.voicemodtest.ui.utils.getDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.io.File
import java.lang.Exception

open class InternalFileStorage(private val context : Context, private val dispatcher : CoroutineDispatcher? = null) : FileStorage {

    override fun createFile() = liveData(getDispatcher(dispatcher, Dispatchers.IO)) {
        try {
            emit(Result(status= Status.SUCCESS, message="Ok",data = File(context?.filesDir, "VoicemodTest_${System.currentTimeMillis()}$FILE_EXTENSION")))
        } catch (error : Exception) {
            emit(Result(
                    status = Status.ERROR,
                    message = error.message ?: ""
                )
            )
            error.printStackTrace()
        }
    }

    override fun getFile(path: String) = liveData(getDispatcher(dispatcher, Dispatchers.IO)) {
        try {
            emit(Result(status= Status.SUCCESS, message="Ok",data = File(path)))
        } catch (error : Exception) {
            emit(Result(
                status = Status.ERROR,
                message = error.message ?: ""
            )
            )
            error.printStackTrace()
        }
    }

    override fun deleteFile(path: String) = liveData(getDispatcher(dispatcher, Dispatchers.IO)) {
        try {
            if (File(path).delete()) {
                emit(Result(status = Status.SUCCESS, message = "Ok", data = true))
            } else {
                emit(Result(
                    status = Status.ERROR,
                    message = "Error deleting file"
                ))
            }
        } catch (error : Exception) {
            emit(Result(
                status = Status.ERROR,
                message = error.message ?: ""
            ))
            error.printStackTrace()
        }
    }

    companion object {
        const val FILE_EXTENSION = ".mp4"
    }

}