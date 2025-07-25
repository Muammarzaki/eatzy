package com.github.eatzy.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
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
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine

@Composable
fun SummaryPieChart(
    modifier: Modifier = Modifier,
    remaining: Float = 1500f,
    mitigated: Float = 3000f,
    lost: Float = 30f,
    elevation: CardElevation = CardDefaults.cardElevation(),
    containerColor: Color = Color.White
) {
    val total = remaining + mitigated + lost

    val (lostPercentage, mitigatedPercentage, remainingPercentage) = if (total > 0f) {
        Triple(
            (lost / total) * 100,
            (mitigated / total) * 100,
            (remaining / total) * 100
        )
    } else {
        Triple(0f, 0f, 0f)
    }

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
            containerColor = containerColor
        ),
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.surface
                    ),
                )
            ),
        elevation = elevation
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            BoxWithConstraints(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16 / 10f)
            ) {
                val boxWithConstraintsScope = this

                val chartSize = boxWithConstraintsScope.maxWidth.coerceAtMost(300.dp)

                Box(
                    modifier = Modifier.size(chartSize),
                    contentAlignment = Alignment.Center
                ) {
                    val baseStrokeWidth = 25f
                    val ringSpacing = 30f
                    val gapAngle = 7f

                    Canvas(modifier = Modifier.aspectRatio(1f)) {
                        val canvasSize = size.minDimension
                        val backgroundRingOffset = 0f
                        val backgroundRingSize =
                            canvasSize - 2 * backgroundRingOffset - baseStrokeWidth

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
            SummaryPieChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .aspectRatio(16 / 14f),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 5.dp
                )
            )
        }
    }
}

@Composable
fun SalesRevenueChart(
    modifier: Modifier = Modifier,
    salesData: List<Float>,
    months: List<String>,
    elevation: CardElevation = CardDefaults.cardElevation(),
    containerColor: Color = Color.White
) {
    val icon = Icons.AutoMirrored.Default.TrendingUp
    val title = "Wasted Food Amount Trend"
    val subtitle = "Monthly performance overview"

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = elevation
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (salesData.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No data available")
                }
                return@Card
            }

            val points = salesData.mapIndexed { index, value ->
                Point(index.toFloat(), value)
            }

            val xAxisData = AxisData.Builder()
                .axisStepSize(40.dp)
                .backgroundColor(Color.Transparent)
                .steps(points.size - 1)
                .labelData { i -> months.getOrElse(i) { "" } }
                .labelAndAxisLinePadding(15.dp)
                .axisLabelFontSize(12.sp)
                .axisLineColor(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                .build()

            val maxSales = salesData.maxOrNull() ?: 0f
            val yStepSize = maxSales / 4

            val yAxisData = AxisData.Builder()
                .steps(2)
                .backgroundColor(Color.Transparent)
                .labelAndAxisLinePadding(20.dp)
                .labelData { i ->
                    val value = (i * yStepSize)
                    if (value >= 1000) "${value / 1000}K" else "${value.toInt()}"
                }
                .axisLabelFontSize(10.sp)
                .axisLineColor(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                .build()

            val lineChartData = LineChartData(
                linePlotData = LinePlotData(
                    lines = listOf(
                        Line(
                            dataPoints = points,
                            lineStyle = LineStyle(
                                color = MaterialTheme.colorScheme.primary,
                                width = 3f
                            ),
                            intersectionPoint = IntersectionPoint(
                                color = MaterialTheme.colorScheme.primary,
                                radius = 4.dp
                            ),
                            selectionHighlightPoint = SelectionHighlightPoint(
                                color = MaterialTheme.colorScheme.primary,
                                radius = 6.dp
                            ),
                            shadowUnderLine = ShadowUnderLine(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                            ),
                            selectionHighlightPopUp = SelectionHighlightPopUp(
                                popUpLabel = { x, y ->
                                    "${months.getOrElse(x.toInt()) { "?" }}: $${
                                        y.toInt().toString().reversed().chunked(3)
                                            .joinToString(",").reversed()
                                    }"
                                }
                            )
                        )
                    )
                ),
                xAxisData = xAxisData,
                yAxisData = yAxisData,
                gridLines = GridLines(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                    enableHorizontalLines = true,
                    enableVerticalLines = true
                ),
                backgroundColor = Color.Transparent
            )

            LineChart(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
                lineChartData = lineChartData,
            )
        }
    }
}


@Preview(showBackground = true, name = "Sales Revenue Chart")
@Composable
fun SalesRevenueChartPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.aspectRatio(16 / 14f),
        ) {
            val salesData = listOf(12f, 15f, 11f, 18f, 16f, 20f, 17f, 22f, 19f, 21f, 23f, 25f)
            val months = listOf(
                "Jan",
                "Feb",
                "Mar",
                "Apr",
                "May",
                "Jun",
                "Jul",
                "Aug",
                "Sep",
                "Oct",
                "Nov",
                "Dec"
            )
            SalesRevenueChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .aspectRatio(16 / 14f),
                salesData = salesData, months = months,
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                containerColor = MaterialTheme.colorScheme.surface
            )
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun CalorieTrackerChartEmptyPreview() {
//    MaterialTheme {
//        Surface(color = Color(0xFF1E1E1E), contentColor = Color.White) {
//            SummaryPieChart(
//                remaining = 0f,
//                mitigated = 0f,
//                lost = 0f,
//                modifier = Modifier.aspectRatio(16 / 14f)
//            )
//        }
//    }
//}
//
//@Preview(showBackground = true, name = "Sales Revenue Chart")
//@Composable
//fun SalesRevenueChartEmptyPreview() {
//    MaterialTheme {
//        Surface(
//            modifier = Modifier.aspectRatio(16 / 14f),
//            color = MaterialTheme.colorScheme.background
//        ) {
//            val salesData = emptyList<Float>()
//            val months = emptyList<String>()
//            SalesRevenueChart(
//                salesData = salesData, months = months,
//                containerColor = MaterialTheme.colorScheme.surface
//            )
//        }
//    }
//}
