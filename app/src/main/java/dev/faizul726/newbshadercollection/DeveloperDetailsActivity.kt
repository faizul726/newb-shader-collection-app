package dev.faizul726.newbshadercollection

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import coil3.compose.AsyncImage
import dev.faizul726.newbshadercollection.MainActivity.Companion.okHttpClient
import dev.faizul726.newbshadercollection.data.customJson
import dev.faizul726.newbshadercollection.data.developer
import dev.faizul726.newbshadercollection.data.models.Developer
import dev.faizul726.newbshadercollection.data.shaderList
import dev.faizul726.newbshadercollection.data.shaderVersionsList
import dev.faizul726.newbshadercollection.ui.components.ItemCard
import dev.faizul726.newbshadercollection.ui.theme.NewbShaderCollectionTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Request

class DeveloperDetailsActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val expandedAppBarHeight = 168.dp
            val developer = remember { developer }
            val shaderVersions by remember { shaderVersionsList }
            val developerLink = intent.getStringExtra("DEVELOPER_LINK") ?: ""
            val shaders = shaderList.value.filter { it.creator.contains(developer.value?.name ?: "Unknown") }
            val shaderCount = shaders.size
            val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
            val scrollState = scrollBehavior.state
            val headerTranslation = expandedAppBarHeight / 2

            val appBarExpanded by remember {
                derivedStateOf { scrollState.collapsedFraction < 0.9f }
            }

            val request = Request.Builder()
                .url(developerLink)
                .build()

            LaunchedEffect(Unit) {
                lifecycleScope.launch(Dispatchers.IO) {
                    developer.value = try {
                        val json = okHttpClient.newCall(request).execute().body.string()
                        Log.d("Fzul", json)
                        customJson.decodeFromString<Developer>(json)
                    } catch (e: Exception) {
                        Log.d("Fzul", "fail" + e.message)
                        null
                    }
                }
            }

            NewbShaderCollectionTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.tertiary)
                        ) {
                            CollapsedAppBar(shaderCount, !appBarExpanded)
                            TopAppBar(
                                title = {
                                    AppBarHeader(
                                        shaderCount = shaderCount,
                                        visibility = appBarExpanded,
                                        modifier = Modifier.graphicsLayer {
                                            translationY =
                                                scrollState.collapsedFraction * headerTranslation.toPx()
                                        }
                                    )
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = Color.Transparent,
                                    scrolledContainerColor = Color.Transparent
                                ),
                                expandedHeight = expandedAppBarHeight,
                                windowInsets = WindowInsets(0),
                                scrollBehavior = scrollBehavior
                            )
                        }
                    }
                ) { innerPadding ->
                    LazyColumn(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection)
                    ) {
                        item {
                            Column(Modifier.fillMaxWidth()) {
                                BottomSheetDefaults.DragHandle(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        //.padding(vertical = 8.dp)
                                )
                            }
                            /*Text(
                                text = "Shaders",
                                style = MaterialTheme.typography.headlineSmall
                            )*/
                        }
                        items(shaders) {
                            ItemCard(
                                id = it.id,
                                title = it.title,
                                creator = it.creator,
                                screenshots = it.screenshots,
                                supportedVersion = it.supportedVersion,
                                supportedPlatforms = it.platforms,
                                downloadLink = it.downloadLink,
                                links = it.otherLinks
                            )
                        }
                        item {
                            Spacer(Modifier.height(32.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AppBarHeader(
    shaderCount: Int,
    visibility: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visibility,
        enter = fadeIn(tween()),
        exit = fadeOut(tween())
    ) {
        Column(
            modifier = modifier.padding(top = 4.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    modifier = Modifier
                        .size(64.dp)
                    //.border(2.dp, MaterialTheme.colorScheme.onTertiary)
                ) {
                    AsyncImage(
                        model = developer.value?.icon ?: "https://avatars.githubusercontent.com/u/162413089?v=4",
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                    )
                }
                Column {
                    Text(
                        text = developer.value?.name ?: "Unknown",
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                    Text(
                        text = "$shaderCount shader" + if (shaderCount > 1) "s" else "",
                        color = MaterialTheme.colorScheme.onTertiary,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                developer.value?.socials?.forEach {
                    Text(
                        text = it.title,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onTertiary,
                        textDecoration = TextDecoration.Underline
                    )
                    /*Text(
                        text = it.link,
                        fontFamily = FontFamily.Monospace,
                        style = MaterialTheme.typography.labelSmall
                    )*/
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CollapsedAppBar(
    shaderCount: Int,
    visibility: Boolean,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        navigationIcon = {
            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
        title = {
            AnimatedVisibility(
                visible = visibility,
                enter = fadeIn(tween()),
                exit = fadeOut(tween())
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        modifier = Modifier
                            .size(32.dp)
                        //.border(1.dp, MaterialTheme.colorScheme.onTertiary)
                    ) {
                        AsyncImage(
                            model = developer.value?.icon ?: "https://avatars.githubusercontent.com/u/162413089?v=4",
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                        )
                    }
                    Column {
                        Text(
                            text = developer.value?.name ?: "Unknown",
                            color = MaterialTheme.colorScheme.onTertiary
                        )
                        Text(
                            text = "$shaderCount shader" + if (shaderCount > 1) "s" else "",
                            color = MaterialTheme.colorScheme.onTertiary,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent,
            navigationIconContentColor = MaterialTheme.colorScheme.onTertiary
        )
    )
}

