package com.healthanalysis.app.data.models

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    val id: Int,
    val barcode: String,
    val name: String,
    val calories: Double,
    val proteins: Double,
    val fats: Double,
    val carbs: Double,
    @SerializedName("image_url") val imageUrl: String?
)

data class FoodLogResponse(
    val id: Int,
    val product: ProductResponse,
    val weight: Int,
    @SerializedName("meal_type") val mealType: String,
    @SerializedName("created_at") val createdAt: String
)

data class FoodLogRequest(
    @SerializedName("product_id") val productId: Int,
    val weight: Int,
    @SerializedName("meal_type") val mealType: String
)

data class CustomProductRequest(
    val name: String,
    val calories: Double,
    val proteins: Double,
    val fats: Double,
    val carbs: Double,
    @SerializedName("is_global_dish") val isGlobalDish: Boolean = false
)

data class CustomProductResponse(val id: Int)

data class WaterResponse(val date: String, val amount: Int)
data class WaterAmountRequest(val amount: Int, val date: String)
