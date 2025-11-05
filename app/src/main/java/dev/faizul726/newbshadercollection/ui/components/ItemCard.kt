package dev.faizul726.newbshadercollection.ui.components

import android.content.Intent
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.faizul726.newbshadercollection.R
import dev.faizul726.newbshadercollection.ShaderDetailsActivity
import dev.faizul726.newbshadercollection.data.bottomSheetLinks
import dev.faizul726.newbshadercollection.data.models.OtherLink
import dev.faizul726.newbshadercollection.data.models.Platforms
import dev.faizul726.newbshadercollection.data.models.ShaderVersion
import dev.faizul726.newbshadercollection.data.shaderPageToOpen
import dev.faizul726.newbshadercollection.data.showBottomSheet
import dev.faizul726.newbshadercollection.ui.screens.ShaderScreen
import dev.faizul726.newbshadercollection.utils.downloadFile
import kotlinx.serialization.Serializable

@Composable
internal fun ItemCard(
    id: Int?,
    title: String,
    creator: String,
    screenshots: List<String>,
    supportedVersion: String,
    supportedPlatforms: Set<Platforms>,
    downloadLink: String,
    links: Set<OtherLink>,
    shaderVersions: List<ShaderVersion>
) {
    val context = LocalContext.current

    Surface(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(168.dp)
            .combinedClickable(
                onClick = {
                    val intent = Intent(context, ShaderDetailsActivity::class.java).apply {
                        putExtra("TITLE", title)
                        //putExtra("DESCRIPTION", )
                        putExtra("SCREENSHOTS", screenshots.toTypedArray())
                    }
                    context.startActivity(intent)
                },
                onLongClick = {

                }
            )
    ) {
        /*Image(
            painter = painterResource(backgroundImage),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )*/
        AsyncImage(
            model = screenshots[0],
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
        Box(
            modifier = Modifier
                .width(192.dp)
                .fillMaxHeight()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.75f),
                            Color.Transparent
                        )
                    )
                )
        )
        Box(Modifier.padding(8.dp)) {
            Box(Modifier.fillMaxSize()) {
                Column(Modifier.align(Alignment.CenterStart).padding(start = 8.dp)) {
                    Text(title, color = Color.White, style = MaterialTheme.typography.headlineMedium)
                    Text(
                        text = "by $creator",
                        color = Color.White.copy(alpha = 0.75f),
                        style = MaterialTheme.typography.bodyMedium,
                        textDecoration = TextDecoration.Underline
                    )
                }
                Badge(
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Text(
                        text = (shaderVersions.firstOrNull { v -> v.base == supportedVersion}?.let { "v" + it.base }) ?: "Unknown"
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.align(Alignment.BottomStart).padding(start = 6.dp, bottom = 6.dp).height(20.dp)
                ) {
                    when {
                        Platforms.ANDROID in supportedPlatforms -> Icon(painterResource(R.drawable.android), null, tint = Color.White)
                        Platforms.IOS in supportedPlatforms -> Icon(painterResource(R.drawable.ios), null, tint = Color.White)
                        Platforms.WINDOWS in supportedPlatforms -> Icon(painterResource(R.drawable.windows), null, tint = Color.White)
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    /*Button(
                        colors = ButtonDefaults.buttonColors(
                            contentColor = MaterialTheme.colorScheme.primary,
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        onClick = {
                            downloadFile(
                                title = title,
                                url = downloadLink,
                                context = context
                            )
                            Toast.makeText(context, "Download started...", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.download),
                            contentDescription = null
                        )
                    }
                    if (links.isNotEmpty()) {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                contentColor = MaterialTheme.colorScheme.primary,
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            onClick = {
                                bottomSheetLinks.apply {
                                    clear()
                                    addAll(links)
                                }
                                showBottomSheet.value = true
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.globe),
                                contentDescription = null
                            )
                        }
                    }*/
                }
            }
        }
    }
}