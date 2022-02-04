package mx.kodemia.bookodemia.models.categories

import kotlinx.serialization.Serializable

@Serializable
data class SingleCateogory(
    val data: CategoriesData
): java.io.Serializable
