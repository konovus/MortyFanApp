package com.konovus.simplemortyr.ui.bottomSheetScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import com.konovus.simplemortyr.data.MortyRepository
import com.konovus.simplemortyr.entity.Episode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@HiltViewModel
class BottomSheetViewModel @Inject constructor(
    private val repository: MortyRepository
) : ViewModel() {

    val episode = MutableLiveData<Episode?>()

    val characters = episode.asFlow().mapLatest {
        repository.getAllCharacters(it!!.getCharactersIds())
    }.asLiveData()


}