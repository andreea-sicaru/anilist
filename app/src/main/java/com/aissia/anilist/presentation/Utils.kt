package com.aissia.anilist.presentation


// Converts an Int to duration string: eg. 1h 40m
fun Int.toFormattedDuration(): String {
    val h = this / 60
    val m = this % 60
    return if (h > 0) "${h}h ${m}m" else "${m}m"
}


// Converts a country code to a language
fun String?.toLanguage(): String = when (this) {
    "JP" -> "Japanese"
    "CN" -> "Chinese (Mandarin)"
    "TW" -> "Chinese (Traditional)"
    "HK" -> "Chinese (Cantonese)"
    "KR" -> "Korean"
    "US", "GB", "AU", "NZ", "IE", "CA" -> "English"
    "FR" -> "French"
    "DE", "AT", "CH" -> "German"
    "ES", "MX", "AR", "CO", "CL", "PE", "VE" -> "Spanish"
    "PT", "BR" -> "Portuguese"
    "IT" -> "Italian"
    "RU" -> "Russian"
    "IN" -> "Hindi"
    "SA", "EG", "AE", "IQ", "SY" -> "Arabic"
    "TR" -> "Turkish"
    "PL" -> "Polish"
    "NL", "BE" -> "Dutch"
    "SE" -> "Swedish"
    "NO" -> "Norwegian"
    "DK" -> "Danish"
    "FI" -> "Finnish"
    "CZ" -> "Czech"
    "SK" -> "Slovak"
    "HU" -> "Hungarian"
    "RO" -> "Romanian"
    "BG" -> "Bulgarian"
    "HR" -> "Croatian"
    "RS" -> "Serbian"
    "UA" -> "Ukrainian"
    "GR" -> "Greek"
    "HE", "IL" -> "Hebrew"
    "TH" -> "Thai"
    "VN" -> "Vietnamese"
    "ID" -> "Indonesian"
    "MY" -> "Malay"
    "PH" -> "Filipino"
    "FA", "IR" -> "Persian"
    else -> this ?: "Unknown"
}