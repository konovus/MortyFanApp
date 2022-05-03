package com.konovus.simplemortyr.ui.detailsScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.konovus.simplemortyr.databinding.ItemEpisodeBinding
import com.konovus.simplemortyr.entity.Episode

class EpisodesAdapter(private val listener: OnItemClickListener) : ListAdapter<Episode, EpisodesAdapter.EpisodeViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val binding = ItemEpisodeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EpisodeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        getItem(position)?.let{ holder.bind(it) }
    }

    inner class EpisodeViewHolder(private val binding: ItemEpisodeBinding) : RecyclerView.ViewHolder(binding.root){

        init {
            itemView.setOnClickListener {
                val pos = bindingAdapterPosition
                if(pos != RecyclerView.NO_POSITION){
                    getItem(pos)?.let {
                        listener.onItemClick(it)
                    }
                }
            }
        }

        fun bind(episode: Episode){
            binding.apply{
                nameEpisode.text = episode.name
                seasonEpisode.text = episode.episode
                airDate.text = episode.air_date
            }
        }
    }
    interface OnItemClickListener {
        fun onItemClick(episode: Episode)
    }
    class DiffCallback : DiffUtil.ItemCallback<Episode>() {
        override fun areItemsTheSame(oldItem: Episode, newItem: Episode) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Episode, newItem: Episode) =
            oldItem == newItem
    }
}