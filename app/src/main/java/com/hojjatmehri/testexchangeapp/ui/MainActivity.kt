package com.hojjatmehri.testexchangeapp.ui

import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.RadioButton
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blongho.country_data.World
import com.github.angads25.toggle.interfaces.OnToggledListener
import com.github.angads25.toggle.widget.LabeledSwitch
import com.hojjatmehri.testexchangeapp.R
import com.hojjatmehri.testexchangeapp.adapters.CurrencyAdapter
import com.hojjatmehri.testexchangeapp.classes.CurrenciesCB
import com.hojjatmehri.testexchangeapp.classes.WebServiceData
import com.hojjatmehri.testexchangeapp.classes.clsPublic
import com.hojjatmehri.testexchangeapp.moodels.*
import java.math.RoundingMode
import java.security.AccessController.getContext
import java.text.DecimalFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    companion object {
        fun loadMyCurrencies() {
            viewModel.removeAll()
            clsPublic.currenciesList = listOf<CurrencyListModel>()
            for (item in clsPublic.myCurrenciesList) {
                viewModel.add(item)
                clsPublic.currenciesList += item
            }
        }

        var txvInternetConnection: TextView? = null
        var txvTotalBalance: TextView? = null
        lateinit var mainrecycler: RecyclerView

         var viewManager = LinearLayoutManager(clsPublic.act)
         lateinit var viewModel: CurrencyViewModel

    }

    var darkLight: LabeledSwitch? = null
    var searchView: SearchView? = null
    var rdbBuy: RadioButton? = null
    var rdbSell: RadioButton? = null

    var byExitCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.setNavigationBarColor(resources.getColor(R.color.main_list_bg))
        clsPublic.loadJSONFromAsset(this)
        viewManager = LinearLayoutManager(this)
        initials()
        code()

    }


    override fun onResume() {
        super.onResume()
        rdbBuy?.isChecked = true
        checkData()
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if(isOnline()){
                    checkData()
                    this@MainActivity.runOnUiThread(java.lang.Runnable {
                        txvInternetConnection?.visibility = View.GONE
                        clsPublic.bolInternetConnection = true
                    })
                }else {
                    this@MainActivity.runOnUiThread(java.lang.Runnable {
                        txvInternetConnection?.visibility = View.VISIBLE
                        clsPublic.bolInternetConnection = false
                    })
                }
            }
        }, 0, 5000)

    }
    fun isOnline(): Boolean {
        val cm = clsPublic.act?.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        //should check null because in airplane mode it will be null
        return netInfo != null && netInfo.isConnected
    }
    private fun checkData() {
        WebServiceData.allCurrencies(clsPublic.act,
            object : CurrenciesCB {
                override fun onSuccess(data: CurrencyModel?) {
                    if (data != null) {
                        var dataChanged: Boolean = false
                        var i: Int = 0
                        if (data.rates.size == clsPublic.currenciesListPer.size) {
                            for (item in data.rates) {
                                if (!item.key.equals(clsPublic.currenciesListPer[i].name) ||
                                    !item.value.equals(clsPublic.currenciesListPer[i].rate)) {
                                    dataChanged = true;
                                    break
                                }
                                i++
                            }
                        } else
                            dataChanged = true
                        if (data.rates.size != viewModel.lst.value?.size) {
                            dataChanged = true
                        }
                        if (dataChanged) {
                            clsPublic.pd(true, clsPublic.act as MainActivity, "", false)
                            viewModel.removeAll()
                            clsPublic.currenciesList = listOf<CurrencyListModel>()
                            for (item in data.rates) {
                                var model: CurrencyListModel = CurrencyListModel()
                                for (i in 0 until clsPublic.codesAll.length()) {
                                    val jo_inside = clsPublic.codesAll.getJSONObject(i)
                                    val formula_value = jo_inside.getString("AlphabeticCode")
                                    if (formula_value.equals(item.key)) {
                                        val country = jo_inside.getString("Entity")
                                        model.country = country.toLowerCase().capitalize()
                                        break
                                    }
                                }
                                model.name = item.key
                                model.rate = item.value
                                viewModel.add(model)
                                clsPublic.currenciesList += model
                            }
                            clsPublic.currenciesListPer = clsPublic.currenciesList
                        }
                        clsPublic.pd(false, clsPublic.act as MainActivity, "", false)
                    }

                }

                override fun onError(t: Throwable?) {
                    clsPublic.pd(false, clsPublic.act as MainActivity, "", false)
                }
            })

    }

    private fun code() {
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText != "") {
                    if (rdbBuy!!.isChecked) {
                        viewModel.filter(newText)
                    } else {
                        viewModel.removeAll()
                        clsPublic.currenciesList = listOf<CurrencyListModel>()
                        for (item in clsPublic.myCurrenciesList) {
                            if (item.name.toLowerCase()
                                    .contains(newText.toLowerCase()) || item.country.toLowerCase()
                                    .contains(newText.toLowerCase())
                            ) {
                                viewModel.add(item)
                                clsPublic.currenciesList += item
                            }
                        }
                    }
                }
                return false
            }
        })
        searchView?.setOnCloseListener {
            if (rdbBuy!!.isChecked) {
                checkData()
            } else {
                loadMyCurrencies()
            }
            return@setOnCloseListener false
        }
        rdbSell?.setOnClickListener(View.OnClickListener {
            clsPublic.v?.vibrate(50)
            clsPublic.bolBuy = false
            loadMyCurrencies()
        })
        rdbBuy?.setOnClickListener(View.OnClickListener {
            clsPublic.v?.vibrate(50)
            clsPublic.bolBuy = true
            viewModel.removeAll()
            checkData()
        })

        darkLight?.setOnToggledListener(OnToggledListener { toggleableView, isOn ->
            if(isOn){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        })
    }

    private fun initials() {

        clsPublic.v = getSystemService(VIBRATOR_SERVICE) as Vibrator
        txvTotalBalance = findViewById(R.id.txv_total_main)
        txvInternetConnection = findViewById(R.id.txv_internet_connection)
        searchView = findViewById(R.id.searchView)
        val factory = CurrencyViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(CurrencyViewModel::class.java)
        rdbBuy = findViewById(R.id.rdb_buy)
        rdbSell = findViewById(R.id.rdb_sell)
        darkLight  = findViewById(R.id.switch_dark_light)
        darkLight?.setLabelOff("Light")
        darkLight?.setLabelOn("Dark")
        var nightModeFlags = getResources().getConfiguration().uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> darkLight?.isOn = true
            Configuration.UI_MODE_NIGHT_NO -> darkLight?.isOn = false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> darkLight?.isOn = false
        }

        val dec = DecimalFormat("#,###.##")
        dec.roundingMode = RoundingMode.CEILING
        txvTotalBalance?.text = "€ " + dec.format(clsPublic.intDepositedEUR).toString()

        mainrecycler = findViewById(R.id.rec_currencies)
        clsPublic.act = this;
        World.init(getApplicationContext());
        clsPublic.myCurrenciesList  = listOf<CurrencyListModel>()
        var model: CurrencyListModel = CurrencyListModel()
        model.country = "Europe"
        model.name = "EUR"
        model.rate = 1.0
        model.deposit = 1000.0
        clsPublic.myCurrenciesList += model
        initialiseAdapter()


    }


    private fun initialiseAdapter() {
        mainrecycler.setLayoutManager(LinearLayoutManager(this));
        mainrecycler.layoutManager = viewManager
        observeData()
    }

    fun observeData() {
        viewModel.lst.observe(this, Observer {
            Log.i("data", it.toString())
            mainrecycler.adapter = CurrencyAdapter(it, this)
        })
    }



    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (byExitCounter == 0) {
                byExitCounter++
                clsPublic.toast(this, getString(R.string.message_exit), "w")
                val task: TimerTask = object : TimerTask() {
                    override fun run() {
                        // TODO Auto-generated method stub
                        byExitCounter = 0
                    }
                }
                val t = Timer()
                t.schedule(task, 5000)
            } else if (byExitCounter == 1) {
                return super.onKeyDown(keyCode, event)
            }
            return true
        }
        return true
    }

}