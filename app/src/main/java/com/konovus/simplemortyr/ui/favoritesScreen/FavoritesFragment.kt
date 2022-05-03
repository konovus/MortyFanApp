package com.konovus.simplemortyr.ui.favoritesScreen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.konovus.simplemortyr.R
import com.konovus.simplemortyr.databinding.FavoritesFragmentBinding
import com.konovus.simplemortyr.entity.Character
import com.konovus.simplemortyr.ui.mainScreen.MainFragment.Companion.characters
import com.konovus.simplemortyr.ui.mainScreen.MainFragment.Companion.detailsFade
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.favorites_fragment),
    FavoritesAdapter.OnItemClickListener {

    private val viewModel: FavoritesViewModel by viewModels()
    private var _binding: FavoritesFragmentBinding? = null
    private val binding get() = _binding!!

    companion object{
        var fromFavorites = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FavoritesFragmentBinding.bind(view)
        val adapter = FavoritesAdapter(this)

        binding.apply {
            recyclerView.adapter = adapter
            recyclerView.setHasFixedSize(true)
            viewModel.characters.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }

        }

    }

    override fun onItemClick(character: Character) {
        detailsFade = false
        fromFavorites = true
        val action = FavoritesFragmentDirections.actionHiltFavoritesFragmentToDetailsFragment(character)
        findNavController().navigate(action)
    }

    override fun onFavClick(character: Character) {
        if(characters.indexOf(character) != -1)
            characters[characters.indexOf(character)].fav = false
        viewModel.onFavClick(character.copy(fav = !character.fav))
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}