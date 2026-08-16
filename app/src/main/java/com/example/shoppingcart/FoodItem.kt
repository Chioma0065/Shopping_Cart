package com.example.shoppingcart


import androidx.annotation.DrawableRes

object FoodItemDataSource {
    val foodItems = listOf(
        FoodItem(
            name = "Avocado Toast",
            description = "Sourdough, smashed avocado, chili flakes",
            price = 12.00,
            imageRes = R.drawable.img_avocdo_toast
        ),
        FoodItem(
            name = "Matcha Latte",
            description = "Oat milk, premium ceremonial grade matcha",
            price = 6.50,
            imageRes = R.drawable.img_matcha_latte__1_
        ),
        FoodItem(
            name = "Fresh Fruit Bowl",
            description = "Seasonal berries, mango, kiwi, dragon fruit",
            price = 9.00,
            imageRes = R.drawable.img_fresh_food_bowl
        ),
        FoodItem(
            name = "Greek Salad",
            description = "Cucumber, tomatoes, olives, feta cheese",
            price = 11.00,
            imageRes = R.drawable.img_greek_salad
        ),
        FoodItem(
            name = "Iced Americano",
            description = "Double shot espresso over ice",
            price = 4.50,
            imageRes = R.drawable.img_iced_americano
        ),
        FoodItem(
            name = "Berry Muffin",
            description = "Freshly baked with mixed berries",
            price = 3.75,
            imageRes = R.drawable.img_berry_muffin
        )
    )
}

data class FoodItem(
    val name: String,
    val description: String,
    val price: Double,
    @DrawableRes val imageRes: Int
)
