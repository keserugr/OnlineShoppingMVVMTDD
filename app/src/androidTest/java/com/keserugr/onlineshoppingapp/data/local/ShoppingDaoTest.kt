package com.keserugr.onlineshoppingapp.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.atilsamancioglu.artbookhilttesting.getOrAwaitValueTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class ShoppingDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("testDatabase")
    lateinit var db: ShoppingItemDatabase

    @Inject
    @Named("testDao")
    lateinit var dao: ShoppingDao

    @Before
    fun setup(){
        hiltRule.inject()
    }

    @After
    fun teardown(){
        db.close()
    }

    @Test
    fun insertShoppingItemTest() = runBlocking{
        val shoppingItem = ShoppingItem("phone", 1, 89F,"test.com", id=1)
        dao.insertShoppingItem(shoppingItem)

        val shoppingList = dao.observeAllShoppingItems().getOrAwaitValueTest()

        assertThat(shoppingList).contains(shoppingItem)
    }

    @Test
    fun deleteShoppingItemTest() = runBlocking {
        val shoppingItem = ShoppingItem("phone", 1, 89F,"test.com", id=1)
        dao.insertShoppingItem(shoppingItem)
        dao.deleteShoppingItem(shoppingItem)
        val shoppingList = dao.observeAllShoppingItems().getOrAwaitValueTest()

        assertThat(shoppingList).doesNotContain(shoppingItem)
    }

    @Test
    fun observeTotalPriceSumTest() = runBlocking {
        val shoppingItem1 = ShoppingItem("phone1", 2, 2F,"test.com", id=1)
        val shoppingItem2= ShoppingItem("phone2", 1, 3F,"test.com", id=2)
        val shoppingItem3 = ShoppingItem("phone3", 1, 4F,"test.com", id=3)
        dao.insertShoppingItem(shoppingItem1)
        dao.insertShoppingItem(shoppingItem2)
        dao.insertShoppingItem(shoppingItem3)

        val totalPriceSum = dao.observeTotalPrice().getOrAwaitValueTest()

        assertThat(totalPriceSum).isEqualTo(11F)
    }
}