package data

import presentation.models.AuthResponseData
import presentation.models.AuthResultData

fun AuthResponseData.toAuthResultData() : AuthResultData {
    return AuthResultData(
        userId = userId,
        userName = userName,
        email = email,
        bio = bio,
        token = token,
        isFormFilled = isFormFilled
    )
}