package com.example.diashieldcse535.utils

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {

    var heartRate by mutableIntStateOf(0)
    var isHeartRateProcessing by mutableStateOf(false)
    var uri: Uri = Uri.EMPTY

    var respiratoryRate by mutableIntStateOf(0)
    var isRespiratoryRateMeasuring by mutableStateOf(false)
    var isRespiratoryRateProcessing by mutableStateOf(false)

    var isLastFragment by mutableStateOf(false)

    var isSavingInProgress by mutableStateOf(false)

    var symptomNausea: Int = 0
    var symptomHeadache: Int = 0
    var symptomDiarrhea: Int = 0
    var symptomSoarThroat: Int = 0
    var symptomFever: Int = 0
    var symptomMuscleAche: Int = 0
    var symptomLossOfSmellAndTaste: Int = 0
    var symptomCough: Int = 0
    var symptomShortnessOfBreath: Int = 0
    var symptomFeelingTired: Int = 0
}