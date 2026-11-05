package com.socialquest.app.ui.screens

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.socialquest.app.AppViewModel
import com.socialquest.app.ui.components.Gap
import com.socialquest.app.ui.components.QuietButton
import com.socialquest.app.ui.components.ScreenFrame
import com.socialquest.app.ui.components.SettingRow
import com.socialquest.app.ui.components.SwitchRow

@Composable
fun ProfileScreen(vm: AppViewModel, onMode: () -> Unit, onPrivacy: () -> Unit, onSignOut: () -> Unit) {
    ScreenFrame(title = "Profile") {
        Gap(8)
        Text("Account", style = MaterialTheme.typography.titleMedium)
        Gap(8)
        SettingRow(
            title = if (vm.nickname.isBlank()) "No nickname set" else vm.nickname,
            subtitle = vm.email.ifBlank { "Not signed in" },
            onClick = {},
        )
        Gap(20)
        Text("Practice", style = MaterialTheme.typography.titleMedium)
        Gap(8)
        SettingRow("Practice mode", vm.mode.label, onMode)
        Gap(20)
        Text("Comfort", style = MaterialTheme.typography.titleMedium)
        Gap(8)
        SwitchRow("Show captions in voice sessions", vm.captions) { vm.captions = it }
        Gap(8)
        SwitchRow("Reduce movement", vm.reduceMotion) { vm.reduceMotion = it }
        Gap(20)
        Text("Privacy", style = MaterialTheme.typography.titleMedium)
        Gap(8)
        SettingRow("Privacy and data", "Export or delete your data", onPrivacy)
        Gap(20)
        QuietButton("Sign out", onSignOut)
        Gap(8)
        Text(
            "Social Quest is self-guided practice. It isn’t therapy, a diagnosis or a replacement for professional care.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Gap(96)
    }
}
