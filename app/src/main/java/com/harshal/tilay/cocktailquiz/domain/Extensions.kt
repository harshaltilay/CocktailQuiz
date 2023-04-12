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
import android.net.ConnectivityManager
import android.text.Spanned
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.harshal.tilay.cocktailquiz.R


fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) =
    liveData.observe(this, Observer(body))

fun <L : LiveData<FailureException>> LifecycleOwner.failure(
    liveData: L, body: (FailureException?) -> Unit
) = liveData.observe(this, Observer(body))


val Context.connectivityManager: ConnectivityManager
    get() = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

fun View.isVisible(): Boolean {
    return this.visibility == View.VISIBLE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun String.Companion.empty() = ""

fun String.Companion.html(str: String): Spanned {
    return HtmlCompat.fromHtml(
        str, HtmlCompat.FROM_HTML_MODE_COMPACT
    )

}

//Usage
//showToast("Hello World👋")
fun Context.showToast(message: String?, length: Int = Toast.LENGTH_SHORT) {
    message?.let {
        Toast.makeText(this, it, length).show()
    }
}


fun View.showSnackMessage(
    @StringRes message: Int,
    @StringRes actionText: Int,
    anchorView: View? = null,
    backgroundColor: Int,
    textColor: Int,
    length: Int = Snackbar.LENGTH_SHORT,
    action: (() -> Any)?
) {
    message.let {
        try {
            val snack = Snackbar.make(this, it, length)
            with(snack) {
                setBackgroundTint(ContextCompat.getColor(context, backgroundColor))
                setTextColor(ContextCompat.getColor(context, textColor))
                this@with.anchorView = anchorView
                action?.apply {
                    setAction(actionText) { action.invoke() }
                    setActionTextColor(
                        ContextCompat.getColor(
                            context, R.color.white
                        )
                    )
                }
                show()
            }
        } catch (ex: Exception) {
//            ex.showLog()
        }
    }
}

