package com.carlosalbpe.voicemodtest.framework.io

import com.carlosalbpe.voicemodtest.framework.utils.Result
import androidx.lifecycle.LiveData
import java.io.File

interface FileStorage {

    fun createFile() : LiveData<Result<File>>

    fun getFile(path : String) : LiveData<Result<File>>

    fun deleteFile(path : String) : LiveData<Result<Boolean>>

}