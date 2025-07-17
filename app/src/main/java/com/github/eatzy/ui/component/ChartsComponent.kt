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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SummaryPieChart(
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



@Preview(showBackground = true)
@Composable
fun CalorieTrackerChartPreview() {
    MaterialTheme {
        Surface(color = Color(0xFF1E1E1E), contentColor = Color.White) {
            SummaryPieChart(modifier = Modifier.aspectRatio(1f))
        }
    }
}

