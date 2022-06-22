package com.example.pynne.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.pynne.databinding.FragmentMatchInputBinding
import com.example.pynne.ui.MainActivity
import com.example.pynne.viewmodel.MainViewModel
import com.example.pynne.viewmodel.MatchInputViewModel
import kotlinx.android.synthetic.main.fragment_ranking.*
import javax.inject.Inject

class MatchInputFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var binding: FragmentMatchInputBinding

    private val mainViewModel by activityViewModels<MainViewModel> { viewModelFactory }
    private val matchViewModel by viewModels<MatchInputViewModel> { viewModelFactory }

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

        binding.title.setOnClickListener {
            Log.d("lista:", mainViewModel.players.value.toString())
        }

        binding.submitButton.setOnClickListener {
            mainViewModel.handleScore()
//            findNavController().navigateUp()
        }

        binding.run {
            firstPlayerName.addTextChangedListener(textWatcher)
            firstPlayerScore.addTextChangedListener(textWatcher)
            secondPlayerScore.addTextChangedListener(textWatcher)
            secondPlayerName.addTextChangedListener(textWatcher)
        }
    }

    fun requestViewModelValidation(): Boolean = matchViewModel.validateForm(
        binding.firstPlayerName,
        binding.firstPlayerScore,
        binding.secondPlayerName,
        binding.secondPlayerScore
    )

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {

            binding.submitButton.isEnabled = requestViewModelValidation()

            matchViewModel.run {
                firstPlayerName.value = binding.firstPlayerName.getRawText()
                firstPlayerScore.value = binding.firstPlayerScore.getRawText()

                secondPlayerName.value = binding.secondPlayerName.getRawText()
                secondPlayerScore.value = binding.secondPlayerScore.getRawText()
            }

            mainViewModel.run {
                firstPlayer.value?.run {
                    name = binding.firstPlayerName.getRawText()
                    score = binding.firstPlayerScore.getRawText()
                }

                secondPlayer.value?.run {
                    name = binding.secondPlayerName.getRawText()
                    score = binding.secondPlayerScore.getRawText()
                }
            }
        }
    }
}