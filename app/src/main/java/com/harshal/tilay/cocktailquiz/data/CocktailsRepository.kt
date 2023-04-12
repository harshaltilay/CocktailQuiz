/**
 * Copyright (C) 2020 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.harshal.tilay.cocktailquiz.data

import com.harshal.tilay.cocktailquiz.domain.CocktailsContainer
import com.harshal.tilay.cocktailquiz.domain.Either
import com.harshal.tilay.cocktailquiz.domain.FailureException
import com.harshal.tilay.cocktailquiz.frameworks.DeviceNetworkStateFramework
import retrofit2.Call
import javax.inject.Inject

interface CocktailsRepository {
    fun getAlcoholic(): Either<FailureException, CocktailsContainer>

    class Network
    @Inject constructor(
        private val deviceNetworkStateFramework: DeviceNetworkStateFramework,
        private val cocktailsAPI: CocktailsAPI
    ) : CocktailsRepository {

        override fun getAlcoholic(): Either<FailureException, CocktailsContainer> {
            return when (deviceNetworkStateFramework.isNetworkAvailable()) {
                true -> request(
                    cocktailsAPI.getAlcoholic(), { it }, CocktailsContainer(emptyList())
                )
                else -> Either.Left(FailureException.NetworkConnection)
            }
        }

        private fun <T, R> request(
            call: Call<T>, transform: (T) -> R, default: T
        ): Either<FailureException, R> {
            return try {
                val response = call.execute()
                when (response.isSuccessful) {
                    true -> Either.Right(transform((response.body() ?: default)))
                    else -> Either.Left(FailureException.ServerError)
                }
            } catch (exception: Throwable) {
                Either.Left(FailureException.ServerError)
            }
        }
    }
}
