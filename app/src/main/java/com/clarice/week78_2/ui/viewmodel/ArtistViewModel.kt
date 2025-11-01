package com.clarice.week78_2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clarice.week78_2.ui.model.*
import com.clarice.week78_2.ui.repository.ArtistRepository
import com.clarice.week78_2.ui.repository.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ArtistUiState(
    val artist: Artist? = null,
    val albums: List<Album> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class ArtistViewModel(private val repository: ArtistRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ArtistUiState())
    val uiState: StateFlow<ArtistUiState> = _uiState

    fun loadArtist(artistName: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            // Load artist info
            when (val artistResult = repository.getArtist(artistName)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(artist = artistResult.data)
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = artistResult.message
                    )
                    return@launch
                }
                Result.Loading -> {}
            }

            // Load albums
            when (val albumsResult = repository.getAlbums(artistName)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        albums = albumsResult.data,
                        isLoading = false
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = albumsResult.message
                    )
                }
                Result.Loading -> {}
            }
        }
    }
}