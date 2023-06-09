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


package com.harshal.tilay.cocktailquiz.presentations

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.harshal.tilay.cocktailquiz.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseFragment : Fragment() {

    lateinit var mainActivityDelegate: ActivityDelegate
    abstract fun init()
    abstract fun setListeners()
    abstract fun setObservers()
    abstract fun setAdapters()

    fun gotTo(actionId: Int, bundle: Bundle? = null) {
        try {
            findNavController().navigate(
                actionId, bundle
            )
        } finally {

        }
    }

    internal fun notifyWithAction(
        @StringRes message: Int, @StringRes actionText: Int, action: () -> Any
    ) {
        val snackBar = Snackbar.make(requireView(), message, Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(actionText) { _ -> action.invoke() }
        snackBar.setActionTextColor(
            ContextCompat.getColor(
                requireContext(), R.color.white
            )
        )
        snackBar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.coal))
        snackBar.setActionTextColor(resources.getColor(R.color.white, null))
        snackBar.setTextColor(resources.getColor(R.color.golden, null))
        snackBar.show()
    }

    internal fun notify(@StringRes message: Int) =
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT)
            .setTextColor(resources.getColor(R.color.golden, null))
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.black)).show()
}
