package com.example.pynne.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.core.extension.getViewHolder
import com.example.pynne.R
import com.example.pynne.databinding.PlayerListHeaderBinding
import com.example.pynne.databinding.PlayerViewHolderBinding
import com.example.pynne.model.Player

class PlayerAdapter() : RecyclerView.Adapter<ViewHolder>() {

    var players: MutableList<Player> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int): Int =
        if (position == 0) TYPE_HEADER else TYPE_BODY

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                parent.getViewHolder(
                    ::HeaderViewHolder,
                    R.layout.player_list_header
                )
            }
            else -> {
                parent.getViewHolder(
                    ::PlayerViewHolder,
                    R.layout.player_view_holder
                )
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_HEADER -> {
                val binding: PlayerListHeaderBinding? = (holder as HeaderViewHolder).binding
                binding?.executePendingBindings()
            }

            TYPE_BODY -> {
                val binding: PlayerViewHolderBinding? = (holder as PlayerViewHolder).binding
                binding?.player = players[position - 1]
                binding?.executePendingBindings()
            }
        }
    }

    override fun getItemCount(): Int = players.size + 1

    inner class PlayerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: PlayerViewHolderBinding? = DataBindingUtil.bind(view)
    }

    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: PlayerListHeaderBinding? = DataBindingUtil.bind(view)
    }

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_BODY = 1
    }
}