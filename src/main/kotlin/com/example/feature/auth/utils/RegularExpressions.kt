package com.example.feature.auth.utils


 fun isPhoneValid(phone: String): Boolean {
    val phoneNumberRegex = Regex("^\\+?[1-9]\\d{1,14}\$")
    return phone.matches(phoneNumberRegex)
}


 fun isEmailValid(email: String): Boolean {
    val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
    return email.matches(emailRegex)
}