package com.rocqjones.mlocker.logic.data.source

import com.rocqjones.mlocker.logic.data.models.ActiveUsagePeriod
import kotlinx.coroutines.delay
import java.time.OffsetDateTime

class ActiveUsagePeriodDataSource {
    suspend fun getLockingInfo() : ActiveUsagePeriod {
        delay(500) // Simulates a network call
        return ActiveUsagePeriod(
            OffsetDateTime.now().withHour(23).withMinute(0).withSecond(0)
        )
    }
}