package com.pratthamarora.rickandmortycompose.utils

sealed class Path {
    enum class Routes(val route: String) {
        CHARACTER_DETAIL_SCREEN("character_detail_screen"),
        CHARACTER_LIST_SCREEN("character_list_screen"),
    }

    enum class Params(val param: String) {
        DOMINANT_COLOR("dominantColor"),
        CHARACTER_ID("characterId"),
    }
}

