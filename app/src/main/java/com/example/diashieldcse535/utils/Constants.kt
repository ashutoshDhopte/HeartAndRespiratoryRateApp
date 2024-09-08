package com.example.diashieldcse535.utils

interface Constants {

    object Symptom{
        val symptomKeys = listOf(
            "Nausea",
            "Headache",
            "Diarrhea",
            "Soar throat",
            "Fever",
            "Muscle ache",
            "Loss of smell and taste",
            "Cough",
            "Shortness of breath",
            "Feeling tired"
        )
    }

    companion object{
        val DATABASE_NAME = "monitor-db"
        val MEASURING_TIME = 45000L
    }
}