package com.cpik.model

import java.time.LocalDateTime

/**
 * Represents a single instance of a shift
 */
data class Shift(val start: LocalDateTime, val end: LocalDateTime)