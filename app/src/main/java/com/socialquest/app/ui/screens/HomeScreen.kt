package com.socialquest.app.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.socialquest.app.AppViewModel
import com.socialquest.app.navigation.Routes
import com.socialquest.app.ui.components.Gap
import com.socialquest.app.ui.components.HelpNowButton
import com.socialquest.app.ui.components.MotionEntrance
import com.socialquest.app.ui.components.ModuleCard
import com.socialquest.app.ui.components.ScreenFrame
import com.socialquest.app.ui.theme.Coral
import com.socialquest.app.ui.theme.CoralSoft
import com.socialquest.app.ui.theme.Ink
import com.socialquest.app.ui.theme.Lilac
import com.socialquest.app.ui.theme.LilacSoft
import com.socialquest.app.ui.theme.Sunrise
import com.socialquest.app.ui.theme.SunriseSoft
import com.socialquest.app.ui.theme.Tide
import com.socialquest.app.ui.theme.TideSoft

private val cardShapeA = RoundedCornerShape(topStart = 26.dp, topEnd = 10.dp, bottomEnd = 26.dp, bottomStart = 10.dp)
private val cardShapeB = RoundedCornerShape(topStart = 10.dp, topEnd = 26.dp, bottomEnd = 10.dp, bottomStart = 26.dp)

@Composable
fun HomeScreen(vm: AppViewModel, onOpen: (String) -> Unit) {
    ScreenFrame {
        Gap(22)
        MotionEntrance {
            Column {
                Text(
                    if (vm.nickname.isBlank()) "A little progress,\nevery day." else "Welcome back,\n${vm.nickname}.",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Gap(7)
                Text(
                    "Make room for small, meaningful steps.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Gap(22)

        MotionEntrance(delayMillis = 80, reduceMotion = vm.reduceMotion) {
            NextStepCard(goal = vm.goal, onStart = { onOpen(Routes.PRACTISE) })
        }
        Gap(14)

        MotionEntrance(delayMillis = 130, reduceMotion = vm.reduceMotion) {
            HelpNowButton(onClick = { onOpen(Routes.SUPPORT) })
        }
        Gap(26)

        MotionEntrance(delayMillis = 180, reduceMotion = vm.reduceMotion) {
            Text("Explore your journey", style = MaterialTheme.typography.titleLarge)
        }
        Gap(12)

        MotionEntrance(delayMillis = 220, reduceMotion = vm.reduceMotion) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ModuleCard(
                    "Understand your mind", "Map worries and explore your thoughts",
                    Icons.Outlined.Psychology, LilacSoft, Lilac, cardShapeA,
                    Modifier.weight(1f).height(184.dp),
                ) { onOpen(Routes.MIND) }
                ModuleCard(
                    "Practise conversations", "Build confidence at your own pace",
                    Icons.Outlined.Forum, TideSoft, Tide, cardShapeB,
                    Modifier.weight(1f).height(184.dp),
                ) { onOpen(Routes.PRACTISE) }
            }
        }
        Gap(12)
        MotionEntrance(delayMillis = 280, reduceMotion = vm.reduceMotion) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ModuleCard(
                    "Build your courage", "Take small real-world steps",
                    Icons.Outlined.Flag, SunriseSoft, Sunrise, cardShapeB,
                    Modifier.weight(1f).height(184.dp), onAccent = Ink,
                ) { onOpen(Routes.COURAGE) }
                ModuleCard(
                    "Instant support", "A gentle reset for right now",
                    Icons.Outlined.Spa, CoralSoft, Coral, cardShapeA,
                    Modifier.weight(1f).height(184.dp),
                ) { onOpen(Routes.SUPPORT) }
            }
        }
        Gap(22)
        Text(
            "Your journey, your pace. Social Quest is self-guided practice, not therapy or a diagnosis.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Gap(24)
    }
}

@Composable
private fun NextStepCard(goal: String, onStart: () -> Unit) {
    val transition = rememberInfiniteTransition(label = "heroGlow")
    val glow by transition.animateFloat(
        initialValue = 0.10f,
        targetValue = 0.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "heroGlowAlpha",
    )

    val shape = RoundedCornerShape(30.dp)
    Box(
        Modifier.fillMaxWidth()
            .clip(shape)
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFF282B32),
                        Color(0xFF34313A),
                        Color(0xFF3C3435),
                    ),
                ),
            )
            .padding(22.dp),
    ) {
        Box(
            Modifier
                .align(Alignment.TopEnd)
                .padding(top = 4.dp, end = 2.dp)
                .height(118.dp)
                .fillMaxWidth(0.48f)
                .clip(RoundedCornerShape(100))
                .background(
                    Brush.radialGradient(
                        listOf(Coral.copy(alpha = glow), Tide.copy(alpha = glow * 0.35f), Color.Transparent),
                    ),
                ),
        )
        Column {
            Text("A MOMENT FOR YOU", style = MaterialTheme.typography.labelMedium, color = Sunrise)
            Gap(12)
            Text(
                "Your next\nsmall step",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
            )
            Gap(8)
            Text(
                if (goal.isBlank()) "Start with a short practice conversation. There’s no rush." else goal,
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFFE5DFDA),
            )
            Gap(18)
            Button(
                onClick = onStart,
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Sunrise, contentColor = Ink),
            ) {
                Text("Start practising", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}
