package ru.borisov.foodstore

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class FoodListAdapter(
    context: Context,
    foodItemList: List<FoodItem>,
) : ArrayAdapter<FoodItem>(
    /* context = */ context,
    /* resource = */ R.layout.food_list_item,
    /* objects = */ foodItemList
) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: run {
            LayoutInflater.from(context).inflate(R.layout.food_list_item, parent, false)
        }
        val foodImageIV = view?.findViewById<ImageView>(R.id.imageIV)
        val foodNameTV = view?.findViewById<TextView>(R.id.foodNameTV)
        val foodPriceTV = view?.findViewById<TextView>(R.id.foodPriceTV)
        getItem(position)?.apply {
            image?.let { foodImageIV?.setImageBitmap(it) }
                ?: run { foodImageIV?.setImageResource(R.drawable.ic_empty_image_24) }
            foodNameTV?.text = name
            foodPriceTV?.text = price.toString()
        }
        return view
    }
}