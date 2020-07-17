package com.carlosalbpe.voicemodtest.business.usecase

import com.carlosalbpe.voicemodtest.business.data.VideoRepository
import javax.inject.Inject

class GetVideosUseCase @Inject constructor(private val repository: VideoRepository) {

    suspend operator fun invoke() = repository.getVideos()

}