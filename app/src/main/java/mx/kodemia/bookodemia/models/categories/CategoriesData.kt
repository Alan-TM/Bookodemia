package mx.kodemia.bookodemia.models.categories

import kotlinx.serialization.Serializable
import mx.kodemia.bookodemia.models.books.Links

@Serializable
data class CategoriesData(
    val type: String,
    val id: String,
    val attributes: CategoriesAttributes,
    val relationships: CategoriesRelationships,
    val links: Links
): java.io.Serializable
