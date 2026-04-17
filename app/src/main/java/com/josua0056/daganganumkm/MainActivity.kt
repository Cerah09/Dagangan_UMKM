package com.josua0056.daganganumkm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.josua0056.daganganumkm.ui.screen.AppNavigation
import com.josua0056.daganganumkm.ui.theme.DaganganUMKMTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DaganganUMKMTheme {
                AppNavigation()
                }
            }
        }
    }