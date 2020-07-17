package com.carlosalbpe.voicemodtest.ui.videolistfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlosalbpe.voicemodtest.R
import com.carlosalbpe.voicemodtest.business.domain.VideoInfo
import com.carlosalbpe.voicemodtest.framework.io.FileStorage
import com.carlosalbpe.voicemodtest.framework.utils.Status
import com.carlosalbpe.voicemodtest.ui.utils.toast
import com.carlosalbpe.voicemodtest.ui.videolistfragment.viewmodel.VideoListViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.video_list_fragment.*
import javax.inject.Inject

class VideoListFragment @Inject constructor() : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var fileStorage: FileStorage

    private lateinit var viewModel: VideoListViewModel

    lateinit var videosAdapter: VideoAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.video_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory).get(VideoListViewModel::class.java)
        setUpList()
    }

    fun setUpList(){
        videosAdapter = VideoAdapter(listOf(),object: IClickItemInterface{

            //Navigate to video player
            override fun onClick(video: VideoInfo) {
                navigateToVideoPlayer(video.uid)
            }

        }, object: IClickItemInterface{

            //Delete item
            override fun onClick(video: VideoInfo) {
                viewModel.deleteVideo(video).observe(viewLifecycleOwner, Observer {result ->
                    if (result.status == Status.SUCCESS) {

                        context?.toast(getString(R.string.delete_file_success))
                    }else if (result.status == Status.ERROR){
                        context?.toast(getString(R.string.delete_file_error))
                    }
                })
            }

        })
        videos_rv.layoutManager = LinearLayoutManager(requireContext())
        videos_rv.adapter = videosAdapter

        viewModel.getVideos().observe(viewLifecycleOwner, Observer {result ->
            if (result.status == Status.SUCCESS) {
                videosAdapter.setItems(result.data)
                setEmptyView(result.data)
            }else if (result.status == Status.ERROR){
                context?.toast(getString(R.string.list_fetch_error))
            }
        })
    }

    fun deleteFileFromDevice(video : VideoInfo){
        fileStorage.deleteFile(video.path).observe(viewLifecycleOwner, Observer { result->
            if (result.status == Status.SUCCESS) {
                //OK!!!!
            }else if (result.status == Status.ERROR){
                context?.toast(getString(R.string.delete_file_error))
                findNavController().popBackStack()
            }
        })
    }

    fun setEmptyView(data : List<VideoInfo>?){
        empty_tv.visibility = if (data.isNullOrEmpty()) View.VISIBLE else View.GONE
    }

    fun navigateToVideoPlayer(id : Long){
        val action = VideoListFragmentDirections.actionVideoFragmentToVideoPlayerFragment(id)
        findNavController().navigate(action)
    }

}
