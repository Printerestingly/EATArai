package com.example.eatarai.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CuisineData(
    val name: String,
    val isFood: Boolean
) : Parcelable
