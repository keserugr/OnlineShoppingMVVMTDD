package com.keserugr.onlineshoppingapp.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.keserugr.onlineshoppingapp.MainCoroutineRule
import com.keserugr.onlineshoppingapp.data.local.ShoppingItem
import com.keserugr.onlineshoppingapp.getOrAwaitValueTest
import com.keserugr.onlineshoppingapp.repo.FakeShoppingRepository
import com.keserugr.onlineshoppingapp.ui.viewmodel.ShoppingViewModel
import com.keserugr.onlineshoppingapp.util.Consts
import com.keserugr.onlineshoppingapp.util.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ShoppingViewModelTest {

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ShoppingViewModel

    @Before
    fun setup(){
        viewModel = ShoppingViewModel(FakeShoppingRepository())
    }

    @Test
    fun `returns false if any fields of the inserted item is empty`(){
        viewModel.makeShoppingItem("","2","2F")

        val value = viewModel.insertShoppingItem.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `returns false if the name length of the inserted item is too long`(){
        val name = buildString {
            for (i in 1..Consts.MAX_NAME_LENGTH + 1){
                append("n")
            }
        }
        viewModel.makeShoppingItem(name, "3", "4.0")

        val value = viewModel.insertShoppingItem.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `returns false if the price length of the inserted item is too long `(){
        val price = buildString {
            for (i in 1..Consts.MAX_PRICE_LENGTH + 1){
                append(1)
            }
        }
        viewModel.makeShoppingItem("name", "5", price)

        val value = viewModel.insertShoppingItem.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `returns false if the price of inserted item is not float`(){
        viewModel.makeShoppingItem("name", "2", "two")

        val value = viewModel.insertShoppingItem.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `returns if the amount of inserted item is not integer number`(){
        viewModel.makeShoppingItem("name", "price", "2.0")

        val value = viewModel.insertShoppingItem.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `returns true if enter a valid item`(){
        viewModel.makeShoppingItem("item", "2", "3.0")

        val value = viewModel.insertShoppingItem.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }

}