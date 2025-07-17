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
    remaining: Float = 1500f,
    mitigated: Float = 3000f,
    lost: Float = 30f
) {
    val total = remaining + mitigated + lost
    val lostPercentage = (lost / total) * 100
    val mitigatedPercentage = (mitigated / total) * 100
    val remainingPercentage = (remaining / total) * 100
    val wastedSweepAngle = (lostPercentage / 100) * 360f
    val mitigatedSweepAngle = (mitigatedPercentage / 100) * 360f
    val remainingSweepAngle = (remainingPercentage / 100) * 360f
    val animatedWasted = remember { Animatable(0f) }
    val animatedMitigated = remember { Animatable(0f) }
    val animatedScore = remember { Animatable(0f) }

    val score = if (total != 0f) (mitigated / total) * 100 else 0f

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
    LaunchedEffect(key1 = remainingSweepAngle) {
        animatedScore.animateTo(
            targetValue = remainingSweepAngle,
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
                brush = Brush.radialGradient(
                    colors = listOf(MaterialTheme.colorScheme.primary, Color.White),
                )
            )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(220.dp)
            ) {
                val baseStrokeWidth = 25f
                val ringSpacing = 30f
                val gapAngle = 7f

                Canvas(modifier = Modifier.fillMaxSize()) {

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

                    val lv1Offset = backgroundRingOffset + ringSpacing * 2
                    val lv1Size = canvasSize - 2 * lv1Offset - baseStrokeWidth
                    val lv1Angle = -90f

                    val lv2Offset = backgroundRingOffset + ringSpacing
                    val lv2Size = canvasSize - 2 * lv2Offset - baseStrokeWidth
                    val lv2Angle = lv1Angle + animatedScore.value + gapAngle

                    val lv3Offset = backgroundRingOffset
                    val lv3Size = canvasSize - 2 * lv3Offset - baseStrokeWidth
                    val lv3Angle = lv2Angle + animatedMitigated.value + gapAngle

                    drawArc(
                        color = Color(0xFF1976D2),
                        startAngle = lv3Angle,
                        sweepAngle = animatedScore.value,
                        useCenter = false,
                        style = Stroke(width = baseStrokeWidth, cap = StrokeCap.Round),
                        topLeft = Offset(
                            lv3Offset + baseStrokeWidth / 2,
                            lv3Offset + baseStrokeWidth / 2
                        ),
                        size = Size(lv3Size, lv3Size)
                    )

                    drawArc(
                        color = Color(0xFFFFA000),
                        startAngle = lv2Angle,
                        sweepAngle = animatedMitigated.value,
                        useCenter = false,
                        style = Stroke(width = baseStrokeWidth, cap = StrokeCap.Round),
                        topLeft = Offset(
                            lv2Offset + baseStrokeWidth / 2,
                            lv2Offset + baseStrokeWidth / 2
                        ),
                        size = Size(lv2Size, lv2Size)
                    )

                    drawArc(
                        color = Color(0xFFD32F2F),
                        startAngle = lv1Angle,
                        sweepAngle = animatedWasted.value,
                        useCenter = false,
                        style = Stroke(width = baseStrokeWidth, cap = StrokeCap.Round),
                        topLeft = Offset(
                            lv1Offset + baseStrokeWidth / 2,
                            lv1Offset + baseStrokeWidth / 2
                        ),
                        size = Size(lv1Size, lv1Size)
                    )
                }
                Text(
                    text = String.format(java.util.Locale.getDefault(), "%.1f", score),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ChartLegend(
                    color = Color(0xFF1976D2),
                    text = "Remaining",
                    value = remaining.toInt().toString()
                )
                ChartLegend(
                    color = Color(0xFFFFA000),
                    text = "Mitigated",
                    value = mitigated.toInt().toString()
                )
                ChartLegend(
                    color = Color(0xFFD32F2F),
                    text = "Lost",
                    value = lost.toInt().toString()
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


@Composable
fun FoodWasteChart(
    distributed: Float,
    discarded: Float,
    available: Float
) {
    val total = distributed + discarded + available
    val score = if (total != 0f) (distributed / total) * 100 else 0f

    val sections = listOf(
        PieSection(distributed, Color(0xFF4CAF50), "Distributed"), // Green
        PieSection(discarded, Color(0xFFF44336), "Discarded"), // Red
        PieSection(available, Color(0xFF2196F3), "Available") // Blue
    )

    Box(
        modifier = Modifier
            .size(220.dp)
            .background(Color(0xFFE8F5E9), shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            var startAngle = -90f
            sections.forEach { section ->
                val sweep = (section.value / total) * 360f
                drawArc(
                    color = section.color,
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = Stroke(width = 30f, cap = StrokeCap.Round),
                    size = Size(size.width, size.height),
                    topLeft = Offset(0f, 0f)
                )
                startAngle += sweep
            }
        }

        // Center Score
        Column(
            modifier = Modifier
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${score.toInt()}%",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Managed",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        // Legend
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            sections.forEach {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(it.color, shape = CircleShape)
                    )
                    Text(text = "${it.label}: ${it.value.toInt()}")
                }
            }
        }
    }
}

data class PieSection(
    val value: Float,
    val color: Color,
    val label: String
)

@Preview
@Composable
fun PreviewFoodWasteChart() {
    FoodWasteChart(
        distributed = 300f,
        discarded = 200f,
        available = 100f
    )
}
