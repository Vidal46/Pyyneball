package com.example.pynne.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.core.extension.getViewHolder
import com.example.pynne.R
import com.example.pynne.databinding.PlayerViewHolderBinding
import com.example.pynne.model.Player
import com.example.pynne.ui.listener.PlayerListener

class PlayerAdapter(): RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {

    var players: MutableList<Player> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var listener: PlayerListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder =
        parent.getViewHolder(::PlayerViewHolder, R.layout.player_view_holder)

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val binding: PlayerViewHolderBinding? = holder.binding
        binding?.player = players[position]
        binding?.listener = listener
        binding?.executePendingBindings()
    }

    override fun getItemCount(): Int = players.size

    inner class PlayerViewHolder(view : View): RecyclerView.ViewHolder(view) {
        val binding: PlayerViewHolderBinding? = DataBindingUtil.bind(view)

    }
}