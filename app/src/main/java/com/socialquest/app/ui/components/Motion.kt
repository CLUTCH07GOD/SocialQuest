package com.socialquest.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.semantics.Role

/**
 * Entrance wrapper for cards and sections.
 * Pass reduceMotion=true when the user's reduced-motion preference is enabled.
 */
@Composable
fun MotionEntrance(
    visible: Boolean = true,
    delayMillis: Int = 0,
    reduceMotion: Boolean = false,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = if (reduceMotion) fadeIn(tween(100)) else
            fadeIn(tween(420, delayMillis = delayMillis, easing = FastOutSlowInEasing)) +
                slideInVertically(
                    animationSpec = tween(520, delayMillis = delayMillis, easing = FastOutSlowInEasing),
                    initialOffsetY = { it / 9 },
                ),
    ) {
        content()
    }
}

/** Subtle scale feedback while a card or button is being pressed. */
@Composable
fun Modifier.pressScale(
    pressedScale: Float = 0.975f,
    enabled: Boolean = true,
    onClick: () -> Unit,
): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) pressedScale else 1f,
        animationSpec = tween(150, easing = FastOutSlowInEasing),
        label = "pressScale",
    )
    return this
        .scale(scale)
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            enabled = enabled,
            role = Role.Button,
            onClick = onClick,
        )
}
