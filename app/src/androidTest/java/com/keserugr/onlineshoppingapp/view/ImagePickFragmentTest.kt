package com.keserugr.onlineshoppingapp.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.atilsamancioglu.artbookhilttesting.getOrAwaitValueTest
import com.google.common.truth.Truth.assertThat
import com.keserugr.onlineshoppingapp.R
import com.keserugr.onlineshoppingapp.adapter.ImageAdapter
import com.keserugr.onlineshoppingapp.launchFragmentInHiltContainer
import com.keserugr.onlineshoppingapp.repo.FakeShoppingRepositoryTest
import com.keserugr.onlineshoppingapp.ui.ImagePickFragment
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
class ImagePickFragmentTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fragmentFactory: ShoppingFragmentFactory

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun selectImage_popBackStackAndSetImageUrl() {
        val navController = Mockito.mock(NavController::class.java)
        val selectedImageUrl = "test.com"
        val testViewModel = ShoppingViewModel(FakeShoppingRepositoryTest())
        launchFragmentInHiltContainer<ImagePickFragment>(
            factory = fragmentFactory
        ) {
            Navigation.setViewNavController(requireView(), navController)
            viewModel = testViewModel
            imageAdapter.images = listOf(selectedImageUrl)
        }

        Espresso.onView(withId(R.id.rvImages)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ImageAdapter.ImageViewHolder>(
                0, click()
            )
        )

        Mockito.verify(navController).popBackStack()
        assertThat(testViewModel.selectedImage.getOrAwaitValueTest()).isEqualTo(selectedImageUrl)
    }
}
