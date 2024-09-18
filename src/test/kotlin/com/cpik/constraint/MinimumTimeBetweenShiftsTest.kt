package com.cpik.constraint

import com.cpik.BaseTest
import com.cpik.model.Assignment
import com.google.ortools.sat.CpModel
import com.google.ortools.sat.CpSolver
import com.google.ortools.sat.CpSolverStatus
import kotlin.test.Test

class MinimumTimeBetweenShiftsTest : BaseTest() {
    private val model = CpModel()
    private val worker = sampleWorker()
    private val shift1 = sampleShift("2024-10-17T06:00:00", "2024-10-17T08:00:00")
    private val shift2 = sampleShift("2024-10-17T12:00:00", "2024-10-17T18:00:00")
    private val shift3 = sampleShift("2024-10-17T19:00:00", "2024-10-17T23:00:00")

    private val shift1Assignment = Assignment(worker, shift1, model)
    private val shift2Assignment = Assignment(worker, shift2, model)
    private val shift3Assignment = Assignment(worker, shift3, model)

    private val assignments = listOf(shift1Assignment, shift2Assignment, shift3Assignment)

    @Test
    fun `with no shifts assigned`() {
        MinimumTimeBetweenShifts.addConstraints(model, assignments)
        model.addEquality(shift1Assignment.assigned, 0)
        model.addEquality(shift2Assignment.assigned, 0)
        model.addEquality(shift3Assignment.assigned, 0)

        kotlin.test.assertEquals(CpSolverStatus.OPTIMAL, CpSolver().solve(model))
    }

    @Test
    fun `with one shift assigned`() {
        MinimumTimeBetweenShifts.addConstraints(model, assignments)
        model.addEquality(shift1Assignment.assigned, 0)
        model.addEquality(shift2Assignment.assigned, 1)
        model.addEquality(shift3Assignment.assigned, 0)

        kotlin.test.assertEquals(CpSolverStatus.OPTIMAL, CpSolver().solve(model))
    }

    @Test
    fun `with shifts 11 hours apart assigned`() {
        MinimumTimeBetweenShifts.addConstraints(model, assignments)
        model.addEquality(shift1Assignment.assigned, 1)
        model.addEquality(shift2Assignment.assigned, 0)
        model.addEquality(shift3Assignment.assigned, 1)

        kotlin.test.assertEquals(CpSolverStatus.OPTIMAL, CpSolver().solve(model))
    }

    @Test
    fun `with shifts within 11 hours apart assigned 1`() {
        MinimumTimeBetweenShifts.addConstraints(model, assignments)
        model.addEquality(shift1Assignment.assigned, 1)
        model.addEquality(shift2Assignment.assigned, 1)
        model.addEquality(shift3Assignment.assigned, 0)

        kotlin.test.assertEquals(CpSolverStatus.INFEASIBLE, CpSolver().solve(model))
    }

    @Test
    fun `with shifts within 11 hours apart assigned 2`() {
        MinimumTimeBetweenShifts.addConstraints(model, assignments)
        model.addEquality(shift1Assignment.assigned, 0)
        model.addEquality(shift2Assignment.assigned, 1)
        model.addEquality(shift3Assignment.assigned, 1)

        kotlin.test.assertEquals(CpSolverStatus.INFEASIBLE, CpSolver().solve(model))
    }

    @Test
    fun `with shifts within 11 hours apart assigned 3`() {
        MinimumTimeBetweenShifts.addConstraints(model, assignments)
        model.addEquality(shift1Assignment.assigned, 1)
        model.addEquality(shift2Assignment.assigned, 1)
        model.addEquality(shift3Assignment.assigned, 1)

        kotlin.test.assertEquals(CpSolverStatus.INFEASIBLE, CpSolver().solve(model))
    }
}