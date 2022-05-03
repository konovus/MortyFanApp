package com.konovus.simplemortyr.ui.bottomSheetScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.konovus.simplemortyr.R
import com.konovus.simplemortyr.databinding.ItemCharactersEpisodeBinding
import com.konovus.simplemortyr.entity.Character

class BottomSheetAdapter : ListAdapter<Character, BottomSheetAdapter.BottomSheetViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder {
        val binding = ItemCharactersEpisodeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BottomSheetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class BottomSheetViewHolder(private val binding: ItemCharactersEpisodeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(character: Character){
            binding.apply {
                Glide.with(itemView)
                    .load(character.image)
                    .transition(
                        DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.color.light_gray)
                    .into(image)
                name.text = character.name
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Character, newItem: Character) =
            oldItem == newItem
    }
}