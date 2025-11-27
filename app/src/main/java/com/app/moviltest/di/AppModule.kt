package com.app.moviltest.di

import com.app.moviltest.data.remote.api.SudokuApi
import com.app.moviltest.data.repository.SudokuRepositoryImpl
import com.app.moviltest.domain.repository.SudokuRepository
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
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.api-ninjas.com/")
            .addConverterFactory(GsonConverterFactory.create())
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
        api: SudokuApi
    ) : SudokuRepository {
        return SudokuRepositoryImpl(api)
    }
}