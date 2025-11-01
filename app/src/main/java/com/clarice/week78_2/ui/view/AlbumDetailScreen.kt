package com.clarice.week78_2.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.clarice.week78_2.ui.model.Track
import com.clarice.week78_2.ui.viewmodel.AlbumUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailScreen(
    uiState: AlbumUiState,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.album?.strAlbum ?: "Album",
                            color = Color(0xFFC8C7AE)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFFC8C7AE)
                        )
                    }
                },
                actions = {
                    // Invisible spacer to balance the back button
                    Spacer(modifier = Modifier.width(48.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1C2021)
                )
            )
        },
        containerColor = Color(0xFF1C2021)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    LoadingScreen()
                }
                uiState.error != null -> {
                    ErrorScreen(message = uiState.error)
                }
                uiState.album != null -> {
                    AlbumDetailContent(
                        uiState = uiState
                    )
                }
            }
        }
    }
}

@Composable
fun AlbumDetailContent(uiState: AlbumUiState) {
    val album = uiState.album ?: return

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        // Album Card with Cover and Info
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF282828))
            ) {
                // Album Cover
                AsyncImage(
                    model = album.strAlbumThumb,
                    contentDescription = album.strAlbum,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    contentScale = ContentScale.Crop
                )

                // Album Info
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = album.strAlbum,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFFC8C7AE)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${album.intYearReleased ?: ""} • ${album.strGenre ?: "Indie"}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFC8C7AE).copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Description
                    if (!album.strDescriptionEN.isNullOrBlank()) {
                        Text(
                            text = album.strDescriptionEN,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFC8C7AE).copy(alpha = 0.8f),
                            maxLines = 6,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        // Tracks Header
        item {
            Text(
                text = "Tracks",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFFC8C7AE),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )
        }

        // Track List
        itemsIndexed(uiState.tracks) { index, track ->
            TrackItem(
                track = track,
                trackNumber = index + 1
            )
            if (index < uiState.tracks.size - 1) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    thickness = 1.dp,
                    color = Color(0xFF333333)
                )
            }
        }

        // Bottom spacing
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun TrackItem(
    track: Track,
    trackNumber: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Track Number with background
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFF493D28)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = trackNumber.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFC8C7AE)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Track Name
        Text(
            text = track.strTrack,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFFC8C7AE),
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        // Duration
        if (!track.intDuration.isNullOrBlank()) {
            val durationInMillis = track.intDuration.toLongOrNull() ?: 0
            val minutes = durationInMillis / 60000
            val seconds = (durationInMillis % 60000) / 1000
            Text(
                text = String.format("%d:%02d", minutes, seconds),
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFC8C7AE).copy(alpha = 0.7f)
            )
        }
    }
}