package com.socialquest.app.ui.screens
import com.socialquest.app.ui.components.GrowthJourney
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.socialquest.app.ui.components.Gap
import com.socialquest.app.ui.components.PrimaryButton
import com.socialquest.app.ui.components.QuietButton
import com.socialquest.app.ui.components.StepsMark

@Composable
fun WelcomeScreen(onStart: () -> Unit, onSignIn: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(24.dp)) {
        Gap(24)
        StepsMark(Modifier.weight(1f).fillMaxWidth())
        Gap(16)
        Text("Social Quest", style = MaterialTheme.typography.displayLarge)
        Gap(12)
        Text(
            "Practise real conversations out loud, one small step at a time.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Gap(28)
        PrimaryButton("Get started", onStart)
        QuietButton("I already have an account", onSignIn)
        Text(
            "For adults aged 18 and over. Self-guided practice, not therapy.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        GrowthJourney(
            modifier = Modifier.padding(vertical = 20.dp)
        )
    }
}
