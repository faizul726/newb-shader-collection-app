package dev.faizul726.newbshadercollection.ui.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ShaderScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("hello") }
            )
        }
    ) { innerPadding ->

    }
}