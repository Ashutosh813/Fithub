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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DirectionsRun
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
    onMetricClick: (TodayMetric) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val animatedScale by animateFloatAsState(
        targetValue = if (isPressed) 0.985f else 1f,
        animationSpec = spring(dampingRatio = 0.75f, stiffness = 400f),
        label = "today_card_scale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .scale(animatedScale)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = Color(0x0D000000),
                spotColor = Color(0x14000000)
            )
            .clip(RoundedCornerShape(24.dp))
            .background(CardBackground)
            .border(0.5.dp, BorderColor, RoundedCornerShape(24.dp))
            .padding(20.dp)
            .testTag("today_card")
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // "Today" Section Title
            Text(
                text = "Today",
                fontFamily = InterFontFamily,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.4).sp,
                color = TextMain,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // 2x2 Grid of Metrics
            // Row 1: Calories & Water
            val topRowMetrics = metrics.take(2)
            val bottomRowMetrics = metrics.drop(2).take(2)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (topRowMetrics.isNotEmpty()) {
                    TodayItem(
                        metric = topRowMetrics[0],
                        onClick = { onMetricClick(topRowMetrics[0]) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (topRowMetrics.size > 1) {
                    TodayItem(
                        metric = topRowMetrics[1],
                        onClick = { onMetricClick(topRowMetrics[1]) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Row 2: Habits & Activity
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (bottomRowMetrics.isNotEmpty()) {
                    TodayItem(
                        metric = bottomRowMetrics[0],
                        onClick = { onMetricClick(bottomRowMetrics[0]) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (bottomRowMetrics.size > 1) {
                    TodayItem(
                        metric = bottomRowMetrics[1],
                        onClick = { onMetricClick(bottomRowMetrics[1]) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun TodayItem(
    metric: TodayMetric,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val iconVector: ImageVector = when (metric.type) {
        TodayMetricType.CALORIES -> Icons.Rounded.LocalFireDepartment
        TodayMetricType.WATER -> Icons.Rounded.WaterDrop
        TodayMetricType.HABITS -> Icons.Rounded.Check
        TodayMetricType.ACTIVITY -> Icons.Rounded.DirectionsRun
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true, color = TextMain.copy(alpha = 0.08f)),
                onClick = onClick
            )
            .padding(4.dp)
            .testTag("today_item_${metric.id}"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Icon Circle (40.dp, background rgba(242, 242, 247, 0.8))
        Box(
            modifier = Modifier
                .size(40.dp)
                .shadow(
                    elevation = 2.dp,
                    shape = CircleShape,
                    ambientColor = Color(0x08000000),
                    spotColor = Color(0x0C000000)
                )
                .clip(CircleShape)
                .background(Color(0xCCF2F2F7))
                .border(0.5.dp, Color(0x0A000000), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = iconVector,
                contentDescription = metric.title,
                tint = TextMain,
                modifier = Modifier.size(18.dp)
            )
        }

        // Texts
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = metric.title,
                fontFamily = InterFontFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.2).sp,
                color = TextMain
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            fontFamily = InterFontFamily,
                            fontWeight = if (metric.isPrimaryBold) FontWeight.Bold else FontWeight.Normal,
                            color = TextMain,
                            fontSize = 12.sp
                        )
                    ) {
                        append(metric.primaryValue)
                    }
                    append(" ")
                    withStyle(
                        SpanStyle(
                            fontFamily = InterFontFamily,
                            fontWeight = if (metric.isSecondaryBold) FontWeight.Bold else FontWeight.Normal,
                            color = if (metric.isSecondaryBold) Color.Black else TextMuted,
                            fontSize = 12.sp
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
