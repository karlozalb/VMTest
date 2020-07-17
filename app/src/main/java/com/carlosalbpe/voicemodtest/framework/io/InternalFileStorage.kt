package com.carlosalbpe.voicemodtest.framework.io

import android.content.Context
import java.io.File

class InternalFileStorage(private val context : Context) : FileStorage {

    override fun createFile(): File {
        return File(context?.filesDir, "VoicemodTest_${System.currentTimeMillis()}.$fileExtension")
    }

    override fun getFile(path: String): File {
        return File(path)
    }

    override fun deleteFile(path: String) {
        File(path).delete()
    }

    companion object {
        const val fileExtension = ".mp4"
    }

}