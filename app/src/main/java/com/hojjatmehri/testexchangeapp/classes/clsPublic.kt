package com.hojjatmehri.testexchangeapp.classes

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.os.Vibrator
import android.widget.Toast
import com.hojjatmehri.hometoast.HomeToast
import com.hojjatmehri.testexchangeapp.R
import com.hojjatmehri.testexchangeapp.moodels.CurrencyListModel
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.io.InputStream


@SuppressLint("StaticFieldLeak")
class clsPublic {
    companion object {
        var selectedCurrency: CurrencyListModel = CurrencyListModel()

        @kotlin.jvm.JvmField
        var v: Vibrator? = null
        var currenciesList  = listOf<CurrencyListModel>()
        var myCurrenciesList  = listOf<CurrencyListModel>()
        var currenciesListPer  = listOf<CurrencyListModel>()
        var codesAll: JSONArray = JSONArray()
        var BASE_URL =
            "http://api.exchangeratesapi.io/v1/"
        var intFreeChargeTime = 5
        var intFreeChargeEUR = 200
        var intFreeCharge = 200
        var intTime = 0
        var intDepositedEUR = 1000
        var dblCommissionFeePercentage = 0.07
        var act: Activity? = null
        var bolBuy:Boolean = true
        var bolInternetConnection = true
        var progressBar: ProgressDialog? = null


        fun toast(activity: Activity, strToast: String, type: String) {
            var strToast = strToast
            if (strToast == "") {
                strToast = activity.getString(R.string.message_error)
            }
            if (strToast == "nv") {
                strToast = activity.getString(R.string.message_next_version)
            }
            if (strToast == "empty") {
                strToast = activity.getString(R.string.message_information_not_found)
            }
            if (strToast == "searchEmpty") {
                strToast = activity.getString(R.string.message_nothing_found)
            }
            if (strToast == "s") {
                strToast = activity.getString(R.string.successfully_done)
            }
            if (type == "e") HomeToast.showError(
                activity,
                strToast,
                Toast.LENGTH_SHORT
            ) else if (type == "w") HomeToast.showWarning(
                activity,
                strToast,
                Toast.LENGTH_SHORT
            ) else if (type == "s") HomeToast.showSuccess(
                activity,
                strToast,
                Toast.LENGTH_SHORT
            ) else if (type == "i") HomeToast.showInformation(activity, strToast, Toast.LENGTH_SHORT)
        }



        fun pd(bolW: Boolean, context: Context, strMessage: String, setCancelable: Boolean) {
            var strMessage = strMessage
            try {
                if (bolW) {
                    progressBar = ProgressDialog(context)
                    progressBar!!.setCancelable(true)
                    if (strMessage == "") strMessage = context.getString(R.string.loading_downloading)
                    if (strMessage == "r") strMessage = context.getString(R.string.loading_downloading)
                    if (strMessage == "s") strMessage = context.getString(R.string.loading_uploading)
                    if (strMessage == "p") strMessage = context.getString(R.string.loading_processing)
                    progressBar!!.setMessage(strMessage)
                    progressBar!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                    progressBar!!.setCanceledOnTouchOutside(true)
                    progressBar!!.show()
                } else {
                    progressBar!!.dismiss()
                }
            } catch (e: Exception) {
            }
        }

        fun loadJSONFromAsset(context: Context): String? {
            var json: String? = null
            json = try {
                val `is`: InputStream = context.getAssets().open("codes_all.json")
                val size: Int = `is`.available()
                val buffer = ByteArray(size)
                `is`.read(buffer)
                `is`.close()
                String(buffer)
            } catch (ex: IOException) {
                ex.printStackTrace()
                return null
            }

            try {
                codesAll = JSONArray(json)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return json
        }


    }


}