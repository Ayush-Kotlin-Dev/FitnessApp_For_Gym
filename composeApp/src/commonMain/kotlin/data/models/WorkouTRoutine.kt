package data.models

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class WorkoutPlanDb : RealmObject {
    @PrimaryKey
    var id: ObjectId = ObjectId()
    var name: String = ""
    var days: RealmList<WorkoutDayDb> = realmListOf()
}

class WorkoutDayDb : RealmObject {
    var day: String = ""
    var focus: String = ""
    var exerciseDbs: RealmList<ExerciseDb> = realmListOf()
}

class PersonalRecordDb :RealmObject {
    @PrimaryKey
    var id: ObjectId = ObjectId()
    var exercise: String= ""
    var weight: Float = 0f
    var reps: Int = 0
}


class ExerciseDb : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var name: String = ""
    var description: String = ""
    var muscleGroup: String = ""
    var equipment: String = ""
    var lastWeekWeight: Double? = null
    var lastWeekReps: Int? = null
    var lastWeekSets: Int? = null
}

data class Exercise(
    val name: String,
    val description: String,
    val muscleGroup: String,
    val equipment: String,
    val lastWeekWeight: Double?,
    val lastWeekReps: Int?,
    val lastWeekSets: Int?
)