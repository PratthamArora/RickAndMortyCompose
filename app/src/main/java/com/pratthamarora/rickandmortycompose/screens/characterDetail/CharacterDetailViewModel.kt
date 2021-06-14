package com.pratthamarora.rickandmortycompose.screens.characterDetail

import androidx.lifecycle.ViewModel
import com.pratthamarora.rickandmortycompose.data.remote.response.CharacterDetailResponse
import com.pratthamarora.rickandmortycompose.repository.RickMortyRepository
import com.pratthamarora.rickandmortycompose.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val repository: RickMortyRepository
) : ViewModel() {

    suspend fun getCharacterDetail(id: Int): Resource<CharacterDetailResponse> {
        return repository.getCharacterDetails(id)
    }
}