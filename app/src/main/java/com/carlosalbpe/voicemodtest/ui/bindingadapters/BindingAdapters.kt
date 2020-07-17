package com.carlosalbpe.voicemodtest.ui.bindingadapters

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.carlosalbpe.voicemodtest.business.domain.VideoInfo
import com.squareup.picasso.Picasso
import java.io.File

@BindingAdapter("android:loadThumbnail")
fun loadThumbnail(view: View, videoInfo: VideoInfo) {
    if(view is ImageView){
        var options = RequestOptions()
            .centerCrop()

        Glide.with(view.context)
            .asBitmap()
            .load(Uri.fromFile(File(videoInfo.path))) // or URI/p
            .apply(options)
            .into(view)

        /*Picasso.with(view.context)
            .load(videoInfo.path)
            .into(view)*/

    }
}