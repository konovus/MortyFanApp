package com.konovus.simplemortyr.ui.mainScreen

import android.app.SharedElementCallback
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import androidx.transition.TransitionSet
import com.konovus.simplemortyr.R
import com.konovus.simplemortyr.databinding.MainFragmentBinding
import com.konovus.simplemortyr.entity.Character
import com.konovus.simplemortyr.ui.favoritesScreen.FavoritesFragment.Companion.fromFavorites
import com.konovus.simplemortyr.util.Filter
import com.konovus.simplemortyr.util.onActionExpanded
import com.konovus.simplemortyr.util.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

const val TAG = "Morty"

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.main_fragment), MainAdapter.OnItemClickListener {

    private val viewModel: MainViewModel by activityViewModels()

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private val adapter = MainAdapter(this, this)
    private lateinit var searchView: SearchView

    companion object {
        val rvPos = MutableLiveData(0)
        var currentPosition = 0
        var characters = ArrayList<Character>()
        var mainFade = true
        var detailsFade = false
        var isLocalLoading = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = MainFragmentBinding.bind(view)

        initRecyclerView()
//        initEpoxyRecyclerView()
        scrollToPosition()
        setHasOptionsMenu(true)
    }

    private fun initRecyclerView() {
        binding.apply {
            recyclerView.adapter = adapter
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = MortyLoadStateAdapter { adapter.retry() },
                footer = MortyLoadStateAdapter { adapter.retry() }
            )
            retryBtn.setOnClickListener {
                adapter.retry()
            }

            viewModel.characters.observe(viewLifecycleOwner) {
                adapter.submitData(lifecycle, it)
            }

            adapter.addLoadStateListener { loadState ->
                val refreshState = loadState.mediator?.refresh
                binding.progressBar.isVisible = loadState.mediator?.append is LoadState.Loading && !isLocalLoading
                recyclerView.isVisible = refreshState is LoadState.NotLoading
                binding.retryBtn.isVisible = loadState.append is LoadState.Error && adapter.itemCount < 1
                errorTv.isVisible = loadState.append is LoadState.Error && adapter.itemCount < 1

                if (refreshState is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {
                    recyclerView.isVisible = false
                    emptyTv.isVisible = true
                } else emptyTv.isVisible = false
            }

        }

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                characters = adapter.snapshot().items as ArrayList<Character>
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                super.onItemRangeRemoved(positionStart, itemCount)
                characters = adapter.snapshot().items as ArrayList<Character>
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)

        var queryTextChangedJob: Job? = null
        val searchItem = menu.findItem(R.id.search)
        searchView = searchItem.actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE

        searchItem.onActionExpanded {
            viewModel.searchCharacters(Filter.NAME, "")
        }

        searchView.onQueryTextChanged(searchView) { query ->
            queryTextChangedJob?.cancel()

            queryTextChangedJob = lifecycleScope.launch {
                delay(500)
                if (query.isNotEmpty()) {
                    viewModel.searchCharacters(getFilter(query), query)
                    binding.recyclerView.scrollToPosition(0)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite -> {
                exitTransition = null
                val action = MainFragmentDirections.actionMainFragmentToHiltFavoritesFragment()
                findNavController().navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClick(character: Character, imageView: ImageView, position: Int) {
        mainFade = false
        fromFavorites = false
        currentPosition = position
        (exitTransition as TransitionSet).excludeTarget(imageView, true)

        val action = MainFragmentDirections.actionMainFragmentToHiltPagerFragment()
        val extras = FragmentNavigatorExtras(imageView to imageView.transitionName)
        findNavController().navigate(action, extras)
    }

    override fun onFavClick(character: Character) {
        viewModel.onFavClick(character)
    }


    //region boilerplate methods
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        prepareTransitions()
        postponeEnterTransition()
        return view
    }

    private fun getFilter(query: String): Filter {
        val filterAll = "alive dead unknown human alien humanoid mythological disease male female genderless"
            return when {
                filterAll.split(" ")[0].contains(query) -> Filter.STATUS_ALIVE
                filterAll.split(" ")[1].contains(query) -> Filter.STATUS_DEAD
                filterAll.split(" ")[2].contains(query) -> Filter.STATUS_UNKNOWN
                filterAll.split(" ")[3].contains(query) -> Filter.SPECIES_HUMAN
                filterAll.split(" ")[4].contains(query) -> Filter.SPECIES_ALIEN
                filterAll.split(" ")[5].contains(query) -> Filter.SPECIES_HUMANOID
                filterAll.split(" ")[6].contains(query) -> Filter.SPECIES_MYTHOLOGICAL
                filterAll.split(" ")[7].contains(query) -> Filter.SPECIES_DISEASE
                filterAll.split(" ")[8].startsWith(query) -> Filter.GENDER_MALE
                filterAll.split(" ")[9].startsWith(query) -> Filter.GENDER_FEMALE
                filterAll.split(" ")[10].contains(query) -> Filter.GENDER_GENDERLESS
                else -> Filter.NAME
            }
    }

    private fun scrollToPosition() {

        binding.recyclerView.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(
                v: View?,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
            ) {
                binding.recyclerView.removeOnLayoutChangeListener(this)
                val layoutManager: RecyclerView.LayoutManager? = binding.recyclerView.layoutManager
                val viewAtPosition = layoutManager?.findViewByPosition(currentPosition)
                // Scroll to position if the view for the current position is null (not currently part of
                // layout manager children), or it's not completely visible.
                if (viewAtPosition == null || layoutManager
                        .isViewPartiallyVisible(viewAtPosition, false, true)
                ) {
                    binding.recyclerView.post {
                        layoutManager?.scrollToPosition(currentPosition)
                    }
                }
            }
        })
    }
    private fun prepareTransitions() {
        detailsFade = false
        exitTransition = TransitionInflater.from(context)
            .inflateTransition(R.transition.exit_transition)

        // A similar mapping is set at the PagerFragment with a setEnterSharedElementCallback.
        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: MutableList<String>?,
                sharedElements: MutableMap<String, View>?
            ) {
                val selectedViewHolder: RecyclerView.ViewHolder = binding.recyclerView
                    .findViewHolderForAdapterPosition(currentPosition) ?: return

                // Map the first shared element name to the child ImageView.
                sharedElements?.set(
                    names?.get(0).toString(),
                    selectedViewHolder.itemView.findViewById(R.id.image)
                )
            }
        })
    }
    private fun initEpoxyRecyclerView() {
        binding.apply {
            epoxyRv.visibility = View.VISIBLE
            val epoxyController = CharactersEpoxyController()
            epoxyRv.setController(epoxyController)

            lifecycleScope.launch {
                viewModel.charactersFlow.collectLatest {
                    epoxyController.submitData(it)
                }
            }

            epoxyController.addLoadStateListener {
                progressBar.isVisible = it.source.refresh is LoadState.Loading
                recyclerView.isVisible = it.source.refresh is LoadState.NotLoading
                retryBtn.isVisible = it.source.refresh is LoadState.Error
                errorTv.isVisible = it.source.refresh is LoadState.Error

                if (it.source.refresh is LoadState.NotLoading &&
                    it.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {
                    epoxyRv.isVisible = false
                    emptyTv.isVisible = true
                } else emptyTv.isVisible = false
            }
            retryBtn.setOnClickListener { epoxyController.retry() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchView.setOnQueryTextListener(null)
        _binding = null
    }
    //endregion
}