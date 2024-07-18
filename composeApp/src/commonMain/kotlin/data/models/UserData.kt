package data.models

import kotlinx.serialization.Serializable

@Serializable
data class UserInfoData(
    val userId : Long ,
    var fullName: String ,
    var age: Int ,
    var gender: String ,
    var height: Float ,
    var weight: Float ,
    var fitnessGoals: String ,
    var activityLevel: String ,
    var dietaryPreferences: String ,
    var workoutPreferences: String
)

@Serializable
data class  UserInfoResponseData(
    val data : UserInfoData? = null,
    val errorMessage: String? = null
)
