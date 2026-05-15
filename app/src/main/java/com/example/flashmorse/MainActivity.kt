package com.example.flashmorse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.flashmorse.presentation.navigation.NavigationTabs
import com.example.flashmorse.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                // NavigationTabs now manages its own NavController internally,
                // keeping MainActivity clean and focused.
                NavigationTabs(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
