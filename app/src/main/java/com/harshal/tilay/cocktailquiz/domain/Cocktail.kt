package com.harshal.tilay.cocktailquiz.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cocktail(
    val idDrink: String,
    val strDrink: String,
    val strDrinkThumb: String,
    val strInstructions: String? = null,
    var isFavorite: Boolean = false
) : Parcelable