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
package com.harshal.tilay.cocktailquiz.usecases


import com.harshal.tilay.cocktailquiz.data.CocktailsRepository
import com.harshal.tilay.cocktailquiz.domain.CocktailsContainer
import javax.inject.Inject

class GetAlcoholicsUseCase
@Inject constructor(private val cocktailsRepository: CocktailsRepository) :
    BaseUseCase<CocktailsContainer, BaseUseCase.None>() {
    override suspend fun run(params: None) = cocktailsRepository.getAlcoholic()
}
