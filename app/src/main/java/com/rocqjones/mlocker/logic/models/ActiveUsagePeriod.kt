package com.rocqjones.mlocker.logic.models

import java.time.OffsetTime

data class ActiveUsagePeriod(
    val lockTime : OffsetTime,
)