package com.konovus.simplemortyr.ui.mainScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.konovus.simplemortyr.databinding.LoadStateFooterBinding
import com.konovus.simplemortyr.ui.mainScreen.MainFragment.Companion.characters
import com.konovus.simplemortyr.ui.mainScreen.MainFragment.Companion.isLocalLoading

class MortyLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<MortyLoadStateAdapter.LoadStateViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = LoadStateFooterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        return LoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }


    inner class LoadStateViewHolder(private val binding: LoadStateFooterBinding) :
        RecyclerView.ViewHolder(binding.root){

        init {
            binding.retryBtn.setOnClickListener{
                isLocalLoading = true
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState){
            binding.apply {
                    progressBar.isVisible = loadState is LoadState.Loading && characters.size > 0
                    retryBtn.isVisible = loadState !is LoadState.Loading && characters.size > 0
                    tvError.isVisible = loadState !is LoadState.Loading && characters.size > 0
                    isLocalLoading = progressBar.isVisible
            }
        }
    }

}