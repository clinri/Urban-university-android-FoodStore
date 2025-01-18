package ru.borisov.foodstore

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ListView

class StoreActivity : CustomActivity() {

    private lateinit var foodNameET: EditText
    private lateinit var foodDescriptionET: EditText
    private lateinit var foodPriceET: EditText
    private lateinit var addFoodItemBTN: Button
    private lateinit var foodItemListLV: ListView
    private val adapter: FoodListAdapter by lazy { FoodListAdapter(this, foodItemList) }
    private var hasIntentFromDetailsActivity: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)
        initVariables()
        setSupportActionBar(toolbar)
        hasIntentFromDetailsActivity = intent.getBooleanExtra(HAS_INTENT_FROM_DETAILS_ACTIVITY_KEY, false)
        if (hasIntentFromDetailsActivity) {
            intent.parcelableArrayList<FoodItem>(FOOD_ITEM_LIST_KEY)?.let {
                foodItemList.addAll(it)
            }
        }
        foodItemListLV.adapter = adapter
        foodItemListLV.onItemClickListener = onItemClickListener()
        foodImageIV.setOnClickListener { startGetImageIntent() }
        addFoodItemBTN.setOnClickListener { onAddClick() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu_store_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.exit) {
            showToastFinishProgram()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onItemClickListener(): AdapterView.OnItemClickListener =
        AdapterView.OnItemClickListener { _, _, position, _ ->
            startActivity(Intent(this, DetailsActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                putParcelableArrayListExtra(FOOD_ITEM_LIST_KEY, foodItemList)
                putExtra(POSITION_KEY, position)
            })
        }

    private fun onAddClick() {
        if (foodNameET.text.isNotEmpty() && foodPriceET.text.isNotEmpty()) {
            foodItemList.add(createFoodItem())
            clearEditField()
            adapter.notifyDataSetChanged()
        }
    }

    private fun createFoodItem(): FoodItem = FoodItem(
        name = foodNameET.text.toString(),
        price = foodPriceET.text.toString().toInt(),
        description = if (foodDescriptionET.text.isNotEmpty()) foodDescriptionET.text.toString() else null,
        image = photoUri?.toString()
    )

    private fun clearEditField() {
        foodNameET.text.clear()
        foodDescriptionET.text.clear()
        foodPriceET.text.clear()
        foodImageIV.setImageResource(R.drawable.ic_add_image_24)
        photoUri = null
    }

    private fun initVariables() {
        toolbar = findViewById(R.id.toolbar)
        foodImageIV = findViewById(R.id.foodImageIV)
        foodNameET = findViewById(R.id.foodNameET)
        foodDescriptionET = findViewById(R.id.foodDescriptionET)
        foodPriceET = findViewById(R.id.foodPriceET)
        addFoodItemBTN = findViewById(R.id.addFoodItemBTN)
        foodItemListLV = findViewById(R.id.foodItemListLV)
    }
}