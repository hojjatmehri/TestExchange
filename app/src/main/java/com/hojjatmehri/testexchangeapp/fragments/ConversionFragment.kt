package com.hojjatmehri.testexchangeapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.blongho.country_data.World
import com.hojjatmehri.testexchangeapp.R
import com.hojjatmehri.testexchangeapp.classes.clsPublic
import com.hojjatmehri.testexchangeapp.ui.MainActivity
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView
import de.hdodenhof.circleimageview.CircleImageView
import java.math.RoundingMode
import java.text.DecimalFormat


class ConversionFragment : DialogFragment() {
    var txvTitle: TextView? = null
    var txvName: TextView? = null
    var txvAmount: TextView? = null
    var txvDes: TextView? = null
    var txvCountry: TextView? = null
    var txvRate: TextView? = null
    var txvRateTitle: TextView? = null
    var txvSubmit: TextView? = null
    var txvCancel: TextView? = null
    var img: CircleImageView? = null
    var spnCurrency: PowerSpinnerView? = null
    var edtAmount: EditText? = null
    var commissionFee: Double = 0.0
    var amount: Double = 0.0
    val lstCurrencies: MutableList<String?> = ArrayList()
    var eur: Double = 0.0
    var rate: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_conversion, null)
        dialog?.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        txvTitle = view.findViewById(R.id.txv_frg_title)
        txvName = view.findViewById(R.id.txv_frg_name)
        txvAmount = view.findViewById(R.id.txv_frg_Amount)
        txvDes = view.findViewById(R.id.txv_frg_des)
        txvCountry = view.findViewById(R.id.txv_frg_coountry)
        txvRate = view.findViewById(R.id.txv_frg_rate)
        txvRateTitle = view.findViewById(R.id.txv_frg_rate_title)
        txvSubmit = view.findViewById(R.id.txv_frg_submit)
        txvCancel = view.findViewById(R.id.txv_frg_cancel)
        img = view.findViewById(R.id.img_frg_flag)
        spnCurrency = view.findViewById(R.id.spinner_frg_currency)
        edtAmount = view.findViewById(R.id.edt_frg_currency)
        rate = clsPublic.selectedCurrency.rate
        txvName?.text = clsPublic.selectedCurrency.name
        txvRate?.text = String.format("%.2f", clsPublic.selectedCurrency.rate)
        try {
            val flag = World.getFlagOf(clsPublic.selectedCurrency.country)
            img?.setImageResource(flag);
            txvCountry?.text = clsPublic.selectedCurrency.country
        }catch (e: Exception){

        }
        if (clsPublic.bolBuy) {
            txvTitle?.text = "Buy"
            for (i in clsPublic.myCurrenciesList.indices) {
                if(clsPublic.myCurrenciesList[i].name != clsPublic.selectedCurrency.name)
                    lstCurrencies.add(clsPublic.myCurrenciesList[i].name)
            }
            spnCurrency?.setItems(lstCurrencies)
            spnCurrency?.selectItemByIndex(0)
            for (item in clsPublic.myCurrenciesList) {
                if (item.name == lstCurrencies[0]) {
                    eur = item.deposit / item.rate
                    rate =  clsPublic.selectedCurrency.rate / item.rate
                    txvRateTitle?.text = "Rate to ${item.name}"
                    txvRate?.text =  String.format("%.2f", rate)
                    clsPublic.intFreeCharge = (clsPublic.intFreeChargeEUR * item.rate).toInt()
                    break
                }
            }
        }else{
            txvTitle?.text = "Sell"
            for (i in clsPublic.currenciesListPer.indices) {
                if(clsPublic.currenciesListPer[i].name != clsPublic.selectedCurrency.name)
                    lstCurrencies.add(clsPublic.currenciesListPer[i].name)

            }
            spnCurrency?.setItems(lstCurrencies)
            spnCurrency?.selectItemByIndex(0)
            for (item in clsPublic.currenciesListPer) {
                if (item.name == lstCurrencies[0]) {
                    eur = item.deposit / item.rate
                    rate =  clsPublic.selectedCurrency.rate / item.rate
                    txvRateTitle?.text = "Rate to ${item.name}"
                    txvRate?.text =  String.format("%.2f", rate)
                    clsPublic.intFreeCharge = (clsPublic.intFreeChargeEUR * item.rate).toInt()
                    break
                }
            }
        }

        spnCurrency?.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            if(clsPublic.bolBuy) {
                for (item in clsPublic.myCurrenciesList) {
                    if (item.name == lstCurrencies[spnCurrency?.selectedIndex!!]) {
                        eur = item.deposit / item.rate
                        rate = clsPublic.selectedCurrency.rate / item.rate
                        txvRateTitle?.text = "Rate to ${item.name}"
                        txvRate?.text = String.format("%.2f", rate)
                        clsPublic.intFreeCharge = (clsPublic.intFreeChargeEUR * item.rate).toInt()
                        break
                    }
                }
            }else{
                for (item in clsPublic.currenciesListPer) {
                    if (item.name == newText) {
                        eur = item.deposit / item.rate
                        rate =  clsPublic.selectedCurrency.rate / item.rate
                        txvRateTitle?.text = "Rate to ${item.name}"
                        txvRate?.text =  String.format("%.2f", rate)
                        clsPublic.intFreeCharge = (clsPublic.intFreeChargeEUR * item.rate).toInt()
                        break
                    }
                }
            }
            edtAmount?.setText("")
            txvAmount?.text = "0"
            txvDes?.text = ""
        }

        edtAmount?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString() != "") {
                    amount = rate * s.toString().toDouble()
                    txvAmount?.text = String.format("%.2f", amount)
                    if(clsPublic.intTime > clsPublic.intFreeChargeTime ||
                        edtAmount?.text.toString().toDouble() > clsPublic.intFreeCharge ){
                         commissionFee = s.toString().toDouble() * clsPublic.dblCommissionFeePercentage
                    }else{
                        commissionFee = 0.0
                    }
                    txvDes?.text = "Commission fee is ${String.format("%.2f", commissionFee)}"
                }else{
                    txvAmount?.text = ""
                    txvDes?.text = ""
                }
            }
        })


        txvSubmit?.setOnClickListener {
            var exist: Boolean = false
            var possible: Boolean = true
            var i: Int = 0
            if(amount > 0) {
                for (item in clsPublic.myCurrenciesList){
                    if(item.name == lstCurrencies[spnCurrency?.selectedIndex!!]){
                        if (item.deposit -  edtAmount?.text.toString().toDouble() + commissionFee <0) {
                            possible = false
                            clsPublic.toast(clsPublic.act!!,getString(R.string.zero_error) , "e")
                        }
                        break
                    }
                }
                if(possible) {
                    clsPublic.intTime++
                    if(clsPublic.bolBuy){
                        clsPublic.selectedCurrency.deposit = amount
                        for (item in clsPublic.myCurrenciesList) {
                            if (item.name == clsPublic.selectedCurrency.name) {
                                exist = true
                                break
                            }
                            i++
                        }
                        if (exist) {
                            clsPublic.myCurrenciesList[i].deposit += amount
                        } else {
                            clsPublic.myCurrenciesList += clsPublic.selectedCurrency
                        }
                        for (item in clsPublic.myCurrenciesList){
                            if(item.name == lstCurrencies[spnCurrency?.selectedIndex!!]){
                                item.deposit -=  edtAmount?.text.toString().toDouble() + commissionFee
                                MainActivity.txvTotalBalance?.text =
                                    String.format("%.2f", clsPublic.myCurrenciesList[0].deposit)

                                break
                            }
                        }
                    }else{
                        clsPublic.selectedCurrency.deposit -= amount + commissionFee
                        i = 0
                        for (item in clsPublic.myCurrenciesList) {
                            if (item.name == lstCurrencies[spnCurrency?.selectedIndex!!]  ) {
                                exist = true
                                break
                            }
                            i++
                        }
                        if (exist) {
                            clsPublic.myCurrenciesList[i].deposit += edtAmount?.text.toString().toDouble()
                        } else {
                            for (item in clsPublic.currenciesListPer){
                                if (item.name == lstCurrencies[spnCurrency?.selectedIndex!!]  ) {
                                    //item.deposit = edtAmount?.text.toString().toDouble()
                                    clsPublic.myCurrenciesList += item
                                    break
                                }
                            }
                        }
                        for (item in clsPublic.myCurrenciesList){
                            if(item.name == lstCurrencies[spnCurrency?.selectedIndex!!]){
                                item.deposit +=  edtAmount?.text.toString().toDouble()
                                val dec = DecimalFormat("#,###.##")
                                dec.roundingMode = RoundingMode.CEILING
                                MainActivity.txvTotalBalance?.text =
                                    dec.format(clsPublic.myCurrenciesList[0].deposit).toString()

                                break
                            }
                        }

                        MainActivity.loadMyCurrencies()
                    }
                    dismiss()
                }
            }
        }

        txvCancel?.setOnClickListener {
            dismiss()
        }
        return view
    }


}

