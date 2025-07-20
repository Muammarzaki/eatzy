package com.github.eatzy.ui.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.eatzy.R
import com.github.eatzy.ui.component.RegularButton
import com.github.eatzy.ui.theme.EaTzyTheme
import kotlinx.coroutines.delay

@Composable
fun DistributionSuccessScreen(modifier: Modifier = Modifier, onContinueClicked: () -> Unit = {}) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onSurface)
    ) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            AnimatedCheck(
                modifier = Modifier
                    .size(200.dp),
                strokeWidth = 30f,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(R.string.distributing_success_message),
                color = MaterialTheme.colorScheme.surface,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = W700
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

@Composable
private fun AnimatedCheck(
    modifier: Modifier,
    strokeWidth: Float = 10f,
    color: Color = Color.Black
) {
    val shortStrokeProgress = remember { Animatable(0f) }
    val longStrokeProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            shortStrokeProgress.snapTo(0f)
            longStrokeProgress.snapTo(0f)
            shortStrokeProgress.animateTo(1f, animationSpec = tween(500))
            delay(250)
            longStrokeProgress.animateTo(1f, animationSpec = tween(500))
            delay(500)
        }
    }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        val start = Offset(width * 0.29f, height * 0.58f)
        val mid = Offset(width * 0.5f, height * 0.75f)
        val end = Offset(width * 0.8f, height * 0.3f)

        drawLine(
            start = start,
            end = lerp(start, mid, shortStrokeProgress.value),
            strokeWidth = strokeWidth,
            color = color,
            cap = StrokeCap.Round
        )

        drawLine(
            start = mid,
            end = lerp(mid, end, longStrokeProgress.value),
            strokeWidth = strokeWidth,
            color = color,
            cap = StrokeCap.Round
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun DistributionSuccessScreenPreview() {
    EaTzyTheme {
        DistributionSuccessScreen()
    }
}