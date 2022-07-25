package com.keserugr.onlineshoppingapp.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.atilsamancioglu.artbookhilttesting.getOrAwaitValueTest
import com.google.common.truth.Truth.assertThat
import com.keserugr.onlineshoppingapp.R
import com.keserugr.onlineshoppingapp.adapter.ShoppingItemAdapter
import com.keserugr.onlineshoppingapp.data.local.ShoppingItem
import com.keserugr.onlineshoppingapp.launchFragmentInHiltContainer
import com.keserugr.onlineshoppingapp.ui.ShoppingFragment
import com.keserugr.onlineshoppingapp.ui.ShoppingFragmentDirections
import com.keserugr.onlineshoppingapp.ui.ShoppingFragmentFactory
import com.keserugr.onlineshoppingapp.ui.viewmodel.ShoppingViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ShoppingFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fragmentFactory: ShoppingFragmentFactoryTest

    @Before
    fun setup(){
        hiltRule.inject()
    }

    @Test
    fun swipeShoppingItem_deleteItemInDB(){
        val shoppingItem = ShoppingItem("test", 1, 1f, "test.com", 1)
        var testViewModel: ShoppingViewModel? = null
        launchFragmentInHiltContainer<ShoppingFragment>(
            factory = fragmentFactory
        ){
            testViewModel = viewModel
            viewModel?.insertShoppingItemToDB(shoppingItem)
        }

        val perform = onView(withId(R.id.rvShoppingItems)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ShoppingItemAdapter.ShoppingViewHolder>(
                0,
                swipeLeft()
            )
        )

        val value = testViewModel?.shoppingItems?.getOrAwaitValueTest()

        assertThat(value).doesNotContain(shoppingItem)
    }

    @Test
    fun clickAddShoppingItemButton_navigateToAddShoppingItemFragment(){
        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<ShoppingFragment>(
            factory = fragmentFactory
        ){
            Navigation.setViewNavController(requireView(),navController)
        }

        onView(ViewMatchers.withId(R.id.fabAddShoppingItem)).perform(click())

        Mockito.verify(navController).navigate(
            ShoppingFragmentDirections.actionShoppingFragmentToAddShoppingItemFragment()
        )
    }
}