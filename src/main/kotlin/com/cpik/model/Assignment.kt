package com.cpik.model

import com.google.ortools.sat.BoolVar
import com.google.ortools.sat.CpModel

/**
 * There will be one instance of these for
 * each possible combination of worker and
 * shift.
 *
 * The `assigned: BoolVar` is a reference to
 * a Google OR Tools variable which can be either
 * zero or one.
 *
 * We will be using the OR Tools CpSolver to
 * work out which of these should be zero
 * (the worker is not assigned to this shift)
 * and which should be one (the worker is assigned
 * to this shift) to produce an optimal solution
 * given the constraints.
 */
data class Assignment(
    val worker: Worker,
    val shift: Shift,
    val assigned: BoolVar
) {
    constructor(worker: Worker, shift: Shift, model: CpModel) :
            this(
                worker = worker,
                shift = shift,
                assigned = model.newBoolVar("$worker $shift")
            )
}