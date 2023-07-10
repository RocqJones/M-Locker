package com.rocqjones.mlocker.logic.data.source

import com.rocqjones.mlocker.logic.data.enums.CountryIsoCode

class CountryIsoCodeDataSource {
    suspend fun getCountryIsoCode() : CountryIsoCode {
        return CountryIsoCode.KE
    }
}