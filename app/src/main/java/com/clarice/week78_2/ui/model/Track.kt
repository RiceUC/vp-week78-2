package com.clarice.week78_2.ui.model

data class TrackResponse(
    val track: List<Track>?
)

data class Track(
    val idTrack: String,
    val strTrack: String,
    val intDuration: String?
)