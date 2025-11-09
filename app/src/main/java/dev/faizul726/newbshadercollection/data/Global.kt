package dev.faizul726.newbshadercollection.data

import androidx.compose.runtime.mutableStateOf
import dev.faizul726.newbshadercollection.data.models.Developer
import dev.faizul726.newbshadercollection.data.models.OtherLink
import dev.faizul726.newbshadercollection.data.models.Shader
import dev.faizul726.newbshadercollection.data.models.ShaderVersion

internal val bottomSheetLinks = mutableListOf<OtherLink>()

internal val showBottomSheet = mutableStateOf(false)

internal val shaderList = mutableStateOf(emptyList<Shader>())
internal val shaderVersionsList = mutableStateOf(emptyList<ShaderVersion>())
internal val developerList = mutableStateOf(emptyList<Developer>())
internal val developer = mutableStateOf<Developer?>(null)