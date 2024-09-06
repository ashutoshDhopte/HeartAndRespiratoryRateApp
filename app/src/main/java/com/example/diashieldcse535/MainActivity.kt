package com.example.diashieldcse535

import android.annotation.SuppressLint
import android.app.StatusBarManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.diashieldcse535.ui.theme.DiaShieldCSE535Theme
import com.example.diashieldcse535.ui.theme.Purple40
import com.example.diashieldcse535.ui.theme.Purple80

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            Home()
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Home(){
    DiaShieldCSE535Theme(
        darkTheme = false,
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .safeDrawingPadding()
        ) {
            Content()
        }
    }
}

@Composable
fun Content(modifier: Modifier = Modifier){

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 10.dp, horizontal = 20.dp)
    ) {
        Card(
            modifier = modifier.weight(1f)){
            Column(
                modifier = modifier
                    .weight(1f)
                    .padding(20.dp)
                    .verticalScroll(
                        rememberScrollState(),
                        true
                    ),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row{
                    Text(
                        "Heart rate",
                        modifier = modifier.weight(1f)
                    )
                    Text(//todo -- value from db
                        "3456",
                        textAlign = TextAlign.End
                    )
                }
                HorizontalDivider(thickness = 1.dp)
                Row{
                    Text(
                        "Respiratory rate",
                        modifier = modifier.weight(1f)
                    )
                    Text(//todo -- value from db
                        "3456",
                        textAlign = TextAlign.End
                    )
                }
                HorizontalDivider(thickness = 1.dp)
                Text("Symptoms / 5*")
                Column(
                    modifier = modifier.padding(horizontal = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    repeat(10) {
                        Row { //todo -- this will be a list and value from db
                            Text(
                                "Dizziness",
                                modifier = modifier.weight(1f)
                            )
                            Text(
                                "3*",
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }
            }
        }
        Button(
            onClick = {},
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 10.dp)
        ) {
            Text(
                "Measure",
                fontWeight = FontWeight.Bold
            ) //todo -- go to next screen
        }
    }
}

@Preview(showBackground = true)
@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun Preview() {
    Home()
}