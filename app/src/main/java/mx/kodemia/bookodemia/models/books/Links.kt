package mx.kodemia.bookodemia.models.books

import kotlinx.serialization.Serializable

@Serializable
data class Links
    (
    val self: String,
    val related: String = ""
            ): java.io.Serializable
