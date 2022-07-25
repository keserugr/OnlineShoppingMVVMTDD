package com.keserugr.onlineshoppingapp.di

import android.content.Context
import androidx.room.Room
import com.keserugr.onlineshoppingapp.data.local.ShoppingItemDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestApppModule {

    @Provides
    @Named("testDatabase")
    fun provideInMemoryDb(@ApplicationContext context: Context)  =
        Room.inMemoryDatabaseBuilder(context, ShoppingItemDatabase::class.java)
            .allowMainThreadQueries()
            .build()

    @Provides
    @Named("testDao")
    fun provideDaoTest(database: ShoppingItemDatabase) = database.shoppingDao()
}