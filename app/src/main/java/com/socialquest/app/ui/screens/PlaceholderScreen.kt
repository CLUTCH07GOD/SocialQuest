package com.socialquest.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.socialquest.app.ui.components.Gap
import com.socialquest.app.ui.components.ScreenFrame
import com.socialquest.app.ui.theme.Paper

/** Stand-in for a module owned by another teammate. They replace this call in AppNav.kt with their own screen. */
@Composable
fun PlaceholderScreen(title: String, owner: String, body: String, onBack: (() -> Unit)?) {
    ScreenFrame(title = title, onBack = onBack) {
        Gap(16)
        Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(Paper).padding(20.dp)) {
            Text("Built by $owner", style = MaterialTheme.typography.titleMedium)
            Gap(8)
            Text(body, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
