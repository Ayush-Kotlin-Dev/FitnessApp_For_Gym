package presentation.models

data class AuthResultData(
    val userId: Long,
    val userName: String,
    val email: String,
    val bio: String,
    val token: String,
    val isFormFilled : Boolean,
)