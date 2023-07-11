package com.rocqjones.mlocker.logic.data.source

import com.rocqjones.mlocker.logic.data.models.CountryIsoCodeModel

class CountryIsoCodeDataSource {
    fun getCountryIsoCode() : CountryIsoCodeModel {
        return CountryIsoCodeModel("KE")
    }
}