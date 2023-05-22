package com.example.health_ass

data class MedecineModel(
    var medecineId: String? = null,
    var medecineName: String? = null,
    var medecineDose: String? = null,
    var medecineTime1: String? = null,
    var medecineTime2: String? = null,
    var medecineTime3: String? = null,
    var medecineStart: String? = null,
    var medecineDurationValue: String? = null,
    var medecineDurationUnit: String? = null,
    val medecineOrdre: String? = null,
    val downloadUrl: String? = null,


)