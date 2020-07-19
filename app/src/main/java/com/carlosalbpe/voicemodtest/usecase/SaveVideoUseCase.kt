package com.carlosalbpe.voicemodtest.usecase

import com.carlosalbpe.voicemodtest.data.VideoRepository
import java.io.File
import javax.inject.Inject

class SaveVideoUseCase @Inject constructor(private val repository: VideoRepository) {

    suspend operator fun invoke(file : File) = repository.saveVideo(file)

}