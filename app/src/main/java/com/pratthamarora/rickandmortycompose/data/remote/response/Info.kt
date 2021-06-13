package com.pratthamarora.rickandmortycompose.data.remote.response

data class Info(
    val count: Int,
    val next: String,
    val pages: Int,
    val prev: String
)