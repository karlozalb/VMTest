package com.carlosalbpe.voicemodtest.business.usecase

import com.carlosalbpe.voicemodtest.business.data.VideoRepository
import com.carlosalbpe.voicemodtest.business.domain.VideoInfo
import javax.inject.Inject

class DeleteVideoUseCase @Inject constructor(private val repository: VideoRepository) {

    suspend operator fun invoke(video : VideoInfo) = repository.deleteVideo(video)

}