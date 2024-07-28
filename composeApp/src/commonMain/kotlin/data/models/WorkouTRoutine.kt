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
    var exercises: RealmList<String> = realmListOf()
}