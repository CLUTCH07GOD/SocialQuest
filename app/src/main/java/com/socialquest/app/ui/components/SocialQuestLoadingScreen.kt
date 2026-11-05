
package com.socialquest.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.sin

private val SQBackground = Color(0xFF171C22)
private val SQCoral = Color(0xFFE97865)
private val SQTeal = Color(0xFF63B6A5)
private val SQGold = Color(0xFFF4C875)
private val SQWhite = Color(0xFFFFF8F0)

@Composable
fun SocialQuestLoadingScreen(
    modifier: Modifier = Modifier,
    message: String = "Preparing your journey..."
) {
    val character = remember { Animatable(0f) }
    val landing = remember { Animatable(0f) }
    val arrival = remember { Animatable(0f) }

    val infinite = rememberInfiniteTransition(label = "ambient")

    val ambient by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(2600, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "ambient"
    )

    val particleMotion by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(5000, easing = LinearEasing)
        ),
        label = "particles"
    )

    // Choreographed journey:
    // walk -> jump -> land -> pause -> next platform.
    LaunchedEffect(Unit) {
        while (true) {
            character.snapTo(0f)
            landing.snapTo(0f)
            arrival.snapTo(0f)

            character.animateTo(
                0.25f,
                tween(900, easing = FastOutSlowInEasing)
            )
            landing.animateTo(1f, spring(dampingRatio = 0.45f))
            delay(180)

            character.animateTo(
                0.50f,
                tween(850, easing = FastOutSlowInEasing)
            )
            landing.animateTo(0f, tween(180))
            delay(120)

            character.animateTo(
                0.75f,
                tween(900, easing = FastOutSlowInEasing)
            )
            landing.animateTo(1f, spring(dampingRatio = 0.5f))
            delay(180)

            character.animateTo(
                1f,
                tween(1100, easing = FastOutSlowInEasing)
            )
            landing.animateTo(0f, tween(200))

            arrival.animateTo(
                1f,
                tween(1000, easing = FastOutSlowInEasing)
            )

            delay(900)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF202832),
                        SQBackground,
                        Color(0xFF28232A)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Atmospheric light.
            drawCircle(
                brush = Brush.radialGradient(
                    listOf(
                        SQCoral.copy(alpha = 0.14f),
                        Color.Transparent
                    ),
                    center = Offset(w * 0.5f, h * 0.35f),
                    radius = h * 0.48f
                ),
                radius = h * 0.48f,
                center = Offset(w * 0.5f, h * 0.35f)
            )

            // Floating particles.
            repeat(24) { i ->
                val x = ((i * 47f) % w)
                val baseY = ((i * 73f) % h)
                val y = (baseY - particleMotion * 80.dp.toPx() + h) % h

                drawCircle(
                    color = SQWhite.copy(
                        alpha = 0.15f + 0.18f * ambient
                    ),
                    radius = (i % 3 + 1).dp.toPx(),
                    center = Offset(x, y)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.weight(0.45f))

            Text(
                text = "YOUR JOURNEY",
                color = SQWhite.copy(alpha = 0.58f),
                letterSpacing = 4.sp,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(24.dp))

            // Main animated mountain illustration.
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                val w = size.width
                val h = size.height

                val points = listOf(
                    Offset(w * 0.12f, h * 0.86f),
                    Offset(w * 0.32f, h * 0.69f),
                    Offset(w * 0.52f, h * 0.51f),
                    Offset(w * 0.72f, h * 0.33f),
                    Offset(w * 0.88f, h * 0.14f)
                )

                // Mountain silhouette.
                val mountain = Path().apply {
                    moveTo(0f, h)
                    lineTo(0f, h * 0.91f)
                    cubicTo(
                        w * 0.24f, h * 0.74f,
                        w * 0.35f, h * 0.84f,
                        w * 0.49f, h * 0.60f
                    )
                    cubicTo(
                        w * 0.67f, h * 0.34f,
                        w * 0.82f, h * 0.47f,
                        w, h * 0.05f
                    )
                    lineTo(w, h)
                    close()
                }

                drawPath(
                    mountain,
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF34444B),
                            Color(0xFF242C35),
                            Color(0xFF1C232B)
                        )
                    )
                )

                // Curved journey trail.
                val trail = Path().apply {
                    moveTo(points[0].x, points[0].y)
                    cubicTo(
                        w * 0.18f, h * 0.77f,
                        w * 0.26f, h * 0.75f,
                        points[1].x, points[1].y
                    )
                    cubicTo(
                        w * 0.40f, h * 0.60f,
                        w * 0.45f, h * 0.59f,
                        points[2].x, points[2].y
                    )
                    cubicTo(
                        w * 0.62f, h * 0.43f,
                        w * 0.66f, h * 0.41f,
                        points[3].x, points[3].y
                    )
                    cubicTo(
                        w * 0.80f, h * 0.23f,
                        w * 0.82f, h * 0.20f,
                        points[4].x, points[4].y
                    )
                }

                drawPath(
                    trail,
                    Color.White.copy(alpha = 0.18f),
                    style = Stroke(
                        width = 2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(7.dp.toPx(), 8.dp.toPx())
                        )
                    )
                )

                // Stepping platforms.
                points.forEachIndexed { index, point ->
                    val completed = character.value * 4 >= index
                    val glow = if (completed) SQTeal else Color(0xFF59636C)

                    drawCircle(
                        color = glow.copy(alpha = 0.17f),
                        radius = 24.dp.toPx(),
                        center = point
                    )

                    drawRoundRect(
                        color = glow,
                        topLeft = Offset(
                            point.x - 23.dp.toPx(),
                            point.y
                        ),
                        size = Size(
                            46.dp.toPx(),
                            10.dp.toPx()
                        ),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                            6.dp.toPx()
                        )
                    )

                    if (completed) {
                        drawCircle(
                            color = SQWhite,
                            radius = 2.dp.toPx(),
                            center = Offset(
                                point.x,
                                point.y + 5.dp.toPx()
                            )
                        )
                    }
                }

                // Character travels smoothly between platforms.
                val p = character.value * 4f
                val segment = p.toInt().coerceAtMost(3)
                val fraction = (p - segment).coerceIn(0f, 1f)

                val a = points[segment]
                val b = points[segment + 1]

                val jump = sin(fraction * PI).toFloat() * 28.dp.toPx()

                val cx = a.x + (b.x - a.x) * fraction
                val cy = a.y + (b.y - a.y) * fraction - jump

                val bounce = sin(ambient * PI).toFloat() * 2.dp.toPx()

                val head = Offset(
                    cx,
                    cy - 29.dp.toPx() + bounce
                )

                val bodyTop = Offset(
                    cx,
                    cy - 18.dp.toPx() + bounce
                )

                val bodyBottom = Offset(cx, cy + bounce)

                // Character glow.
                drawCircle(
                    color = SQCoral.copy(alpha = 0.16f),
                    radius = 23.dp.toPx(),
                    center = Offset(cx, cy - 10.dp.toPx())
                )

                // Head.
                drawCircle(
                    color = SQCoral,
                    radius = 8.dp.toPx(),
                    center = head
                )

                // Body.
                drawLine(
                    SQWhite,
                    bodyTop,
                    bodyBottom,
                    5.dp.toPx(),
                    cap = StrokeCap.Round
                )

                // Walking arm movement.
                val armSwing = sin(ambient * 2 * PI).toFloat() * 8.dp.toPx()

                drawLine(
                    SQWhite,
                    bodyTop,
                    Offset(cx - 10.dp.toPx(), cy - 12.dp.toPx() + armSwing),
                    3.dp.toPx(),
                    cap = StrokeCap.Round
                )

                drawLine(
                    SQWhite,
                    bodyTop,
                    Offset(cx + 10.dp.toPx(), cy - 12.dp.toPx() - armSwing),
                    3.dp.toPx(),
                    cap = StrokeCap.Round
                )

                // Legs.
                drawLine(
                    SQWhite,
                    bodyBottom,
                    Offset(cx - 8.dp.toPx(), cy + 13.dp.toPx()),
                    4.dp.toPx(),
                    cap = StrokeCap.Round
                )

                drawLine(
                    SQWhite,
                    bodyBottom,
                    Offset(cx + 8.dp.toPx(), cy + 13.dp.toPx()),
                    4.dp.toPx(),
                    cap = StrokeCap.Round
                )

                // Summit sunrise.
                val summit = points.last()

                drawCircle(
                    color = SQGold.copy(alpha = 0.13f + arrival.value * 0.22f),
                    radius = (18f + arrival.value * 25f).dp.toPx(),
                    center = summit
                )

                drawCircle(
                    color = SQGold,
                    radius = (5f + arrival.value * 5f).dp.toPx(),
                    center = summit
                )

                // Sunrise ring.
                drawCircle(
                    color = SQGold.copy(alpha = arrival.value * 0.8f),
                    radius = (15f + arrival.value * 15f).dp.toPx(),
                    center = summit,
                    style = Stroke(width = 1.5.dp.toPx())
                )
            }

            Spacer(Modifier.height(25.dp))

            Text(
                text = "Every step takes you further.",
                color = SQWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(9.dp))

            Text(
                text = message,
                color = SQWhite.copy(alpha = 0.57f),
                fontSize = 13.sp
            )

            Spacer(Modifier.height(22.dp))

            // Animated progress line.
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.48f)
                    .height(3.dp)
                    .background(
                        Color.White.copy(alpha = 0.12f),
                        CircleShape
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(character.value.coerceAtLeast(0.04f))
                        .fillMaxHeight()
                        .background(
                            Brush.horizontalGradient(
                                listOf(SQCoral, SQGold)
                            ),
                            CircleShape
                        )
                )
            }

            Spacer(Modifier.height(15.dp))

            Text(
                text = "SOCIALQUEST",
                color = SQWhite.copy(alpha = 0.72f),
                letterSpacing = 4.sp,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            )

            Spacer(Modifier.weight(0.55f))
        }
    }
}

