package com.example.pynne.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.BindingAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.core.extension.toMutableListSortedByDescending
import com.example.pynne.R
import com.example.pynne.databinding.FragmentRankingBinding
import com.example.pynne.model.Player
import com.example.pynne.ui.MainActivity
import com.example.pynne.ui.adapter.PlayerAdapter
import com.example.pynne.viewmodel.MainViewModel
import javax.inject.Inject

class RankingFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by activityViewModels<MainViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).mainComponent.inject(this)
    }

    lateinit var binding: FragmentRankingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRankingBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            findNavController().navigate(R.id.inputMatch)
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {}
    }
}

@BindingAdapter("players")
fun setPlayers(recyclerView: RecyclerView, players: List<Player>) {
    recyclerView.adapter = PlayerAdapter().apply {
        this.players = players.toMutableListSortedByDescending { it.wins }
    }
}