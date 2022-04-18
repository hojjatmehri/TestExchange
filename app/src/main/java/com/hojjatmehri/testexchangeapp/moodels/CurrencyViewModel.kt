package com.hojjatmehri.testexchangeapp.moodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hojjatmehri.testexchangeapp.classes.clsPublic

class CurrencyViewModel : ViewModel() {
    var lst = MutableLiveData<ArrayList<CurrencyListModel>>()
    var newlist = arrayListOf<CurrencyListModel>()

    fun add(item: CurrencyListModel){
        newlist.add(item)
        lst.value=newlist
    }

    fun remove(item: CurrencyListModel){
        newlist.remove(item)
        lst.value=newlist
    }
    fun filter(search: String ){
        removeAll()
        clsPublic.currenciesList = listOf<CurrencyListModel>()
        for (item in clsPublic.currenciesListPer) {
            if (item.name.toLowerCase()
                    .contains(search.toLowerCase()) || item.country.toLowerCase()
                    .contains(search.toLowerCase())
            ) {
                clsPublic.currenciesList += item
                add(item)
            }
        }
    }
    fun removeAll(){
        newlist.clear()
        lst.value=newlist
    }

}