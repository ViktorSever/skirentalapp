package com.example.skirental.state

data class PersonalDetailsState(
    val fullName: String = "",
    val phone: String = "",
    val date: String = "",
    val timeSlot: String = "",
    val pickupLocation: String = "Summit Base Lodge, Counter #4"
) {
    val isConfirmEnabled: Boolean
        get() = fullName.isNotBlank() &&
            phone.isNotBlank() &&
            date.isNotBlank() &&
            timeSlot.isNotBlank()
}

