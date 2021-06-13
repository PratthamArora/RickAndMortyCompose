package com.pratthamarora.rickandmortycompose.di

import com.pratthamarora.rickandmortycompose.data.remote.RickMortyApi
import com.pratthamarora.rickandmortycompose.repository.RickMortyRepository
import com.pratthamarora.rickandmortycompose.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRickMortyApi(): RickMortyApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()
            .create(RickMortyApi::class.java)
    }

    @Singleton
    @Provides
    fun provideRickMortyRepository(api: RickMortyApi) = RickMortyRepository(api = api)
}