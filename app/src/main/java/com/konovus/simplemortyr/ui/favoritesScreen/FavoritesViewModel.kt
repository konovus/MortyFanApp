package com.konovus.simplemortyr.ui.favoritesScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.konovus.simplemortyr.data.MortyDao
import com.konovus.simplemortyr.data.MortyRepository
import com.konovus.simplemortyr.entity.Character
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val mortyDao: MortyDao,
    private val repository: MortyRepository
): ViewModel() {

    private val charactersList = mortyDao.getAllCharactersFlow().map {
        it.filter { it.fav }
    }
    val characters = charactersList.asLiveData()

    fun onFavClick(character: Character){
        viewModelScope.launch {
            mortyDao.insert(character)
        }
    }}