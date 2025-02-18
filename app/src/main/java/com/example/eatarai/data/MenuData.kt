package com.example.eatarai.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuData(
    val name: String,
    val cuisineName: String
): Parcelable
