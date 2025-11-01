package com.clarice.week78_2.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.clarice.week78_2.ui.model.Album
import com.clarice.week78_2.ui.model.Artist
import com.clarice.week78_2.ui.viewmodel.ArtistUiState

@Composable
fun ArtistScreen(
    uiState: ArtistUiState,
    onAlbumClick: (Album) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C2021))
    ) {
        when {
            uiState.isLoading -> {
                LoadingScreen()
            }
            uiState.error != null -> {
                ErrorScreen(message = uiState.error)
            }
            uiState.artist != null -> {
                ArtistContent(
                    artist = uiState.artist,
                    albums = uiState.albums,
                    onAlbumClick = onAlbumClick
                )
            }
        }
    }
}

@Composable
fun ArtistContent(
    artist: Artist,
    albums: List<Album>,
    onAlbumClick: (Album) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        // Top Bar with Artist Name
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1C2021))
                .padding(horizontal = 16.dp, vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = artist.strArtist,
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFFC8C7AE),
                fontWeight = FontWeight.Normal
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Artist Image Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(
                    width = 1.dp,
                    color = Color(0xFF3A3A3A),
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            AsyncImage(
                model = artist.strArtistThumb ?: artist.strArtistBanner,
                contentDescription = artist.strArtist,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp),
                contentScale = ContentScale.Crop
            )

            // Gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            ),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            )

            // Artist name and genre on bottom left
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = artist.strArtist,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFFC8C7AE),
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = artist.strGenre ?: "Indie",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFC8C7AE).copy(alpha = 0.7f)
                )
            }
        }

        // Biography Section
        if (!artist.strBiographyEN.isNullOrBlank()) {
            Text(
                text = artist.strBiographyEN,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFC8C7AE).copy(alpha = 0.8f),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )
        }

        // Albums Section Header
        Text(
            text = "Albums",
            style = MaterialTheme.typography.titleLarge,
            color = Color(0xFFC8C7AE),
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // Albums Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.height(600.dp)
        ) {
            items(albums) { album ->
                AlbumCard(
                    album = album,
                    onClick = { onAlbumClick(album) }
                )
            }
        }
    }
}

@Composable
fun AlbumCard(
    album: Album,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .border(
                width = 1.dp,
                color = Color(0xFF3A3A3A),
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF282828)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            // Album Cover
            AsyncImage(
                model = album.strAlbumThumb,
                contentDescription = album.strAlbum,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Album Name
            Text(
                text = album.strAlbum,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFC8C7AE),
                fontWeight = FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Year & Genre
            Text(
                text = "${album.intYearReleased ?: ""} • ${album.strGenre ?: ""}",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFC8C7AE).copy(alpha = 0.7f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = Color(0xFFD79921),
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Loading...",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ErrorScreen(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "Error",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFFC8C7AE)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFFB4934),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}