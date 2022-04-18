package com.hojjatmehri.testexchangeapp.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.blongho.country_data.World
import com.hojjatmehri.testexchangeapp.R
import com.hojjatmehri.testexchangeapp.classes.clsPublic
import com.hojjatmehri.testexchangeapp.fragments.ConversionFragment
import com.hojjatmehri.testexchangeapp.moodels.CurrencyListModel
import de.hdodenhof.circleimageview.CircleImageView
import java.math.RoundingMode
import java.text.DecimalFormat


class CurrencyAdapter(var arrayList: ArrayList<CurrencyListModel>, val context: Activity): RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CurrencyAdapter.CurrencyViewHolder {
        var root = LayoutInflater.from(parent.context).inflate(R.layout.currency_item,parent,false)
        return CurrencyViewHolder(root)
    }

    override fun onBindViewHolder(holder: CurrencyAdapter.CurrencyViewHolder, position: Int) {
        holder.bind(arrayList.get(position) )

    }


    override fun getItemCount(): Int {
        return arrayList.size
    }


    inner  class CurrencyViewHolder(private val binding: View) : RecyclerView.ViewHolder(binding) {
        fun bind(currency: CurrencyListModel){
            var txvName: TextView = binding.findViewById(R.id.txv_currency_item_name)
            var txvCountry: TextView= binding.findViewById(R.id.txv_currency_item_coountry)
            var txvRate: TextView= binding.findViewById(R.id.txv_currency_item_rate)
            var img: CircleImageView= binding.findViewById(R.id.img_currency_item_flag)
            var lilMain: LinearLayout = binding.findViewById(R.id.lil_currency_main_item)
            txvName.text = currency.name
            txvRate.text = String.format("%.2f", currency.rate)
            if(clsPublic.bolBuy) {
                try {
                    val flag = World.getFlagOf(currency.country)
                    img.setImageResource(flag);
                    txvCountry.text = currency.country
                } catch (e: Exception) {

                }
            }else{
                    try {
                        val flag = World.getFlagOf(currency.country)
                        img.setImageResource(flag);
                    } catch (e: Exception) {

                    }
                val dec = DecimalFormat("#,###.##")
                dec.roundingMode = RoundingMode.CEILING
                txvCountry.text = dec.format(currency.deposit).toString()
            }
            lilMain.setOnClickListener {
                clsPublic.selectedCurrency = currency
                var frg = ConversionFragment()
                frg.show((context as AppCompatActivity).supportFragmentManager,"")
            }

        }

    }

}