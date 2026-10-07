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
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.ui.theme.InterFontFamily

/**
 * TodayCard: Exact 1-to-1 translation of HTML/CSS .today-container & .today-card:
 * - container padding: 12px 20px
 * - background: rgba(255, 255, 255, 0.92);
 * - border-radius: 24px;
 * - padding: 20px;
 * - border: 0.5px solid rgba(0, 0, 0, 0.08);
 * - box-shadow: 0 15px 35px rgba(0, 0, 0, 0.05), 0 4px 10px rgba(0, 0, 0, 0.02);
 * - h2: font-size: 18px; font-weight: 700; margin-bottom: 16px; color: #111115; letter-spacing: -0.4px;
 * - today-grid: 1fr 1fr; gap: 16px;
 * - today-item: display: flex; align-items: center; gap: 12px;
 * - today-icon: width: 40px; height: 40px; border-radius: 50%; background: rgba(242, 242, 247, 0.8);
 *               font-size: 15px; color: #111115; border: 0.5px solid rgba(0, 0, 0, 0.04);
 * - today-text h4: font-size: 12px; font-weight: 600; color: #111115; margin-bottom: 1px; letter-spacing: -0.2px;
 * - today-text p: font-size: 12px; letter-spacing: -0.2px;
 *   - .fw-bold: font-weight: 700; color: #111115;
 *   - .fw-regular: font-weight: 400; color: #8e8e93;
 *   - .text-dark-black: color: #000000; font-weight: 700;
 */
@Composable
fun TodayCard(
    metrics: List<TodayMetric>,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val animatedScale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 400f),
        label = "today_scale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp) // .today-container
            .scale(animatedScale)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = Color(0x0D000000), // rgba(0, 0, 0, 0.05)
                spotColor = Color(0x05000000)
            )
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xEBFFFFFF)) // rgba(255, 255, 255, 0.92)
            .border(0.5.dp, Color(0x14000000), RoundedCornerShape(24.dp)) // 0.5px solid rgba(0, 0, 0, 0.08)
            .padding(20.dp) // exact padding: 20px
            .testTag("today_card")
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // h2: font-size: 18px; font-weight: 700; margin-bottom: 16px; color: var(--text-main); letter-spacing: -0.4px;
            Text(
                text = "Today",
                fontFamily = InterFontFamily,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.4).sp,
                color = Color(0xFF111115),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // today-grid: display: grid; grid-template-columns: 1fr 1fr; gap: 16px;
            val chunkedMetrics = metrics.chunked(2)
            chunkedMetrics.forEachIndexed { rowIndex, rowItems ->
                if (rowIndex > 0) {
                    Box(modifier = Modifier.padding(top = 16.dp))
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
                        Box(modifier = Modifier.weight(1f))
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
        modifier = modifier
            .padding(4.dp)
            .testTag("today_item_${metric.id}"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp) // gap: 12px
    ) {
        // today-icon: width: 40px; height: 40px; border-radius: 50%; background: rgba(242, 242, 247, 0.8);
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xCCF2F2F7)) // rgba(242, 242, 247, 0.8)
                .border(0.5.dp, Color(0x0A000000), CircleShape), // 0.5px solid rgba(0, 0, 0, 0.04)
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = iconVector,
                contentDescription = metric.title,
                tint = Color(0xFF111115),
                modifier = Modifier.size(15.dp) // font-size: 15px
            )
        }

        // today-text: h4: 12px, font-weight: 600; p: 12px
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = metric.title,
                fontFamily = InterFontFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF111115),
                letterSpacing = (-0.2).sp,
                modifier = Modifier.padding(bottom = 1.dp)
            )

            // p styling matching HTML
            if (metric.type == TodayMetricType.ACTIVITY) {
                // <span class="fw-bold">6,842</span> <span class="fw-bold text-dark-black">steps</span>
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF111115),
                                fontSize = 12.sp,
                                letterSpacing = (-0.2).sp
                            )
                        ) {
                            append(metric.primaryValue)
                        }
                        append(" ")
                        withStyle(
                            SpanStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF000000),
                                fontSize = 12.sp,
                                letterSpacing = (-0.2).sp
                            )
                        ) {
                            append("steps")
                        }
                    }
                )
            } else if (metric.type == TodayMetricType.CALORIES) {
                // <span class="fw-bold">1,320</span> <span class="fw-regular">/ 2,500 kcal</span>
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF111115),
                                fontSize = 12.sp,
                                letterSpacing = (-0.2).sp
                            )
                        ) {
                            append(metric.primaryValue)
                        }
                        append(" ")
                        withStyle(
                            SpanStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Normal,
                                color = Color(0xFF8E8E93),
                                fontSize = 12.sp,
                                letterSpacing = (-0.2).sp
                            )
                        ) {
                            append("/ 2,500 kcal")
                        }
                    }
                )
            } else {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF111115),
                                fontSize = 12.sp,
                                letterSpacing = (-0.2).sp
                            )
                        ) {
                            append(metric.primaryValue)
                        }
                        append(" ")
                        withStyle(
                            SpanStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Normal,
                                color = Color(0xFF8E8E93),
                                fontSize = 12.sp,
                                letterSpacing = (-0.2).sp
                            )
                        ) {
                            append(metric.secondaryValue.removePrefix("/ ").let { if (!it.startsWith("/")) "/ $it" else it })
                        }
                    }
                )
            }
        }
    }
}
