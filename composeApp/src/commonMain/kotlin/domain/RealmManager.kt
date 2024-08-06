package domain

import data.models.PersonalRecordDb
import data.models.WorkoutDayDb
import data.models.WorkoutPlanDb
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.types.RealmObject
import kotlinx.coroutines.flow.Flow
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.coroutines.flow.map
import presentation.screens.stats.PersonalRecord

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
        val config = RealmConfiguration.Builder(schema = setOf(WorkoutPlanDb::class, WorkoutDayDb::class, PersonalRecordDb::class, Config::class))
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

    //method to get the last selected plan
    suspend fun saveLastSelectedPlan(planName: String) {
        realm.write {
            val config = query<Config>("key == $0", "lastSelectedPlan").first().find()
                ?: Config().apply { key = "lastSelectedPlan" }
            config.value = planName
            copyToRealm(config)
        }
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
    //method to reorder the workout days in a workout plan
    suspend fun reorderWorkoutDays(planName: String, fromIndex: Int, toIndex: Int) {
        realm.write {
            val plan = query<WorkoutPlanDb>("name == $0", planName).first().find()
            plan?.let { foundPlan ->
                val mutableDays = foundPlan.days.toMutableList()
                if (fromIndex in mutableDays.indices && toIndex in mutableDays.indices) {
                    val movedDay = mutableDays.removeAt(fromIndex)
                    mutableDays.add(toIndex, movedDay)

                    // Update the day numbers
                    mutableDays.forEachIndexed { index, day ->
                        day.day = "Day ${index + 1}"
                    }

                    foundPlan.days.clear()
                    foundPlan.days.addAll(mutableDays)
                } else {
                    println("Invalid reorder indexes")
                }
            }
        }
    }
    suspend fun savePersonalRecord(record: PersonalRecord) {
        val normalizedExercise = record.exercise.trim().lowercase()
        realm.write {
            val existingRecord = query<PersonalRecordDb>("exercise == $0", normalizedExercise).first().find()
            if (existingRecord != null) {
                existingRecord.weight = record.weight
                existingRecord.reps = record.reps
            } else {
                val recordDb = PersonalRecordDb().apply {
                    exercise = normalizedExercise
                    weight = record.weight
                    reps = record.reps
                }
                copyToRealm(recordDb)
            }
        }
    }

    fun getPersonalRecords(): Flow<List<PersonalRecord>> {
        return realm.query<PersonalRecordDb>().asFlow().map { results ->
            results.list.map { recordDb ->
                PersonalRecord(
                    exercise = recordDb.exercise,
                    weight = recordDb.weight,
                    reps = recordDb.reps
                )
            }
        }
    }

    suspend fun reorderExercises(planName: String, dayName: String, fromIndex: Int, toIndex: Int) {
        realm.write {
            val plan = query<WorkoutPlanDb>("name == $0", planName).first().find()
            plan?.days?.find { it.day == dayName }?.let { day ->
                val mutableExercises = day.exercises.toMutableList()
                if (fromIndex in mutableExercises.indices && toIndex in mutableExercises.indices) {
                    val movedExercise = mutableExercises.removeAt(fromIndex)
                    mutableExercises.add(toIndex, movedExercise)

                    day.exercises.clear()
                    day.exercises.addAll(mutableExercises)
                } else {
                    println("Invalid reorder indexes for exercises")
                }
            }
        }
    }
}