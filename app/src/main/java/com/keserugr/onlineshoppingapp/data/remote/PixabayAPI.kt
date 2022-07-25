package com.keserugr.onlineshoppingapp.data.remote

import com.keserugr.onlineshoppingapp.BuildConfig
import com.keserugr.onlineshoppingapp.data.remote.responses.ImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayAPI {

    @GET("/api/")
    suspend fun searchForImage(
        @Query("q") searchQuery: String,
        @Query("key") apikey: String = BuildConfig.API_KEY
    ): Response<ImageResponse>
}