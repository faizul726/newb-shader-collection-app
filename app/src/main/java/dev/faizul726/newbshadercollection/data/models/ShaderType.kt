package dev.faizul726.newbshadercollection.data.models

import androidx.annotation.DrawableRes
import dev.faizul726.newbshadercollection.ui.components.Platforms
import kotlinx.serialization.Serializable


@Serializable
data class Shader(
    val title: String,
    val thumbnail: String,
    val creator: String,
    val platforms: Set<Platforms>,
    val downloadLink: String,
    val otherLinks: Set<String> = emptySet()
)
