package com.keserugr.onlineshoppingapp.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import com.atilsamancioglu.artbookhilttesting.getOrAwaitValueTest
import com.google.common.truth.Truth.assertThat
import com.keserugr.onlineshoppingapp.R
import com.keserugr.onlineshoppingapp.data.local.ShoppingItem
import com.keserugr.onlineshoppingapp.launchFragmentInHiltContainer
import com.keserugr.onlineshoppingapp.repo.FakeShoppingRepositoryTest
import com.keserugr.onlineshoppingapp.ui.AddShoppingItemFragment
import com.keserugr.onlineshoppingapp.ui.AddShoppingItemFragmentDirections
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
class AddShoppingItemFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fragmentFactory: ShoppingFragmentFactory


    @Before
    fun setup(){
        hiltRule.inject()
    }

    @Test
    fun clickInsetInToDb_shoppingItemInsertedIntoDb(){
        val testViewModel = ShoppingViewModel(FakeShoppingRepositoryTest())

        launchFragmentInHiltContainer<AddShoppingItemFragment>(
            factory = fragmentFactory
        ){
            viewModel = testViewModel
        }

        Espresso.onView(ViewMatchers.withId(R.id.etShoppingItemName)).perform(replaceText("shopping item"))
        Espresso.onView(ViewMatchers.withId(R.id.etShoppingItemAmount)).perform(replaceText("2"))
        Espresso.onView(ViewMatchers.withId(R.id.etShoppingItemPrice)).perform(replaceText("3.1"))
        Espresso.onView(ViewMatchers.withId(R.id.btnAddShoppingItem)).perform(click())

        assertThat(testViewModel.shoppingItems.getOrAwaitValueTest())
            .contains(ShoppingItem("shopping item", 2, 3.1f, ""))
    }

    @Test
    fun clickImageView_navigateToImagePickFragment(){
        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<AddShoppingItemFragment>(
            factory = fragmentFactory
        ){
            Navigation.setViewNavController(requireView(), navController)
        }

        Espresso.onView(ViewMatchers.withId(R.id.ivShoppingImage)).perform(click())

        Mockito.verify(navController).navigate(
            AddShoppingItemFragmentDirections.actionAddShoppingItemFragmentToImagePickFragment()
        )
    }

    @Test
    fun clickBackButton_popBackStack(){
        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<AddShoppingItemFragment>(
            factory = fragmentFactory
        ){
            Navigation.setViewNavController(requireView(), navController)
        }

        Espresso.pressBack()

        Mockito.verify(navController).popBackStack()
    }
}