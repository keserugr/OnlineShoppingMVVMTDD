package com.keserugr.onlineshoppingapp.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.keserugr.onlineshoppingapp.R
import com.keserugr.onlineshoppingapp.data.local.ShoppingDao
import com.keserugr.onlineshoppingapp.data.local.ShoppingItemDatabase
import com.keserugr.onlineshoppingapp.data.remote.PixabayAPI
import com.keserugr.onlineshoppingapp.repository.IShoppingRepository
import com.keserugr.onlineshoppingapp.repository.ShoppingRepositoryImp
import com.keserugr.onlineshoppingapp.util.Consts.BASE_URL
import com.keserugr.onlineshoppingapp.util.Consts.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideShoppingItemDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context,ShoppingItemDatabase::class.java,DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideShoppingDao(
        database: ShoppingItemDatabase
    ) = database.shoppingDao()

    @Singleton
    @Provides
    fun providePixabayAPI(): PixabayAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PixabayAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideShoppingRepository(
        dao: ShoppingDao,
        pixabayAPI: PixabayAPI
    ) = ShoppingRepositoryImp(dao,pixabayAPI) as IShoppingRepository

    @Singleton
    @Provides
    fun provideGlide(
        @ApplicationContext context: Context
    ) = Glide.with(context).setDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_image)
        )
}