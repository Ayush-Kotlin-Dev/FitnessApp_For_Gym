package data

import presentation.models.AuthResponseData
import presentation.models.AuthResultData

fun AuthResponseData.toAuthResultData() : AuthResultData {
    return AuthResultData(
        id = id,
        name = name,
        bio = bio,
        avatar = avatar,
        token = token
    )
}