package dev.faizul726.newbshadercollection.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.faizul726.newbshadercollection.data.models.Shader
import dev.faizul726.newbshadercollection.ui.components.ItemCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(modifier: Modifier) {

    val jsonLink = "https://raw.githubusercontent.com/faizul726/newb-shader-repo/refs/heads/main/shader-list-testing.json"

    val okHttpClient = OkHttpClient()

    val request = Request.Builder()
        .url(jsonLink)
        .build()

    // Thanks https://stackoverflow.com/questions/32598044/how-can-i-extract-the-raw-json-string-from-an-okhttp-response-object

    lateinit var json: String
    var shaders by remember { mutableStateOf(emptyList<Shader>()) }

    val scope = rememberCoroutineScope()
    val kJson = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
        allowTrailingComma = true
    }

    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            json = okHttpClient.newCall(request).execute().body.string()
            shaders = kJson.decodeFromString<List<Shader>>(json)
        }
    }

    LazyColumn {
        item {
            Spacer(Modifier.height(112.dp))
        }
        items(shaders) {
            ItemCard(
                thumbnail = it.thumbnail,
                title = it.title,
                creator = it.creator,
                platforms = it.platforms,
                downloadLink = it.downloadLink,
                links = it.otherLinks
            )
        }
        item {
            Spacer(Modifier.height(32.dp))
        }
    }
}