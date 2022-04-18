package com.hojjatmehri.testexchangeapp.moodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CurrencyViewModelFactory (): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CurrencyViewModel::class.java)){
            return CurrencyViewModel() as T
        }
        throw IllegalArgumentException ("UnknownViewModel")
    }

}