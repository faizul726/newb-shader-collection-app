package dev.faizul726.newbshadercollection.ui.screens

import android.content.ClipData
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import dev.faizul726.newbshadercollection.data.bottomSheetLinks
import dev.faizul726.newbshadercollection.data.models.Shader
import dev.faizul726.newbshadercollection.data.showBottomSheet
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
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val uri = LocalUriHandler.current
    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current

    val jsonLink = "https://raw.githubusercontent.com/faizul726/newb-shader-repo/refs/heads/main/shader-list-testing.json"

    val okHttpClient = OkHttpClient()

    val request = Request.Builder()
        .url(jsonLink)
        .build()

    // Thanks https://stackoverflow.com/questions/32598044/how-can-i-extract-the-raw-json-string-from-an-okhttp-response-object

    lateinit var json: String
    var shaders by remember { mutableStateOf(emptyList<Shader>()) }

    val kJson = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
        allowTrailingComma = true
    }

    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            json = try {
                val j = okHttpClient.newCall(request).execute().body.string()
                Log.d("HomeScreen", j)
                j
            } catch (e: Exception) {
                """
                    {
                    
                    }
                """.trimIndent()
            }
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

    if (showBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet.value = false
            },
            sheetState = sheetState
        ) {
            LazyColumn {
                items(bottomSheetLinks) {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceDim,
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .combinedClickable(
                                onClick = {
                                    uri.openUri(it.link)
                                    scope.launch {
                                        sheetState.hide()
                                    }.invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            showBottomSheet.value = false
                                        }
                                    }
                                },
                                onLongClick = {
                                    clipboard.setText(AnnotatedString(it.link))
                                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                                        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                    ) {
                        Column {
                            Text(
                                text = it.title,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = it.link,
                                style = MaterialTheme.typography.bodyMedium,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }
    }
}