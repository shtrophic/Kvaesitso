package de.mm20.launcher2.search

import android.util.Log
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeParseException

interface Location : SavableSearchable {

    val category: LocationCategory?

    val latitude: Double
    val longitude: Double

    val street: String?
    val houseNumber: String?

    val openingHours: List<OpeningTime>?

    override val preferDetailsOverLaunch: Boolean
        get() = false
}

enum class LocationCategory {
    RESTAURANT,
    FAST_FOOD,
    BAR,
    CAFE,
    HOTEL,
    SUPERMARKET,
    OTHER
}

data class OpeningTime(val dayOfWeek: DayOfWeek, val startTime: LocalTime, val duration: Duration) {
    val isOpen: Boolean
        get() = LocalDate.now().dayOfWeek == dayOfWeek &&
                LocalTime.now().isAfter(startTime) &&
                LocalTime.now().isBefore(startTime.plus(duration))

    companion object {

        private val regex: Lazy<Regex> = lazy {
            // TODO come up with regex to alleviate parsing errors
            Regex("asdf")
        }

        fun fromOverpassElement(it: String) : List<OpeningTime>? {
            // TODO log parsing errors
            if (it.isBlank())
                return null

            val openingTimes = mutableListOf<OpeningTime>()

            // "Mo-Sa 11:00-14:00, 17:00-23:00; Su 11:00-23:00"
            // "Mo-Sa 11:00-21:00; PH,Su off"

            // wont work: "Mo-Th 10:00-24:00, Fr,Sa 10:00-05:00, PH,Su 12:00-22:00"
            for (dayBlock in it.split(';').map { it.trim() }) {
                val days = dayBlock.substringBefore(' ')
                val times = dayBlock.substringAfter(' ')

                var daysOfWeekRange = days.split('-').mapNotNull { dayOfWeekFromString(it.trim()) }
                val daysOfWeekList = days.split(',').mapNotNull { dayOfWeekFromString(it.trim()) }

                if (daysOfWeekRange.size == 2) {
                    daysOfWeekRange = enumValues<DayOfWeek>().toList().subList(
                        daysOfWeekRange[0].ordinal,
                        daysOfWeekRange[1].ordinal + 1
                    )
                }

                val startAndDurations = times.split(',').mapNotNull {
                    try {
                        val startTime = LocalTime.parse(it.substringBefore('-').trim())
                        val endTime = LocalTime.parse(it.substringAfter('-').trim())

                        startTime to Duration.between(startTime, endTime)
                    } catch (e: DateTimeParseException) {
                        Log.e("OpeningTimeFromOverpassElement", "Failed to parse opening time $it", e)
                        null
                    }
                }

                openingTimes.addAll(
                    daysOfWeekRange
                        .union(daysOfWeekList)
                        .flatMap {
                            day -> startAndDurations.map {
                                (start, duration) -> OpeningTime(day, start, duration)
                            }
                        }
                )
            }

            return openingTimes
        }
    }
}

private fun dayOfWeekFromString(it: String): DayOfWeek? = when (it.lowercase()) {
    "mo" -> DayOfWeek.MONDAY
    "tu" -> DayOfWeek.TUESDAY
    "we" -> DayOfWeek.WEDNESDAY
    "th" -> DayOfWeek.THURSDAY
    "fr" -> DayOfWeek.FRIDAY
    "sa" -> DayOfWeek.SATURDAY
    "su" -> DayOfWeek.SUNDAY
    else -> null
}


