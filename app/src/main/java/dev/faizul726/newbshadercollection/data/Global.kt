package dev.faizul726.newbshadercollection.data

import androidx.compose.runtime.mutableStateOf
import dev.faizul726.newbshadercollection.data.models.OtherLink

internal val bottomSheetLinks = mutableListOf<OtherLink>()

internal val showBottomSheet = mutableStateOf(false)

internal val shaderPageToOpen = mutableStateOf<Int?>(null)

internal val shaderVersions = mutableListOf(emptyList<String>())