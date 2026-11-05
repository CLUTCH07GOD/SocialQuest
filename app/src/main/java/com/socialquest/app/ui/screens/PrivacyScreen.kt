package com.socialquest.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.socialquest.app.AppViewModel
import com.socialquest.app.ui.components.Gap
import com.socialquest.app.ui.components.ScreenFrame
import com.socialquest.app.ui.components.SettingRow
import com.socialquest.app.ui.components.SwitchRow
import com.socialquest.app.ui.theme.Coral

/** TODO: wire Export to GET /me/export and Delete to DELETE /me once the backend exists. */
@Composable
fun PrivacyScreen(vm: AppViewModel, onBack: () -> Unit, onDeleted: () -> Unit) {
    var confirmDelete by remember { mutableStateOf(false) }
    var note by remember { mutableStateOf<String?>(null) }

    ScreenFrame(title = "Privacy and data", onBack = onBack) {
        Gap(8)
        Text("What we keep", style = MaterialTheme.typography.titleMedium)
        Gap(6)
        Text(
            "Your account, Fear Map, reflections and progress. Voice audio is not stored by default.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Gap(16)
        SwitchRow("Keep a short summary after each voice session", vm.keepSummary) { vm.keepSummary = it }
        Gap(8)
        SettingRow("Export my data", "Get a copy of what we hold") {
            note = "Export will work once the server is connected."
        }
        if (note != null) {
            Gap(8)
            Text(note.orEmpty(), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Gap(28)
        OutlinedButton(
            onClick = { confirmDelete = true },
            modifier = Modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Coral),
            border = BorderStroke(1.dp, Coral),
        ) { Text("Delete my account", style = MaterialTheme.typography.labelLarge) }
        Gap(24)
    }

    if (confirmDelete) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text("Delete your account?") },
            text = { Text("This removes your account and everything saved in Social Quest. It can’t be undone.") },
            confirmButton = {
                TextButton(onClick = { confirmDelete = false; onDeleted() }) { Text("Delete", color = Coral) }
            },
            dismissButton = { TextButton(onClick = { confirmDelete = false }) { Text("Keep my account") } },
        )
    }
}
