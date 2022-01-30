package mx.kodemia.bookodemia.models.categories

import kotlinx.serialization.Serializable

@Serializable
data class CategoriesAttributes(
    val name: String,
    val slug: String
): java.io.Serializable