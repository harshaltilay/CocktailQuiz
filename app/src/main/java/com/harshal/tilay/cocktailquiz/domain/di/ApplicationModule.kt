/*
 *
 *  Copyright (C) 2023 Harshal Tilay
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.harshal.tilay.cocktailquiz.domain.di

import android.content.Context
import androidx.databinding.ktx.BuildConfig
import com.harshal.tilay.cocktailquiz.data.CocktailsAPI
import com.harshal.tilay.cocktailquiz.data.CocktailsRepository
import com.harshal.tilay.cocktailquiz.data.HighScoreAPI
import com.harshal.tilay.cocktailquiz.data.HighScoreRepo
import com.harshal.tilay.cocktailquiz.domain.SharedPreference
import com.harshal.tilay.cocktailquiz.frameworks.CocktailsApiFramework
import com.harshal.tilay.cocktailquiz.frameworks.HighScoreFramework
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Lets declare dependency modules to be Injected (in our case all singleton)
 * */

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("https://www.thecocktaildb.com/api/json/v1/1/")
            .client(createClient()).addConverterFactory(GsonConverterFactory.create()).build()
    }

    private fun createClient(): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
        }
        return okHttpClientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideMoviesAPI(frameWork: CocktailsApiFramework): CocktailsAPI = frameWork

    @Provides
    @Singleton
    fun provideMoviesRepository(dataSource: CocktailsRepository.Network): CocktailsRepository =
        dataSource


    @Provides
    @Singleton
    fun provideHighScoreRepo(dataSource: HighScoreRepo.HighScoreRepoImpl): HighScoreRepo =
        dataSource

    @Provides
    @Singleton
    fun provideHighScoreAPI(framework: HighScoreFramework): HighScoreAPI = framework

    @Provides
    @Singleton
    fun provideSharedPreferenceFramework(@ApplicationContext context: Context): SharedPreference =
        SharedPreference.i(context)
}
