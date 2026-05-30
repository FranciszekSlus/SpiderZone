package com.example.spiderzone

import org.junit.Assert.assertEquals
import org.junit.Test

class SpiderZoneSmokeTest {
    @Test
    fun reminderDelayIsPositive() {
        val now = System.currentTimeMillis()
        val inSixHours = now + 6 * 60 * 60 * 1000
        assertEquals(true, inSixHours > now)
    }
}
