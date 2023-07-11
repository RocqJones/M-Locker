package com.rocqjones.mlocker.models

import com.rocqjones.mlocker.logic.data.models.ActiveUsagePeriod
import com.rocqjones.mlocker.logic.data.models.CountryIsoCodeModel
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import java.time.OffsetDateTime

class LockerModelTest : TestCase() {

    private lateinit var activeUsagePeriod : ActiveUsagePeriod
    private lateinit var countryIsoCodeModel : CountryIsoCodeModel

    @Before
    override fun setUp() {
        activeUsagePeriod = ActiveUsagePeriod(
            OffsetDateTime.parse("2023-07-10T23:00:00Z")
        )

        countryIsoCodeModel = CountryIsoCodeModel(
            "KE"
        )
    }

    @Test
    fun testActiveUsagePeriod() {
        val lockTime = OffsetDateTime.parse("2023-07-10T23:00:00Z")
        assertEquals(lockTime, activeUsagePeriod.lockTime)
    }

    @Test
    fun testCountryIsoCodeModel() {
        assertEquals("KE", countryIsoCodeModel.code)
    }

    @Test
    fun testCountryIsoTdd() {
        // assertNotSame
        assertEquals("UG", countryIsoCodeModel.code)
    }
}