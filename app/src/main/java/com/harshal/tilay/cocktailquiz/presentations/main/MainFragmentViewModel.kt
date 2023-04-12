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


package com.harshal.tilay.cocktailquiz.presentations.main

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.harshal.tilay.cocktailquiz.R
import com.harshal.tilay.cocktailquiz.domain.Cocktail
import com.harshal.tilay.cocktailquiz.domain.quiz.Question
import com.harshal.tilay.cocktailquiz.domain.quiz.QuestionPaper
import com.harshal.tilay.cocktailquiz.domain.quiz.Score
import com.harshal.tilay.cocktailquiz.presentations.BaseViewModel
import com.harshal.tilay.cocktailquiz.usecases.BaseUseCase
import com.harshal.tilay.cocktailquiz.usecases.GetAlcoholicsUseCase
import com.harshal.tilay.cocktailquiz.usecases.GetHighScoreUseCase
import com.harshal.tilay.cocktailquiz.usecases.SetHighScoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    private val _saveHighScoreUseCase: SetHighScoreUseCase,
    private val _getHighScoreUseCase: GetHighScoreUseCase,
    private val _getAlcoholicsUseCase: GetAlcoholicsUseCase
) : BaseViewModel() {

    private var questionPaper: QuestionPaper? = null
    private val _errorLoadingLiveData = MutableLiveData(false)
    private val _loadingLiveData = MutableLiveData(false)
    private val _questionLiveData = MutableLiveData<Question>()
    private val _scoreLiveData = MutableLiveData<Score>()

    val errorLoadingLiveData: LiveData<Boolean> get() = _errorLoadingLiveData
    val loadingLiveData: LiveData<Boolean> get() = _loadingLiveData
    val questionLiveData: LiveData<Question> get() = _questionLiveData
    val scoreLiveData: LiveData<Score> get() = _scoreLiveData

    fun init() {
        _loadingLiveData.postValue(true)
        getAllCockTails()
    }

    fun nextQuestion() {
        questionPaper?.let {
            _questionLiveData.postValue(it.nextQuestion())
        }
    }

    fun answerQuestion(option: String): Boolean {
        questionPaper?.let {
            val isRight = it.answer(questionLiveData.value!!, option)
            _scoreLiveData.postValue(it.score)
            saveHighScore(it.score.highest)
            return isRight
        }
        return false
    }

    private fun saveHighScore(score: Int) {
        _saveHighScoreUseCase(score)
    }

    private fun getHighScore(): Int = _getHighScoreUseCase()

    private fun getAllCockTails() =
        _getAlcoholicsUseCase(BaseUseCase.None(), viewModelScope) { it ->
            it.fold(::handleFailure) { container ->
                questionPaper =
                    QuestionPaper(buildQuestions(container.drinks!!), Score(getHighScore()))
                questionPaper?.run {
                    _scoreLiveData.postValue(this.score)
                    this@MainFragmentViewModel.nextQuestion()
                }
                Unit
            }
        }

    private fun buildQuestions(cocktailList: List<Cocktail>) = cocktailList.map { cocktail ->
        val otherCocktail = cocktailList.shuffled().first { it != cocktail }
        Question(
            cocktail.strDrink, otherCocktail.strDrink, cocktail.strDrinkThumb
        )
    }.shuffled()

    fun loadImage(url: String, imageView: ImageView) {
        _loadingLiveData.postValue(true)
        _errorLoadingLiveData.postValue(false)
        Glide.with(imageView.rootView).load(url).placeholder(R.drawable.cocktail)
            .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    _errorLoadingLiveData.postValue(true)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    _loadingLiveData.postValue(false)
                    return false
                }
            }).into(imageView)
    }
}