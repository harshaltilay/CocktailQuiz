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


package com.harshal.tilay.cocktailquiz.frameworks

import com.harshal.tilay.cocktailquiz.data.HighScoreAPI
import com.harshal.tilay.cocktailquiz.domain.SharedPreference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HighScoreFramework @Inject constructor(private var sharedPreference: SharedPreference) :
    HighScoreAPI {
    override fun saveHighScore(value: Int) {
        sharedPreference.setVal("KEY_HIGH_SCORE", value)
    }

    override fun getHighScore(): Int {
        return sharedPreference.getVal("KEY_HIGH_SCORE")
    }
}