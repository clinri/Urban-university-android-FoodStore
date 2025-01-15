package ru.borisov.foodstore

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider

class StoreActivity : AppCompatActivity() {

    private val GALLERY_REQUEST_CODE = 302
    lateinit var toolbar: Toolbar
    lateinit var foodImageIV: ImageView
    lateinit var foodNameET: EditText
    lateinit var foodDescriptionET: EditText
    lateinit var foodPriceET: EditText
    lateinit var addFoodItemBTN: Button
    lateinit var foodItemListLV: ListView
    private val foodItemList: MutableList<FoodItem> = mutableListOf()
    lateinit var storeViewModel: StoreViewModel
    private var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)
        storeViewModel = ViewModelProvider(this)[StoreViewModel::class.java]
        initVariables()
        setSupportActionBar(toolbar)
        val adapter = FoodListAdapter(this, foodItemList)
        foodItemListLV.adapter = adapter
        foodItemListLV.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val foodItem = adapter.getItem(position)
                startActivity(Intent(this, DetailsActivity::class.java).apply {
                    putExtra(FoodItem::class.java.simpleName, foodItem)
                })
            }
        storeViewModel.foodItemList.observe(this) { list ->
            list?.let {
                foodItemList.clear()
                foodItemList.addAll(it)
                adapter.notifyDataSetChanged()
            }
        }
        foodImageIV.setOnClickListener { startGetImageIntent() }
        addFoodItemBTN.setOnClickListener { onAddClick() }
    }

    private fun onAddClick() {
        if (foodNameET.text.isNotEmpty() && foodPriceET.text.isNotEmpty()) {
            val foodItem = FoodItem(
                name = foodNameET.text.toString(),
                price = foodPriceET.text.toString().toInt(),
                description = if (foodDescriptionET.text.isNotEmpty()) foodDescriptionET.text.toString() else null,
                image = photoUri?.toString()
            )
            val list = storeViewModel.foodItemList.value?.toMutableList() ?: mutableListOf()
            list.add(foodItem)
            storeViewModel.foodItemList.value = list
            foodNameET.text.clear()
            foodDescriptionET.text.clear()
            foodPriceET.text.clear()
            photoUri = null
            foodImageIV.setImageResource(R.drawable.ic_add_image_24)
        }
    }

    private fun startGetImageIntent() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST_CODE)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GALLERY_REQUEST_CODE -> if (resultCode == RESULT_OK) {
                photoUri = data?.data
                foodImageIV.setImageURI(photoUri)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.exit) finish()
        return super.onOptionsItemSelected(item)
    }
}