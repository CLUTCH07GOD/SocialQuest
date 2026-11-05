package com.socialquest.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.socialquest.app.ui.components.ChoiceRow
import com.socialquest.app.ui.components.Gap
import com.socialquest.app.ui.components.PrimaryButton
import com.socialquest.app.ui.components.ScreenFrame
import com.socialquest.app.ui.theme.SunriseSoft

/** TODO: send accepted consent types + versions to POST /consents once the backend exists. */
@Composable
fun ConsentScreen(onBack: () -> Unit, onAgree: () -> Unit) {
    var terms by remember { mutableStateOf(false) }
    var privacy by remember { mutableStateOf(false) }
    var ai by remember { mutableStateOf(false) }

    ScreenFrame(onBack = onBack) {
        Gap(16)
        Text("A few things to agree to", style = MaterialTheme.typography.headlineMedium)
        Gap(8)
        Text(
            "Short and plain. You can read the full text in Settings at any time.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Gap(20)
        ChoiceRow("I accept the Terms of Service", terms, { terms = !terms })
        Gap(10)
        ChoiceRow("I accept the Privacy Policy", privacy, { privacy = !privacy })
        Gap(10)
        ChoiceRow(
            "I understand the characters are AI",
            ai, { ai = !ai },
            supporting = "They are not real people, and they are not therapists.",
        )
        Gap(20)
        Column(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(SunriseSoft).padding(16.dp),
        ) {
            Text("About your microphone", style = MaterialTheme.typography.titleMedium)
            Gap(4)
            Text(
                "We’ll ask for microphone access before your first voice session. Audio is processed live and is not stored by default.",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Gap(24)
        PrimaryButton("Agree and continue", onAgree, enabled = terms && privacy && ai)
        Gap(16)
    }
}
