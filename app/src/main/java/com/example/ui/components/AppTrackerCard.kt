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
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.rounded.DirectionsRun
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
import androidx.compose.ui.graphics.Brush
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
 * AppCard: Exact 1-to-1 translation of HTML/CSS .app-card:
 * - background: rgba(255, 255, 255, 0.92);
 * - border-radius: 20px;
 * - padding: 12px;
 * - border: 0.5px solid rgba(0, 0, 0, 0.08);
 * - box-shadow: 0 10px 25px rgba(0, 0, 0, 0.05), 0 2px 6px rgba(0, 0, 0, 0.03);
 * - aspect-ratio: 1 / 1;
 * - icon-box: 30px x 30px, border-radius: 9px;
 * - icon gradients:
 *   .icon-red { background: linear-gradient(135deg, #fff2f2, #fff); color: #ff3b30; }
 *   .icon-green { background: linear-gradient(135deg, #f0fdf4, #fff); color: #34c759; }
 *   .icon-purple { background: linear-gradient(135deg, #f3f3f8, #fff); color: #5856d6; }
 * - h3: font-size: 11px; font-weight: 600; color: #111115; letter-spacing: -0.2px; line-height: 1.15;
 * - p.stat: font-size: 10px; margin-bottom: 2px; .fw-bold (700, #111115), .fw-regular (400, #8e8e93)
 * - progress-bar: width: 100%; height: 4px; background: rgba(120, 120, 128, 0.12); border-radius: 4px;
 * - progress fill: purple (#5856d6), green (#34c759)
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
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = 420f),
        label = "app_card_scale"
    )
    val animatedElevation by animateFloatAsState(
        targetValue = if (isPressed) 2f else 8f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 400f),
        label = "app_card_elevation"
    )

    Box(
        modifier = modifier
            .aspectRatio(1f) // exact 1 / 1 square aspect ratio
            .scale(animatedScale)
            .shadow(
                elevation = animatedElevation.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Color(0x0D000000), // rgba(0, 0, 0, 0.05)
                spotColor = Color(0x08000000)
            )
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xEBFFFFFF)) // rgba(255, 255, 255, 0.92)
            .border(0.5.dp, Color(0x14000000), RoundedCornerShape(20.dp)) // 0.5px solid rgba(0, 0, 0, 0.08)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true, color = Color.Black.copy(alpha = 0.05f)),
                onClick = onClick
            )
            .padding(12.dp) // exact padding: 12px
            .testTag("app_card_${tracker.id}")
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            // Icon Box: 30px x 30px, border-radius: 9px, linear gradient background
            val (iconVector, iconTint, gradLight) = when (tracker.iconType) {
                TrackerIconType.CALORIE -> Triple(
                    Icons.Rounded.LocalFireDepartment,
                    Color(0xFFFF3B30), // #ff3b30
                    Color(0xFFFFF2F2)  // #fff2f2
                )
                TrackerIconType.HABIT -> Triple(
                    Icons.Rounded.Check,
                    Color(0xFF34C759), // #34c759
                    Color(0xFFF0FDF4)  // #f0fdf4
                )
                TrackerIconType.SLEEP -> Triple(
                    Icons.Rounded.Bedtime,
                    Color(0xFF5856D6), // #5856d6
                    Color(0xFFF3F3F8)  // #f3f3f8
                )
                TrackerIconType.WATER -> Triple(
                    Icons.Rounded.WaterDrop,
                    Color(0xFF007AFF),
                    Color(0xFFF0F8FF)
                )
                TrackerIconType.WORKOUT -> Triple(
                    if (tracker.id == "activity") Icons.Rounded.DirectionsRun else Icons.Rounded.FitnessCenter,
                    Color(0xFF5856D6),
                    Color(0xFFF3F3F8)
                )
                TrackerIconType.FASTING -> Triple(
                    Icons.Rounded.AvTimer,
                    Color(0xFFFF9500),
                    Color(0xFFFFF7ED)
                )
                TrackerIconType.MEDITATION -> Triple(
                    Icons.Rounded.SelfImprovement,
                    Color(0xFF34C759),
                    Color(0xFFF0FDF4)
                )
            }

            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(gradLight, Color.White)
                        )
                    )
                    .border(0.5.dp, Color(0x10000000), RoundedCornerShape(9.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = iconVector,
                    contentDescription = tracker.title,
                    tint = iconTint,
                    modifier = Modifier.size(13.dp)
                )
            }

            // Title: h3 font-size: 11px; font-weight: 600; color: var(--text-main); letter-spacing: -0.2px; line-height: 1.15;
            Text(
                text = tracker.title,
                fontFamily = InterFontFamily,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF111115),
                letterSpacing = (-0.2).sp,
                lineHeight = 13.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Stat & Progress Bar: p.stat font-size: 10px; margin-bottom: 2px
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Bold, // .fw-bold
                                color = Color(0xFF111115),
                                fontSize = 10.sp,
                                letterSpacing = (-0.2).sp
                            )
                        ) {
                            append(tracker.currentFormatted)
                        }
                        append(" ")
                        withStyle(
                            SpanStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Normal, // .fw-regular
                                color = Color(0xFF8E8E93),
                                fontSize = 10.sp,
                                letterSpacing = (-0.2).sp
                            )
                        ) {
                            append(tracker.targetFormatted)
                        }
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 3.dp)
                )

                // iOS Progress Bar: height: 4px; background: rgba(120, 120, 128, 0.12); border-radius: 4px;
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0x1F787880)) // rgba(120, 120, 128, 0.12)
                ) {
                    val fillColor = when (tracker.iconType) {
                        TrackerIconType.CALORIE -> Color(0xFF5856D6) // progress-purple
                        TrackerIconType.HABIT -> Color(0xFF34C759)   // progress-green
                        TrackerIconType.SLEEP -> Color(0xFF5856D6)   // progress-purple
                        else -> tracker.progressColor
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(tracker.progress.coerceIn(0f, 1f))
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(4.dp))
                            .background(fillColor)
                    )
                }
            }
        }
    }
}
