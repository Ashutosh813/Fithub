package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AvTimer
import androidx.compose.material.icons.rounded.Bedtime
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.SelfImprovement
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppTracker
import com.example.model.TrackerIconType
import com.example.ui.theme.InterFontFamily

/**
 * App Tracker Card: Exact reproduction of the attached reference design.
 * Rounded pure white card (rounded corner shape ~28dp) with light soft border,
 * icon badge in rounded square with soft pastel tint, bold black title,
 * and current / target values with rounded track progress bar below.
 */
@Composable
fun AppTrackerCard(
    tracker: AppTracker,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val animatedScale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = 420f),
        label = "card_scale"
    )
    val animatedElevation by animateFloatAsState(
        targetValue = if (isPressed) 1f else 6f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 400f),
        label = "card_elevation"
    )
    val animatedOffsetY by animateFloatAsState(
        targetValue = if (isPressed) 2.5f else 0f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = 420f),
        label = "card_offset_y"
    )

    Box(
        modifier = modifier
            .height(148.dp)
            .offset(y = animatedOffsetY.dp)
            .scale(animatedScale)
            .shadow(
                elevation = animatedElevation.dp,
                shape = RoundedCornerShape(26.dp),
                ambientColor = Color(0x0A000000),
                spotColor = Color(0x14000000)
            )
            .clip(RoundedCornerShape(26.dp))
            .background(Color.White)
            .border(0.5.dp, Color(0x18000000), RoundedCornerShape(26.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true, color = Color.Black.copy(alpha = 0.06f)),
                onClick = onClick
            )
            .padding(14.dp)
            .testTag("app_card_${tracker.id}")
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            // Icon in rounded square with gentle pastel background
            val (iconVector, iconTintColor, iconBgColor) = when (tracker.iconType) {
                TrackerIconType.CALORIE -> Triple(
                    Icons.Rounded.LocalFireDepartment,
                    Color(0xFFE11D48),
                    Color(0xFFFFF1F2)
                )
                TrackerIconType.HABIT -> Triple(
                    Icons.Rounded.Check,
                    Color(0xFF10B981),
                    Color(0xFFECFDF5)
                )
                TrackerIconType.SLEEP -> Triple(
                    Icons.Rounded.Bedtime,
                    Color(0xFF4F46E5),
                    Color(0xFFEEF2FF)
                )
                TrackerIconType.WATER -> Triple(
                    Icons.Rounded.WaterDrop,
                    Color(0xFF0284C7),
                    Color(0xFFF0F9FF)
                )
                TrackerIconType.WORKOUT -> Triple(
                    Icons.Rounded.FitnessCenter,
                    Color(0xFF7C3AED),
                    Color(0xFFF5F3FF)
                )
                TrackerIconType.FASTING -> Triple(
                    Icons.Rounded.AvTimer,
                    Color(0xFFD97706),
                    Color(0xFFFFFBEB)
                )
                TrackerIconType.MEDITATION -> Triple(
                    Icons.Rounded.SelfImprovement,
                    Color(0xFF059669),
                    Color(0xFFECFDF5)
                )
            }

            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBgColor)
                    .border(0.5.dp, iconTintColor.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = iconVector,
                    contentDescription = tracker.title,
                    tint = iconTintColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            // App Name - Medium bold, clean black, matching Image 1
            Text(
                text = tracker.title,
                fontFamily = InterFontFamily,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111115),
                lineHeight = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Progress stats & bar (e.g. "1.3k / 2.5k", "3 / 5 today", "6.5 / 8 hrs")
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF111115),
                                fontSize = 12.5.sp
                            )
                        ) {
                            append(tracker.currentFormatted)
                        }
                        append(" ")
                        withStyle(
                            SpanStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Normal,
                                color = Color(0xFF8E8E93),
                                fontSize = 12.sp
                            )
                        ) {
                            append(tracker.targetFormatted)
                        }
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Track and bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.5.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color(0xFFE5E7EB))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(tracker.progress.coerceIn(0f, 1f))
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(3.dp))
                            .background(
                                when (tracker.iconType) {
                                    TrackerIconType.CALORIE -> Color(0xFF4338CA)
                                    TrackerIconType.HABIT -> Color(0xFF10B981)
                                    TrackerIconType.SLEEP -> Color(0xFF4338CA)
                                    else -> tracker.progressColor
                                }
                            )
                    )
                }
            }
        }
    }
}
