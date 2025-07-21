package com.github.eatzy.ui.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.eatzy.R
import com.github.eatzy.ui.component.RegularButton
import com.github.eatzy.ui.theme.AccentColor1
import com.github.eatzy.ui.theme.EaTzyTheme
import kotlinx.coroutines.delay
import kotlin.math.pow

private const val animationDuration = 1200
private const val animationDelay = 1000

@Composable
fun DistributionSuccessScreen(
    modifier: Modifier = Modifier,
    onContinueClicked: () -> Unit = {}
) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = animationDuration,
                    easing = FastOutSlowInEasing
                )
            )
            delay(animationDelay.toLong())
            progress.snapTo(0f)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onSurface)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    drawOrganicPulse(
                        progress = progress.value,
                        color = AccentColor1.copy(alpha = 0.6f),
                        backgroundColor = AccentColor1.copy(alpha = 0.6f),
                    )
                }
        )
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.wrapContentSize()) {
                AnimatedCheck(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(150.dp),
                    progress = progress.value
                )
            }
            Text(
                text = stringResource(R.string.distributing_success_message),
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.W700,
                modifier = Modifier.padding(top = 24.dp)
            )
        }

        RegularButton(
            text = stringResource(R.string.continue_button),
            onClick = onContinueClicked,
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.BottomCenter)
        )
    }
}

private fun DrawScope.drawOrganicPulse(
    progress: Float,
    color: Color,
    backgroundColor: Color
) {
    val easedProgress = FastOutSlowInEasing.transform(progress)
    val maxRadius = size.maxDimension / 2f
    val outerRadius = maxRadius * easedProgress
    if (outerRadius <= 0f) return

    // Fade the pulse alpha slower — tail lingers *after* progress == 1f
    val lingerAlpha = if (progress < 1f) {
        1f - progress.pow(2.2f)  // fade slower before 1.0
    } else {
        (1.1f - progress).coerceIn(0f, 1f).pow(1.8f)  // slow decay after 1.0
    }

    val alpha = lingerAlpha * 1f  // total alpha scaling

    val brush = Brush.radialGradient(
        colors = listOf(color.copy(alpha = alpha), Color.Transparent),
        center = center,
        radius = outerRadius
    )
    drawCircle(brush = brush, radius = outerRadius, center = center)

    val hollowFade = lingerAlpha.pow(1.1f)
    val hollowRadius = outerRadius * 0.35f + (outerRadius * 0.15f * easedProgress)
    drawCircle(
        color = backgroundColor.copy(alpha = hollowFade),
        radius = hollowRadius,
        center = center
    )
}

@Composable
private fun AnimatedCheck(
    modifier: Modifier,
    progress: Float
) {
    val colorPrimary = MaterialTheme.colorScheme.tertiaryContainer

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        val start = Offset(width * 0.25f, height * 0.5f)
        val mid = Offset(width * 0.45f, height * 0.7f)
        val end = Offset(width * 0.75f, height * 0.3f)

        val brush = Brush.linearGradient(
            colors = listOf(colorPrimary.copy(alpha = 1f), colorPrimary.copy(alpha = 1f))
        )

        val midProgress = (progress * 2f).coerceAtMost(1f)
        val endProgress = (progress - 0.5f).coerceAtLeast(0f) * 2f

        if (midProgress > 0) {
            drawLine(
                brush = brush,
                start = start,
                end = lerp(start, mid, midProgress),
                strokeWidth = 30f,
                cap = StrokeCap.Round
            )
        }

        if (endProgress > 0) {
            drawLine(
                brush = brush,
                start = mid,
                end = lerp(mid, end, endProgress),
                strokeWidth = 30f,
                cap = StrokeCap.Round
            )
        }
    }
}

@Preview(showBackground = true, device = "spec:width=390dp,height=844dp,dpi=460")
@Composable
private fun DistributionSuccessScreenPreview() {
    EaTzyTheme {
        DistributionSuccessScreen()
    }
}
