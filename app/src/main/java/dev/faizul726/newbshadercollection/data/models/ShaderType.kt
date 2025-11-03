package dev.faizul726.newbshadercollection.data.models

import androidx.annotation.DrawableRes
import kotlinx.serialization.Serializable

@Serializable
enum class Platforms {
    ANDROID, IOS, WINDOWS
}

@Serializable
data class Shader(
    val title: String,
    val thumbnail: String,
    val creator: String,
    val platforms: Set<Platforms>,
    val downloadLink: String,
    val otherLinks: Set<OtherLink> = emptySet()
)

@Serializable
data class OtherLink(
    val title: String = "Link",
    val link: String
)