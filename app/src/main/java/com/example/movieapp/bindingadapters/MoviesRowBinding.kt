package com.example.movieapp.bindingadapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load

object MoviesRowBinding {
    @JvmStatic
    @BindingAdapter("loadImageFromUrl")
    fun loadImageFromUrl(imageView: ImageView, imageUrl: String?) {
        if (!imageUrl.isNullOrBlank()) {
            imageView.load(imageUrl) {
                crossfade(600)
            }
        }
    }

}