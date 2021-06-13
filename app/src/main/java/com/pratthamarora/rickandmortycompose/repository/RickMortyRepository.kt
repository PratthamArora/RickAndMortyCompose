package com.pratthamarora.rickandmortycompose.repository

import com.pratthamarora.rickandmortycompose.data.remote.RickMortyApi
import com.pratthamarora.rickandmortycompose.data.remote.response.CharacterDetailResponse
import com.pratthamarora.rickandmortycompose.data.remote.response.CharactersListResponse
import com.pratthamarora.rickandmortycompose.utils.Resource
import dagger.hilt.android.scopes.ActivityScoped
import timber.log.Timber
import javax.inject.Inject

@ActivityScoped
class RickMortyRepository @Inject constructor(
    private val api: RickMortyApi
) {

    suspend fun getCharactersList(page: Int): Resource<CharactersListResponse> {
        val response = try {
            api.getCharactersList(page = page)
        } catch (e: Exception) {
            Timber.e(e)
            return Resource.Error(e.localizedMessage ?: "An error occurred")
        }
        return Resource.Success(response)
    }

    suspend fun getCharacterDetails(characterId: Int): Resource<CharacterDetailResponse> {
        val response = try {
            api.getCharacterDetails(id = characterId)
        } catch (e: Exception) {
            Timber.e(e)
            return Resource.Error(e.localizedMessage ?: "An error occurred")
        }
        return Resource.Success(response)
    }
}