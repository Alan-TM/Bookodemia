package mx.kodemia.bookodemia.models.authors

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import mx.kodemia.bookodemia.models.Links
import mx.kodemia.bookodemia.models.categories.CategoriesRelationships

@Serializable
data class AuthorsData(
    val type: String,
    val id: String,
    val attributes: AuthorsAttributes,
    val relationships: CategoriesRelationships,
    val links: Links
): java.io.Serializable