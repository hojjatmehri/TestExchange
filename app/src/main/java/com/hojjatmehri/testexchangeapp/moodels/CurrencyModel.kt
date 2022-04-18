package com.hojjatmehri.testexchangeapp.moodels

class CurrencyModel {
    var success: Boolean = true
    var timestamp: Long = 0
    var base: String = "EUR"
    var date: String = ""
    var rates = mapOf<String , Double>()
}
