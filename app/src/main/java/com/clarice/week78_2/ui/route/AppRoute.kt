package com.clarice.week78_2.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.clarice.week78_2.ui.view.AlbumDetailScreen
import com.clarice.week78_2.ui.view.ArtistScreen
import com.clarice.week78_2.ui.viewmodel.AlbumViewModel
import com.clarice.week78_2.ui.viewmodel.ArtistViewModel

sealed class Screen(val route: String) {
    object Artist : Screen("artist")
    object AlbumDetail : Screen("album_detail")
}

@Composable
fun AppNavigation(
    artistViewModel: ArtistViewModel,
    albumViewModel: AlbumViewModel,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Artist.route
    ) {
        // Artist Screen
        composable(Screen.Artist.route) {
            val uiState by artistViewModel.uiState.collectAsState()

            ArtistScreen(
                uiState = uiState,
                onAlbumClick = { album ->
                    albumViewModel.loadAlbumDetails(album)
                    navController.navigate(Screen.AlbumDetail.route)
                }
            )
        }

        // Album Detail Screen
        composable(Screen.AlbumDetail.route) {
            val uiState by albumViewModel.uiState.collectAsState()

            AlbumDetailScreen(
                uiState = uiState,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}