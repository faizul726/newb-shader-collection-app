package dev.faizul726.newbshadercollection.ui.components

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import dev.faizul726.newbshadercollection.DeveloperDetailsActivity
import dev.faizul726.newbshadercollection.R
import dev.faizul726.newbshadercollection.ShaderDetailsActivity
import dev.faizul726.newbshadercollection.data.DEVELOPERS
import dev.faizul726.newbshadercollection.data.customJson
import dev.faizul726.newbshadercollection.data.models.OtherLink
import dev.faizul726.newbshadercollection.data.models.Platforms
import dev.faizul726.newbshadercollection.data.shaderVersionsList
import kotlinx.serialization.encodeToString

@Composable
internal fun ItemCard(
    id: Int?,
    title: String,
    creator: String,
    screenshots: List<String>,
    supportedVersion: String,
    supportedPlatforms: Set<Platforms>,
    downloadLink: String,
    links: Set<OtherLink>
) {
    val context = LocalContext.current

    Surface(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(168.dp)
            .clickable {
                val intent = Intent(context, ShaderDetailsActivity::class.java).apply {
                    putExtra("TITLE", title)
                    //putExtra("DESCRIPTION", )
                    putExtra("SCREENSHOTS", screenshots.toTypedArray())
                    putExtra("DOWNLOAD_LINK", downloadLink)
                    putExtra("LINKS", customJson.encodeToString(links))
                }
                context.startActivity(intent)
            }
    ) {
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
                        text = "by ${creator.substringAfter('_')}",
                        color = Color.White.copy(alpha = 0.75f),
                        style = MaterialTheme.typography.bodyMedium,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable() {
                            val intent = Intent(context, DeveloperDetailsActivity::class.java)
                            Log.d("Fzul", "$DEVELOPERS/$creator.json")
                            intent.putExtra("DEVELOPER_LINK", "$DEVELOPERS/$creator.json")
                            context.startActivity(intent)
                        }
                    )
                }
                Badge(
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Text(
                        text = (shaderVersionsList.value.firstOrNull { v -> v.base == supportedVersion}?.let { "v" + it.base }) ?: "Unknown"
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.align(Alignment.BottomStart).padding(start = 6.dp, bottom = 6.dp).height(20.dp)
                ) {
                    if (Platforms.ANDROID in supportedPlatforms) Icon(painterResource(R.drawable.android), null, tint = Color.White)
                    if (Platforms.IOS in supportedPlatforms)  Icon(painterResource(R.drawable.ios), null, tint = Color.White)
                    if (Platforms.WINDOWS in supportedPlatforms)  Icon(painterResource(R.drawable.windows), null, tint = Color.White)
                }
            }
        }
    }
}