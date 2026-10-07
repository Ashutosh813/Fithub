package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderColor
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.TextMain
import com.example.ui.theme.TextMuted

@Composable
fun FitHubHeader(
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .testTag("fithub_header"),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: Logo badge + Title/Subtitle
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Small badge with "FitHub" + "fitness"
            Box(
                modifier = Modifier
                    .shadow(
                        elevation = 3.dp,
                        shape = RoundedCornerShape(12.dp),
                        ambientColor = Color(0x0A000000),
                        spotColor = Color(0x0F000000)
                    )
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .border(0.5.dp, BorderColor, RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 6.dp)
                    .testTag("header_logo_badge"),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "FitHub",
                        fontFamily = InterFontFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMain,
                        lineHeight = 12.sp
                    )
                    Text(
                        text = "fitness",
                        fontFamily = InterFontFamily,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Normal,
                        color = TextMuted,
                        lineHeight = 11.sp
                    )
                }
            }

            // Header titles
            Column {
                Text(
                    text = "FitHub",
                    fontFamily = InterFontFamily,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp,
                    color = TextMain
                )
                Text(
                    text = "Your fitness, all in one place.",
                    fontFamily = InterFontFamily,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = (-0.2).sp,
                    color = TextMuted
                )
            }
        }

        // Right: Profile icon button
        Box(
            modifier = Modifier
                .size(42.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = CircleShape,
                    ambientColor = Color(0x0A000000),
                    spotColor = Color(0x14000000)
                )
                .clip(CircleShape)
                .background(Color.White)
                .border(0.5.dp, BorderColor, CircleShape)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = true, color = TextMain.copy(alpha = 0.2f)),
                    onClick = onProfileClick
                )
                .testTag("profile_button"),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = "User Profile",
                tint = TextMain,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
