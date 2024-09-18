package com.cpik.constraint

import com.cpik.model.Assignment
import com.google.ortools.sat.CpModel
import com.google.ortools.sat.LinearExpr

/**
 * This object will add constraints to the model so
 * that each shift can be assigned to at most one
 * worker.
 */
object ShiftSingleWorkerConstraint {
    fun addConstraints(
        cpModel: CpModel,
        assignments: List<Assignment>
    ) {
        // Get a list of all the unique shifts
        val shifts = assignments.map { it.shift }.distinct()
        for (shift in shifts) {
            // Get a list of all the assignments for this shift.
            // There will be one of these assignments per worker
            val shiftAssignments = assignments.filter { it.shift == shift }

            // Create a linear expression that is a sum of all the
            // assignment vars
            val sumOfAssignments = LinearExpr.sum(shiftAssignments.map { it.assigned }.toTypedArray())

            // Add a constraint to the CP Model to say that
            // at most one worker can be assigned to this shift
            cpModel.addLessOrEqual(sumOfAssignments, 1)
        }
    }
}



