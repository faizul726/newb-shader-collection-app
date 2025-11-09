package dev.faizul726.newbshadercollection.data

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

const val REPO = "https://raw.githubusercontent.com/faizul726/newb-shader-repo/refs/heads/main"

const val SHADER_REPO = "$REPO/shader-list-testing.json"
const val SHADER_VERSIONS = "$REPO/shader-versions-testing.json"
const val DEVELOPERS = "$REPO/developers"

@OptIn(ExperimentalSerializationApi::class)
val customJson = Json {
    ignoreUnknownKeys = true
    isLenient = true
    coerceInputValues = true
    allowTrailingComma = true
}

/*
val shaders = listOf(
    ShaderType(
        backgroundImage = R.drawable.newb_shader,
        title = "Newb Shader",
        creator = "devendrn",
        platforms = setOf(Platforms.ANDROID, Platforms.IOS, Platforms.WINDOWS),
        downloadLink = "Murgi"
    ),
    ShaderType(
        backgroundImage = R.drawable.newb_x_lmi,
        title = "Newb X LMI",
        creator = "EnoughRise11",
        platforms = setOf(Platforms.ANDROID, Platforms.IOS, Platforms.WINDOWS),
        downloadLink = "Murgi",
        links = setOf("hello", "world")
    ),
    ShaderType(
        backgroundImage = R.drawable.ic_launcher_background,
        title = "Newb X Murgi",
        creator = "fazul",
        platforms = setOf(Platforms.ANDROID),
        downloadLink = "Murgi"
    )
)*/
