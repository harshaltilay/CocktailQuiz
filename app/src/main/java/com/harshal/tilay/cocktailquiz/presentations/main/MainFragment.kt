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

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import com.harshal.tilay.cocktailquiz.R
import com.harshal.tilay.cocktailquiz.databinding.FragmentMainBinding
import com.harshal.tilay.cocktailquiz.domain.*
import com.harshal.tilay.cocktailquiz.presentations.ActivityDelegate
import com.harshal.tilay.cocktailquiz.presentations.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment() {


    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val _mainFragmentViewModel: MainFragmentViewModel by viewModels()

    private var answer: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mainActivityDelegate = context as ActivityDelegate
        } catch (e: ClassCastException) {
            throw ClassCastException("Host activity must implement ActivityDelegate")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivityDelegate.fragmentIsReady()
        init()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun init() {
        setListeners()
        setAdapters()
        setObservers()
        _mainFragmentViewModel.init()
    }

    override fun setListeners() {
        binding.radioOptions.setOnCheckedChangeListener { _, checkedId ->
            if (answer != null) return@setOnCheckedChangeListener

            val answerA = binding.rbOptionA.text.toString()
            val answerB = binding.rbOptionB.text.toString()

            answer = if (checkedId == R.id.rb_option_a) answerA
            else answerB

            val isRight = _mainFragmentViewModel.answerQuestion(answer!!)
            if (!isRight) answer = if (answer == answerA) answerB
            else answerA


            binding.tvAnswer.text = answer
            binding.radioOptions.clearCheck()

            showAnswer()

        }

        binding.btnNext.setOnClickListener {
            showLoading()
            answer = null
            _mainFragmentViewModel.nextQuestion()
        }
    }

    override fun setAdapters() {

    }

    override fun setObservers() {
        with(_mainFragmentViewModel) {

            failure(failureException, ::handleFailure)

            observe(errorLoadingLiveData) {
                it?.let {
                    if (it) handleFailure(null)
                }
            }
            observe(loadingLiveData) {
                it?.let {
                    if (it) showLoading()
                    else showQuestion()
                }
            }

            observe(scoreLiveData) {
                it?.let {
                    binding.tvHighScore.text = getString(R.string.high_score, it.highest.toString())
                    binding.tvScore.text = getString(R.string.score, it.current.toString())
                }
            }

            observe(questionLiveData) {
//                if (_mainFragmentViewModel.loadingLiveData.value!!) return@observe

                it?.let {
                    it.imageUrl?.run {
                        _mainFragmentViewModel.loadImage(this, binding.ivCocktail)
                    }
                    val options = it.getOptions()
                    binding.rbOptionA.text = options[0]
                    binding.rbOptionB.text = options[1]
                }
            }
        }
    }

    private fun showLoading() {
        binding.question.gone()
        binding.answer.gone()
        binding.loading.visible()
    }

    private fun showQuestion() {
        binding.question.visible()
        binding.answer.gone()
        binding.loading.gone()
    }

    private fun showAnswer() {
        binding.question.gone()
        binding.answer.visible()
        binding.loading.gone()
    }

    private fun hideAll() {
        binding.question.gone()
        binding.answer.gone()
        binding.loading.gone()
    }


    private fun handleFailure(failure: FailureException?) {
        hideAll()
        notifyWithAction(R.string.message, R.string.action_ok) { }
        binding.ivCocktail.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources, R.drawable.error, null
            )
        )
    }

}








