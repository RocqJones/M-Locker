package com.rocqjones.mlocker.logic

import com.rocqjones.mlocker.logic.data.source.ActiveUsagePeriodDataSource
import com.rocqjones.mlocker.logic.utils.HelperUtil
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.time.Duration
import java.time.OffsetDateTime

class LogicTest : TestCase() {

    // Duration object : 2 hours, 30 minutes, & 15 seconds
    private val duration: Duration = Duration.ofSeconds((2 * 60 * 60) + (30 * 60) + 15)

    private lateinit var testDurationResult : String

    @Before
    override fun setUp() {
        testDurationResult = HelperUtil().formatDuration(duration)
    }

    @Test
    fun testFormatDuration() {
        assertEquals("02:30:15", testDurationResult)
    }

    @Test
    fun testGetLockingInfo() = runBlocking {
        // An instance of ActiveUsagePeriodDataSource
        val dataSource = ActiveUsagePeriodDataSource()
        val lockingInfo = dataSource.getLockingInfo()

        // Calculate the expected lock time
        val expectedLockTime = OffsetDateTime.now().withHour(23).withMinute(59).withSecond(0)

        // assertEquals(expectedLockTime, lockingInfo.lockTime)
        // Truncate the fractional seconds for comparison
        val expectedLockTimeWithoutFraction = expectedLockTime.withNano(0)
        val lockingInfoWithoutFraction = lockingInfo.lockTime.withNano(0)

        assertEquals(expectedLockTimeWithoutFraction, lockingInfoWithoutFraction)
    }
}