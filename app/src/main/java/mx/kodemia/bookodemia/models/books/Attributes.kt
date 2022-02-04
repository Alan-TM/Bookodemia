package mx.kodemia.bookodemia.models.books

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Attributes(
    val title: String,
    val slug: String,
    val content: String,
    @SerialName("created-at")
    val createdAt: String,
    @SerialName("updated-at")
    val updatedAt: String
): java.io.Serializable
