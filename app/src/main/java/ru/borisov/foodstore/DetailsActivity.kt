package ru.borisov.foodstore

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import java.util.Locale

class DetailsActivity : CustomActivity() {

    lateinit var foodNameTV: TextView
    lateinit var foodPriceTV: TextView
    lateinit var foodDescriptionTV: TextView
    private var foodItem: FoodItem? = null
    private var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        intent.parcelableArrayList<FoodItem>(FOOD_ITEM_LIST_KEY)?.let {
            foodItemList.addAll(it)
        }
        position = intent.getIntExtra(POSITION_KEY, 0)
        foodItem = foodItemList[position]
        initVariables()
        setSupportActionBar(toolbar)
        foodItem?.apply {
            image?.let {
                photoUri = Uri.parse(image)
                foodImageIV.setImageURI(photoUri)
            }
            foodNameTV.text = name
            foodDescriptionTV.text = description ?: ""
            foodPriceTV.text = String.format(Locale.getDefault(), "%d", price)
        }
        foodImageIV.setOnClickListener { startGetImageIntent() }
    }

    private fun initVariables() {
        toolbar = findViewById(R.id.toolbar)
        foodImageIV = findViewById(R.id.foodImageIV)
        foodNameTV = findViewById(R.id.foodNameTV)
        foodPriceTV = findViewById(R.id.foodPriceTV)
        foodDescriptionTV = findViewById(R.id.foodDescriptionTV)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu_details_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exit -> {
                showToastFinishProgram()
                finish()
            }

            R.id.back -> {
                changeList()
                val intent = Intent(this, StoreActivity::class.java).apply {
                    putParcelableArrayListExtra(FOOD_ITEM_LIST_KEY, foodItemList)
                    putExtra(HAS_INTENT_FROM_DETAILS_ACTIVITY_KEY, true)
                }
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun changeList() {
        val changedFoodItem = foodItem?.copy(image = photoUri.toString()) ?: FoodItem()
        foodItemList.add(position + 1, changedFoodItem)
        foodItemList.removeAt(position)
    }
}