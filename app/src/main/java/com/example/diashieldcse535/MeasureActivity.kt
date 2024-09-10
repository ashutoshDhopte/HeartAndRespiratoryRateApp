package com.example.diashieldcse535

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventCallback
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Recording
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.camera.view.video.AudioConfig
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.diashieldcse535.MeasureActivity.Companion.CAMERAX_PERMISSION
import com.example.diashieldcse535.db.Databases
import com.example.diashieldcse535.db.Monitor
import com.example.diashieldcse535.ui.theme.DiaShieldCSE535Theme
import com.example.diashieldcse535.utils.Constants
import com.example.diashieldcse535.utils.MonitorUtil
import com.example.diashieldcse535.utils.SharedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

private const val FRAGMENT_HEART = "heart"
private const val FRAGMENT_RESPIRATORY = "respiratory"
private const val FRAGMENT_SYMPTOM = "symptom"
private const val FRAGMENT_CAMERA = "camera"
private const val FRAGMENT_MEASURE = "measure"
private var recording: Recording? = null
private var sensorManager: SensorManager? = null
private var respRateListX = mutableListOf<Float>()
private var respRateListY = mutableListOf<Float>()
private var respRateListZ = mutableListOf<Float>()
private var sensorEventListener: SensorEventListener? = MeasureActivity.AccelerometerSensor()
private var db: Databases? = null

class MeasureActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(
            this.applicationContext,
            Databases::class.java,
            Constants.DATABASE_NAME
        ).build()

        setContent {

            Home()
        }
    }

    companion object{
        val CAMERAX_PERMISSION = arrayOf(
            Manifest.permission.CAMERA
        )
    }

    class AccelerometerSensor: SensorEventCallback(){

        override fun onSensorChanged(event: SensorEvent?) {
            super.onSensorChanged(event)

            event?.let {
                respRateListX.add(event.values[0])
                respRateListY.add(event.values[1])
                respRateListZ.add(event.values[2])
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Home(modifier: Modifier = Modifier){

    val navController = rememberNavController()
    val outerNavController = rememberNavController()
    val context = LocalContext.current
    val sharedViewModel: SharedViewModel = viewModel()

    DiaShieldCSE535Theme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                    title = {
                        Text(
                            "Context Monitoring",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                backAction(context, navController, sharedViewModel)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Localized description"
                            )
                        }
                    },
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = outerNavController,
                startDestination = OuterFragments.MeasureFragment.route,
                modifier = modifier
                    .fillMaxSize()
            ) {
                composable(OuterFragments.MeasureFragment.route){
                    MeasureFragment(
                        context,
                        navController,
                        outerNavController,
                        innerModifier = Modifier.padding(innerPadding),
                        sharedViewModel
                    )
                }
                composable(OuterFragments.CameraFragment.route){
                    CameraFragment(sharedViewModel, outerNavController)
                }
            }
        }
    }
}

private fun backAction(context: Context, navController: NavHostController, sharedViewModel: SharedViewModel){

    if(currentOuterFragment === FRAGMENT_CAMERA){
        navToNextOuterFragment(navController)
    }else {
        if (currentFragment === FRAGMENT_HEART) {
            navToMainActivity(context)

        } else {
            navToNextFragment(navController, sharedViewModel, true)
        }
    }
}

private fun navToMainActivity(context: Context){

    currentFragment = FRAGMENT_HEART
    currentOuterFragment = FRAGMENT_MEASURE

    context.startActivity(Intent(context, MainActivity::class.java)
        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    )
}

@Composable
private fun MeasureFragment(context: Context, navController: NavHostController, outerNavController: NavHostController,
                            innerModifier: Modifier, sharedViewModel: SharedViewModel, modifier: Modifier = Modifier){

    var heartRateValue by remember { mutableIntStateOf(0) }
    var respiratoryRateValue by remember { mutableIntStateOf(0) }

    var isSavingInProgressValue by remember { mutableStateOf(false) }
    LaunchedEffect(sharedViewModel.isSavingInProgress) {
        isSavingInProgressValue = sharedViewModel.isSavingInProgress
    }

    var isLastFragmentValue by remember { mutableStateOf(false) }
    LaunchedEffect(sharedViewModel.isLastFragment) {
        isLastFragmentValue = sharedViewModel.isLastFragment
    }

    Column(
        modifier = innerModifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = Fragments.HeartFragment.route,
            modifier = modifier
                .weight(1f)
                .fillMaxWidth()
        ){
            composable(Fragments.HeartFragment.route){
                HeartFragment(outerNavController, sharedViewModel)
            }
            composable(Fragments.RespiratoryFragment.route){
                RespiratoryFragment(context, sharedViewModel)
            }
            composable(Fragments.SymptomFragment.route){
                SymptomFragment(sharedViewModel)
            }
        }
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = modifier
                .wrapContentHeight()
                .fillMaxWidth()
        ) {
            Column(
                modifier = modifier.fillMaxWidth()
                    .wrapContentHeight()
                    .align(Alignment.CenterHorizontally)
                    .height(100.dp)
            ) {
                if(isSavingInProgressValue) {
                    LinearProgressIndicator(
                        modifier = modifier.fillMaxWidth()
                            .height(6.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }else{
                    HorizontalDivider(
                        thickness = 6.dp,
                        color = Color.Transparent
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .height(IntrinsicSize.Max)
                        .padding(10.dp)
                ){
                    Column(
                        modifier = modifier
                            .weight(1f)
                            .height(IntrinsicSize.Max)
                            .align(Alignment.CenterVertically),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.heartrate),
                            contentDescription = null,
                            modifier = modifier.size(30.dp)
                        )
                        Text(
                            heartRateValue.toString(),
                            fontWeight = FontWeight.Bold,
                            modifier = modifier.padding(5.dp)
                        )
                    }
                    Column(
                        modifier = modifier
                            .weight(1f)
                            .height(IntrinsicSize.Max)
                            .align(Alignment.CenterVertically),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.stethescope),
                            contentDescription = null,
                            modifier = modifier.size(30.dp)
                        )
                        Text(
                            respiratoryRateValue.toString(),
                            fontWeight = FontWeight.Bold,
                            modifier = modifier.padding(5.dp)
                        )
                    }
                    Column(
                        modifier = modifier.weight(1f)
                            .align(Alignment.CenterVertically)
                    ) {
                        AnimatedVisibility(
                            !isLastFragmentValue,
                            modifier = modifier.height(IntrinsicSize.Max)
                                .width(IntrinsicSize.Max)
                                .weight(1f)
                                .align(Alignment.CenterHorizontally),
                            exit = slideOutVertically() + fadeOut(),
                            enter = slideInVertically() + fadeIn()
                        ) {
                            Button(
                                onClick = {
                                    navToNextFragment(navController, sharedViewModel)
                                    heartRateValue = sharedViewModel.heartRate
                                    respiratoryRateValue = sharedViewModel.respiratoryRate
                                },
                                modifier = modifier
                                    .wrapContentSize()
                                    .align(Alignment.CenterHorizontally)
                            ) {
                                Text(
                                    "Next",
                                    modifier = modifier.padding(5.dp),
                                    color = Color.White
                                )
                                Image(
                                    painter = painterResource(R.drawable.baseline_arrow_forward_ios_24),
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(Color.White),
                                    modifier = modifier.padding(5.dp)
                                )
                            }
                        }
                        AnimatedVisibility(
                            isLastFragmentValue,
                            modifier = modifier.height(IntrinsicSize.Max)
                                .width(IntrinsicSize.Max)
                                .weight(1f)
                                .align(Alignment.CenterHorizontally),
                            exit = slideOutVertically(
                                targetOffsetY = { -it }
                            ) + fadeOut(
                                targetAlpha = -0.3f
                            ),
                            enter = slideInVertically(
                                initialOffsetY = { -it }
                            ) + fadeIn(
                                initialAlpha = -0.3f
                            )
                        ) {
                            Button(
                                onClick = {
                                    saveAndNavToMainActivity(context, sharedViewModel)
                                },
                                modifier = modifier
                                    .wrapContentSize()
                                    .align(Alignment.CenterHorizontally),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047))
                            ) {
                                Text(
                                    "Save",
                                    modifier = modifier.padding(5.dp),
                                    color = Color.White
                                )
                                Image(
                                    painter = painterResource(R.drawable.baseline_save_24),
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(Color.White),
                                    modifier = modifier.padding(5.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun saveAndNavToMainActivity(context: Context, sharedViewModel: SharedViewModel){

    sharedViewModel.isSavingInProgress = true

    CoroutineScope(Dispatchers.IO).launch {

        delay(1000)

        var isDataSaved = true

        try{
            saveMonitorData(sharedViewModel)

        }catch (e: Exception){
            isDataSaved = false
            e.message?.let { Log.d("Room exception", it) }
            Toast.makeText(context, "Failed to save data: ${e.message}", Toast.LENGTH_LONG).show()

        }finally {

            sharedViewModel.isSavingInProgress = false

            if(isDataSaved){
                Toast.makeText(context, "Data saved!!", Toast.LENGTH_LONG).show()
                navToMainActivity(context)
            }
        }
    }
}

private fun saveMonitorData(sharedViewModel: SharedViewModel){

    db?.let {

        val monitor = Monitor(
            heartRate = sharedViewModel.heartRate,
            respiratoryRate = sharedViewModel.respiratoryRate,
            symptomNausea = sharedViewModel.symptomNausea,
            symptomHeadache = sharedViewModel.symptomHeadache,
            symptomDiarrhea = sharedViewModel.symptomDiarrhea,
            symptomSoarThroat = sharedViewModel.symptomSoarThroat,
            symptomFever = sharedViewModel.symptomFever,
            symptomMuscleAche = sharedViewModel.symptomMuscleAche,
            symptomLossOfSmellAndTaste = sharedViewModel.symptomLossOfSmellAndTaste,
            symptomCough = sharedViewModel.symptomCough,
            symptomShortnessOfBreath = sharedViewModel.symptomShortnessOfBreath,
            symptomFeelingTired = sharedViewModel.symptomFeelingTired
        )

        val dao = db?.monitorDao()
        dao?.insertMonitorWithTimestamp(monitor)
    }
}

private fun navToNextFragment(navController: NavHostController, sharedViewModel: SharedViewModel, isBackAction: Boolean = false){

    var navToFragment = ""
    if(isBackAction){
        if(currentFragment === FRAGMENT_RESPIRATORY){
            navToFragment = FRAGMENT_HEART
        }else{
            navToFragment = FRAGMENT_RESPIRATORY
        }
    }else {
        if (currentFragment === FRAGMENT_HEART) {
            navToFragment = FRAGMENT_RESPIRATORY
        } else if(currentFragment === FRAGMENT_RESPIRATORY) {
            navToFragment = FRAGMENT_SYMPTOM
        }
    }

    fragmentMap[navToFragment]?.let {

        navController.navigate(it.route) {

            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // re-selecting the same item
            launchSingleTop = true
            // Restore state when re-selecting a previously selected item
            restoreState = true

            currentFragment = navToFragment

            sharedViewModel.isLastFragment = currentFragment === FRAGMENT_SYMPTOM
        }
    }
}

private fun navToNextOuterFragment(outerNavController: NavHostController){

    val navToFragment =
        if(currentOuterFragment === FRAGMENT_MEASURE){
            FRAGMENT_CAMERA
        }else{
            FRAGMENT_MEASURE
        }

    outerFragmentMap[navToFragment]?.let {

        outerNavController.navigate(it.route) {

            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(outerNavController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // re-selecting the same item
            launchSingleTop = true
            // Restore state when re-selecting a previously selected item
            restoreState = true

            currentOuterFragment = navToFragment
        }
    }
}

@Composable
private fun HeartFragment(outerNavController: NavHostController, sharedViewModel: SharedViewModel, modifier: Modifier = Modifier){

    var heartRateValue by remember { mutableIntStateOf(0) }
    var isHeartRateProcessingValue by remember { mutableStateOf(false) }

    LaunchedEffect(sharedViewModel.heartRate) {
        heartRateValue = sharedViewModel.heartRate
    }

    LaunchedEffect(sharedViewModel.isHeartRateProcessing) {

        isHeartRateProcessingValue = sharedViewModel.isHeartRateProcessing

        if(isHeartRateProcessingValue){
            sharedViewModel.heartRate = 0
            try {
                sharedViewModel.heartRate = MonitorUtil.heartRateCalculator(sharedViewModel.uri)
            }catch(e: Exception){
                e.printStackTrace()
            }
            sharedViewModel.isHeartRateProcessing = false
            sharedViewModel.uri = Uri.EMPTY
        }
    }

    Column(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        Text(
            "Measure Heart Rate",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .padding(5.dp),
            fontSize = 20.sp
        )
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 20.dp)
        ) {
            Text(
                "Instructions",
                color = Color.Gray,
                modifier = modifier.padding(vertical = 5.dp),
                fontWeight = FontWeight.Bold
            )
            Text(
                "1. Tap on the big button to the open camera.",
                color = Color.Gray,
                modifier = modifier.padding(vertical = 2.dp),
                textAlign = TextAlign.Justify
            )
            Text(
                "2. Put a finger on the camera lens while covering the flash light.",
                color = Color.Gray,
                modifier = modifier.padding(vertical = 2.dp),
                textAlign = TextAlign.Justify
            )
            Text(
                "3. Wait for 45 seconds, then stop the recording.",
                color = Color.Gray,
                modifier = modifier.padding(vertical = 2.dp),
                textAlign = TextAlign.Justify
            )
            Text(
                "4. Wait for a moment to complete processing.",
                color = Color.Gray,
                modifier = modifier.padding(vertical = 2.dp),
                textAlign = TextAlign.Justify
            )
            Text(
                "5. Tap on Next to record the respiratory rate.",
                color = Color.Gray,
                modifier = modifier.padding(vertical = 2.dp),
                textAlign = TextAlign.Justify
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            ElevatedButton(
                onClick = {
                    navToNextOuterFragment(outerNavController)
                },
                shape = RoundedCornerShape(50.dp),
                modifier = modifier.size(200.dp)
            ) {
                Column {
                    Image(
                        painter = painterResource(R.drawable.heartrate),
                        contentDescription = null,
                        modifier = modifier.padding(10.dp).weight(1f),
                        contentScale = ContentScale.Fit
                    )
                    Text(
                        "Measure",
                        modifier = modifier.padding(5.dp)
                            .align(Alignment.CenterHorizontally),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        AnimatedVisibility(isHeartRateProcessingValue) {
            ProcessingMessage(modifier)
        }
        Text(
            heartRateValue.toString(),
            fontSize = 40.sp,
            modifier = modifier.padding(top = 10.dp, bottom = 50.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFC62828)
        )
    }
}

@Composable
private fun RespiratoryFragment(context: Context, sharedViewModel: SharedViewModel, modifier: Modifier = Modifier){

    var respiratoryRateValue by remember { mutableIntStateOf(0) }
    var isMeasuring by remember { mutableStateOf(false) }
    var isProcessing by remember { mutableStateOf(false) }
    var timeRemaining by remember { mutableIntStateOf(0) }

    LaunchedEffect(sharedViewModel.respiratoryRate) {
        respiratoryRateValue = sharedViewModel.respiratoryRate
    }

    LaunchedEffect(sharedViewModel.isRespiratoryRateMeasuring) {
        isMeasuring = sharedViewModel.isRespiratoryRateMeasuring
    }

    LaunchedEffect(sharedViewModel.isRespiratoryRateProcessing) {
        isProcessing = sharedViewModel.isRespiratoryRateProcessing
    }

    LaunchedEffect(isMeasuring) {

        if(isMeasuring){

            currentTimer = object: CountDownTimer(Constants.MEASURING_TIME, 1000){

                override fun onTick(p0: Long) {
                    timeRemaining = (p0 / 1000).toInt()
                }

                override fun onFinish() {
                    unRegisterSensorListener(sharedViewModel)
                }
            }.start()

        }else{
            stopTimer()
        }
    }

    Column(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        Text(
            "Measure Respiratory Rate",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .padding(5.dp),
            fontSize = 20.sp
        )
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 20.dp)
        ) {
            Text(
                "Instructions",
                color = Color.Gray,
                modifier = modifier.padding(vertical = 5.dp),
                fontWeight = FontWeight.Bold
            )
            Text(
                "1. Lay down facing upwards and place the phone on the chest.",
                color = Color.Gray,
                modifier = modifier.padding(vertical = 2.dp),
                textAlign = TextAlign.Justify
            )
            Text(
                "2. Tap on the big button to measure the respiratory rate.",
                color = Color.Gray,
                modifier = modifier.padding(vertical = 2.dp),
                textAlign = TextAlign.Justify
            )
            Text(
                "3. Wait for 45 seconds, then stop measuring.",
                color = Color.Gray,
                modifier = modifier.padding(vertical = 2.dp),
                textAlign = TextAlign.Justify
            )
            Text(
                "4. Wait few moments to complete processing.",
                color = Color.Gray,
                modifier = modifier.padding(vertical = 2.dp),
                textAlign = TextAlign.Justify
            )
            Text(
                "5. Tap on Next to rate the symptoms.",
                color = Color.Gray,
                modifier = modifier.padding(vertical = 2.dp),
                textAlign = TextAlign.Justify
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column(
                modifier = modifier.align(Alignment.Center)
            ) {
                ElevatedButton(
                    onClick = {
                        recordRespiratoryRate(context, sharedViewModel)
                    },
                    shape = RoundedCornerShape(50.dp),
                    modifier = modifier.size(200.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Column {
                        Image(
                            painter = painterResource(R.drawable.stethescope),
                            contentDescription = null,
                            modifier = modifier
                                .padding(10.dp)
                                .weight(1f),
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            "Measure",
                            modifier = modifier.padding(5.dp)
                                .align(Alignment.CenterHorizontally),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                AnimatedVisibility(isMeasuring) {
                    Column(
                        modifier = modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                unRegisterSensorListener(sharedViewModel)
                            },
                            colors = ButtonDefaults.buttonColors(Color(0xFFC62828)),
                            modifier = modifier.align(Alignment.CenterHorizontally)
                                .fillMaxWidth()
                                .wrapContentWidth()
                                .padding(20.dp)
                        ){
                            Text(
                                "Stop measuring",
                                color = Color.White
                            )
                        }
                        Text(
                            "Auto-stops in $timeRemaining s",
                            textAlign = TextAlign.Center,
                            modifier = modifier.background(
                                color = Color.Black.copy(0.5f),
                                RoundedCornerShape(5.dp)
                            ).align(Alignment.CenterHorizontally)
                                .padding(10.dp),
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
        AnimatedVisibility(isProcessing) {
            ProcessingMessage(modifier)
        }
        Text(
            respiratoryRateValue.toString(),
            fontSize = 40.sp,
            modifier = modifier.padding(top = 10.dp, bottom = 50.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1565C0)
        )
    }
}

@Composable
private fun ProcessingMessage(modifier: Modifier = Modifier){
    Text(
        "Processing...",
        textAlign = TextAlign.Center,
        fontSize = 20.sp,
        modifier = modifier.padding(20.dp)
            .fillMaxWidth(),
    )
}

@Composable
private fun SymptomFragment(sharedViewModel: SharedViewModel, modifier: Modifier = Modifier){

    val symptomMap = remember {
        mutableStateMapOf<String, MutableState<Float>>().apply {
            Constants.Symptom.symptomKeys.forEach { key ->
                this[key] = mutableFloatStateOf(0f)
            }
        }
    }

    LazyColumn (
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        item{
            Text(
                "Rate your symptoms",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = modifier.padding(vertical = 10.dp).fillMaxWidth()
            )
        }
        items(10){ i ->
            val symptom = symptomMap[Constants.Symptom.symptomKeys[i]]
            symptom?.let {
                Card(
                    modifier = modifier.padding(5.dp)
                ){
                    Column(
                        modifier = modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Row {
                            Text(Constants.Symptom.symptomKeys[i], modifier = modifier.weight(1f))
                            Text("${symptom.value.toInt()} / 5", textAlign = TextAlign.End)
                        }
                        Slider(
                            enabled = true,
                            value = symptom.value,
                            onValueChange = {
                                updateSymptomsData(symptomMap, i, it, sharedViewModel)
                            },
                            steps = 4,
                            valueRange = 0f..5f,
                            colors = SliderDefaults.colors(
                                inactiveTrackColor = MaterialTheme.colorScheme.inversePrimary
                            )
                        )
                    }
                }
            }
        }
    }
}

private fun updateSymptomsData(symptomMap: SnapshotStateMap<String, MutableState<Float>>, i: Int, value: Float,
                               sharedViewModel: SharedViewModel){

    val key = Constants.Symptom.symptomKeys[i]
    symptomMap[key]?.value = value

    when(key){
        "Nausea" -> sharedViewModel.symptomNausea = value.toInt()
        "Headache" -> sharedViewModel.symptomHeadache = value.toInt()
        "Diarrhea" -> sharedViewModel.symptomDiarrhea = value.toInt()
        "Soar throat" -> sharedViewModel.symptomSoarThroat = value.toInt()
        "Fever" -> sharedViewModel.symptomFever = value.toInt()
        "Muscle ache" -> sharedViewModel.symptomMuscleAche = value.toInt()
        "Loss of smell and taste" -> sharedViewModel.symptomLossOfSmellAndTaste = value.toInt()
        "Cough" -> sharedViewModel.symptomCough = value.toInt()
        "Shortness of breath" -> sharedViewModel.symptomShortnessOfBreath = value.toInt()
        "Feeling tired" -> sharedViewModel.symptomFeelingTired = value.toInt()
    }
}

sealed class Fragments(val route: String){
    data object HeartFragment: Fragments(FRAGMENT_HEART)
    data object RespiratoryFragment: Fragments(FRAGMENT_RESPIRATORY)
    data object SymptomFragment: Fragments(FRAGMENT_SYMPTOM)
}

private val fragmentMap = mapOf(
    FRAGMENT_HEART to Fragments.HeartFragment,
    FRAGMENT_RESPIRATORY to Fragments.RespiratoryFragment,
    FRAGMENT_SYMPTOM to Fragments.SymptomFragment
)

private var currentFragment = FRAGMENT_HEART

sealed class OuterFragments(val route: String){
    data object MeasureFragment: OuterFragments(FRAGMENT_MEASURE)
    data object CameraFragment: OuterFragments(FRAGMENT_CAMERA)
}

private val outerFragmentMap = mapOf(
    FRAGMENT_MEASURE to OuterFragments.MeasureFragment,
    FRAGMENT_CAMERA to OuterFragments.CameraFragment
)

private var currentOuterFragment = FRAGMENT_MEASURE

@Composable
@Preview(showBackground = true)
fun MeasurePreview(modifier: Modifier = Modifier) {
    RespiratoryFragment(LocalContext.current, viewModel())
}

var currentTimer: CountDownTimer? = null

fun stopTimer(){
    currentTimer?.cancel()
}

@Composable
fun CameraFragment(sharedViewModel: SharedViewModel, outerNavController: NavHostController, modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var isRecording by remember { mutableStateOf(false) }
    var cameraMessage by remember { mutableStateOf("Start") }

    val cameraController = remember {
        LifecycleCameraController(context).apply {
            bindToLifecycle(lifecycleOwner)
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE or
                        CameraController.VIDEO_CAPTURE
            )
        }
    }

    LaunchedEffect(isRecording) {

        if(isRecording) {

            currentTimer = object : CountDownTimer(Constants.MEASURING_TIME, 1000) {

                override fun onTick(p0: Long) {
                    cameraMessage = "Auto-stops in ${(p0 / 1000).toInt()} s"
                }

                override fun onFinish() {
                    cameraMessage = "Processing..."
                    isRecording = !isRecording
                    captureVideo(context, cameraController, sharedViewModel, outerNavController)
                }
            }.start()

        }else{
            stopTimer()
        }
    }

    cameraController.enableTorch(true)

    Box(
        modifier = modifier.fillMaxSize()
    ){
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                PreviewView(ctx).apply {
                    scaleType = PreviewView.ScaleType.FILL_START
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    controller = cameraController
                }
            },
            onRelease = {
                cameraController.unbind()
            }
        )
        Column(
            modifier = modifier.align(Alignment.BottomCenter)
        ) {
            Text(
                cameraMessage,
                textAlign = TextAlign.Center,
                modifier = modifier.background(
                    color = Color.Black.copy(0.5f),
                    RoundedCornerShape(5.dp)
                ).align(Alignment.CenterHorizontally)
                    .padding(10.dp),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            AnimatedVisibility(
                !isRecording,
                modifier = modifier.align(Alignment.CenterHorizontally)
            ) {
                IconButton(
                    onClick = {
                        isRecording = !isRecording
                        captureVideo(context, cameraController, sharedViewModel, outerNavController)
                    },
                    modifier = modifier.align(Alignment.CenterHorizontally)
                        .padding(30.dp)
                        .background(Color.White, CircleShape)
                        .size(70.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_camera_24),
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = modifier.size(50.dp)
                            .padding(2.dp)
                    )
                }
            }
            AnimatedVisibility(
                isRecording,
                modifier = modifier.align(Alignment.CenterHorizontally)
            ) {
                IconButton(
                    onClick = {
                        isRecording = !isRecording
                        captureVideo(context, cameraController, sharedViewModel, outerNavController)
                    },
                    modifier = modifier.align(Alignment.CenterHorizontally)
                        .padding(30.dp)
                        .background(Color.White, CircleShape)
                        .size(70.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_stop_24),
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = modifier.size(50.dp)
                            .padding(2.dp)
                    )
                }
            }
        }
    }
}

@SuppressLint("MissingPermission")
fun captureVideo(context: Context, cameraController: LifecycleCameraController,
                 sharedViewModel: SharedViewModel, outerNavController: NavHostController) {

    if(recording != null){
        recording?.stop()
        recording = null
        return
    }

    if(!hasRequiredPermissions(context)){
        return
    }

    val file = File(context.filesDir, "heartRateRecording.mp4")

    recording = cameraController.startRecording(
        FileOutputOptions.Builder(file).build(),
        AudioConfig.create(false),
        ContextCompat.getMainExecutor(context)
    ){ event ->
        when (event){
            is VideoRecordEvent.Finalize -> {

                navToNextOuterFragment(outerNavController)

                recording?.close()
                recording = null

                if(event.hasError()){
                    Toast.makeText(context, "Error occurred while recording", Toast.LENGTH_LONG).show()

                }else{
                    Toast.makeText(context, "Heart rate processing. Please wait...", Toast.LENGTH_LONG).show()
                    sharedViewModel.uri = event.outputResults.outputUri
                    sharedViewModel.isHeartRateProcessing = true
                }
            }
        }
    }
}

private fun hasRequiredPermissions(context: Context): Boolean{
    return CAMERAX_PERMISSION.all {
        ContextCompat.checkSelfPermission(
            context,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }
}

private fun recordRespiratoryRate(context: Context, sharedViewModel: SharedViewModel){

    sharedViewModel.respiratoryRate = 0
    sharedViewModel.isRespiratoryRateMeasuring = true
    sharedViewModel.isRespiratoryRateProcessing = false

    if(sensorManager == null) {
        sensorManager = getSystemService(context, SensorManager::class.java)
    }

    sensorEventListener?.let {
        val sensor: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager?.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }
}

private fun unRegisterSensorListener(sharedViewModel: SharedViewModel){

    sharedViewModel.isRespiratoryRateMeasuring = false
    sharedViewModel.isRespiratoryRateProcessing = true

    sensorManager?.unregisterListener(sensorEventListener)

    sharedViewModel.respiratoryRate = MonitorUtil.respiratoryRateCalculator(
        respRateListX, respRateListY, respRateListZ
    )

    sharedViewModel.isRespiratoryRateProcessing = false
}
