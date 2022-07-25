package com.keserugr.onlineshoppingapp.repository

import androidx.lifecycle.LiveData
import com.keserugr.onlineshoppingapp.data.local.ShoppingDao
import com.keserugr.onlineshoppingapp.data.local.ShoppingItem
import com.keserugr.onlineshoppingapp.data.remote.PixabayAPI
import com.keserugr.onlineshoppingapp.data.remote.responses.ImageResponse
import com.keserugr.onlineshoppingapp.util.Resource
import java.lang.Exception
import javax.inject.Inject

class ShoppingRepositoryImp @Inject constructor(
    private val shoppingDao: ShoppingDao,
    private val pixabayAPI: PixabayAPI
) : IShoppingRepository {
    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.deleteShoppingItem(shoppingItem)
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return shoppingDao.observeAllShoppingItems()
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return shoppingDao.observeTotalPrice()
    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
        return try {
            val response = pixabayAPI.searchForImage(imageQuery)
            if (response.isSuccessful){
                response.body()?.let {
                    return@let Resource.success(it)
                }?: Resource.error("Error!",null)
            }else{
                return Resource.error("Error!", null)
            }
        }catch (e: Exception){
            Resource.error("No data!", null)
        }
    }
}