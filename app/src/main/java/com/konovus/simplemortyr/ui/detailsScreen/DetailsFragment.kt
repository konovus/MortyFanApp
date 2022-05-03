package com.konovus.simplemortyr.ui.detailsScreen

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.konovus.simplemortyr.R
import com.konovus.simplemortyr.databinding.DetailsFragmentBinding
import com.konovus.simplemortyr.entity.Character
import com.konovus.simplemortyr.entity.Episode
import com.konovus.simplemortyr.ui.favoritesScreen.FavoritesFragment.Companion.fromFavorites
import com.konovus.simplemortyr.ui.mainScreen.MainFragment.Companion.detailsFade
import com.konovus.simplemortyr.ui.pagerScreen.PagerFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.details_fragment), EpisodesAdapter.OnItemClickListener {

    private var _binding: DetailsFragmentBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<DetailsFragmentArgs>()
    private val viewModel: DetailsViewModel by viewModels()
    private var character = Character()

    companion object {
        fun newInstance(_character: Character) = DetailsFragment().apply {
            arguments?.putParcelable("character", _character)
            character = _character
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null)
            character = args.character

        _binding = DetailsFragmentBinding.bind(view)
        binding.imageDetails.transitionName = character.id.toString()
        val adapter = EpisodesAdapter(this)

        binding.apply {

            loadImage(imageDetails, character)
            name.text = character.name

            if (character.gender == "Male")
                gender.setImageResource(R.drawable.ic_baseline_male_24)
            else gender.setImageResource(R.drawable.ic_baseline_female_24)

            status.text = character.status
            originStatus.text = character.origin.name
            speciesStatus.text = character.species

            viewModel.characters.observe(viewLifecycleOwner) {
                if (it.map { character -> character.id }.contains(character.id)) {
                    setFavorite(true, fav)
                    character.fav = true
                }
            }

            viewModel.episodeIds.value = character.getEpisodesList()

            recyclerView.adapter = adapter
            recyclerView.setHasFixedSize(true)
            viewModel.episodes.observe(viewLifecycleOwner) {
                adapter.submitList(it)
                if (it.isEmpty())
                    recyclerView.visibility = View.GONE
            }

            fav.setOnClickListener {
                character.fav = !character.fav
                setFavorite(character.fav, fav)
                viewModel.onFavClick(character)
            }
        }
    }

    private fun setFavorite(fav: Boolean, ivFav: ImageView) {
        if (fav)
            ivFav.setImageResource(R.drawable.ic_baseline_favorite_24)
        else ivFav.setImageResource(R.drawable.ic_baseline_favorite_border_24)
    }

    private fun loadImage(imageDetails: ImageView, character: Character) {
        if (detailsFade) {
            Glide.with(this@DetailsFragment)
                .load(character.image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(R.color.light_gray)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        parentFragment?.startPostponedEnterTransition()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        parentFragment?.startPostponedEnterTransition()
                        return false
                    }
                })
                .into(imageDetails)
        } else {
            Glide.with(this@DetailsFragment)
                .load(character.image)
                .placeholder(R.color.light_gray)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        parentFragment?.startPostponedEnterTransition()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        parentFragment?.startPostponedEnterTransition()
                        detailsFade = true
                        return false
                    }
                })
                .into(imageDetails)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(episode: Episode) {
        val action: NavDirections = if (fromFavorites)
            DetailsFragmentDirections.actionDetailsFragmentToHiltBottomSheetFragment(episode)
            else PagerFragmentDirections.actionHiltPagerFragmentToHiltBottomSheetFragment(episode)

        findNavController().navigate(action)
    }
}