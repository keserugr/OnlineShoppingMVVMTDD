package com.keserugr.onlineshoppingapp.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bumptech.glide.RequestManager
import com.keserugr.onlineshoppingapp.adapter.ImageAdapter
import com.keserugr.onlineshoppingapp.adapter.ShoppingItemAdapter
import javax.inject.Inject

class ShoppingFragmentFactory @Inject constructor(
    private val glide: RequestManager,
    private val imageAdapter: ImageAdapter,
    private val shoppingItemAdapter: ShoppingItemAdapter
): FragmentFactory(){
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className){
            ShoppingFragment::class.java.name -> ShoppingFragment(shoppingItemAdapter)
            AddShoppingItemFragment::class.java.name -> AddShoppingItemFragment(glide)
            ImagePickFragment::class.java.name -> ImagePickFragment(imageAdapter)
            else -> super.instantiate(classLoader, className)
        }
    }
}