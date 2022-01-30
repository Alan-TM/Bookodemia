package mx.kodemia.bookodemia.models.categories

import kotlinx.serialization.Serializable
import mx.kodemia.bookodemia.models.Links

@Serializable
data class CategoriesBook(
    val links: Links
): java.io.Serializable
