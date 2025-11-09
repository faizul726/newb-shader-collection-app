package dev.faizul726.newbshadercollection.ui.screens

import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.faizul726.newbshadercollection.data.bottomSheetLinks
import dev.faizul726.newbshadercollection.data.models.Shader
import dev.faizul726.newbshadercollection.data.models.ShaderVersion
import dev.faizul726.newbshadercollection.data.showBottomSheet
import dev.faizul726.newbshadercollection.ui.components.ItemCard
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSerializationApi::class)
@Composable
internal fun HomeScreen(
    modifier: Modifier,
    shaders: List<Shader>,
    shaderVersions: List<ShaderVersion>
) {
    if (shaders.isEmpty()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "No internet connection :(\n\n",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
        }
    } else {
        LazyColumn {
            item {
                Spacer(Modifier.height(112.dp))
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