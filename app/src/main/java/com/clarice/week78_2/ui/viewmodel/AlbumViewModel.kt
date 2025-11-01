package com.clarice.week78_2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clarice.week78_2.ui.model.Album
import com.clarice.week78_2.ui.model.Track
import com.clarice.week78_2.ui.repository.ArtistRepository
import com.clarice.week78_2.ui.repository.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AlbumUiState(
    val album: Album? = null,
    val tracks: List<Track> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class AlbumViewModel(private val repository: ArtistRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AlbumUiState())
    val uiState: StateFlow<AlbumUiState> = _uiState

    fun loadAlbumDetails(album: Album) {
        viewModelScope.launch {
            _uiState.value = AlbumUiState(
                album = album,
                isLoading = true
            )

            when (val result = repository.getAlbumTracks(album.idAlbum)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        tracks = result.data,
                        isLoading = false
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                Result.Loading -> {}
            }
        }
    }
}