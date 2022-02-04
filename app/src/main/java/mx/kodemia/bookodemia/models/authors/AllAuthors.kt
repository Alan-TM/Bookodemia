package mx.kodemia.bookodemia.models.authors

import kotlinx.serialization.Serializable

@Serializable
data class AllAuthors(
    val data: MutableList<AuthorsData>
): java.io.Serializable
