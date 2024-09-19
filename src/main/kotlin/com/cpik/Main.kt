package com.cpik

import com.cpik.constraint.MinimumTimeBetweenShifts
import com.cpik.constraint.ShiftSingleWorkerConstraint
import com.cpik.model.Assignment
import com.cpik.model.Shift
import com.cpik.model.Worker
import com.cpik.objective.FillAllShifts
import com.google.ortools.Loader
import com.google.ortools.sat.CpModel
import com.google.ortools.sat.CpSolver
import com.google.ortools.sat.CpSolverStatus
import java.time.LocalDate
import kotlin.system.exitProcess

fun main() {
    Loader.loadNativeLibraries()

    val workers = listOf(
        Worker("Anne"),
        Worker("Bob"),
        Worker("Claire"),
    )

    val startDate = LocalDate.parse("2024-11-01")
    val endDate = LocalDate.parse("2024-11-15")

    val shifts = startDate.datesUntil(endDate).toList().flatMap { date ->
        listOf(
            Shift(date.atTime(6, 0), date.atTime(12, 0)),
            Shift(date.atTime(9, 0), date.atTime(17, 0)),
            Shift(date.atTime(16, 0), date.atTime(20, 0)),
        )
    }

    val model = CpModel()

    val assignments = workers.flatMap { worker ->
        shifts.map { shift ->
            Assignment(worker, shift, model)
        }
    }.shuffled()

    MinimumTimeBetweenShifts.addConstraints(model, assignments)
    ShiftSingleWorkerConstraint.addConstraints(model, assignments)

    model.maximize(FillAllShifts.linearExpression(assignments))

    val solver = CpSolver()

    val result = solver.solve(model)

    if (result == CpSolverStatus.INFEASIBLE) {
        println("Ooops, it was INFEASIBLE")
        exitProcess(-1)
    }

    println("Solved with result: $result")

    for (shift in shifts) {
        val assignment = assignments.find { it.shift == shift && solver.booleanValue(it.assigned) }
        println("$shift: ${assignment?.worker?.name}")
    }

}