package com.pratthamarora.rickandmortycompose.data.remote

import com.pratthamarora.rickandmortycompose.data.remote.response.CharacterDetailResponse
import com.pratthamarora.rickandmortycompose.data.remote.response.CharactersListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickMortyApi {

    @GET("character")
    suspend fun getCharactersList(@Query("page") page: Int): CharactersListResponse

    @GET("character/{id}")
    suspend fun getCharacterDetails(@Path("id") id: Int): CharacterDetailResponse
}