package com.pratthamarora.rickandmortycompose.characterList

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.pratthamarora.rickandmortycompose.data.models.CharacterListEntry
import com.pratthamarora.rickandmortycompose.repository.RickMortyRepository
import com.pratthamarora.rickandmortycompose.utils.Constants
import com.pratthamarora.rickandmortycompose.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val repository: RickMortyRepository
) : ViewModel() {

    private var currentPage = 1

    var characterList = mutableStateOf<List<CharacterListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var isEndOfList = mutableStateOf(false)

    private var cachedCharactersList = listOf<CharacterListEntry>()
    private var isSearchStarting = true
    var isSearching = mutableStateOf(false)


    init {
        loadPaginatedCharacters()
    }

    fun searchCharacterList(query: String) {
        val listToSearch = if (isSearchStarting) {
            characterList.value
        } else {
            cachedCharactersList
        }
        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty()) {
                characterList.value = cachedCharactersList
                isSearching.value = false
                isSearchStarting = true
                return@launch
            }
            val results = listToSearch.filter {
                it.characterName.contains(
                    query.trim(),
                    ignoreCase = true
                ) || it.characterId.toString() == query.trim()
            }
            if (isSearchStarting) {
                cachedCharactersList = characterList.value
                isSearchStarting = false
            }
            characterList.value = results
            isSearching.value = true
        }
    }

    fun loadPaginatedCharacters() {
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getCharactersList(currentPage)
            when (result) {
                is Resource.Success -> {
                    isEndOfList.value =
                        currentPage * Constants.PAGE_SIZE >= result.data!!.info.count
                    val entries = result.data.results.mapIndexed { _, response ->
                        CharacterListEntry(
                            characterId = response.id,
                            characterName = response.name,
                            imageUrl = response.image
                        )
                    }
                    currentPage++
                    loadError.value = ""
                    isLoading.value = false
                    characterList.value += entries
                }
                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
            }
        }
    }

    fun calculateDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bitmap = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Palette.from(bitmap).generate {
            it?.dominantSwatch?.rgb?.let { color ->
                onFinish(Color(color))
            }
        }
    }
}