package com.konovus.simplemortyr.ui.detailsScreen

import androidx.lifecycle.*
import com.konovus.simplemortyr.data.MortyDao
import com.konovus.simplemortyr.data.MortyRepository
import com.konovus.simplemortyr.entity.Character
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val mortyDao: MortyDao,
    private val repository: MortyRepository
): ViewModel() {

    val characters = mortyDao.getAllCharactersFlow().mapLatest {
        it.filter { it.fav }
    }.asLiveData()

    var episodeIds = MutableLiveData<String>()

    val episodes = episodeIds.asFlow().mapLatest {
//        repository.getAllEpisodes(it)
        repository.getAllEpisodesSafe(it)
    }.asLiveData()

    fun onFavClick(character: Character){
        viewModelScope.launch {
            mortyDao.insert(character)
        }
    }
}