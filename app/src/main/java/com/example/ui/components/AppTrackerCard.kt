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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bedtime
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.LocalFireDepartment
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
import com.example.ui.theme.BorderColor
import com.example.ui.theme.CardBackground
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.ProgressTrackColor
import com.example.ui.theme.TextMain
import com.example.ui.theme.TextMuted

@Composable
fun AppTrackerCard(
    tracker: AppTracker,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val animatedScale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 400f),
        label = "card_scale"
    )

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .scale(animatedScale)
            .shadow(
                elevation = if (isPressed) 2.dp else 8.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Color(0x0D000000),
                spotColor = Color(0x12000000)
            )
            .clip(RoundedCornerShape(20.dp))
            .background(CardBackground)
            .border(0.5.dp, BorderColor, RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true, color = tracker.progressColor.copy(alpha = 0.15f)),
                onClick = onClick
            )
            .padding(12.dp)
            .testTag("app_card_${tracker.id}")
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Icon Box
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(9.dp),
                        ambientColor = Color(0x0A000000),
                        spotColor = Color(0x0F000000)
                    )
                    .clip(RoundedCornerShape(9.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(tracker.iconBgLight, Color.White)
                        )
                    )
                    .border(0.5.dp, Color(0x0F000000), RoundedCornerShape(9.dp)),
                contentAlignment = Alignment.Center
            ) {
                val iconVector = when (tracker.iconType) {
                    TrackerIconType.CALORIE -> Icons.Rounded.LocalFireDepartment
                    TrackerIconType.HABIT -> Icons.Rounded.Check
                    TrackerIconType.SLEEP -> Icons.Rounded.Bedtime
                    TrackerIconType.WATER -> Icons.Rounded.WaterDrop
                    TrackerIconType.WORKOUT -> Icons.Rounded.FitnessCenter
                }
                Icon(
                    imageVector = iconVector,
                    contentDescription = tracker.title,
                    tint = tracker.iconColor,
                    modifier = Modifier.size(15.dp)
                )
            }

            // Title
            Text(
                text = tracker.title,
                fontFamily = InterFontFamily,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextMain,
                lineHeight = 13.sp,
                letterSpacing = (-0.2).sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Stat & Progress Bar
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = TextMain,
                                fontSize = 10.sp
                            )
                        ) {
                            append(tracker.currentFormatted)
                        }
                        append(" ")
                        withStyle(
                            SpanStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Normal,
                                color = TextMuted,
                                fontSize = 10.sp
                            )
                        ) {
                            append(tracker.targetFormatted)
                        }
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // iOS Progress Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(ProgressTrackColor)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(tracker.progress.coerceIn(0f, 1f))
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(4.dp))
                            .background(tracker.progressColor)
                    )
                }
            }
        }
    }
}
