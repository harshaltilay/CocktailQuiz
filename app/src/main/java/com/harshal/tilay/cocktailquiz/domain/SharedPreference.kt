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
package com.harshal.tilay.cocktailquiz.domain

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import javax.inject.Singleton

@Singleton
class SharedPreference {

    companion object {
        private lateinit var cookiesPrivate: SharedPreferences
        private var cookieEditor: SharedPreferences.Editor? = null

        @Volatile
        private var INSTANCE: SharedPreference? = null

        fun i(context: Context): SharedPreference {
            return INSTANCE ?: synchronized(this) {
                val masterKey =
                    MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
                cookiesPrivate = EncryptedSharedPreferences.create(
                    context,
                    "FINANCE_COOKIES",
                    masterKey, // masterKey created above
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )
                val instance = SharedPreference()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

//    fun clearAll() {
//        cookiesPrivate.edit().clear().apply()
//    }

    fun setVal(key: String, value: Int) {
        cookieEditor = cookiesPrivate.edit()
        cookieEditor?.putInt(
            key, value
        )
        cookieEditor?.apply()
    }

    fun getVal(key: String): Int {
        return cookiesPrivate.getInt(key, 0)
    }
}