package com.konovus.simplemortyr.ui.mainScreen

import androidx.lifecycle.*
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import com.konovus.simplemortyr.data.MortyDao
import com.konovus.simplemortyr.data.MortyRepository
import com.konovus.simplemortyr.entity.Character
import com.konovus.simplemortyr.util.Filter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MortyRepository,
    private val mortyDao: MortyDao,
    private val state: SavedStateHandle
) : ViewModel(){

    var filter = Filter.NAME
    var fromMain = false

    @OptIn(ExperimentalPagingApi::class)
    private val _characters = state.getLiveData("query", "").asFlow().flatMapLatest { query ->
        repository.getAllCharactersRemoteMediator( query, filter)
    }.cachedIn(viewModelScope)

    val characters = _characters.asLiveData()
    val charactersFlow = _characters

    fun onFavClick(character: Character){
        val item = character.copy(fav = true)
        viewModelScope.launch {
            mortyDao.insert(item)
        }
    }

    fun searchCharacters(filter: Filter, query: String, fromMain: Boolean = false) {
        this.filter = filter
        state["query"] = query
        this.fromMain = fromMain
    }
}