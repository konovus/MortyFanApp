package com.konovus.simplemortyr.ui.mainScreen

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.konovus.simplemortyr.R
import com.konovus.simplemortyr.databinding.ItemMortyCharacterBinding
import com.konovus.simplemortyr.entity.Character
import com.konovus.simplemortyr.ui.mainScreen.MainFragment.Companion.currentPosition
import com.konovus.simplemortyr.ui.mainScreen.MainFragment.Companion.mainFade
import java.util.concurrent.atomic.AtomicBoolean

class MainAdapter(private val listener: OnItemClickListener, private val fragment: MainFragment) :
    PagingDataAdapter<Character, MainAdapter.MainViewHolder>(Differ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = ItemMortyCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }


    inner class MainViewHolder(
        private val binding: ItemMortyCharacterBinding,
        private val enterTransitionStarted: AtomicBoolean = AtomicBoolean()) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val pos = bindingAdapterPosition
                if(pos != RecyclerView.NO_POSITION){
                    getItem(pos)?.let {
                        listener.onItemClick(it, binding.image, pos)
                    }
                }
            }
        }

        fun bind(character: Character) {
            
            binding.apply {
                image.transitionName = character.id.toString()
                loadImage(image, itemView, bindingAdapterPosition, enterTransitionStarted, character.image)
                name.text = character.name

            }
        }
    }

    fun loadImage(image: ImageView, itemView: View, bindingAdapterPosition: Int, enterTransitionStarted: AtomicBoolean,
                  imageUrl: String, ){
        if(mainFade){
            Glide.with(itemView)
                .load(imageUrl)
                .placeholder(R.color.light_gray)
                .transition(DrawableTransitionOptions.withCrossFade())
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        if(currentPosition != bindingAdapterPosition)
                            return false
                        if(enterTransitionStarted.getAndSet(true))
                            return false
                        fragment.startPostponedEnterTransition()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        if(currentPosition != bindingAdapterPosition)
                            return false
                        if(enterTransitionStarted.getAndSet(true))
                            return false
                        fragment.startPostponedEnterTransition()
                        return false
                    }
                })
                .into(image)
        } else {
            Glide.with(itemView)
                .load(imageUrl)
                .placeholder(R.color.light_gray)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        if(currentPosition != bindingAdapterPosition)
                            return false
                        if(enterTransitionStarted.getAndSet(true))
                            return false
                        fragment.startPostponedEnterTransition()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        if(currentPosition != bindingAdapterPosition)
                            return false
                        if(enterTransitionStarted.getAndSet(true))
                            return false
                        fragment.startPostponedEnterTransition()
                        mainFade = true
                        return false
                    }
                })
                .into(image)
        }

    }
    interface OnItemClickListener {
        fun onItemClick(character: Character, imageView: ImageView, position: Int)
        fun onFavClick(character: Character)
    }
    companion object {
        private val Differ = object : DiffUtil.ItemCallback<Character>() {
            override fun areItemsTheSame(oldItem: Character, newItem: Character) =
                oldItem.id == newItem.id


            override fun areContentsTheSame(oldItem: Character, newItem: Character) =
                oldItem == newItem
        }
    }
}