package com.keserugr.onlineshoppingapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keserugr.onlineshoppingapp.data.local.ShoppingItem
import com.keserugr.onlineshoppingapp.data.remote.responses.ImageResponse
import com.keserugr.onlineshoppingapp.repository.IShoppingRepository
import com.keserugr.onlineshoppingapp.util.Consts
import com.keserugr.onlineshoppingapp.util.Event
import com.keserugr.onlineshoppingapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val repository: IShoppingRepository
) : ViewModel() {

    val shoppingItems = repository.observeAllShoppingItems()

    val totalPrice = repository.observeTotalPrice()

    // all images when typing in the pixabay matched images
    private val _images = MutableLiveData<Event<Resource<ImageResponse>>>()
    val images: LiveData<Event<Resource<ImageResponse>>>
        get() = _images

    // selected image when chose an image from pixaby searches
    private val _selectedImage = MutableLiveData<String>()
    val selectedImage: LiveData<String>
        get() = _selectedImage

    // created a shopping item into the db
    private var _insertShoppingItem = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItem: LiveData<Event<Resource<ShoppingItem>>>
        get() = _insertShoppingItem

    fun setSelectedImageURL(url: String){
        _selectedImage.postValue(url)
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.deleteShoppingItem(shoppingItem)
    }

    fun insertShoppingItemToDB(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.insertShoppingItem(shoppingItem)
    }

    fun makeShoppingItem(name: String, amountString: String, priceString: String){
        if (name.isEmpty() || amountString.isEmpty() || priceString.isEmpty()){
            _insertShoppingItem.postValue(Event(Resource.error("Please fill the blanks!", null)))
            return
        }

        if (name.length > Consts.MAX_NAME_LENGTH){
            _insertShoppingItem.postValue(Event(Resource.error("Item Name can not be bigger than 20 char!", null)))
            return
        }

        if (priceString.length > Consts.MAX_PRICE_LENGTH){
            _insertShoppingItem.postValue(Event(Resource.error("Price is to high!", null)))
            return
        }

        val priceFloat = try {
            priceString.toFloat()
        }catch (e: Exception){
            _insertShoppingItem.postValue(Event(Resource.error("Please enter a valid price!", null)))
            return
        }

        val amountInt = try {
            amountString.toInt()
        }catch (e: Exception){
            _insertShoppingItem.postValue(Event(Resource.error("Please enter a valid amount!", null)))
            return
        }

        val shoppingItem = ShoppingItem(name,amountInt,priceFloat,_selectedImage.value ?: "")
        insertShoppingItemToDB(shoppingItem)
        setSelectedImageURL("")
        _insertShoppingItem.postValue(Event(Resource.success(shoppingItem)))
    }

    fun searchForImage(imageQuery: String){
        if (imageQuery.isEmpty())
            return
        _images.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.searchForImage(imageQuery)
            _images.value = Event(response)
        }
    }
}