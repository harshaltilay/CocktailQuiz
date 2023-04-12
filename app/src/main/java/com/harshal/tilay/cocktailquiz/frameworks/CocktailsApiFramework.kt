package com.harshal.tilay.cocktailquiz.frameworks

import com.harshal.tilay.cocktailquiz.data.CocktailsAPI
import com.harshal.tilay.cocktailquiz.domain.CocktailsContainer
import retrofit2.Call
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CocktailsApiFramework
@Inject constructor(retrofit: Retrofit) : CocktailsAPI {
    private val cocktailsAPI by lazy { retrofit.create(CocktailsAPI::class.java) }
    override fun getAlcoholic(): Call<CocktailsContainer> = cocktailsAPI.getAlcoholic()
}
