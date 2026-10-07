package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.NavTab
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.BorderColor
import com.example.ui.theme.BottomNavBackground
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.TextMuted

@Composable
fun FitHubBottomNav(
    selectedTab: NavTab,
    onTabSelected: (NavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val navShape = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp, bottomStart = 0.dp, bottomEnd = 0.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 16.dp,
                shape = navShape,
                ambientColor = Color(0x0F000000),
                spotColor = Color(0x18000000)
            )
            .clip(navShape)
            .background(BottomNavBackground)
            .border(0.5.dp, BorderColor, navShape)
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .testTag("bottom_nav_bar")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavTab.entries.forEach { tab ->
                val isSelected = tab == selectedTab
                val iconVector: ImageVector = when (tab) {
                    NavTab.HOME -> Icons.Rounded.Home
                    NavTab.APPS -> Icons.Rounded.GridView
                    NavTab.PROGRESS -> Icons.Rounded.TrendingUp
                    NavTab.PROFILE -> if (isSelected) Icons.Rounded.Person else Icons.Outlined.Person
                }

                val iconBgColor by animateColorAsState(
                    targetValue = if (isSelected) AccentPurple.copy(alpha = 0.10f) else Color.Transparent,
                    animationSpec = tween(durationMillis = 200),
                    label = "tab_pill_color"
                )
                val contentColor by animateColorAsState(
                    targetValue = if (isSelected) AccentPurple else TextMuted,
                    animationSpec = tween(durationMillis = 200),
                    label = "tab_content_color"
                )

                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = true, color = AccentPurple.copy(alpha = 0.15f)),
                            onClick = { onTabSelected(tab) }
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .testTag("nav_item_${tab.name.lowercase()}"),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Pill container behind active icon (44px wide, 28px tall, radius 14px)
                    Box(
                        modifier = Modifier
                            .width(44.dp)
                            .height(28.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(iconBgColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = iconVector,
                            contentDescription = tab.label,
                            tint = contentColor,
                            modifier = Modifier.size(19.dp)
                        )
                    }

                    Text(
                        text = tab.label,
                        fontFamily = InterFontFamily,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        letterSpacing = (-0.2).sp,
                        color = contentColor
                    )
                }
            }
        }
    }
}
