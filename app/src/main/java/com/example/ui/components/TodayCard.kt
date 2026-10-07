package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TodayMetric
import com.example.model.TodayMetricType
import com.example.ui.theme.BorderColor
import com.example.ui.theme.InterFontFamily

@Composable
fun TodayCard(
    metrics: List<TodayMetric>,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // 3D tactile press animation
    val animatedScale by animateFloatAsState(
        targetValue = if (isPressed) 0.985f else 1f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 400f),
        label = "today_scale"
    )
    val animatedElevation by animateFloatAsState(
        targetValue = if (isPressed) 2f else 10f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 400f),
        label = "today_elevation"
    )
    val animatedOffsetY by animateFloatAsState(
        targetValue = if (isPressed) 2f else 0f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 400f),
        label = "today_offset"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .offset(y = animatedOffsetY.dp)
            .scale(animatedScale)
            .shadow(
                elevation = animatedElevation.dp,
                shape = RoundedCornerShape(32.dp),
                ambientColor = Color(0x10000000),
                spotColor = Color(0x18000000)
            )
            .clip(RoundedCornerShape(32.dp))
            .background(Color.White)
            .border(0.5.dp, BorderColor, RoundedCornerShape(32.dp))
            .padding(horizontal = 24.dp, vertical = 24.dp)
            .testTag("today_card")
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // "Today" Section Title (Pure and clean, exactly matching Image 1)
            Text(
                text = "Today",
                fontFamily = InterFontFamily,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.5).sp,
                color = Color(0xFF111115)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Dynamic grid based on apps currently in My Apps (matching Image 1)
            val chunkedMetrics = metrics.chunked(2)
            chunkedMetrics.forEachIndexed { rowIndex, rowItems ->
                if (rowIndex > 0) {
                    Spacer(modifier = Modifier.height(24.dp))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    rowItems.forEach { metric ->
                        TodayItem(
                            metric = metric,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun TodayItem(
    metric: TodayMetric,
    modifier: Modifier = Modifier
) {
    val iconVector: ImageVector = when (metric.type) {
        TodayMetricType.CALORIES -> Icons.Rounded.LocalFireDepartment
        TodayMetricType.WATER -> Icons.Rounded.WaterDrop
        TodayMetricType.HABITS -> Icons.Rounded.Check
        TodayMetricType.ACTIVITY -> Icons.Rounded.DirectionsRun
        TodayMetricType.SLEEP -> Icons.Rounded.Bedtime
        TodayMetricType.WORKOUT -> Icons.Rounded.FitnessCenter
        TodayMetricType.FASTING -> Icons.Rounded.AvTimer
        TodayMetricType.MEDITATION -> Icons.Rounded.SelfImprovement
    }

    Row(
        modifier = modifier.testTag("today_item_${metric.id}"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Soft Light Gray Circle Container with Dark Icon (Identical to Image 1)
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(Color(0xFFF3F4F6)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = iconVector,
                contentDescription = metric.title,
                tint = Color(0xFF111115),
                modifier = Modifier.size(20.dp)
            )
        }

        // Clean stacked Typography matching Image 1
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = metric.title,
                fontFamily = InterFontFamily,
                fontSize = 14.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111115),
                lineHeight = 17.sp
            )

            if (metric.type == TodayMetricType.CALORIES) {
                // Image 1 format: Line 2: "1,320 / 2,500", Line 3: "kcal"
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF111115),
                                fontSize = 13.5.sp
                            )
                        ) {
                            append(metric.primaryValue)
                        }
                        withStyle(
                            SpanStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF8E8E93),
                                fontSize = 13.sp
                            )
                        ) {
                            append(" / 2,500")
                        }
                    },
                    lineHeight = 16.sp
                )
                Text(
                    text = "kcal",
                    fontFamily = InterFontFamily,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF8E8E93),
                    lineHeight = 15.sp
                )
            } else {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF111115),
                                fontSize = 13.5.sp
                            )
                        ) {
                            append(metric.primaryValue)
                        }
                        append(" ")
                        withStyle(
                            SpanStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF8E8E93),
                                fontSize = 13.sp
                            )
                        ) {
                            append(metric.secondaryValue.removePrefix("/ ").let { if (!it.startsWith("/")) "/ $it" else it })
                        }
                    },
                    lineHeight = 16.sp
                )
            }
        }
    }
}
