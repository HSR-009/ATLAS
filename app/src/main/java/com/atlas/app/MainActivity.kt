package com.atlas.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.atlas.app.navigation.AppNavGraph
import com.atlas.app.ui.theme.AtlasTheme
import com.atlas.app.ui.theme.PageBg

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enable edge-to-edge so content can draw behind system bars.
        // Each screen applies statusBarsPadding() to its header and
        // AtlasBottomNav applies navigationBarsPadding() for the system nav bar.
        enableEdgeToEdge()
        setContent {
            AtlasTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = PageBg
                ) {
                    AppNavGraph()
                }
            }
        }
    }
}
