package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.TextMain

/**
 * SectionHeader matching attached reference design:
 * "My Apps" on left in bold black, "Manage >" on right in gentle blue/purple (Color(0xFF4F46E5))
 */
@Composable
fun SectionHeader(
    title: String,
    actionText: String = "Manage >",
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp, vertical = 6.dp)
            .testTag("section_header_$title"),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontFamily = InterFontFamily,
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = (-0.5).sp,
            color = TextMain
        )
        Text(
            text = actionText,
            fontFamily = InterFontFamily,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4F46E5), // Indigo blue matching screenshot
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = false, radius = 24.dp),
                    onClick = onActionClick
                )
                .padding(vertical = 4.dp, horizontal = 2.dp)
                .testTag("section_action_$title")
        )
    }
}
