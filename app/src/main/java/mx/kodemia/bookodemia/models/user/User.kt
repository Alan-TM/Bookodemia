package mx.kodemia.bookodemia.models.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class User(
    val id: String,
    val name: String,
    val email: String,
    @SerialName("email_verified_at")
    val emailVerifiedAt: String?,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
): java.io.Serializable
