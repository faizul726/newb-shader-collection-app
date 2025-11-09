package dev.faizul726.newbshadercollection.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Developer(
    val name: String,
    val icon: String,
    val website: String,
    val socials: List<OtherLink>
)
