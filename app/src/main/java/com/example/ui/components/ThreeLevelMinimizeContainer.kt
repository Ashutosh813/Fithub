package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * 3-Level Drag-to-Minimize Container:
 * Allows user to drag down starting from top 1/3 of the screen.
 * Stops/snaps at 3 discrete levels:
 * - Level 0: 0px (Fully open)
 * - Level 1: 1/3 height (H/3)
 * - Level 2: 1.7/3 height (1.7 * H / 3)
 * - Below Level 2 or fast fling: Total minimize (invokes onClose)
 */
@Composable
fun ThreeLevelMinimizeContainer(
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (dragModifier: Modifier) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current
    val offsetY = remember { Animatable(0f) }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val totalHeightPx = with(density) { maxHeight.toPx() }
        val level1Px = totalHeightPx / 3f
        val level2Px = (1.7f / 3f) * totalHeightPx

        val draggableState = rememberDraggableState { delta ->
            coroutineScope.launch {
                val newTarget = (offsetY.value + delta).coerceAtLeast(0f)
                offsetY.snapTo(newTarget)
            }
        }

        val dragModifier = Modifier.draggable(
            state = draggableState,
            orientation = Orientation.Vertical,
            onDragStopped = { velocity ->
                coroutineScope.launch {
                    val currentY = offsetY.value
                    if (velocity > 1200f || currentY > level2Px + 70f) {
                        // Total minimize
                        offsetY.animateTo(
                            totalHeightPx,
                            animationSpec = spring(dampingRatio = 0.85f, stiffness = Spring.StiffnessMediumLow)
                        )
                        onClose()
                    } else if (velocity < -800f) {
                        // Flung upwards: snap back to fully open (0)
                        offsetY.animateTo(0f, animationSpec = spring(dampingRatio = 0.85f, stiffness = Spring.StiffnessMediumLow))
                    } else {
                        // Snap to nearest of the 3 levels (0, Level 1, Level 2)
                        val dist0 = kotlin.math.abs(currentY - 0f)
                        val dist1 = kotlin.math.abs(currentY - level1Px)
                        val dist2 = kotlin.math.abs(currentY - level2Px)

                        if (currentY > level2Px + 40f) {
                            offsetY.animateTo(totalHeightPx, animationSpec = spring(dampingRatio = 0.85f, stiffness = Spring.StiffnessMediumLow))
                            onClose()
                        } else if (dist0 <= dist1 && dist0 <= dist2) {
                            offsetY.animateTo(0f, animationSpec = spring(dampingRatio = 0.85f, stiffness = Spring.StiffnessMediumLow))
                        } else if (dist1 <= dist2) {
                            offsetY.animateTo(level1Px, animationSpec = spring(dampingRatio = 0.85f, stiffness = Spring.StiffnessMediumLow))
                        } else {
                            offsetY.animateTo(level2Px, animationSpec = spring(dampingRatio = 0.85f, stiffness = Spring.StiffnessMediumLow))
                        }
                    }
                }
            }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(0, offsetY.value.roundToInt()) }
        ) {
            content(dragModifier)
        }
    }
}
