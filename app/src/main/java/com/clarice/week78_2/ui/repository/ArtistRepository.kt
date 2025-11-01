package com.clarice.week78_2.ui.repository

import com.clarice.week78_2.ui.model.*
import com.clarice.week78_2.ui.service.AudioDbService

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

class ArtistRepository(private val service: AudioDbService) {

    suspend fun getArtist(artistName: String): Result<Artist> {
        return try {
            val response = service.searchArtist(artistName)
            if (response.isSuccessful && response.body()?.artists != null) {
                val artist = response.body()?.artists?.firstOrNull()
                if (artist != null) {
                    Result.Success(artist)
                } else {
                    Result.Error("Artist not found")
                }
            } else {
                Result.Error("Failed to load artist data")
            }
        } catch (e: Exception) {
            Result.Error("Error: Tidak ada koneksi internet")
        }
    }

    suspend fun getAlbums(artistName: String): Result<List<Album>> {
        return try {
            val response = service.searchAlbums(artistName)
            if (response.isSuccessful && response.body()?.album != null) {
                Result.Success(response.body()?.album ?: emptyList())
            } else {
                Result.Error("Failed to load albums")
            }
        } catch (e: Exception) {
            Result.Error("Error: Tidak ada koneksi internet")
        }
    }

    suspend fun getAlbumTracks(albumId: String): Result<List<Track>> {
        return try {
            val response = service.getAlbumTracks(albumId)
            if (response.isSuccessful && response.body()?.track != null) {
                Result.Success(response.body()?.track ?: emptyList())
            } else {
                Result.Error("Failed to load tracks")
            }
        } catch (e: Exception) {
            Result.Error("Error: Tidak ada koneksi internet")
        }
    }
}