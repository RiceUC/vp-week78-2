package com.clarice.week78_2.ui.model

data class AlbumResponse(
    val album: List<Album>?
)

data class Album(
    val idAlbum: String,
    val strAlbum: String,
    val strArtist: String?,
    val intYearReleased: String?,
    val strGenre: String?,
    val strAlbumThumb: String?,
    val strDescriptionEN: String?
)