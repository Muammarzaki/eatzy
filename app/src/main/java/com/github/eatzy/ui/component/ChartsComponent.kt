package com.github.eatzy.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.eatzy.ui.theme.EaTzyTheme
import java.text.NumberFormat
import java.util.Locale

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
            containerColor = Color.Transparent // Make the default container color transparent
        ),
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(MaterialTheme.colorScheme.primary, Color.White),
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CalorieTrackerChartPreview() {
    EaTzyTheme {
        CalorieTrackerChart()
    }
}


@Composable
fun TotalValueCard(
    value: Int,
    progress: Float,
    modifier: Modifier = Modifier,
    text: String
) {
    val formattedValue = NumberFormat.getNumberInstance(Locale.US).format(value)

    Card(
        modifier = modifier
            .width(220.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE6F5DD)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(130.dp)
            ) {
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF5A623),
                    strokeWidth = 18.dp,
                    trackColor = Color(0xFFE0E0E0),
                    strokeCap = StrokeCap.Round,
                )

                Text(
                    text = formattedValue,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = text,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TotalValueCardPreview() {
    TotalValueCard(value = 7550, progress = 0.8f, text = "Total")
}