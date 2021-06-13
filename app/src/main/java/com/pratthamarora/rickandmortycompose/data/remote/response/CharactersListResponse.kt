package com.pratthamarora.rickandmortycompose.data.remote.response

data class CharactersListResponse(
    val info: Info,
    val results: List<CharacterDetailResponse>
)