package com.example.diashieldcse535

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.diashieldcse535.db.Databases
import com.example.diashieldcse535.db.Monitor
import com.example.diashieldcse535.ui.theme.DiaShieldCSE535Theme
import com.example.diashieldcse535.utils.Constants

private var db: Databases? = null

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(
            this.applicationContext,
            Databases::class.java,
            Constants.DATABASE_NAME
        ).allowMainThreadQueries().build()

        val monitor = getMonitorData()

        setContent {
            Home(monitor)
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun Home(monitor: Monitor?){
    DiaShieldCSE535Theme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .safeDrawingPadding()
        ) {
            Content(monitor)
        }
    }
}

@Composable
private fun Content(monitor: Monitor?, modifier: Modifier = Modifier){

    val context = LocalContext.current

    val symptomMap = mutableMapOf<String, Int>().apply {
        Constants.Symptom.symptomKeys.forEach { key ->
            val value = when (key) {
                "Nausea" -> monitor?.symptomNausea ?: 0
                "Headache" -> monitor?.symptomHeadache ?: 0
                "Diarrhea" -> monitor?.symptomDiarrhea ?: 0
                "Soar throat" -> monitor?.symptomSoarThroat ?: 0
                "Fever" -> monitor?.symptomFever ?: 0
                "Muscle ache" -> monitor?.symptomMuscleAche ?: 0
                "Loss of smell and taste" -> monitor?.symptomLossOfSmellAndTaste ?: 0
                "Cough" -> monitor?.symptomCough ?: 0
                "Shortness of breath" -> monitor?.symptomShortnessOfBreath ?: 0
                "Feeling tired" -> monitor?.symptomFeelingTired ?: 0
                else -> 0 // Default value for unknown symptoms
            }
            put(key, value)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 10.dp, horizontal = 20.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.logoimage),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = modifier.fillMaxWidth()
                .size(250.dp)
        )
        Card(
            modifier = modifier.weight(1f)){
            LazyColumn (
                modifier = modifier
                    .weight(1f)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item{
                    Row{
                        Text(
                            "Heart rate",
                            modifier = modifier.weight(1f)
                        )
                        Text(
                            monitor?.heartRate.toString(),
                            textAlign = TextAlign.End
                        )
                    }
                }
                item {
                    HorizontalDivider(thickness = 1.dp)
                }
                item{
                    Row{
                        Text(
                            "Respiratory rate",
                            modifier = modifier.weight(1f)
                        )
                        Text(
                            monitor?.respiratoryRate.toString(),
                            textAlign = TextAlign.End
                        )
                    }
                }
                item {
                    HorizontalDivider(thickness = 1.dp)
                }
                item{
                    Text("Symptoms")
                }
                items(count = Constants.Symptom.symptomKeys.size){ i ->
                    val key = Constants.Symptom.symptomKeys[i]
                    Column(
                        modifier = modifier.padding(horizontal = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row {
                            Text(
                                key,
                                modifier = modifier.weight(1f)
                            )
                            Text(
                                "${symptomMap[key]} / 5",
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }
            }
        }
        Button(
            onClick = {
                context.startActivity(Intent(context, MeasureActivity::class.java))
            },
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 10.dp)
        ) {
            Text(
                "Measure",
                fontWeight = FontWeight.Bold,
                color = Color.White
            ) //todo -- go to next screen
        }
    }
}

private fun getMonitorData(): Monitor?{

    var monitor: Monitor? = null

    db?.let {

        val dao = db?.monitorDao()

        monitor = dao?.findLast()
    }

    return monitor
}

@Preview(showBackground = true)
@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun MainPreview() {
}