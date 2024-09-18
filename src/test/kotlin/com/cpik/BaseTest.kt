package com.cpik

import com.cpik.model.Shift
import com.cpik.model.Worker
import com.google.ortools.Loader
import org.junit.jupiter.api.BeforeAll
import java.math.BigInteger
import java.security.SecureRandom
import java.time.LocalDateTime

fun String.Companion.secureRandom() = BigInteger(130, SecureRandom()).toString(32)

open class BaseTest {
    companion object {
        @JvmStatic
        @BeforeAll
        fun setup() {
            // Load the Google OR Tools' native libraries
            Loader.loadNativeLibraries()
        }
    }

    fun sampleWorker(name: String = String.secureRandom()): Worker = Worker(name)

    fun sampleShift(
        start: String = "2024-10-17T09:00:00",
        end: String = "2024-10-17T17:00:00"
    ): Shift =
        Shift(
            start = LocalDateTime.parse(start),
            end = LocalDateTime.parse(end)
        )
}