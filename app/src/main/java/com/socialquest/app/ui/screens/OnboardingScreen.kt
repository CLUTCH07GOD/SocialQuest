package com.socialquest.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.socialquest.app.AppViewModel
import com.socialquest.app.ui.components.ChoiceRow
import com.socialquest.app.ui.components.Gap
import com.socialquest.app.ui.components.PrimaryButton
import com.socialquest.app.ui.components.QuietButton
import com.socialquest.app.ui.components.ScreenFrame

private val situationOptions = listOf(
    "Public speaking", "Talking to strangers", "Starting conversations", "Joining group conversations",
    "Job interviews", "Phone calls", "Answering questions in class", "Speaking up in meetings",
)
private val reasonOptions = listOf(
    "Fear of being judged", "Fear of making mistakes", "Fear of embarrassment",
    "Worry that my voice will shake", "My mind goes blank", "Feeling watched by others",
)

/**
 * Three short steps. Answers only personalise practice; they are never used to diagnose anyone.
 * TODO: save to fear_map_items / user_goals through the API.
 */
@Composable
fun OnboardingScreen(vm: AppViewModel, onBack: () -> Unit, onFinished: () -> Unit) {
    var step by rememberSaveable { mutableStateOf(0) }
    val last = 2
    val next = { if (step < last) step++ else onFinished() }

    ScreenFrame(onBack = { if (step == 0) onBack() else step-- }) {
        Gap(8)
        Text("Step ${step + 1} of 3", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Gap(8)
        LinearProgressIndicator(
            progress = { (step + 1) / 3f },
            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
        )
        Gap(24)

        when (step) {
            0 -> {
                Text("Which situations feel hard?", style = MaterialTheme.typography.headlineMedium)
                Gap(8)
                Text("Pick as many as you like. You can skip this.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Gap(16)
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    situationOptions.forEach { s ->
                        ChoiceRow(s, s in vm.situations, { vm.toggle(vm.situations, s) })
                    }
                }
            }
            1 -> {
                Text("What makes them hard?", style = MaterialTheme.typography.headlineMedium)
                Gap(8)
                Text("This helps us suggest practice that fits you. It isn’t a diagnosis.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Gap(16)
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    reasonOptions.forEach { r ->
                        ChoiceRow(r, r in vm.difficulties, { vm.toggle(vm.difficulties, r) })
                    }
                }
            }
            else -> {
                Text("Want to set a first goal?", style = MaterialTheme.typography.headlineMedium)
                Gap(8)
                Text("One small, specific thing is plenty. You can change it later.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Gap(16)
                OutlinedTextField(
                    value = vm.goal,
                    onValueChange = { vm.goal = it },
                    label = { Text("Your goal (optional)") },
                    placeholder = { Text("Ask one question in a meeting") },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        Gap(24)
        PrimaryButton(if (step < last) "Continue" else "Finish", { next() })
        QuietButton("Skip for now", { next() })
        Gap(16)
    }
}
