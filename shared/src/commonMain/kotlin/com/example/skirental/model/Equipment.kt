package com.example.skirental.model

import kotlinx.serialization.Serializable

@Serializable
data class Equipment(
    val name: String,
    val count: String,
    val type: EquipmentType,
)
