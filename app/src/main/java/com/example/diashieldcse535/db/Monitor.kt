package com.example.diashieldcse535.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "monitor")
data class Monitor(

    @PrimaryKey(autoGenerate = true)
    val monitorID: Int = 0,

    @ColumnInfo(name = "heart_rate")
    val heartRate: Int = 0,

    @ColumnInfo(name = "respiratory_rate")
    val respiratoryRate: Int = 0,

    @ColumnInfo(name = "symptom_nausea")
    val symptomNausea: Int = 0,

    @ColumnInfo(name = "symptom_headache")
    val symptomHeadache: Int = 0,

    @ColumnInfo(name = "symptom_diarrhea")
    val symptomDiarrhea: Int = 0,

    @ColumnInfo(name = "symptom_soar_throat")
    val symptomSoarThroat: Int = 0,

    @ColumnInfo(name = "symptom_fever")
    val symptomFever: Int = 0,

    @ColumnInfo(name = "symptom_muscle_ache")
    val symptomMuscleAche: Int = 0,

    @ColumnInfo(name = "symptom_loss_of_smell_and_taste")
    val symptomLossOfSmellAndTaste: Int = 0,

    @ColumnInfo(name = "symptom_cough")
    val symptomCough: Int = 0,

    @ColumnInfo(name = "symptom_shortness_of_breath")
    val symptomShortnessOfBreath: Int = 0,

    @ColumnInfo(name = "symptom_feeling_tried")
    val symptomFeelingTired: Int = 0,

    @ColumnInfo(name = "created_on")
    var createdOn: Long = 0L
)