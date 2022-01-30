package mx.kodemia.bookodemia.models

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val data: MutableList<Book>
)
