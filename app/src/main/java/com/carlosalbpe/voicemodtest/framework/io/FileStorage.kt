package com.carlosalbpe.voicemodtest.framework.io

import java.io.File

interface FileStorage {

    fun createFile() : File

    fun getFile(path : String) : File

    fun deleteFile(path : String)

}