package com.carlosalbpe.voicemodtest.ui.videofragment

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.carlosalbpe.voicemodtest.R
import com.carlosalbpe.voicemodtest.business.domain.VideoInfo
import com.carlosalbpe.voicemodtest.ui.videofragment.viewmodel.VideoPlayerViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_video_player.*
import kotlinx.android.synthetic.main.fragment_video_player.view.*
import java.lang.Exception
import javax.inject.Inject


class VideoPlayerFragment : DaggerFragment(), MediaPlayer.OnPreparedListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: VideoPlayerViewModel

    private var mediaPlayer : MediaPlayer? = MediaPlayer()
    private lateinit var surfaceHolder : SurfaceHolder

    private val args: VideoPlayerFragmentArgs by navArgs()

    private var currentHolder : SurfaceHolder? = null
    private var currentVideo : VideoInfo? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_video_player, container, false)

        surfaceHolder = view.video_view.holder
        view.video_view.keepScreenOn = true
        surfaceHolder.addCallback(object: SurfaceHolder.Callback{
            override fun surfaceChanged(
                holder: SurfaceHolder?,
                format: Int,
                width: Int,
                height: Int
            ) {
                Log.v(TAG,"Surface changed")
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                Log.v(TAG,"Surface destroyed")
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                Log.v(TAG,"Surface created")
                currentHolder = holder
                //This call will be useless 99.999999999% of times, but... who knows. (Carlos AP)
                setUpMediaPlayer()
            }
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory).get(VideoPlayerViewModel::class.java)

        viewModel.getVideo(args.id).observe(viewLifecycleOwner, Observer { result ->
            result.data?.run {
                currentVideo = this
                setUpMediaPlayer();
            }
        })

        setUpButton()
    }

    fun setUpMediaPlayer(){
        if (currentVideo != null && currentHolder != null) {
            try {
                mediaPlayer?.apply {
                    setDisplay(currentHolder)
                    setDataSource(currentVideo!!.path)
                    prepareAsync()
                    setOnPreparedListener(this@VideoPlayerFragment)
                    setOnCompletionListener {
                        playBackFinished()
                    }
                }
            } catch (error: Exception) {
                error.printStackTrace()
            }
        }
    }

    override fun onPrepared(mp: MediaPlayer?) {
        mp?.start()
        showPauseButton()
    }

    fun playBackFinished(){
        showPlayButton()
    }

    fun showPlayButton(){
        play_button.setBackgroundResource(R.drawable.play_white)
    }

    fun showPauseButton(){
        play_button.setBackgroundResource(R.drawable.pause_white)
    }

    private fun setUpButton() {
        play_button.setOnClickListener {

            mediaPlayer?.run {
                if (isPlaying){
                    pause()
                    showPlayButton()
                }else{
                    start()
                    showPauseButton()
                }
            }
        }
    }

    private fun disposeMediaPlayer(){
        mediaPlayer?.apply {
            release()
        }

        mediaPlayer = null
    }

    override fun onPause() {
        super.onPause()
        disposeMediaPlayer()
    }

    companion object {
        val TAG = VideoPlayerFragment::class.java.simpleName
    }

}
