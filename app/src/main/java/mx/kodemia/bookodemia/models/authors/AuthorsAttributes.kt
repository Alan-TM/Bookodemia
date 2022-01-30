package mx.kodemia.bookodemia.models.authors

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthorsAttributes(
    val name: String,
    @SerialName("created-at")
    val createdAt: String,
    @SerialName("updated-at")
    val updatedAt: String
): java.io.Serializable
