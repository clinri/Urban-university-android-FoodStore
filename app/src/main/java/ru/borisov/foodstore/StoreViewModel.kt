package ru.borisov.foodstore

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.collections.List

class StoreViewModel : ViewModel() {
    val foodItemList: MutableLiveData<List<FoodItem>> by lazy { MutableLiveData<List<FoodItem>>() }
}