package com.konovus.simplemortyr.ui.bottomSheetScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.konovus.simplemortyr.databinding.BottomSheetEpisodeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetEpisodeBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<BottomSheetFragmentArgs>()
    private val viewModel: BottomSheetViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetEpisodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val episode = args.episode
        viewModel.episode.value = episode
        val adapter = BottomSheetAdapter()

        binding.apply {
            seasonEpisode.text = episode.getFormattedEpisodeName()
            nameEpisode.text = episode.name
            airDate.text = episode.air_date

            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter

            viewModel.characters.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}