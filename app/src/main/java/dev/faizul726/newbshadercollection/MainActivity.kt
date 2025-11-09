package dev.faizul726.newbshadercollection

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
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
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.lifecycleScope
import dev.faizul726.newbshadercollection.MainActivity.Companion.okHttpClient
import dev.faizul726.newbshadercollection.data.SHADER_REPO
import dev.faizul726.newbshadercollection.data.SHADER_VERSIONS
import dev.faizul726.newbshadercollection.data.customJson
import dev.faizul726.newbshadercollection.data.models.Shader
import dev.faizul726.newbshadercollection.data.models.ShaderVersion
import dev.faizul726.newbshadercollection.data.shaderList
import dev.faizul726.newbshadercollection.data.shaderVersionsList
import dev.faizul726.newbshadercollection.ui.screens.HomeScreen
import dev.faizul726.newbshadercollection.ui.theme.NewbShaderCollectionTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.OkHttpClient
import okhttp3.Request


@OptIn(ExperimentalSerializationApi::class)
class MainActivity : ComponentActivity() {
    companion object {
        internal val okHttpClient = OkHttpClient()
    }
    /*internal var downloadId: Long = 0

    private val onDownloadComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            //Fetching the download id received with the broadcast
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadId == id) {
                Toast.makeText(this@MainActivity, "Download Completed", Toast.LENGTH_SHORT).show()
            }
        }
    }*/

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val shaders = remember { shaderList }
            val shaderVersions = remember { shaderVersionsList }
            val isRefreshing = remember { mutableStateOf(false) }
            val scope = rememberCoroutineScope()

            LaunchedEffect(Unit) { refreshList(scope, shaders, shaderVersions, isRefreshing) }

            NewbShaderCollectionTheme {
                PullToRefreshBox(
                    isRefreshing = isRefreshing.value,
                    onRefresh = { refreshList(scope, shaders, shaderVersions, isRefreshing) }
                ) {
                    Scaffold(
                        topBar = { TopBar() },
                        modifier = Modifier.fillMaxSize()
                    ) { innerPadding ->
                        HomeScreen(Modifier.padding(innerPadding), shaders.value, shaderVersions.value)
                    }
                }
            }
        }
    }
}

private fun refreshList(
    scope: CoroutineScope,
    shaders: MutableState<List<Shader>>,
    shaderVersions: MutableState<List<ShaderVersion>>,
    isRefreshing: MutableState<Boolean>
) {
    scope.launch(Dispatchers.IO) {
        isRefreshing.value = true

        shaders.value = run {
            val shadersRequest = Request.Builder()
                .url(SHADER_REPO)
                .build()

            val json: String? = try {
                val j = okHttpClient.newCall(shadersRequest).execute().body.string()
                Log.d("Fzul", j)
                j
            } catch(e: Exception) {
                null
            }
            json?.let { customJson.decodeFromString(it) } ?: emptyList()
        }

        shaderVersions.value = run {
            val shaderVersionsRequest = Request.Builder()
                .url(SHADER_VERSIONS)
                .build()

            val json: String? = try {
                okHttpClient.newCall(shaderVersionsRequest).execute().body.string()
            } catch(e: Exception) {
                null
            }
            json?.let { customJson.decodeFromString(it) } ?: emptyList()
        }
    }.invokeOnCompletion {
        isRefreshing.value = false
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar() {
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
}

private fun isOnline(context: Context): Boolean {
    // Thanks https://stackoverflow.com/a/57237708
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val capabilities =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
            return true
        }
    }
    return false
}