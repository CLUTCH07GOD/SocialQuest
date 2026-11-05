package com.socialquest.app.ui.components


import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.sin

@Composable
fun GrowthJourney(
    modifier: Modifier = Modifier
) {
    val infinite = rememberInfiniteTransition(label = "growth")

    // Character gradually climbs the path.
    val progress by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 6500,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "climb"
    )

    // Gentle floating movement.
    val float by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {
        val w = size.width
        val h = size.height

        val coral = Color(0xFFE97865)
        val teal = Color(0xFF438E89)
        val gold = Color(0xFFF3BE58)
        val ink = Color(0xFF292522)

        // Background glow.
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    teal.copy(alpha = 0.12f),
                    Color.Transparent
                ),
                center = Offset(w * 0.72f, h * 0.25f),
                radius = h * 0.7f
            ),
            radius = h * 0.7f,
            center = Offset(w * 0.72f, h * 0.25f)
        )

        // Stepping stone positions, bottom-left to top-right.
        val stones = listOf(
            Offset(w * 0.13f, h * 0.79f),
            Offset(w * 0.34f, h * 0.64f),
            Offset(w * 0.54f, h * 0.48f),
            Offset(w * 0.73f, h * 0.32f),
            Offset(w * 0.89f, h * 0.16f)
        )

        val stoneWidth = w * 0.17f
        val stoneHeight = 13.dp.toPx()

        // Draw stepping stones.
        stones.forEachIndexed { index, point ->

            val activated = progress * stones.size > index

            val color = if (activated) {
                teal
            } else {
                Color(0xFFD9D1C9)
            }

            drawRoundRect(
                color = color.copy(alpha = 0.85f),
                topLeft = Offset(
                    point.x - stoneWidth / 2,
                    point.y
                ),
                size = Size(stoneWidth, stoneHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx())
            )

            if (activated) {
                drawCircle(
                    color = teal.copy(alpha = 0.16f),
                    radius = 17.dp.toPx(),
                    center = Offset(point.x, point.y + stoneHeight / 2)
                )
            }
        }

        // Character position along the stepping stones.
        val segment = progress * (stones.size - 1)
        val index = segment.toInt().coerceAtMost(stones.size - 2)
        val fraction = segment - index

        val start = stones[index]
        val end = stones[index + 1]

        val personX = start.x + (end.x - start.x) * fraction
        val personY = start.y + (end.y - start.y) * fraction -
                sin(fraction * Math.PI).toFloat() * 10.dp.toPx()

        val bob = sin(float * Math.PI).toFloat() * 3.dp.toPx()

        val head = Offset(
            personX,
            personY - 26.dp.toPx() + bob
        )

        val shoulder = Offset(
            personX,
            personY - 14.dp.toPx() + bob
        )

        val hip = Offset(
            personX,
            personY + bob
        )

        // Character shadow.
        drawCircle(
            color = ink.copy(alpha = 0.08f),
            radius = 12.dp.toPx(),
            center = Offset(personX, personY + 8.dp.toPx())
        )

        // Character head.
        drawCircle(
            color = coral,
            radius = 7.dp.toPx(),
            center = head
        )

        // Body.
        drawLine(
            color = ink,
            start = shoulder,
            end = hip,
            strokeWidth = 4.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Arms.
        drawLine(
            color = ink,
            start = shoulder,
            end = Offset(
                personX - 10.dp.toPx(),
                personY - 22.dp.toPx() + bob
            ),
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Round
        )

        drawLine(
            color = ink,
            start = shoulder,
            end = Offset(
                personX + 10.dp.toPx(),
                personY - 25.dp.toPx() + bob
            ),
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Legs.
        drawLine(
            color = ink,
            start = hip,
            end = Offset(
                personX - 8.dp.toPx(),
                personY + 12.dp.toPx() + bob
            ),
            strokeWidth = 3.5.dp.toPx(),
            cap = StrokeCap.Round
        )

        drawLine(
            color = ink,
            start = hip,
            end = Offset(
                personX + 9.dp.toPx(),
                personY + 9.dp.toPx() + bob
            ),
            strokeWidth = 3.5.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Goal glow at the summit.
        drawCircle(
            color = gold.copy(alpha = 0.17f),
            radius = 23.dp.toPx(),
            center = stones.last()
        )

        drawCircle(
            color = gold,
            radius = 7.dp.toPx(),
            center = stones.last()
        )

        // Goal rays.
        drawCircle(
            color = gold.copy(alpha = 0.65f),
            radius = 14.dp.toPx(),
            center = stones.last(),
            style = Stroke(width = 1.5.dp.toPx())
        )
    }
}