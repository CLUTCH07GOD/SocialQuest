package com.socialquest.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.socialquest.app.AppViewModel
import com.socialquest.app.ExperienceMode
import com.socialquest.app.ui.components.ChoiceRow
import com.socialquest.app.ui.components.Gap
import com.socialquest.app.ui.components.PrimaryButton
import com.socialquest.app.ui.components.ScreenFrame

/** TODO: persist preferred_mode through PATCH /profile. */
@Composable
fun ModeScreen(vm: AppViewModel, doneLabel: String, onBack: () -> Unit, onDone: () -> Unit) {
    ScreenFrame(onBack = onBack) {
        Gap(16)
        Text("How would you like to practise?", style = MaterialTheme.typography.headlineMedium)
        Gap(8)
        Text(
            "You can switch any time. Your progress stays the same in every mode.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Gap(20)
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            ExperienceMode.entries.forEach { m ->
                ChoiceRow(
                    label = m.label,
                    supporting = m.blurb,
                    selected = vm.mode == m,
                    radio = true,
                    enabled = m.available,
                    onToggle = { vm.mode = m },
                )
            }
        }
        Gap(24)
        PrimaryButton(doneLabel, onDone)
        Gap(16)
    }
}
