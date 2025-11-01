package com.clarice.week78_2.ui.service

import com.clarice.week78_2.ui.model.AlbumResponse
import com.clarice.week78_2.ui.model.ArtistResponse
import com.clarice.week78_2.ui.model.TrackResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface AudioDbService {

    @GET("search.php")
    suspend fun searchArtist(@Query("s") artistName: String): Response<ArtistResponse>

    @GET("searchalbum.php")
    suspend fun searchAlbums(@Query("s") artistName: String): Response<AlbumResponse>

    @GET("track.php")
    suspend fun getAlbumTracks(@Query("m") albumId: String): Response<TrackResponse>

    companion object {
        private const val BASE_URL = "https://www.theaudiodb.com/api/v1/json/2/"

        fun create(): AudioDbService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AudioDbService::class.java)
        }
    }
}