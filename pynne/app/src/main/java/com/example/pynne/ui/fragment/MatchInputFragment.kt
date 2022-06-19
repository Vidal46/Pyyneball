package com.example.pynne.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.pynne.databinding.FragmentMatchInputBinding
import com.example.pynne.ui.MainActivity
import com.example.pynne.viewmodel.MatchViewModel
import javax.inject.Inject

class MatchInputFragment : Fragment() {

    lateinit var binding: FragmentMatchInputBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val matchViewModel by viewModels<MatchViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).mainComponent.inject(this)
    }

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