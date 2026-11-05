package com.socialquest.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.socialquest.app.AppViewModel
import com.socialquest.app.navigation.Routes
import com.socialquest.app.ui.components.Gap
import com.socialquest.app.ui.components.HelpNowButton
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

// Alternating corners give each card its own silhouette without extra decoration.
private val shapeA = RoundedCornerShape(topStart = 28.dp, topEnd = 8.dp, bottomEnd = 28.dp, bottomStart = 8.dp)
private val shapeB = RoundedCornerShape(topStart = 8.dp, topEnd = 28.dp, bottomEnd = 8.dp, bottomStart = 28.dp)

@Composable
fun HomeScreen(vm: AppViewModel, onOpen: (String) -> Unit) {
    ScreenFrame {
        Gap(24)
        Text(
            if (vm.nickname.isBlank()) "Welcome" else "Hi, ${vm.nickname}",
            style = MaterialTheme.typography.headlineMedium,
        )
        Gap(4)
        Text("What would you like to work on today?", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Gap(20)

        NextStepCard(goal = vm.goal, onStart = { onOpen(Routes.PRACTISE) })
        Gap(14)
        HelpNowButton(onClick = { onOpen(Routes.SUPPORT) })
        Gap(20)

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            ModuleCard(
                "Understand your mind", "Map your worries and test your predictions",
                Icons.Outlined.Psychology, LilacSoft, Lilac, shapeA,
                Modifier.weight(1f).height(184.dp),
            ) { onOpen(Routes.MIND) }
            ModuleCard(
                "Practise conversations", "Speak with AI characters, out loud",
                Icons.Outlined.Forum, TideSoft, Tide, shapeB,
                Modifier.weight(1f).height(184.dp),
            ) { onOpen(Routes.PRACTISE) }
        }
        Gap(14)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            ModuleCard(
                "Build your courage", "Small real-world steps, at your pace",
                Icons.Outlined.Flag, SunriseSoft, Sunrise, shapeB,
                Modifier.weight(1f).height(184.dp), onAccent = Ink,
            ) { onOpen(Routes.COURAGE) }
            ModuleCard(
                "Instant support", "Short exercises for right now",
                Icons.Outlined.Spa, CoralSoft, Coral, shapeA,
                Modifier.weight(1f).height(184.dp),
            ) { onOpen(Routes.SUPPORT) }
        }
        Gap(20)
        Text(
            "Social Quest is self-guided practice. It isn’t therapy or a diagnosis.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Gap(24)
    }
}

@Composable
private fun NextStepCard(goal: String, onStart: () -> Unit) {
    Column(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(28.dp)).background(Ink).padding(20.dp),
    ) {
        Text("Your next step", style = MaterialTheme.typography.titleMedium, color = Sunrise)
        Gap(6)
        Text(
            if (goal.isBlank()) "Choose a situation and try a short practice conversation." else goal,
            style = MaterialTheme.typography.headlineMedium,
            color = androidx.compose.ui.graphics.Color.White,
        )
        Gap(16)
        Button(
            onClick = onStart,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Sunrise, contentColor = Ink),
        ) { Text("Start practising", style = MaterialTheme.typography.labelLarge) }
    }
}
