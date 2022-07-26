package com.keserugr.onlineshoppingapp.data.remote.responses

import com.google.gson.annotations.SerializedName

data class ImageDetail(
    val comments: Int,
    val downloads: Int,
    val fullHDURL: String,
    val id: Int,
    val imageHeight: Int,
    val imageSize: Int,
    val imageURL: String,
    val imageWidth: Int,
    val largeImageURL: String,
    val likes: Int,
    val pageURL: String,
    val previewHeight: Int,
    val previewURL: String,
    val previewWidth: Int,
    val tags: String,
    val type: String,
    val user: String,
    val userImageURL: String,
    @SerializedName("user_id")
    val useId: Int,
    val views: Int,
    val webformatHeight: Int,
    val webformatURL: String,
    val webformatWidth: Int
)
