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
import androidx.compose.material.icons.rounded.Bedtime
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.LocalFireDepartment
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
import androidx.compose.ui.graphics.Brush
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
import com.example.ui.theme.CardBackground
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.TextMain
import com.example.ui.theme.TextMuted

@Composable
fun TodayCard(
    metrics: List<TodayMetric>,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // 3D tactile press animation
    val animatedScale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 400f),
        label = "today_scale"
    )
    val animatedElevation by animateFloatAsState(
        targetValue = if (isPressed) 3f else 12f,
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
                shape = RoundedCornerShape(26.dp),
                ambientColor = Color(0x16000000),
                spotColor = Color(0x22000000)
            )
            .clip(RoundedCornerShape(26.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.White,
                        Color(0xFFFAFAFD)
                    )
                )
            )
            .border(0.75.dp, Color(0x1A000000), RoundedCornerShape(26.dp))
            .padding(20.dp)
            .testTag("today_card")
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // "Today" Section Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Today",
                    fontFamily = InterFontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.4).sp,
                    color = Color(0xFF0F0F14)
                )
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFFF2F2F7))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Overview",
                        fontFamily = InterFontFamily,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6B7280)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Dynamic grid based on apps in My Apps
            val chunkedMetrics = metrics.chunked(2)
            chunkedMetrics.forEachIndexed { rowIndex, rowItems ->
                if (rowIndex > 0) {
                    Spacer(modifier = Modifier.height(16.dp))
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
                    // If odd number of items in the last row, fill remaining space
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
    }

    val iconColor = when (metric.type) {
        TodayMetricType.CALORIES -> Color(0xFFFF3B30)
        TodayMetricType.WATER -> Color(0xFF007AFF)
        TodayMetricType.HABITS -> Color(0xFF34C759)
        TodayMetricType.ACTIVITY -> Color(0xFFFF9500)
        TodayMetricType.SLEEP -> Color(0xFF5856D6)
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFF7F8FA))
            .border(0.5.dp, Color(0x0F000000), RoundedCornerShape(14.dp))
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .testTag("today_item_${metric.id}"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 3D Circular Icon Badge
        Box(
            modifier = Modifier
                .size(40.dp)
                .shadow(
                    elevation = 2.dp,
                    shape = CircleShape,
                    ambientColor = Color(0x0E000000),
                    spotColor = Color(0x14000000)
                )
                .clip(CircleShape)
                .background(Color.White)
                .border(0.5.dp, Color(0x12000000), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = iconVector,
                contentDescription = metric.title,
                tint = iconColor,
                modifier = Modifier.size(19.dp)
            )
        }

        // Bold readable Texts
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = metric.title,
                fontFamily = InterFontFamily,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.2).sp,
                color = Color(0xFF1F2937)
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F0F14),
                            fontSize = 12.5.sp
                        )
                    ) {
                        append(metric.primaryValue)
                    }
                    append(" ")
                    withStyle(
                        SpanStyle(
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6B7280),
                            fontSize = 11.sp
                        )
                    ) {
                        append(metric.secondaryValue)
                    }
                },
                letterSpacing = (-0.2).sp,
                maxLines = 1
            )
        }
    }
}
