package mx.kodemia.bookodemia.models.books

import kotlinx.serialization.Serializable
import mx.kodemia.bookodemia.models.books.Links

@Serializable
data class Authors(
    val links: Links
): java.io.Serializable
