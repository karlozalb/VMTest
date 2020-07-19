package com.carlosalbpe.voicemodtest.usecase

import com.carlosalbpe.voicemodtest.data.VideoRepository
import com.carlosalbpe.voicemodtest.data.model.VideoInfo
import javax.inject.Inject

class DeleteVideoUseCase @Inject constructor(private val repository: VideoRepository) {

    suspend operator fun invoke(video : VideoInfo) = repository.deleteVideo(video)

}