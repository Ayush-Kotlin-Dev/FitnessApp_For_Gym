package data

import data.models.AuthResponseData
import data.models.AuthResultData
import data.models.UserInfoData

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

fun UserInfoData.toUserInfoData() : UserInfoData {
    return UserInfoData(
        userId = userId,
        fullName = fullName,
        age = age,
        gender = gender,
        weight = weight,
        height = height,
        fitnessGoals = fitnessGoals,
        activityLevel = activityLevel,
        dietaryPreferences = dietaryPreferences,
        workoutPreferences = workoutPreferences
    )
}