package domain

import data.models.WorkoutDayDb
import data.models.WorkoutPlanDb
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.types.RealmObject
import kotlinx.coroutines.flow.Flow
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.coroutines.flow.map

class Config : RealmObject {
    @PrimaryKey
    var key: String = ""
    var value: String = ""
}

class RealmManager {
    private lateinit var realm: Realm

    init {
        initialize()
    }

    private fun initialize() {
        val config = RealmConfiguration.Builder(schema = setOf(WorkoutPlanDb::class, WorkoutDayDb::class, Config::class))
            .name("workout_database")
            .compactOnLaunch()
            .build()
        realm = Realm.open(config)
    }

    suspend fun saveWorkoutPlan(plan: WorkoutPlanDb) {
        realm.write {
            copyToRealm(plan)
        }
    }

    fun getWorkoutPlan(planName: String): Flow<WorkoutPlanDb?> {
        return realm.query<WorkoutPlanDb>("name == $0", planName)
            .first()
            .asFlow()
            .map { it.obj }
    }

    fun getWorkoutDayForDate(planName: String, dayName: String): Flow<WorkoutDayDb?> {
        return realm.query<WorkoutPlanDb>("name == $0", planName)
            .first()
            .asFlow()
            .map { plan ->
                plan.obj?.days?.find { it.day == dayName }
            }
    }

    suspend fun clear() {
        realm.write {
            deleteAll()
        }
    }

    suspend fun saveLastSelectedPlan(planName: String) {
        realm.write {
            val config = query<Config>("key == $0", "lastSelectedPlan").first().find()
                ?: Config().apply { key = "lastSelectedPlan" }
            config.value = planName
            copyToRealm(config)
        }
    }

    fun getLastSelectedPlan(): Flow<String?> {
        return realm.query<Config>("key == $0", "lastSelectedPlan")
            .first()
            .asFlow()
            .map { it.obj?.value }
    }
    //method to update the exercises for a specific day in a workout plan
    //TODO: Add a method to update the exercises for a specific day in a workout plan
    suspend fun updateWorkoutDayExercises(planName: String, dayName: String, exercises: List<String>) {
        realm.write {
            val plan = query<WorkoutPlanDb>("name == $0", planName).first().find()
            plan?.days?.find { it.day == dayName }?.let { day ->
                day.exercises.clear()
                day.exercises.addAll(exercises)
            }
        }
    }

}