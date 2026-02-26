package com.example.skirental.intent

sealed class PersonalDetailsIntent {
    data class FullNameChanged(val value: String) : PersonalDetailsIntent()
    data class PhoneChanged(val value: String) : PersonalDetailsIntent()
    data class DateChanged(val value: String) : PersonalDetailsIntent()
    data class TimeSlotChanged(val value: String) : PersonalDetailsIntent()
    object ConfirmClicked : PersonalDetailsIntent()
}

