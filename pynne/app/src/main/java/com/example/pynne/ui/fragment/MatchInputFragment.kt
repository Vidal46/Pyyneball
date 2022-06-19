package com.example.pynne.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pynne.R
import com.example.pynne.databinding.FragmentMatchInputBinding
import com.example.pynne.databinding.FragmentRankingBinding
import com.example.pynne.ui.MatchViewModel

class MatchInputFragment : Fragment() {

    lateinit var binding: FragmentMatchInputBinding
    private val matchViewModel = MatchViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMatchInputBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = matchViewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            firstPlayerScore.addTextChangedListener(textWatcher)
            SecondPlayerScore.addTextChangedListener(textWatcher)
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {

            matchViewModel.run {
                firstPlayerScore.value = binding.firstPlayerScore.getRawText()
                secondPlayerScore.value = binding.SecondPlayerScore.getRawText()
            }
        }
    }
}