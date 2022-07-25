package com.keserugr.onlineshoppingapp.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bumptech.glide.RequestManager
import com.keserugr.onlineshoppingapp.adapter.ImageAdapter
import com.keserugr.onlineshoppingapp.adapter.ShoppingItemAdapter
import com.keserugr.onlineshoppingapp.repo.FakeShoppingRepositoryTest
import com.keserugr.onlineshoppingapp.ui.AddShoppingItemFragment
import com.keserugr.onlineshoppingapp.ui.ImagePickFragment
import com.keserugr.onlineshoppingapp.ui.ShoppingFragment
import com.keserugr.onlineshoppingapp.ui.viewmodel.ShoppingViewModel
import javax.inject.Inject

class ShoppingFragmentFactoryTest @Inject constructor(
    private val glide: RequestManager,
    private val imageAdapter: ImageAdapter,
    private val shoppingItemAdapter: ShoppingItemAdapter
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            ShoppingFragment::class.java.name -> ShoppingFragment(
                shoppingItemAdapter,
                ShoppingViewModel(FakeShoppingRepositoryTest())
            )
            AddShoppingItemFragment::class.java.name -> AddShoppingItemFragment(glide)
            ImagePickFragment::class.java.name -> ImagePickFragment(imageAdapter)
            else -> super.instantiate(classLoader, className)
        }
    }
}