package com.keserugr.onlineshoppingapp.data.remote.responses

data class ImageResponse(
    val hits: List<ImageDetail>,
    val total: Int,
    val totalHits: Int
)