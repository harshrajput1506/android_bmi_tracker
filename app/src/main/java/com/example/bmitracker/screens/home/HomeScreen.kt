package com.example.bmitracker.screens.home

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bmitracker.R
import com.example.bmitracker.models.WeightRecord
import com.example.bmitracker.ui.theme.DMSansFamily
import com.example.bmitracker.utils.Constants
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    goToProfile : () -> Unit
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp)
    ) {
        
        val user = viewModel.currentUser
        val state = viewModel.state.value
        val category = viewModel.categorizeBMI(bmi = state.profile.bmi.toDouble())
        TitleBar(imageUrl = user?.photoUrl!!, name = user.displayName ?: "master")
        Spacer(modifier = Modifier.height(48.dp))
        BMILayout(
            bmi = state.profile.bmi,
            lastUpdated = state.profile.lastUpdated ?: "",
            category = category,
            color = viewModel.bmiColor(category)
        )
        Spacer(modifier = Modifier.height(36.dp))
        ProfileLayout(
            height = state.profile.height,
            age = state.profile.age,
        ){
            goToProfile()
        }
        Spacer(modifier = Modifier.height(24.dp))
        WeightLayout(
            weight = state.profile.weight,
            records = state.profile.weightRecord
        )
    }
}

@Composable
fun BMILayout(bmi : String, lastUpdated : String, category : String, color: Color){
    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "your BMI",
            fontSize = 14.sp,
            fontFamily = DMSansFamily,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = bmi,
            fontSize = 48.sp,
            fontFamily = DMSansFamily,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = category,
                fontSize = 18.sp,
                fontFamily = DMSansFamily,
                fontWeight = FontWeight.Medium,
                color = color
            )
            Spacer(modifier = Modifier.width(4.dp))
            Image(
                painter = painterResource(id = R.drawable.info),
                contentDescription = "BMI Info",
                modifier = Modifier
                    .size(16.dp)
                    .clickable { },
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.clock),
                contentDescription = "Last update",
                modifier = Modifier.size(12.dp),
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "last updated at $lastUpdated",
                fontSize = 12.sp,
                fontFamily = DMSansFamily,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

    }
}

@Composable
fun ProfileLayout(height : String, age : String, goToProfile : () -> Unit){
    Column  (
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(40.dp)
            )
            .padding(top = 20.dp, bottom = 4.dp, start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column {
                Text(
                    text = "height",
                    fontSize = 14.sp,
                    fontFamily = DMSansFamily,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "$height cm",
                    fontSize = 18.sp,
                    fontFamily = DMSansFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Column {
                Text(
                    text = "age",
                    fontSize = 14.sp,
                    fontFamily = DMSansFamily,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "$age years",
                    fontSize = 18.sp,
                    fontFamily = DMSansFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        
        Spacer(modifier = Modifier.height(4.dp))

        TextButton(onClick = { goToProfile() }) {
            Text(
                text = "update profile",
                fontFamily = DMSansFamily
            )
        }
    }
}

@Composable
fun WeightLayout(weight : String, records : List<WeightRecord>?){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(40.dp)
            )
            .padding(top = 20.dp, bottom = 20.dp, start = 30.dp, end = 30.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "weight",
            fontSize = 14.sp,
            fontFamily = DMSansFamily,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "$weight kg",
            fontSize = 18.sp,
            fontFamily = DMSansFamily,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth().height(120.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            records?.let {
                if(it.isNotEmpty() && it.size > 1) ShowChart(it)
                else {
                    Text(
                        text = "no weight records",
                        fontSize = 14.sp,
                        fontFamily = DMSansFamily,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }

    }
}

@Composable
fun TitleBar(imageUrl : Uri, name : String) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Card (
            shape = RoundedCornerShape(10.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Image from URL",
                modifier = Modifier
                    .size(40.dp), // Adjust size as needed
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.avatar), // Optional error placeholder
                placeholder = painterResource(id = R.drawable.avatar) // Optional loading placeholder
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "hi, ${name.lowercase()}"+Constants.wave_emoji,
            color = MaterialTheme.colorScheme.onSurface,
            fontFamily = DMSansFamily,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun UserDialog(){

}


@Composable
fun InteractiveLineChart(points : List<Offset>, records : List<WeightRecord>) {
    val oldPoints = listOf(
        Offset(50f, 300f),
        Offset(150f, 100f),
        Offset(250f, 250f),
        Offset(350f, 50f),
        Offset(450f, 200f)
    )
    var selectedPoint by remember { mutableStateOf<Offset?>(null) }
    var selectedRecord by remember { mutableStateOf<WeightRecord?>(null) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        val color = MaterialTheme.colorScheme.tertiary
        val circleColor = MaterialTheme.colorScheme.onBackground

        Canvas(modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    selectedPoint = points.minByOrNull { (it - offset).getDistanceSquared() }
                    selectedRecord = records[points.indexOf(selectedPoint)]
                }
            }
        ) {
            val path = Path().apply {
                moveTo(points.first().x, points.first().y)
                for (i in 1 until points.size) {
                    val midPointX = (points[i].x + points[i - 1].x) / 2
                    val midPointY = (points[i].y + points[i - 1].y) / 2
                    quadraticTo(points[i - 1].x, points[i - 1].y, midPointX, midPointY)
                    quadraticTo(midPointX, midPointY, points[i].x, points[i].y)
                }
            }

            drawPath(
                path = path,
                color = color,
                style = Stroke(
                    width = 4.dp.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round,
                    pathEffect = PathEffect.cornerPathEffect(50f)
                )
            )

            points.forEach { point ->
                drawCircle(
                    color = circleColor,
                    radius = 16f,
                    center = Offset(point.x, point.y)
                )
            }
        } // End of Canvas

        // Tooltip outside of Canvas scope
        selectedRecord?.let { record ->
            Box(
                modifier = Modifier
                    .offset {
                        val index = records.indexOf(record)
                        val offsetX = points[index].x
                        val offsetY = points[index].y
                        IntOffset(offsetX.roundToInt() + 10, offsetY.roundToInt() - 30)
                    }
                    .background(
                        MaterialTheme.colorScheme.tertiaryContainer,
                        RoundedCornerShape(16.dp)
                    )
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = "${record.weight} kg",
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        fontFamily = DMSansFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "at ${record.date}",
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        fontFamily = DMSansFamily,
                        fontSize = 10.sp
                    )
                }

            }
        }
    } // End of Box
}

@Composable
fun ShowChart(data : List<WeightRecord>){

    val density = LocalDensity.current
    val widthInPx = with(density) { 200.dp.toPx() }
    val heightInPx = with(density) { 100.dp.toPx() }
    InteractiveLineChart(weightRecordsToOffsets(data, widthInPx, heightInPx), data)

}
fun weightRecordsToOffsets(
    data: List<WeightRecord>,
    width: Float, // Width of the chart area
    height: Float // Height of the chart area
): List<Offset> {
    val sortedData = data.sortedBy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val dateFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")
            LocalDate.parse(it.date, dateFormatter)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
    } // Sort by date

    val minWeight = sortedData.minOf { it.weight?.toDouble() ?: 0.0 }
    val maxWeight = sortedData.maxOf { it.weight?.toDouble() ?: 0.0 }

    return sortedData.mapIndexed { index, record ->
        val x = index * width / (data.size - 1f) // Evenly space along X-axis
        val normalizedWeight = ((record.weight?.toDouble() ?: 0.0)  - minWeight) / (maxWeight - minWeight)
        val y = height - (normalizedWeight * height).toFloat()  // Map to Y-axis

        Offset(x, y)
    }
}
