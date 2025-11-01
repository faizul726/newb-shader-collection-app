package dev.faizul726.newbshadercollection

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.faizul726.newbshadercollection.ui.screens.HomeScreen
import dev.faizul726.newbshadercollection.ui.theme.NewbShaderCollectionTheme
import okhttp3.OkHttpClient

class MainActivity : ComponentActivity() {
    //@OptIn(ExperimentalMaterial3Api::class)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewbShaderCollectionTheme {
                Scaffold(
                    topBar = {
                        var searchBarExpanded by rememberSaveable { mutableStateOf(false) }
                        var textFieldState by remember { mutableStateOf(TextFieldState()) }
                        var showMenu by remember { mutableStateOf(false) }

                        SearchBar(
                            expanded = searchBarExpanded,
                            onExpandedChange = { searchBarExpanded = it },
                            inputField = {
                                SearchBarDefaults.InputField(
                                    leadingIcon = { Icon(Icons.Default.Search, null) },
                                    trailingIcon = {
                                        IconButton(
                                            onClick = {
                                                showMenu = !showMenu
                                            }
                                        ) {
                                            Icon(Icons.Default.MoreVert, null)
                                        }
                                        DropdownMenu(
                                            expanded = showMenu,
                                            onDismissRequest = { showMenu = false }
                                        ) {
                                            DropdownMenuItem(
                                                text = { Text("Downloads") },
                                                onClick = {}
                                            )
                                            DropdownMenuItem(
                                                text = { Text("Settings") },
                                                onClick = {}
                                            )
                                            DropdownMenuItem(
                                                text = { Text("About") },
                                                onClick = {}
                                            )
                                        }
                                    },
                                    expanded = searchBarExpanded,
                                    query = textFieldState.text.toString(),
                                    onQueryChange = { textFieldState.edit { replace(0, length, it) } },
                                    onSearch = {
                                        searchBarExpanded = false
                                    },
                                    onExpandedChange = { searchBarExpanded = it },
                                    placeholder = { Text("Search shaders") },
                                    //modifier = Modifier.padding(horizontal = 12.dp).padding(top = 12.dp)
                                )
                            },
                            modifier = animateDpAsState(
                                targetValue = if (!searchBarExpanded) 10.dp else 0.dp,
                                //animationSpec = spring() // Optional: you can customize the spring animation
                            ).value.let { dp ->
                                Modifier
                                    .padding(horizontal = dp)
                                    .padding(top = dp)
                            }
                            //if (!searchBarExpanded) Modifier.padding(horizontal = 12.dp).padding(top = 12.dp) else Modifier
                            //windowInsets = WindowInsets(top = 12.dp, left = 12.dp, right = 12.dp)
                        ) {
                            Text("hello")
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    HomeScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }
}