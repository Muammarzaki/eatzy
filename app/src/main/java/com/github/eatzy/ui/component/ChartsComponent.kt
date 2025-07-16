package com.github.eatzy.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalorieTrackerChart(
    modifier: Modifier = Modifier,
    wasted: Float = 1500f,
    mitigated: Float = 300f,
    score: Float = 30f
) {
    val total = wasted + mitigated + score
    val wastedPercentage = (wasted / total) * 100
    val mitigatedPercentage = (mitigated / total) * 100
    val scorePercentage = (score / total) * 100
    val wastedSweepAngle = (wastedPercentage / 100) * 360f
    val mitigatedSweepAngle = (mitigatedPercentage / 100) * 360f
    val scoreSweepAngle = (scorePercentage / 100) * 360f
    val animatedWasted = remember { Animatable(0f) }
    val animatedMitigated = remember { Animatable(0f) }
    val animatedScore = remember { Animatable(0f) }
    LaunchedEffect(key1 = wastedSweepAngle) {
        animatedWasted.animateTo(
            targetValue = wastedSweepAngle,
            animationSpec = tween(durationMillis = 1000)
        )
    }
    LaunchedEffect(key1 = mitigatedSweepAngle) {
        animatedMitigated.animateTo(
            targetValue = mitigatedSweepAngle,
            animationSpec = tween(durationMillis = 1000, delayMillis = 500)
        )
    }
    LaunchedEffect(key1 = scoreSweepAngle) {
        animatedScore.animateTo(
            targetValue = scoreSweepAngle,
            animationSpec = tween(durationMillis = 1000, delayMillis = 1000)
        )
    }
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(MaterialTheme.colorScheme.primary, Color.White),
                    startY = 0f,
                    endY = 500f
                )
            )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Macros", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Card(
                    shape = RoundedCornerShape(50),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Text(
                        text = "Calories",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(200.dp)
            ) {
                val baseStrokeWidth = 15f
                val ringSpacing = 30f
                val gapAngle = 5f
                Canvas(modifier = Modifier.size(200.dp)) {
                    val canvasSize = size.minDimension
                    val backgroundRingOffset = 0f
                    val backgroundRingSize = canvasSize - 2 * backgroundRingOffset - baseStrokeWidth
                    drawArc(
                        color = Color(0xFFE0E0E0),
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = baseStrokeWidth, cap = StrokeCap.Round),
                        topLeft = Offset(
                            backgroundRingOffset + baseStrokeWidth / 2,
                            backgroundRingOffset + baseStrokeWidth / 2
                        ),
                        size = Size(backgroundRingSize, backgroundRingSize)
                    )
                    val scoreRingOffset = backgroundRingOffset + ringSpacing * 2
                    val scoreRingSize = canvasSize - 2 * scoreRingOffset - baseStrokeWidth
                    val scoreStartAngle = -90f // Starts at the top
                    drawArc(
                        color = Color(0xFFD32F2F), // Red for Score
                        startAngle = scoreStartAngle,
                        sweepAngle = animatedScore.value,
                        useCenter = false,
                        style = Stroke(width = baseStrokeWidth, cap = StrokeCap.Round),
                        topLeft = Offset(
                            scoreRingOffset + baseStrokeWidth / 2,
                            scoreRingOffset + baseStrokeWidth / 2
                        ),
                        size = Size(scoreRingSize, scoreRingSize)
                    )
                    val mitigatedRingOffset =
                        backgroundRingOffset + ringSpacing // One step inward from Score
                    val mitigatedSize = canvasSize - 2 * mitigatedRingOffset - baseStrokeWidth
                    val mitigatedStartAngle = scoreStartAngle + animatedScore.value + gapAngle
                    drawArc(
                        color = Color(0xFFFFA000), // Orange
                        startAngle = mitigatedStartAngle,
                        sweepAngle = animatedMitigated.value,
                        useCenter = false,
                        style = Stroke(width = baseStrokeWidth, cap = StrokeCap.Round),
                        topLeft = Offset(
                            mitigatedRingOffset + baseStrokeWidth / 2,
                            mitigatedRingOffset + baseStrokeWidth / 2
                        ),
                        size = Size(mitigatedSize, mitigatedSize)
                    )
                    val wastedRingOffset = backgroundRingOffset // Smallest offset for data rings
                    val wastedRingSize = canvasSize - 2 * wastedRingOffset - baseStrokeWidth
                    val wastedStartAngle = mitigatedStartAngle + animatedMitigated.value + gapAngle
                    drawArc(
                        color = Color(0xFF1976D2), // Blue for Wasted
                        startAngle = wastedStartAngle,
                        sweepAngle = animatedWasted.value,
                        useCenter = false,
                        style = Stroke(width = baseStrokeWidth, cap = StrokeCap.Round),
                        topLeft = Offset(
                            wastedRingOffset + baseStrokeWidth / 2,
                            wastedRingOffset + baseStrokeWidth / 2
                        ),
                        size = Size(wastedRingSize, wastedRingSize)
                    )
                }
                Text(
                    text = wasted.toInt().toString(),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ChartLegend(
                    color = Color(0xFF1976D2),
                    text = "Wasted",
                    value = wasted.toInt().toString()
                )
                ChartLegend(
                    color = Color(0xFFFFA000),
                    text = "Mitigated",
                    value = mitigated.toInt().toString()
                )
                ChartLegend(
                    color = Color(0xFFD32F2F),
                    text = "Score",
                    value = score.toInt().toString()
                )
            }
        }
    }
}

@Composable
fun ChartLegend(color: Color, text: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Canvas(modifier = Modifier.size(12.dp)) {
            drawRect(color = color, size = Size(size.width, size.height))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = text, fontSize = 14.sp, color = Color.Gray)
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun LegendItem(color: Color, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        Text(
            text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun KeyMetricsDonutCard(modifier: Modifier = Modifier) {
    val metrics = listOf(
        Triple(0.45f, Color(0xFF8A2BE2), "Key title goes here"), // Ungu
        Triple(0.30f, Color(0xFFFFA500), "Key title goes here"), // Oranye
        Triple(0.25f, Color(0xFF1E90FF), "Key title goes here")  // Biru
    )
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier.size(200.dp) // Maintain consistent size
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Donut Chart
            Box(
                modifier = Modifier.size(120.dp), // Adjusted for consistency
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    var startAngle = -90f
                    metrics.forEach { (percentage, color, _) ->
                        val sweepAngle = percentage * 360f
                        drawArc(
                            color = color,
                            startAngle = startAngle,
                            sweepAngle = sweepAngle - 2f, // Sedikit celah antar segmen
                            useCenter = false,
                            style = Stroke(width = 35f)
                        )
                        startAngle += sweepAngle
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                metrics.forEach { (percentage, color, label) ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(color, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.DarkGray,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "${(percentage * 100).toInt()}%",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FoodProgressCard(modifier: Modifier = Modifier) {
    val foodItems = listOf(
        FoodDataMetric(
            "Yogurt",
            648,
            751,
            Brush.horizontalGradient(listOf(Color(0xFF81FBB8), Color(0xFF28C76F)))
        ), FoodDataMetric(
            "Donut",
            215,
            651,
            Brush.horizontalGradient(listOf(Color(0xFFF7971E), Color(0xFFFFD200)))
        ), FoodDataMetric(
            "Nasgor",
            84,
            120,
            Brush.horizontalGradient(listOf(Color(0xFF4facfe), Color(0xFF00f2fe)))
        )
    )
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier.size(200.dp) // Maintain consistent size
    ) {
        Column(
            modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            foodItems.forEach { food ->
                FoodProgressItem(food)
            }
        }
    }
}

@Composable
fun FoodProgressItem(data: FoodDataMetric) {
    val progress = data.current.toFloat() / data.max
    val animatedProgress =
        animateFloatAsState(targetValue = progress, animationSpec = tween(1000)).value
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${data.current} of ${data.max}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Text(
                text = data.name,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .height(10.dp)
                .fillMaxWidth()
                .background(Color.LightGray.copy(alpha = 0.3f), CircleShape)
        ) {
            Box(
                modifier = Modifier
                    .height(10.dp)
                    .fillMaxWidth(animatedProgress)
                    .background(data.brush, CircleShape)
            )
        }
    }
}

data class FoodDataMetric(val name: String, val current: Int, val max: Int, val brush: Brush)

@Composable
fun MonthlyTrendCard(modifier: Modifier = Modifier) {
    val trendData = listOf(
        78f, 65f, 70f, 55f, 40f, 42f, 15f
    )
    val months = listOf("Jan", "Feb", "March", "Apr", "May", "June", "July")
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier.size(200.dp) // Maintain consistent size
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Macros", style = MaterialTheme.typography.titleMedium, color = Color.Black)
                Card(
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFC8E6C9))
                ) {
                    Text(
                        text = "Calories",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Line Chart
            val path = Path()
            val animatedProgress =
                animateFloatAsState(targetValue = 1f, animationSpec = tween(1500)).value
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                val maxVal = 100f
                val minVal = 0f
                val xStep = size.width / (trendData.size - 1)
                trendData.forEachIndexed { index, value ->
                    val x = index * xStep
                    val y =
                        size.height * (1 - (value - minVal) / (maxVal - minVal)) * animatedProgress
                    if (index == 0) {
                        path.moveTo(x, y)
                    } else {
                        path.lineTo(x, y)
                    }
                }
                (0..2).forEach { i ->
                    val y = size.height * (1 - (i * 50f) / maxVal)
                    drawLine(
                        color = Color.LightGray.copy(alpha = 0.5f),
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                    )
                }
                drawPath(
                    path = path,
                    color = Color.Blue,
                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                )
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                months.forEach { month ->
                    Text(month, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalorieTrackerChartPreview() {
    MaterialTheme {
        Surface(color = Color(0xFF1E1E1E), contentColor = Color.White) {
            CalorieTrackerChart(modifier = Modifier.aspectRatio(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KeyMetricsDonutCardPreview() {
    MaterialTheme {
        Surface(color = Color(0xFF1E1E1E), contentColor = Color.White) {
            KeyMetricsDonutCard(modifier = Modifier.aspectRatio(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MonthlyTrendCardPreview() {
    MaterialTheme {
        Surface(color = Color(0xFF1E1E1E), contentColor = Color.White) {
            MonthlyTrendCard(modifier = Modifier.aspectRatio(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FoodProgressCardPreview() {
    MaterialTheme {
        Surface(color = Color(0xFF1E1E1E), contentColor = Color.White) {
            FoodProgressCard(modifier = Modifier.aspectRatio(1f))
        }
    }
}

