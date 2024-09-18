package com.cpik.constraint

import com.cpik.model.Assignment
import com.cpik.model.Shift
import com.google.ortools.sat.CpModel
import com.google.ortools.sat.LinearExpr
import java.time.Duration
import java.time.LocalDateTime

/**
 * This object will add constraints to the model so
 * that each worker will have at least an 11-hour
 * break between shifts
 */
object MinimumTimeBetweenShifts {
    private val duration = Duration.ofHours(11)

    /**
     * The time span of the shift start to 11 hours after
     * the shift end
     */
    data class TimeSpan(val start: LocalDateTime, val end: LocalDateTime) {
        constructor(shift: Shift) : this(shift.start, shift.end.plus(duration))
        fun containsStartOfShift(shift: Shift): Boolean =
            shift.start.isEqual(start) || (shift.start.isAfter(start) && shift.start.isBefore(end))
    }

    fun addConstraints(
        cpModel: CpModel,
        assignments: List<Assignment>
    ) {
        // Get all the shifts
        val shifts = assignments.map { it.shift }.distinct()

        // Get all the unique time spans of
        // shift start time to shift end time plus 11 hours
        val timeSpans = shifts.map { TimeSpan(it) }.distinct()

        // Get all the workers
        val workers = assignments.map { it.worker }.distinct()

        for (worker in workers) {
            val workerAssignments = assignments.filter { it.worker == worker }
            for (timeSpan in timeSpans) {
                // For each worker and time span, get all the worker
                // assignments for shifts that start within that time span
                val shiftAssignments = workerAssignments.filter { timeSpan.containsStartOfShift(it.shift) }

                // Create a linear expression that is a sum of all the
                // assignment vars
                val sumOfAssignments = LinearExpr.sum(shiftAssignments.map { it.assigned }.toTypedArray())

                // Add a constraint to the CP Model to say that the
                // worker can be assigned to at most one of these shifts
                cpModel.addLessOrEqual(sumOfAssignments, 1)
            }
        }
    }
}