package mx.kodemia.bookodemia.models.categories

import kotlinx.serialization.Serializable

@Serializable
data class AllCategories(
    val data: MutableList<CategoriesData>
): java.io.Serializable
