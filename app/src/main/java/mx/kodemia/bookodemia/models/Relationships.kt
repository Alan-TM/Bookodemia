package mx.kodemia.bookodemia.models

import kotlinx.serialization.Serializable

@Serializable
data class Relationships(
    val authors: Authors,
    val categories: Categories
): java.io.Serializable