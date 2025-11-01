package com.clarice.week78_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.clarice.week78_2.ui.theme.Week78_2Theme
import com.clarice.week78_2.ui.repository.ArtistRepository
import com.clarice.week78_2.ui.route.AppNavigation
import com.clarice.week78_2.ui.service.AudioDbService
import com.clarice.week78_2.ui.viewmodel.AlbumViewModel
import com.clarice.week78_2.ui.viewmodel.ArtistViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Service, Repository, and ViewModels
        val service = AudioDbService.create()
        val repository = ArtistRepository(service)
        val artistViewModel = ArtistViewModel(repository)
        val albumViewModel = AlbumViewModel(repository)

        enableEdgeToEdge()
        setContent {
            Week78_2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Load artist data when app starts
                    LaunchedEffect(Unit) {
                        artistViewModel.loadArtist("Taylor Swift")
                    }

                    // Navigation from ui/route/AppRoute.kt
                    AppNavigation(
                        artistViewModel = artistViewModel,
                        albumViewModel = albumViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Week78_2Theme {
        Greeting("Android")
    }
}