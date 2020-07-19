package com.carlosalbpe.voicemodtest.ui.videolistfragment

import androidx.databinding.library.baseAdapters.BR
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.carlosalbpe.voicemodtest.R
import com.carlosalbpe.voicemodtest.data.model.VideoInfo
import com.carlosalbpe.voicemodtest.databinding.VideoListItemBinding

class VideoAdapter(private var items : List<VideoInfo>, private val listener : IClickItemInterface, private val deleteListener : IClickItemInterface) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    class VideoViewHolder(private val binding : VideoListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(video : VideoInfo, listener : IClickItemInterface, deleteListener : IClickItemInterface){
            binding.setVariable(BR.videoInfo, video)
            binding.setVariable(BR.listener, listener)
            binding.setVariable(BR.deleteListener, deleteListener)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.video_list_item
    }

    fun setItems(items : List<VideoInfo>?){
        this.items = items ?: listOf()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<VideoListItemBinding>(layoutInflater, viewType, parent, false)
        return VideoViewHolder(binding)
    }

    override fun getItemCount() : Int = items.size

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) =  holder.bind(items[position], listener, deleteListener)

}