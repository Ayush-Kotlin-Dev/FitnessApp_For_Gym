package domain

import data.models.WorkoutDayDb
import data.models.WorkoutPlanDb
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class  RealmManager {
    private lateinit var realm: Realm

    init {
        initialize()
    }
    private fun initialize() {
        val config = RealmConfiguration.Builder(schema = setOf(WorkoutPlanDb::class, WorkoutDayDb::class))
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
}