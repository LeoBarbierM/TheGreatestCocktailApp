package com.example.thegreatestcocktailapp.network

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Drinks(
    @SerializedName("drinks") val drinks: List<DrinkModel>?
) : Serializable

class DrinkModel(
    @SerializedName("idDrink") val id: String? = null,
    @SerializedName("strDrink") val name: String? = null,
    @SerializedName("strDrinkThumb") val imageURL: String? = null,
    @SerializedName("strCategory") val category: String? = null,
    @SerializedName("strAlcoholic") val alcoholic: String? = null,
    @SerializedName("strGlass") val glass: String? = null,
    @SerializedName("strInstructions") val instructions: String? = null,
    @SerializedName("strIngredient1") val ingredient1: String? = null,
    @SerializedName("strIngredient2") val ingredient2: String? = null,
    @SerializedName("strIngredient3") val ingredient3: String? = null,
    @SerializedName("strIngredient4") val ingredient4: String? = null,
    @SerializedName("strIngredient5") val ingredient5: String? = null,
    @SerializedName("strIngredient6") val ingredient6: String? = null,
    @SerializedName("strMeasure1") val measure1: String? = null,
    @SerializedName("strMeasure2") val measure2: String? = null,
    @SerializedName("strMeasure3") val measure3: String? = null,
    @SerializedName("strMeasure4") val measure4: String? = null,
    @SerializedName("strMeasure5") val measure5: String? = null,
    @SerializedName("strMeasure6") val measure6: String? = null
) : Serializable
