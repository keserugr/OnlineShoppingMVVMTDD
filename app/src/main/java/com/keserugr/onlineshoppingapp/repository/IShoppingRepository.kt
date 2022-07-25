package com.keserugr.onlineshoppingapp.repository

import androidx.lifecycle.LiveData
import com.keserugr.onlineshoppingapp.data.local.ShoppingItem
import com.keserugr.onlineshoppingapp.data.remote.responses.ImageResponse
import com.keserugr.onlineshoppingapp.util.Resource

interface IShoppingRepository {

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>

    fun observeTotalPrice(): LiveData<Float>

    suspend fun searchForImage(imageQuery: String): Resource<ImageResponse>
}