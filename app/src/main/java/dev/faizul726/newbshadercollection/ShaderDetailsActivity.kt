package dev.faizul726.newbshadercollection

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.faizul726.newbshadercollection.data.bottomSheetLinks
import dev.faizul726.newbshadercollection.data.customJson
import dev.faizul726.newbshadercollection.data.models.OtherLink
import dev.faizul726.newbshadercollection.data.showBottomSheet
import dev.faizul726.newbshadercollection.ui.theme.NewbShaderCollectionTheme
import kotlinx.coroutines.launch

class ShaderDetailsActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val title = intent.extras?.getString("TITLE") ?: "Unknown"
        val description = intent.extras?.getString("DESCRIPTION") ?: "N/A"
        val downloadLink = intent.extras!!.getString("DOWNLOAD_LINK") ?: "https://example.com"
        val otherLinks = (intent.extras?.getString("LINKS"))?.let { json ->
            customJson.decodeFromString<Set<OtherLink>>(json)
        } ?: emptySet()
        val screenshots = (intent.extras?.getStringArray("SCREENSHOTS") ?: emptyArray()).toList()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val screenshotToShow = remember { mutableStateOf("") }
            val sheetState = rememberModalBottomSheetState()

            BackHandler {
                Log.d("Fzul", "Cond 0.1 met")
                if (screenshotToShow.value.isEmpty()) {
                    Log.d("Fzul", "Cond 0.2 met")
                    finish()
                } else {
                    Log.d("Fzul", "Cond 0.3 met")
                    screenshotToShow.value = ""
                }
                Log.d("Fzulll", screenshotToShow.value)
            }
            NewbShaderCollectionTheme {
                Scaffold(
                    topBar = { TopBar(title, this) },
                    bottomBar = { BottomBar(downloadLink, otherLinks) },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    ShaderScreen(
                        title = title,
                        description = description,
                        screenshots = screenshots,
                        screenshotToShow = screenshotToShow,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                if (screenshotToShow.value.isNotBlank()) {
                    Scaffold { innerPadding ->
                        ScreenshotViewer(screenshotToShow, screenshots, Modifier.padding(innerPadding))
                    }
                }
                if (showBottomSheet.value) {
                    BottomSheet(
                        visibility = showBottomSheet,
                        sheetState = sheetState
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheet(visibility: MutableState<Boolean>, sheetState: SheetState) {
    val uriHandler = LocalUriHandler.current
    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Log.d("Fzul", "show bottom sheet")
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
                                uriHandler.openUri(it.link)
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

@Composable
private fun ScreenshotViewer(screenshotToShow: MutableState<String>, screenshots: List<String>, modifier: Modifier = Modifier) {
    var currentScreenshot by remember { mutableStateOf(screenshots.indexOf(screenshotToShow.value)) }

    var totalScreenshots = screenshots.size - 1

    fun setCurrentScreenshot(index: Int) {
        totalScreenshots = index.coerceIn(0, totalScreenshots)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        AsyncImage(
            model = screenshots[currentScreenshot],
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.align(Alignment.Center)
        )

        IconButton(
            onClick = { screenshotToShow.value = ""  },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .background(
                    color = Color.Black.copy(alpha = 0.35f),
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = Color.White
            )
        }
        if (currentScreenshot > 0) {
            IconButton(
                onClick = { setCurrentScreenshot(currentScreenshot--) },
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .background(
                        color = Color.Black.copy(alpha = 0.35f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.LightGray
                )
            }
        }

        if (currentScreenshot < totalScreenshots) {
            IconButton(
                onClick = { setCurrentScreenshot(currentScreenshot++) },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .background(
                        color = Color.Black.copy(alpha = 0.35f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color.LightGray
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(title: String, context: Context) {
    TopAppBar(
        title = {
            Text(title)
        },
        navigationIcon = {
            IconButton(
                onClick = { (context as Activity).finish() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
        actions = {
            var isStarred by remember { mutableStateOf(false) }

            IconButton(
                onClick = {

                }
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null
                )
            }
            IconButton(
                onClick = {
                    isStarred = !isStarred
                    Log.d("Fzul", isStarred.toString())
                }
            ) {
                Icon(
                    painter = painterResource(
                        if (isStarred)
                            R.drawable.ic_star_filled
                        else
                            R.drawable.ic_star_outlined
                    ),
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
private fun BottomBar(
    downloadLink: String,
    otherLinks: Set<OtherLink>
) {
    val uriHandler = LocalUriHandler.current

    BottomAppBar {
        Button(
            onClick = {
                uriHandler.openUri(downloadLink)
            }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_download),
                contentDescription = null
            )
            Text("Download")
        }
        if (otherLinks.isNotEmpty()) {
            Button(
                onClick = {
                    bottomSheetLinks.clear()
                    bottomSheetLinks.addAll(otherLinks)
                    showBottomSheet.value = true
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_globe),
                    contentDescription = null
                )
                Text("Other links")
            }
        }
    }
}

@Composable
private fun ShaderScreen(
    title: String,
    description: String,
    screenshots: List<String>,
    screenshotToShow: MutableState<String>,
    modifier: Modifier = Modifier,
) {
    Column(modifier.padding(horizontal = 16.dp)) {
        val itemWidth = LocalConfiguration.current.screenWidthDp * 0.8f

        Text(
            text = description,
        )
        Text(
            text = "Screenshots",
            style = MaterialTheme.typography.headlineSmall
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(screenshots) { url ->
                Surface(
                    modifier = Modifier
                        .width(itemWidth.dp)
                        .aspectRatio(16f / 9f)
                        .clip(MaterialTheme.shapes.medium)
                        .shadow(
                            elevation = 8.dp,
                            shape = MaterialTheme.shapes.medium
                        )
                ) {
                    AsyncImage(
                        model = url,
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                screenshotToShow.value = url
                            }
                    )
                }
            }
        }
    }
}