package mx.kodemia.bookodemia.models.books

import kotlinx.serialization.Serializable

@Serializable
data class AllBooks(
    val data: MutableList<Book>
)
