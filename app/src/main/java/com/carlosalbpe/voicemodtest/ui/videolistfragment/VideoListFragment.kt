package com.carlosalbpe.voicemodtest.ui.videolistfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carlosalbpe.voicemodtest.MainActivity
import com.carlosalbpe.voicemodtest.R
import com.carlosalbpe.voicemodtest.data.model.VideoInfo
import com.carlosalbpe.voicemodtest.framework.io.FileStorage
import com.carlosalbpe.voicemodtest.framework.utils.Status
import com.carlosalbpe.voicemodtest.ui.utils.toast
import com.carlosalbpe.voicemodtest.ui.videolistfragment.viewmodel.VideoListViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.video_list_fragment.*
import javax.inject.Inject


class VideoListFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var fileStorage: FileStorage

    private lateinit var viewModel: VideoListViewModel

    lateinit var videosAdapter: VideoAdapter
    lateinit var layoutManager: LinearLayoutManager

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
                        deleteFileFromDevice(video)
                    }else if (result.status == Status.ERROR){
                        context?.toast(getString(R.string.delete_file_error))
                    }
                })
            }

        })

        layoutManager = LinearLayoutManager(requireContext())

        videos_rv.layoutManager = layoutManager
        videos_rv.adapter = videosAdapter

        videos_rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                updateFabVisibility(dx,dy)
            }
        })

        viewModel.getVideos().observe(viewLifecycleOwner, Observer {result ->
            if (result.status == Status.SUCCESS) {
                videosAdapter.setItems(result.data)
                updateFabVisibility(0,0)
                setEmptyView(result.data)
            }else if (result.status == Status.ERROR){
                context?.toast(getString(R.string.list_fetch_error))
            }
        })
    }

    fun updateFabVisibility(dx: Int, dy: Int){
        var activity = (activity as MainActivity)

        val pos = layoutManager.findLastCompletelyVisibleItemPosition()
        val numItems: Int = videosAdapter.itemCount

        if (dy < 0 && !activity.isShown() || pos >= numItems) activity.showFab() else if (dy > 0 && activity.isShown()) activity.hideFab()
    }

    fun deleteFileFromDevice(video : VideoInfo){
        fileStorage.deleteFile(video.path).observe(viewLifecycleOwner, Observer { result->
            if (result.status == Status.SUCCESS) {
                context?.toast(getString(R.string.delete_file_success))
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
