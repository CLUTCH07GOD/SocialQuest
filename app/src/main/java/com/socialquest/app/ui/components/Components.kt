package com.socialquest.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.socialquest.app.ui.theme.Coral
import com.socialquest.app.ui.theme.Ink
import com.socialquest.app.ui.theme.InkSoft
import com.socialquest.app.ui.theme.Line
import com.socialquest.app.ui.theme.Paper
import com.socialquest.app.ui.theme.Sunrise
import com.socialquest.app.ui.theme.Tide
import com.socialquest.app.ui.theme.TideSoft

@Composable
fun Gap(height: Int) = Spacer(Modifier.height(height.dp))

/** Standard page: scrolls, respects the keyboard, optional back arrow + title. */
@Composable
fun ScreenFrame(
    modifier: Modifier = Modifier,
    title: String? = null,
    onBack: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(horizontal = 24.dp),
    ) {
        if (title != null || onBack != null) {
            Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                if (onBack != null) {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
                }
                if (!title.isNullOrEmpty()) Text(title, style = MaterialTheme.typography.titleLarge)
            }
        }
        content()
    }
}

@Composable
fun PrimaryButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth().height(54.dp),
        shape = RoundedCornerShape(16.dp),
    ) { Text(text, style = MaterialTheme.typography.labelLarge) }
}

@Composable
fun QuietButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    TextButton(onClick = onClick, modifier = modifier.fillMaxWidth().height(48.dp)) {
        Text(text, style = MaterialTheme.typography.labelLarge)
    }
}

/** Checkbox-style (multi-select) or radio-style (single-select) row. Whole row is the tap target. */
@Composable
fun ChoiceRow(
    label: String,
    selected: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    supporting: String? = null,
    radio: Boolean = false,
    enabled: Boolean = true,
) {
    val shape = RoundedCornerShape(16.dp)
    val select = if (radio) {
        Modifier.selectable(selected = selected, enabled = enabled, role = Role.RadioButton, onClick = onToggle)
    } else {
        Modifier.toggleable(value = selected, enabled = enabled, role = Role.Checkbox, onValueChange = { onToggle() })
    }
    Row(
        modifier
            .fillMaxWidth()
            .alpha(if (enabled) 1f else 0.5f)
            .clip(shape)
            .background(if (selected) TideSoft else Paper)
            .border(if (selected) 2.dp else 1.dp, if (selected) Tide else Line, shape)
            .then(select)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
            if (supporting != null) {
                Text(supporting, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        if (selected) Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Tide)
    }
}

@Composable
fun ModuleCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    container: Color,
    accent: Color,
    shape: Shape,
    modifier: Modifier = Modifier,
    onAccent: Color = Color.White,
    onClick: () -> Unit,
) {
    Column(
        modifier
            .clip(shape)
            .background(container)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(18.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Box(Modifier.size(44.dp).clip(CircleShape).background(accent), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = onAccent)
        }
        Column {
            Text(title, style = MaterialTheme.typography.titleMedium, color = Ink)
            Spacer(Modifier.height(4.dp))
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = InkSoft)
        }
    }
}

@Composable
fun HelpNowButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(60.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Coral, contentColor = Color.White),
    ) {
        Icon(Icons.Outlined.Spa, contentDescription = null)
        Spacer(Modifier.width(10.dp))
        Text("I’m feeling anxious right now", style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun SettingRow(title: String, subtitle: String? = null, onClick: () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        Modifier.fillMaxWidth().clip(shape).background(Paper).border(1.dp, Line, shape)
            .clickable(role = Role.Button, onClick = onClick).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
            if (subtitle != null) Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = InkSoft)
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = InkSoft)
    }
}

@Composable
fun SwitchRow(title: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        Modifier.fillMaxWidth().clip(shape).background(Paper).border(1.dp, Line, shape)
            .toggleable(value = checked, role = Role.Switch, onValueChange = onChange).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(title, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
        Switch(checked = checked, onCheckedChange = null)
    }
}

/** Five stepping stones rising toward one amber stone: the Courage Ladder as a logo mark. */
@Composable
fun StepsMark(modifier: Modifier = Modifier) {
    Canvas(modifier.clearAndSetSemantics { }) {
        val steps = 5
        val centers = List(steps) { i ->
            val t = i / (steps - 1f)
            Offset(size.width * (0.10f + 0.80f * t), size.height * (0.88f - 0.74f * t))
        }
        val dash = PathEffect.dashPathEffect(floatArrayOf(14f, 16f))
        for (i in 0 until steps - 1) {
            drawLine(Line, centers[i], centers[i + 1], strokeWidth = 4f, pathEffect = dash)
        }
        centers.forEachIndexed { i, c ->
            val t = i / (steps - 1f)
            val radius = (12f + 14f * t).dp.toPx()
            val color = if (i == steps - 1) Sunrise else Tide.copy(alpha = 0.30f + 0.15f * i)
            drawCircle(color, radius, c)
        }
    }
}
