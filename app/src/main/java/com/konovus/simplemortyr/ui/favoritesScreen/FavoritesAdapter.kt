package com.konovus.simplemortyr.ui.favoritesScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.konovus.simplemortyr.R
import com.konovus.simplemortyr.databinding.ItemMortyCharacterBinding
import com.konovus.simplemortyr.entity.Character

class FavoritesAdapter(private val listener: OnItemClickListener
) : ListAdapter<Character, FavoritesAdapter.FavoritesViewHolder>(DiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val binding = ItemMortyCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoritesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class FavoritesViewHolder(private val binding: ItemMortyCharacterBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {
                val pos = bindingAdapterPosition
                if(pos != RecyclerView.NO_POSITION){
                    getItem(pos)?.let {
                        listener.onItemClick(it)
                    }
                }
            }
            binding.favorite.setOnClickListener {
                val pos = bindingAdapterPosition
                if(pos != RecyclerView.NO_POSITION){
                    getItem(pos)?.let {
                        when(it.fav){
                            true -> binding.favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                            false -> binding.favorite.setImageResource(R.drawable.ic_baseline_favorite_24)
                        }
                        listener.onFavClick(it)
                    }
                }
            }
        }
        fun bind(character: Character){
            binding.apply {
                Glide.with(itemView)
                    .load(character.image)
                    .transition(
                        DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.color.light_gray)
                    .into(image)
                name.text = character.name
                favorite.visibility = ViewGroup.VISIBLE
                when(character.fav){
                    true -> favorite.setImageResource(R.drawable.ic_baseline_favorite_24)
                    false -> favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                }
            }
        }
    }
    interface OnItemClickListener {
        fun onItemClick(character: Character)
        fun onFavClick(character: Character)
    }

    class DiffCallback : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character) =
             oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Character, newItem: Character) =
             oldItem == newItem
    }
}