package mx.kodemia.bookodemia.models.authors

import kotlinx.serialization.Serializable

@Serializable
data class AuthorsAll(
    val data: AuthorsData
): java.io.Serializable
