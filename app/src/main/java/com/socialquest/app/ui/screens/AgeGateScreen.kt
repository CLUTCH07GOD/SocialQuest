package com.socialquest.app.ui.screens

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.socialquest.app.ui.components.ChoiceRow
import com.socialquest.app.ui.components.Gap
import com.socialquest.app.ui.components.PrimaryButton
import com.socialquest.app.ui.components.QuietButton
import com.socialquest.app.ui.components.ScreenFrame

/** Self-declared 18+ gate. Declining creates nothing: no account, no stored profile. */
@Composable
fun AgeGateScreen(onConfirm: () -> Unit, onBack: () -> Unit) {
    var confirmed by remember { mutableStateOf(false) }
    var declined by remember { mutableStateOf(false) }

    if (declined) {
        ScreenFrame(onBack = { declined = false }) {
            Gap(24)
            Text("Social Quest isn’t available to you right now", style = MaterialTheme.typography.headlineMedium)
            Gap(12)
            Text(
                "This app is for adults aged 18 and over. We haven’t created an account or saved anything about you.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Gap(24)
            QuietButton("Go back", { declined = false })
        }
    } else {
        ScreenFrame(onBack = onBack) {
            Gap(16)
            Text("Before we start", style = MaterialTheme.typography.headlineMedium)
            Gap(12)
            Text(
                "Social Quest is for adults aged 18 and over. We only keep a record that you confirmed this.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Gap(24)
            ChoiceRow("I confirm I am 18 or older", confirmed, { confirmed = !confirmed })
            Gap(24)
            PrimaryButton("Continue", onConfirm, enabled = confirmed)
            QuietButton("I’m under 18", { declined = true })
        }
    }
}
