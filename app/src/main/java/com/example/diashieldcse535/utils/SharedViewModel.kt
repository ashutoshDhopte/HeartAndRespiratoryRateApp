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
}