package dev.faizul726.newbshadercollection.data.models

import androidx.annotation.DrawableRes
import kotlinx.serialization.Serializable

@Serializable
enum class Platforms {
    ANDROID, IOS, WINDOWS
}

@Serializable
data class Shader(
    val id: Int,
    val title: String,
    val creator: String,
    val screenshots: List<String>, // [0] is used as thumbnail
    val platforms: Set<Platforms>,
    val supportedVersion: String,
    val downloadLink: String,
    val otherLinks: Set<OtherLink> = emptySet()
)

@Serializable
data class ShaderVersion(
    val base: String,
    val start: ShaderVersionRange,
    val end: ShaderVersionRange? = null
)

@Serializable
data class ShaderVersionRange(
    val major: Int,
    val minor: Int,
    val patch: Int
)

@Serializable
data class OtherLink(
    val title: String = "Link",
    val link: String
)