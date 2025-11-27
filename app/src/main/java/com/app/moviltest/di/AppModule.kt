package com.app.moviltest.di

import com.app.moviltest.data.local.preferences.SudokuPreferences
import com.app.moviltest.data.remote.api.SudokuApi
import com.app.moviltest.data.repository.SudokuRepositoryImpl
import com.app.moviltest.domain.repository.SudokuRepository
import com.google.gson.Gson
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

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.api-ninjas.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideSudokuApi(retrofit: Retrofit): SudokuApi {
        return retrofit.create(SudokuApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSudokuRepository(
        api: SudokuApi,
        preferences: SudokuPreferences
    ): SudokuRepository {
        return SudokuRepositoryImpl(api, preferences)
    }
}