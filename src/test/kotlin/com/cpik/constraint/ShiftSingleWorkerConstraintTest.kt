package com.cpik.constraint

import com.cpik.BaseTest
import com.cpik.model.Assignment
import com.google.ortools.sat.CpModel
import com.google.ortools.sat.CpSolver
import com.google.ortools.sat.CpSolverStatus
import kotlin.test.Test
import kotlin.test.assertEquals

class ShiftSingleWorkerConstraintTest : BaseTest() {
    private val model = CpModel()
    private val worker1 = sampleWorker()
    private val worker2 = sampleWorker()
    private val shift = sampleShift()

    private val worker1Assignment = Assignment(worker1, shift, model)
    private val worker2Assignment = Assignment(worker2, shift, model)

    private val assignments = listOf(worker1Assignment, worker2Assignment)

    @Test
    fun `with no workers assigned`() {
        ShiftSingleWorkerConstraint.addConstraints(model, assignments)
        model.addEquality(worker1Assignment.assigned, 0)
        model.addEquality(worker2Assignment.assigned, 0)

        assertEquals(CpSolverStatus.OPTIMAL, CpSolver().solve(model))
    }

    @Test
    fun `with one worker assigned`() {
        ShiftSingleWorkerConstraint.addConstraints(model, assignments)
        model.addEquality(worker1Assignment.assigned, 1)
        model.addEquality(worker2Assignment.assigned, 0)

        assertEquals(CpSolverStatus.OPTIMAL, CpSolver().solve(model))
    }

    @Test
    fun `with two workers assigned`() {
        ShiftSingleWorkerConstraint.addConstraints(model, assignments)
        model.addEquality(worker1Assignment.assigned, 1)
        model.addEquality(worker2Assignment.assigned, 1)

        assertEquals(CpSolverStatus.INFEASIBLE, CpSolver().solve(model))
    }
}