package mx.kodemia.bookodemia.models.categories

import kotlinx.serialization.Serializable

@Serializable
data class CategoriesRelationships(
    val books: CategoriesBook
): java.io.Serializable
