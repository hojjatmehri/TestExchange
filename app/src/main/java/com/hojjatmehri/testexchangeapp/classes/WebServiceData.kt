package com.hojjatmehri.testexchangeapp.classes

import android.app.Activity
import com.hojjatmehri.testexchangeapp.moodels.CurrencyModel
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import java.util.concurrent.TimeUnit


 interface I_WS {
    @GET("latest?access_key=d115a43293cc252d6afb169ffe220e51")
    @Headers("Content-Type:application/json", "Accept:application/json")
    fun CURRENCY_CALL(): Call<CurrencyModel?>?
}

interface CurrenciesCB {
    fun onSuccess(data: CurrencyModel?)
    fun onError(t: Throwable?)
}

class WebServiceData {


    companion object {

        var client = OkHttpClient.Builder()
            .connectTimeout(30000, TimeUnit.SECONDS) //.addInterceptor(LoggingInterceptor.create())
            .readTimeout(30000, TimeUnit.SECONDS).build()

        var retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(clsPublic.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var i_WS = retrofit.create(I_WS::class.java)

        fun allCurrencies(
            act: Activity?,
            cb: CurrenciesCB?
        ) {

            val call: Call<CurrencyModel?>? = i_WS.CURRENCY_CALL()
            call?.enqueue(object : Callback<CurrencyModel?> {
                override fun onResponse(call: Call<CurrencyModel?>, response: Response<CurrencyModel?>) {
                    when {
                        response.body() == null -> {
                            clsPublic.pd(false, act!!, "" , false)
                            clsPublic.toast(act!!, "","e")
                            cb!!.onError(null)
                        }
                        response.body()!!.success -> {
                            cb?.onSuccess(response.body())
                        }
                        else -> {
                            clsPublic.pd(false, act!!, "" , false)
                            clsPublic.toast(act!!, "","e")
                            cb!!.onError(null)
                        }
                    }
                }

                override fun onFailure(call: Call<CurrencyModel?>, t: Throwable) {
                    t.printStackTrace()
                    clsPublic.pd(false, act!!, "" , false)
                    clsPublic.toast(act!!, "","e")
                    cb?.onError(t)
                }
            })
        }

    }

}