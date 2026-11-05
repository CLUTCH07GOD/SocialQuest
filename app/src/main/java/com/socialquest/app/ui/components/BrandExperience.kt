package com.socialquest.app.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import kotlinx.coroutines.delay


private val QuestInk = Color(0xFF242321)
private val QuestPaper = Color(0xFFFFFAF5)
private val QuestCoral = Color(0xFFE97865)
private val QuestTeal = Color(0xFF438E89)
private val QuestGold = Color(0xFFF3BE58)
private val QuestMuted = Color(0xFF8A817A)

/**
 * Animated SocialQuest brand mark.
 * Rotating orbit + pulsing center + connecting paths.
 */
@Composable
fun SocialQuestLogo(
    modifier: Modifier = Modifier,
    size: Dp = 112.dp
) {
    val infinite = rememberInfiniteTransition(label = "logo")

    val rotation by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(9000, easing = LinearEasing)
        ),
        label = "rotation"
    )

    val pulse by infinite.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1300, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .rotate(rotation)
        ) {
            val canvasSize = this.size

            val center = Offset(
                canvasSize.width / 2f,
                canvasSize.height / 2f
            )

            val radius = canvasSize.minDimension * 0.39f

            drawCircle(
                color = QuestCoral.copy(alpha = 0.16f),
                radius = radius,
                center = center,
                style = Stroke(width = 2.dp.toPx())
            )

            drawCircle(
                color = QuestTeal,
                radius = 5.dp.toPx(),
                center = Offset(
                    center.x + radius,
                    center.y
                )
            )

            drawCircle(
                color = QuestGold,
                radius = 4.dp.toPx(),
                center = Offset(
                    center.x - radius * 0.72f,
                    center.y - radius * 0.69f
                )
            )
        }

        Box(
            modifier = Modifier
                .size(size * 0.59f)
                .scale(pulse)
                .shadow(
                    elevation = 14.dp,
                    shape = CircleShape,
                    ambientColor = QuestCoral.copy(alpha = 0.3f),
                    spotColor = QuestCoral.copy(alpha = 0.3f)
                )
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            QuestCoral,
                            Color(0xFFD95F58)
                        )
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                // Use the Canvas drawing area's size, not the logo's Dp parameter.
                val canvasSize = this.size

                val stroke = 3.5.dp.toPx()
                val w = canvasSize.width
                val h = canvasSize.height

                // A simple rising-path symbol representing progress.
                val path = Path().apply {
                    moveTo(w * 0.25f, h * 0.62f)

                    cubicTo(
                        w * 0.38f, h * 0.72f,
                        w * 0.45f, h * 0.32f,
                        w * 0.57f, h * 0.42f
                    )

                    cubicTo(
                        w * 0.66f, h * 0.49f,
                        w * 0.70f, h * 0.34f,
                        w * 0.77f, h * 0.28f
                    )
                }

                drawPath(
                    path = path,
                    color = Color.White,
                    style = Stroke(
                        width = stroke,
                        cap = StrokeCap.Round
                    )
                )

                drawCircle(
                    color = Color.White,
                    radius = 3.dp.toPx(),
                    center = Offset(w * 0.77f, h * 0.28f)
                )
            }
        }
    }
}

@Composable
fun SocialQuestWordmark(
    modifier: Modifier = Modifier,
    light: Boolean = false
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Social",
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold,
                color = if (light) Color.White else QuestInk
            )
            Text(
                text = "Quest",
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold,
                color = QuestCoral
            )
        }

        Text(
            text = "GROW AT YOUR OWN PACE",
            fontSize = 9.sp,
            letterSpacing = 2.5.sp,
            fontWeight = FontWeight.Medium,
            color = if (light) Color.White.copy(alpha = 0.65f)
            else QuestMuted
        )
    }
}

/**
 * Full-screen branded splash screen.
 * Call onFinished after the animation completes.
 */
@Composable
fun SocialQuestSplash(
    onFinished: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
        delay(2300)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFF9F3),
                        Color(0xFFF6EDE6),
                        Color(0xFFFFF8F2)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Ambient decorative circles.
        Box(
            Modifier
                .align(Alignment.TopEnd)
                .offset(x = 85.dp, y = (-75).dp)
                .size(230.dp)
                .background(
                    QuestCoral.copy(alpha = 0.08f),
                    CircleShape
                )
        )

        Box(
            Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-95).dp, y = 90.dp)
                .size(250.dp)
                .background(
                    QuestTeal.copy(alpha = 0.07f),
                    CircleShape
                )
        )

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(700)) +
                    scaleIn(
                        initialScale = 0.75f,
                        animationSpec = spring(
                            dampingRatio = 0.65f,
                            stiffness = 100f
                        )
                    )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SocialQuestLogo(size = 130.dp)

                Spacer(Modifier.height(24.dp))

                SocialQuestWordmark()

                Spacer(Modifier.height(13.dp))

                Text(
                    text = "Every small step counts.",
                    fontSize = 14.sp,
                    color = QuestMuted
                )

                Spacer(Modifier.height(54.dp))

                QuestLoadingIndicator()

                Spacer(Modifier.height(15.dp))

                Text(
                    text = "PREPARING YOUR JOURNEY",
                    fontSize = 9.sp,
                    letterSpacing = 2.sp,
                    color = QuestMuted
                )
            }
        }

        Text(
            text = "MADE FOR YOUR PROGRESS",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 35.dp),
            fontSize = 9.sp,
            letterSpacing = 2.sp,
            color = QuestMuted.copy(alpha = 0.75f)
        )
    }
}

/**
 * Custom animated three-dot loading indicator.
 */
@Composable
fun QuestLoadingIndicator(
    modifier: Modifier = Modifier,
    dotSize: Dp = 9.dp
) {
    val infinite = rememberInfiniteTransition(label = "loading")

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf(
            QuestCoral,
            QuestTeal,
            QuestGold
        ).forEachIndexed { index, color ->

            val scale by infinite.animateFloat(
                initialValue = 0.65f,
                targetValue = 1.2f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 500,
                        delayMillis = index * 160,
                        easing = FastOutSlowInEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "dot$index"
            )

            Box(
                modifier = Modifier
                    .size(dotSize)
                    .scale(scale)
                    .background(color, CircleShape)
            )
        }
    }
}

/**
 * Reusable loading overlay for API calls.
 */
@Composable
fun QuestLoadingOverlay(
    visible: Boolean,
    message: String = "A moment, please..."
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(200)),
        exit = fadeOut(tween(200))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(QuestInk.copy(alpha = 0.38f)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(28.dp)
                    .clip(RoundedCornerShape(26.dp))
                    .background(QuestPaper)
                    .padding(horizontal = 36.dp, vertical = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SocialQuestLogo(size = 72.dp)

                Spacer(Modifier.height(20.dp))

                QuestLoadingIndicator(dotSize = 8.dp)

                Spacer(Modifier.height(16.dp))

                Text(
                    text = message,
                    color = QuestInk,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
@Composable
fun QuestPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false
) {
    Button(
        onClick = onClick,
        enabled = enabled && !loading,
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp)
            .shadow(
                elevation = if (enabled) 8.dp else 0.dp,
                shape = RoundedCornerShape(18.dp),
                spotColor = QuestCoral.copy(alpha = 0.25f)
            ),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = QuestCoral,
            contentColor = Color.White,
            disabledContainerColor = QuestCoral.copy(alpha = 0.5f)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 2.dp
        )
    ) {
        AnimatedContent(
            targetState = loading,
            label = "buttonLoading"
        ) { isLoading ->
            if (isLoading) {
                QuestLoadingIndicator(
                    dotSize = 7.dp
                )
            } else {
                Text(
                    text = text,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
@Composable
fun QuestFeatureCard(
    title: String,
    description: String,
    icon: String,
    accent: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    var pressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy
        ),
        label = "cardScale"
    )

    Surface(
        modifier = modifier
            .scale(scale)
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp)),
        onClick = onClick,
        color = Color.White,
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 3.dp
    ) {
        Row(
            modifier = Modifier.padding(19.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(53.dp)
                    .background(
                        accent.copy(alpha = 0.13f),
                        RoundedCornerShape(17.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = icon,
                    fontSize = 24.sp
                )
            }

            Spacer(Modifier.width(15.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = QuestInk
                )

                Spacer(Modifier.height(5.dp))

                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = QuestMuted,
                    lineHeight = 18.sp
                )
            }

            Text(
                text = "›",
                fontSize = 28.sp,
                color = accent
            )
        }
    }
}
