package presentation.models

data class AuthResultData(
    val id: Long,
    val name: String,
    val bio: String,
    val avatar: String? = null,
    val token: String,
)