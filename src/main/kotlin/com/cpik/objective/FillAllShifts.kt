package com.cpik.objective

import com.cpik.model.Assignment
import com.google.ortools.sat.LinearExpr

/**
 * This object creates the linear expression to maximise.
 *
 * In this simple instance we just want to fill the most shifts,
 * so we will want to maximise the sum of all shift booleans.
 *
 * In practice these linear expressions will be more complex, e.g.
 * they will give different values/priorities to more important shifts,
 * certain shift combinations, or preferred shifts.
 */
object FillAllShifts {
    fun linearExpression(assignments: List<Assignment>): LinearExpr =
        LinearExpr.sum(assignments.map { it.assigned }.toTypedArray())
}