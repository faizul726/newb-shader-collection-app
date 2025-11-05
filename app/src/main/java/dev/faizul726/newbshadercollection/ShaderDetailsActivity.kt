package dev.faizul726.newbshadercollection

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.faizul726.newbshadercollection.ui.theme.NewbShaderCollectionTheme

class ShaderDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val title = intent.extras?.getString("TITLE") ?: "Unknown"
        val description = intent.extras?.getString("DESCRIPTION") ?: "N/A"
        val screenshots = intent.extras?.getStringArray("SCREENSHOTS") ?: arrayOf("")

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewbShaderCollectionTheme {
                Scaffold(
                    topBar = { TopBar(title, this) },
                    bottomBar = { BottomBar() },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    ShaderScreen(
                        title = title,
                        description = description,
                        screenshots = screenshots,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
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
                    imageVector = Icons.Default.ArrowBack,
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
private fun BottomBar() {
    BottomAppBar {
        Button(
            onClick = {}
        ) {
            Text("Bottom download button")
        }
    }
}

@Composable
private fun ShaderScreen(
    title: String,
    description: String,
    screenshots: Array<String>,
    modifier: Modifier = Modifier,
) {
    Column(modifier.padding(horizontal = 16.dp)) {
        val itemWidth = LocalConfiguration.current.screenWidthDp * 0.8f

        Text(
            text = description,
        )
        Text(
            text = "Screenshots",
            style = MaterialTheme.typography.titleMedium
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
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}