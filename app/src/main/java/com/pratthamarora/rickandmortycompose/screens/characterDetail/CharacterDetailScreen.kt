package com.pratthamarora.rickandmortycompose.screens.characterDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavController
import coil.transform.CircleCropTransformation
import com.google.accompanist.coil.CoilImage
import com.pratthamarora.rickandmortycompose.data.remote.response.CharacterDetailResponse
import com.pratthamarora.rickandmortycompose.ui.theme.Roboto
import com.pratthamarora.rickandmortycompose.utils.Resource
import com.pratthamarora.rickandmortycompose.utils.parseStatusToColor
import java.util.*

@Composable
fun CharacterDetailScreen(
    dominantColor: Color,
    characterId: Int,
    navController: NavController,
    topPadding: Dp = 30.dp,
    imageSize: Dp = 150.dp,
    viewModel: CharacterDetailViewModel = hiltNavGraphViewModel()
) {

    val characterInfo =
        produceState<Resource<CharacterDetailResponse>>(initialValue = Resource.Loading()) {
            value = viewModel.getCharacterDetail(characterId)
        }.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(dominantColor)
            .padding(bottom = 16.dp)

    ) {
        CharacterDetailTopBar(
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .align(Alignment.TopCenter)
        )
        CharacterDetailState(
            character = characterInfo,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = topPadding + imageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .shadow(10.dp, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colors.surface)
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            loadingModifier = Modifier
                .size(100.dp)
                .align(Alignment.Center)
                .padding(
                    top = topPadding + imageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
        )
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (characterInfo is Resource.Success) {
                characterInfo.data?.image?.let {
                    CoilImage(
                        data = it,
                        contentDescription = characterInfo.data.name,
                        requestBuilder = {
                            transformations(CircleCropTransformation())
                        },
                        fadeIn = true,
                        modifier = Modifier
                            .size(imageSize)
                            .offset(y = topPadding)

                    )
                }
            }
        }
    }
}

@Composable
fun CharacterDetailTopBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.Black,
                        Color.Transparent
                    )
                )
            )
    ) {
        Icon(imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(24.dp)
                .offset(16.dp, 16.dp)
                .clickable {
                    navController.popBackStack()
                }
        )
    }
}

@Composable
fun CharacterDetailState(
    character: Resource<CharacterDetailResponse>,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier
) {
    when (character) {
        is Resource.Error -> {
            Text(
                text = character.message!!,
                color = Color.Red,
                modifier = modifier
            )
        }
        is Resource.Loading -> {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
                modifier = loadingModifier
            )
        }
        is Resource.Success -> {
            CharacterDetailLayout(
                character = character.data!!,
                modifier = modifier
                    .offset(y = (-20).dp)
            )
        }
    }
}

@Composable
fun CharacterDetailLayout(
    character: CharacterDetailResponse,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .offset(y = 100.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "#${character.id} ${character.name}",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onSurface
        )
        CharacterTypeLayout(characterInfo = listOf(character.status, character.species))
        CharacterMetaDataLayout(character = character)
    }
}

@Composable
fun CharacterTypeLayout(
    characterInfo: List<String>
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
    ) {
        for (info in characterInfo) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .clip(CircleShape)
                    .background(parseStatusToColor(info))
                    .aspectRatio(3f)
            ) {
                Text(
                    text = info.capitalize(Locale.ROOT),
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun CharacterMetaDataLayout(
    character: CharacterDetailResponse
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
    ) {
        Text(
            text = "Gender: ${character.gender.capitalize(Locale.ROOT)}",
            color = MaterialTheme.colors.onSurface,
            fontSize = 22.sp,
            fontFamily = Roboto,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Origin: ${character.origin.name.capitalize(Locale.ROOT)}",
            color = MaterialTheme.colors.onSurface,
            fontSize = 22.sp,
            fontFamily = Roboto,
            textAlign = TextAlign.Center
        )
    }

}