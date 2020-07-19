package com.carlosalbpe.voicemodtest.usecase

import com.carlosalbpe.voicemodtest.data.VideoRepository
import javax.inject.Inject

class GetVideoUseCase @Inject constructor(private val repository: VideoRepository) {

    suspend operator fun invoke(id : Long) = repository.getVideo(id)

}