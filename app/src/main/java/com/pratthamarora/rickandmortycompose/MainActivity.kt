package com.pratthamarora.rickandmortycompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.pratthamarora.rickandmortycompose.screens.characterDetail.CharacterDetailScreen
import com.pratthamarora.rickandmortycompose.screens.characterList.CharacterListScreen
import com.pratthamarora.rickandmortycompose.ui.theme.RickAndMortyComposeTheme
import com.pratthamarora.rickandmortycompose.utils.Path
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RickAndMortyComposeTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Path.Routes.CHARACTER_LIST_SCREEN.route
                ) {
                    composable(Path.Routes.CHARACTER_LIST_SCREEN.route) {
                        CharacterListScreen(navController = navController)
                    }
                    composable("${Path.Routes.CHARACTER_DETAIL_SCREEN.route}/{${Path.Params.DOMINANT_COLOR.param}}/{${Path.Params.CHARACTER_ID.param}}",
                        arguments = listOf(
                            navArgument(Path.Params.DOMINANT_COLOR.param) {
                                type = NavType.IntType
                            },
                            navArgument(Path.Params.CHARACTER_ID.param) {
                                type = NavType.IntType
                            }
                        )) {
                        val dominantColor = remember {
                            val color = it.arguments?.getInt(Path.Params.DOMINANT_COLOR.param)
                            color?.let {
                                Color(it)
                            } ?: Color.White
                        }
                        val characterId = remember {
                            it.arguments?.getInt(Path.Params.CHARACTER_ID.param)
                        }
                        CharacterDetailScreen(
                            dominantColor = dominantColor,
                            characterId = characterId ?: 0,
                            navController = navController
                        )
                    }

                }
            }
        }
    }


}