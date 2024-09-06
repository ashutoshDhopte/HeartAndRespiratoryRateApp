package com.example.diashieldcse535

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.diashieldcse535.ui.theme.DiaShieldCSE535Theme

class MeasureActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {

            Home()
        }
    }
}

private const val FRAGMENT_HEART = "heart"
private const val FRAGMENT_RESPIRATORY = "respiratory"
private const val FRAGMENT_SYMPTOM = "symptom"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Home(){

    val navController = rememberNavController()
    val context = LocalContext.current

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
                                backAction(context, navController)
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
            MeasureContent(context, navController, innerModifier = Modifier.padding(innerPadding))
        }
    }
}

private fun backAction(context: Context, navController: NavHostController){

    Toast.makeText(context, currentFragment, Toast.LENGTH_LONG).show()

    if(currentFragment === FRAGMENT_HEART){
        navToMainActivity(context)

    }else{
        navToNextFragment(navController, true)
    }
}

private fun navToMainActivity(context: Context){
    context.startActivity(Intent(context, MainActivity::class.java)
        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    )
}

@Composable
private fun MeasureContent(context: Context, navController: NavHostController, innerModifier: Modifier, modifier: Modifier = Modifier){

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
                HeartFragment(context)
            }
            composable(Fragments.RespiratoryFragment.route){
                RespiratoryFragment(context)
            }
            composable(Fragments.SymptomFragment.route){
                SymptomFragment(context)
            }
        }
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = modifier
                .wrapContentHeight()
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = modifier
                    .fillMaxWidth()
                    .height(100.dp)
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
                        "4567",
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
                        "4567",
                        modifier = modifier.padding(5.dp)
                    )
                }
                Button(
                    onClick = {
                        Toast.makeText(context, currentFragment, Toast.LENGTH_LONG).show()
                        navToNextFragment(navController)
                    },
                    modifier = modifier
                        .weight(1f)
                        .wrapContentSize()
                        .align(Alignment.CenterVertically)
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
        }
    }
}

private fun navToNextFragment(navController: NavHostController, isBackAction: Boolean = false){

    val navToFragment =
        if(isBackAction){
            if(currentFragment === FRAGMENT_RESPIRATORY){
                FRAGMENT_HEART
            }else{
                FRAGMENT_RESPIRATORY
            }
        }else {
            if (currentFragment === FRAGMENT_HEART) {
                FRAGMENT_RESPIRATORY
            } else {
                FRAGMENT_SYMPTOM
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
        }
    }
}

@Composable
private fun HeartFragment(context: Context, modifier: Modifier = Modifier){

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
                .padding(10.dp)
        ) {
            Text(
                "Instructions",
                color = Color.Gray,
                modifier = modifier.padding(vertical = 5.dp),
                fontWeight = FontWeight.Bold
            )
            repeat(5) {
                Text(
                    "Instructions dfghj fghj fghj ghj",
                    color = Color.Gray,
                    modifier = modifier.padding(vertical = 2.dp)
                )
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            ElevatedButton(
                onClick = {
                    Toast.makeText(context, "Clicked my Heart", Toast.LENGTH_LONG).show()
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
        Text(
            "4567",
            fontSize = 30.sp,
            modifier = modifier.padding(top = 10.dp, bottom = 50.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun RespiratoryFragment(context: Context, modifier: Modifier = Modifier){

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
                .padding(10.dp)
        ) {
            Text(
                "Instructions",
                color = Color.Gray,
                modifier = modifier.padding(vertical = 5.dp),
                fontWeight = FontWeight.Bold
            )
            repeat(5) {
                Text(
                    "Instructions dfghj fghj fghj ghj",
                    color = Color.Gray,
                    modifier = modifier.padding(vertical = 2.dp)
                )
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            ElevatedButton(
                onClick = {
                    Toast.makeText(context, "Clicked my Lungs", Toast.LENGTH_LONG).show()
                },
                shape = RoundedCornerShape(50.dp),
                modifier = modifier.size(200.dp)
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
        }
        Text(
            "4567",
            fontSize = 30.sp,
            modifier = modifier.padding(top = 10.dp, bottom = 50.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SymptomFragment(context: Context, modifier: Modifier = Modifier){

    val symptomKeys = listOf(
        "Nausea", "Headache", "Diarrhea", "Soar throat",
        "Fever", "Muscle ache", "Loss of smell and taste",
        "Cough", "Shortness of breath", "Feeling tired"
    )

    val symptomMap = remember {
        mutableStateMapOf<String, MutableState<Float>>().apply {
            symptomKeys.forEach { key ->
                this[key] = mutableStateOf(0f)
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
            val symptom = symptomMap[symptomKeys[i]]
            symptom?.let {
                Card(
                    modifier = modifier.padding(5.dp)
                ){
                    Column(
                        modifier = modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Row {
                            Text(symptomKeys[i], modifier = modifier.weight(1f))
                            Text("${symptom.value.toInt()} / 5", textAlign = TextAlign.End)
                        }
                        Slider(
                            enabled = true,
                            value = symptom.value,
                            onValueChange = { symptomMap[symptomKeys[i]]?.value = it },
                            steps = 5,
                            valueRange = 1f..5f
                        )
                    }
                }
            }
        }
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

@Preview(showBackground = true)
@Composable
fun MeasurePreview() {

    SymptomFragment(LocalContext.current)
}